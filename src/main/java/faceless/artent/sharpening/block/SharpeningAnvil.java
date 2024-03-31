package faceless.artent.sharpening.block;

import com.mojang.serialization.MapCodec;
import faceless.artent.api.item.INamed;
import faceless.artent.sharpening.blockEntity.SharpeningAnvilBlockEntity;
import faceless.artent.sharpening.screenHandlers.SharpeningAnvilScreenHandler;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SharpeningAnvil extends BlockWithEntity implements INamed {
	public static final MapCodec<SharpeningAnvil> CODEC = SharpeningAnvil.createCodec(SharpeningAnvil::new);
	public BlockItem Item;

	@Override
	protected MapCodec<? extends BlockWithEntity> getCodec() {
		return CODEC;
	}

	@Override
	public String getId() {
		return "sharpening_anvil";
	}

	public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

	public SharpeningAnvil(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		// With inheriting from BlockWithEntity this defaults to INVISIBLE, so we need to change that!
		return BlockRenderType.MODEL;
	}

	protected void openScreen(World world, BlockState state, BlockPos pos, PlayerEntity player) {
		if (!world.isClient) {
			//This will call the createScreenHandlerFactory method from BlockWithEntity, which will return our blockEntity casted to
			//a namedScreenHandlerFactory. If your block class does not extend BlockWithEntity, it needs to implement createScreenHandlerFactory.
			NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);

			if (screenHandlerFactory != null) {
				//With this call the server will request the client to open the appropriate Screenhandler
				player.openHandledScreen(screenHandlerFactory);
			}
		}
	}

	@Override
	@Nullable
	public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
		var blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof SharpeningAnvilBlockEntity anvil)
			return new SimpleNamedScreenHandlerFactory((syncId, inventory, player) -> new SharpeningAnvilScreenHandler(syncId, inventory, anvil.inventory, ScreenHandlerContext.create(world, pos)), Text.translatable("gui.artent.sharpening_anvil"));
		return null;
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) {
			return ActionResult.SUCCESS;
		}
		this.openScreen(world, state, pos, player);
		return ActionResult.SUCCESS;
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new SharpeningAnvilBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return null;//validateTicker(type, ModBlockEntities.SHARPENING_ANVIL, (world1, pos, state1, be) -> be.tick(world1, pos, state1));
	}

	//This method will drop all items onto the ground when the block is broken
	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof SharpeningAnvilBlockEntity anvil) {
				ItemScatterer.spawn(world, pos, anvil.inventory);
				// update comparators
				world.updateComparators(pos, this);
			}
			super.onStateReplaced(state, world, pos, newState, moved);
		}
	}

	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		var facing = state.get(FACING);
		return switch (facing) {
			case DOWN, UP -> Block.createCuboidShape(0f, 0f, 0, 16, 10, 16);
			case NORTH, SOUTH -> Block.createCuboidShape(5f, 0f, 0, 11f, 10f, 16f);
			case EAST, WEST -> Block.createCuboidShape(0f, 0f, 5f, 16f, 10f, 11f);
		};
	}
}