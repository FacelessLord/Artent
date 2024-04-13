package faceless.artent.spells.blockEntity;

import faceless.artent.objects.ModBlockEntities;
import faceless.artent.objects.ModBlocks;
import faceless.artent.spells.spells.WormHole;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class VoidBlockEntity extends BlockEntity {
	public int ticksLeft = 160;
	public int depth = 0;
	public Direction direction = Direction.EAST;
	public BlockPos centerPos;
	public NbtCompound replacedBlockTag;
	public NbtCompound replacedEntityTag;

	public VoidBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.VoidBlock, pos, state);
	}

	public void tick(World world, BlockPos pos, BlockState state) {
		ticksLeft--;
		if (replacedBlockTag == null) {
			if (ticksLeft <= 0)
				world.setBlockState(pos, Blocks.AIR.getDefaultState());
			return;
		}
		if (ticksLeft <= 0) {
			var replacedBlock = NbtHelper.toBlockState(this.getWorld().createCommandRegistryWrapper(RegistryKeys.BLOCK), replacedBlockTag);
			world.setBlockState(pos, replacedBlock);
			if (replacedBlock.hasBlockEntity()) {
				var replacedBlockEntity = world.getBlockEntity(pos);
				if (replacedBlockEntity != null)
					replacedBlockEntity.readNbt(replacedEntityTag);
			}
		}
		if (ticksLeft % 4 == 0) {
			var offsetPos = pos.offset(direction);
			if (depth > 0)
				WormHole.voidLayer(world, centerPos, direction);
			if (depth <= 1 || world.getBlockState(offsetPos).getBlock() == ModBlocks.VoidBlock || !world.getBlockState(offsetPos).isOpaqueFullCube(world, offsetPos)) {
				depth = 0;
				return;
			}

			WormHole.voidBlock(world, offsetPos, direction, depth - 1, centerPos.offset(direction));
		}
	}

	public boolean shouldDrawSide(Direction side) {
		var sidePos = pos.offset(side.getOpposite());
		return getWorld().getBlockState(sidePos).isOpaqueFullCube(this.getWorld(), sidePos);
	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		nbt.putInt("ticksLeft", ticksLeft);
		nbt.putInt("direction", direction.getId());
		if (centerPos != null)
			nbt.put("centerPos", NbtHelper.fromBlockPos(centerPos));
		if (replacedBlockTag != null) {
			nbt.put("void.replacedBlock", replacedBlockTag);
			if (replacedEntityTag != null)
				nbt.put("void.replacedBlockEntity", replacedEntityTag);
		}
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);

		ticksLeft = nbt.getInt("ticksLeft");
		direction = Direction.byId(nbt.getInt("direction"));
		var centerPosNbt = nbt.getCompound("centerPos");
		if (!centerPosNbt.isEmpty())
			centerPos = NbtHelper.toBlockPos(centerPosNbt);
		replacedBlockTag = nbt.getCompound("void.replacedBlock");
		if (nbt.contains("void.replacedBlockEntity"))
			replacedEntityTag = nbt.getCompound("void.replacedBlockEntity");
	}

	@Override
	public void markDirty() {
		super.markDirty();
		//noinspection DataFlowIssue
		world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
	}

	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}

	@Override
	public NbtCompound toInitialChunkDataNbt() {
		return createNbt();
	}
}
