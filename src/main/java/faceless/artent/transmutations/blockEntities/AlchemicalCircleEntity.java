package faceless.artent.transmutations.blockEntities;

import faceless.artent.api.inventory.InventoryUtils;
import faceless.artent.api.math.Color;
import faceless.artent.objects.ModBlockEntities;
import faceless.artent.objects.ModBlocks;
import faceless.artent.registries.TransmutationRegistry;
import faceless.artent.transmutations.api.*;
import faceless.artent.transmutations.block.AlchemicalCircleBlock;
import net.fabricmc.fabric.api.blockview.v2.RenderDataBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.Pair;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AlchemicalCircleEntity extends BlockEntity implements RenderDataBlockEntity {
	public List<CirclePart> parts = new ArrayList<>();
	public int actionTime = 0;
	public Transmutation transmutation;
	public PlayerEntity alchemist;
	public State state = State.Idle;
	public List<AlchemicalCircleEntity> boundCircles = new ArrayList<>();
	public List<Pair<BlockEntity, Direction>> boundEntities = new ArrayList<>();
	public DefaultedList<ItemStack> inventory = DefaultedList.ofSize(10, ItemStack.EMPTY);
	public NbtCompound circleTag = new NbtCompound();
	NbtCompound loadData = null;

	public AlchemicalCircleEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.AlchemicalCircle, pos, state);
	}

	public static void tick(World world, BlockPos pos, BlockState state, AlchemicalCircleEntity entity) {
		if (entity.loadData != null) {
			entity.readNbt(entity.loadData);
			entity.loadData = null;
			entity.markDirty();
		}

		if (state.getBlock() != ModBlocks.AlchemicalCircle) return;
		var facing = state.get(AlchemicalCircleBlock.FACING);

		updateTransmutation(entity);

		if (entity.transmutation != null) {
			if (entity.state == State.Preparation) {
				entity.actionTime++;
				if (entity.actionTime > entity.transmutation.getPrepTime()) {
					entity.actionTime = 0;
					entity.transmutation.action.accept(facing, entity, entity.alchemist);
					entity.state = State.Action;
					entity.boundCircles.stream().filter(e -> e.transmutation != null).forEach(e -> {
						e.state = State.Action;
						e.alchemist = entity.alchemist;
					});
				}
				entity.markDirty();
			}
			if (entity.state == State.Action) {
				entity.actionTime++;
				if (entity.transmutation.getTickAction().accept(facing, entity, entity.alchemist, entity.actionTime)) {
					entity.actionTime = 0;
					entity.state = State.Idle;
				}
				entity.markDirty();
			}
		}

		if (world.getTime() % 40 == 0) {
			List<AlchemicalCircleEntity> toRemove = new ArrayList<>();
			entity.boundCircles.stream().filter(e -> world.getBlockEntity(e.pos) != e).forEach(toRemove::add);
			toRemove.forEach(e -> e.boundCircles.remove(entity));
			entity.boundCircles.removeAll(toRemove);

			List<Pair<BlockEntity, Direction>> eToRemove = new ArrayList<>();
			entity.boundEntities.stream().filter(e -> e.getLeft() != null && world.getBlockEntity(e.getLeft().getPos()) != e.getLeft()).forEach(eToRemove::add);
			entity.boundEntities.removeAll(eToRemove);

			entity.markDirty();
		}
	}

	private static void updateTransmutation(AlchemicalCircleEntity entity) {
		var formula = CircleHelper.createCircleFormula(entity.parts);
		if (TransmutationRegistry.registry.containsKey(formula)) {
			Transmutation t2 = TransmutationRegistry.registry.get(formula);
			if (entity.transmutation != t2) {
				entity.transmutation = TransmutationRegistry.registry.get(formula);
				entity.actionTime = 0;
				entity.markDirty();
			}
		} else {
			entity.transmutation = null;
			entity.markDirty();
		}
	}

	@Override
	public void writeNbt(NbtCompound tag) {
		writeClientNbt(tag);
		super.writeNbt(tag);
	}

	@Override
	public void readNbt(NbtCompound tag) {
		super.readNbt(tag);
		readClientNbt(tag);
	}

	public void addPart(PartType type, boolean reverse) {
		if (parts.stream().noneMatch(pt -> pt.part == type && pt.reverse == reverse)) {
			parts.add(new CirclePart(type, reverse));
			markDirty();
		}
	}

	public void removePart(CirclePart part) {
		parts.removeIf(pt -> pt == null || pt.equals(part));
		markDirty();
	}

	private void readClientNbt(NbtCompound tag) {
		if (world == null) return;

		parts = CircleHelper.getCircles(tag.getString("artentcircle"));
		transmutation = TransmutationRegistry.registry.getOrDefault(tag.getString("artentcircle"), null);

		if (tag.contains("circleTag")) circleTag = tag.getCompound("circleTag");

		Inventories.readNbt(tag, this.inventory);

		actionTime = tag.getInt("artentActTime");
		state = State.values()[tag.getInt("artentState")];
		if (hasWorld()) {
			NbtCompound lst = tag.getCompound("boundCircles");
			int size = lst.getInt("size");
			for (int i = 0; i < size; i++) {
				int[] posArray = lst.getIntArray(String.valueOf(i));
				BlockPos pos = new BlockPos(posArray[0], posArray[1], posArray[2]);
				BlockEntity entity = world.getBlockEntity(pos);
				if (entity instanceof AlchemicalCircleEntity) boundCircles.add((AlchemicalCircleEntity) entity);
			}

			NbtCompound elst = tag.getCompound("boundEntities");
			int esize = elst.getInt("size");
			for (int i = 0; i < esize; i++) {
				int[] posArray = elst.getIntArray(String.valueOf(i));
				BlockPos pos = new BlockPos(posArray[0], posArray[1], posArray[2]);
				BlockEntity entity = world.getBlockEntity(pos);
				boundEntities.add(new Pair<>(entity, Direction.byId(posArray[3])));
			}

			if (state != State.Idle) {
				if (tag.containsUuid("artentAlchemist"))
					alchemist = world.getPlayerByUuid(tag.getUuid("artentAlchemist"));
			}
		} else orderLoad(tag);
	}

	private void orderLoad(NbtCompound tag) {
		loadData = tag;
	}

	private void writeClientNbt(NbtCompound tag) {
		NbtCompound lst = new NbtCompound();

		tag.putString("artentcircle", CircleHelper.createCircleFormula(parts));
		tag.putInt("artentActTime", actionTime);
		tag.putInt("artentState", state.ordinal());
		if (alchemist != null) tag.putUuid("artentAlchemist", alchemist.getUuid());

		InventoryUtils.writeNbt(tag, this.inventory, true);

		tag.put("circleTag", circleTag);

		lst.putInt("size", boundCircles.size());
		for (int i = 0; i < boundCircles.size(); i++) {
			AlchemicalCircleEntity entity = boundCircles.get(i);
			lst.putIntArray(String.valueOf(i), new int[]{ entity.pos.getX(), entity.pos.getY(), entity.pos.getZ() });
		}
		tag.put("boundCircles", lst);

		NbtCompound elst = new NbtCompound();
		elst.putInt("size", boundEntities.size());
		for (int i = 0; i < boundEntities.size(); i++) {
			Pair<BlockEntity, Direction> entityDir = boundEntities.get(i);
			BlockEntity entity = entityDir.getLeft();
			elst.putIntArray(String.valueOf(i), new int[]{ entity.getPos().getX(), entity.getPos().getY(), entity.getPos().getZ(), entityDir.getRight().getId() });
		}
		tag.put("boundEntities", elst);
	}

	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this, BlockEntity::createNbt);
	}

	@Override
	public NbtCompound toInitialChunkDataNbt() {
		return createNbt();
	}

	public Color getRenderColor() {
		if (transmutation != null) {
			if (state == State.Preparation) {
				float percentage = actionTime / ((float) transmutation.getPrepTime());
				return transmutation.getActionColor().multiply(percentage).addNoMixing(transmutation.getPreparationColor().multiply(1 - percentage));
			}
			if (state == State.Action) {
				return transmutation.getActionColor();
			}
		}
		return new Color();
	}

	@Override
	public @Nullable Object getRenderData() {
		return getRenderColor().asInt();
	}
}
