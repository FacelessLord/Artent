package faceless.artent.transmutations.block;

import com.mojang.serialization.MapCodec;

import faceless.artent.api.item.INamed;
import faceless.artent.objects.ModBlockEntities;
import faceless.artent.objects.ModItems;
import faceless.artent.transmutations.api.State;
import faceless.artent.transmutations.network.AlchemicalCircleServerHook;
import faceless.artent.transmutations.blockEntities.AlchemicalCircleEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class AlchemicalCircleBlock extends BlockWithEntity implements INamed {
	public static final IntProperty CIRCLE_TYPE = IntProperty.of("type", 0, 54);
	public static final DirectionProperty FACING = Properties.FACING;
	public static final MapCodec<AlchemicalCircleBlock> CODEC = AlchemicalCircleBlock.createCodec(AlchemicalCircleBlock::new);

	public AlchemicalCircleBlock(Settings settings) {
		super(settings);
		setDefaultState(getStateManager().getDefaultState().with(CIRCLE_TYPE, 0).with(FACING, Direction.UP));
	}

	public String getId() {
		return "alchemical_circle";
	}

	@SuppressWarnings("deprecation")
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		var facing = state.get(FACING);
		return switch (facing) {
			case UP -> VoxelShapes.cuboid(0f, 0f, 0f, 1f, 0.01f, 1f);
			case DOWN -> VoxelShapes.cuboid(0f, 0.99f, 0f, 1f, 1f, 1f);
			case NORTH -> VoxelShapes.cuboid(0f, 0f, 0.99f, 1f, 1f, 1f);
			case SOUTH -> VoxelShapes.cuboid(0f, 0f, 0f, 1f, 1f, 0.01f);
			case EAST -> VoxelShapes.cuboid(0f, 0f, 0f, 0.01f, 1f, 1f);
			case WEST -> VoxelShapes.cuboid(0.99f, 0f, 0f, 1f, 1f, 1f);
		};
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos var1, BlockState var2) {
		return new AlchemicalCircleEntity(var1, var2);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(CIRCLE_TYPE).add();
		stateManager.add(FACING).add();
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
							  BlockHitResult hit) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (!(blockEntity instanceof AlchemicalCircleEntity circle))
			return ActionResult.FAIL;

		if (player.getEquippedStack(EquipmentSlot.MAINHAND).getItem() == ModItems.chalk) {
			AlchemicalCircleServerHook.packetOpenCircleGui(player, circle);
			return ActionResult.SUCCESS;
		}

		var mainHand = player.getEquippedStack(EquipmentSlot.MAINHAND);

		if (!mainHand.isEmpty()) {
			if (mainHand.getItem() == ModItems.alchemicalClock) {
				var tag = new NbtCompound();
				circle.writeNbt(tag);
				player.sendMessage(Text.literal(tag.asString()));

				return ActionResult.SUCCESS;
			}

			if (circle.inventory.get(0).isEmpty()) {
				circle.inventory.set(0, mainHand);
				player.getInventory().setStack(player.getInventory().selectedSlot, ItemStack.EMPTY);

				return ActionResult.SUCCESS;
			}
		}

		if (player.isSneaking()) {
			if (!circle.inventory.get(0).isEmpty()) {
				if (mainHand.isEmpty()) {
					player.getInventory().setStack(player.getInventory().selectedSlot, circle.inventory.get(0));
					circle.inventory.set(0, ItemStack.EMPTY);

					return ActionResult.SUCCESS;
				}
			}

			if (circle.state != State.Idle) {
				circle.state = State.Idle;
				circle.actionTime = 0;

				return ActionResult.SUCCESS;
			}
		}

		if (circle.transmutation != null) {
			if (circle.state == State.Idle) {
				circle.state = State.Preparation;
				circle.alchemist = player;
			}
		}

		return ActionResult.SUCCESS;
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	protected MapCodec<? extends BlockWithEntity> getCodec() {
		return CODEC;
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return validateTicker(type, ModBlockEntities.AlchemicalCircleEntity, AlchemicalCircleEntity::tick);
	}
}
