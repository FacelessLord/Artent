package faceless.artent.trading.block;

import com.mojang.serialization.MapCodec;
import faceless.artent.api.item.INamed;
import faceless.artent.sharpening.block.SharpeningAnvil;
import faceless.artent.trading.blockEntities.TraderBlockEntity;
import faceless.artent.trading.screenHandlers.TraderScreenHandler;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class Trader extends BlockWithEntity implements INamed {
	@Override
	protected MapCodec<? extends BlockWithEntity> getCodec() {
		return CODEC;
	}

	public static final MapCodec<SharpeningAnvil> CODEC = SharpeningAnvil.createCodec(SharpeningAnvil::new);
	public BlockItem Item;

	public Trader(Settings settings) {
		super(settings);
	}

	@Override
	public String getId() {
		return "trader";
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
		if (blockEntity instanceof TraderBlockEntity trader)
			return new SimpleNamedScreenHandlerFactory((syncId, inventory, player) -> new TraderScreenHandler(syncId, inventory, trader.inventory, ScreenHandlerContext.create(world, pos)), Text.translatable("gui.artent.trader"));
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
		return new TraderBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return null;//validateTicker(type, ModBlockEntities.SHARPENING_ANVIL, (world1, pos, state1, be) -> be.tick(world1, pos, state1));
	}

	//This method will drop all items onto the ground when the block is broken
	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (state.getBlock() != newState.getBlock()) {
			// TODO
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof TraderBlockEntity trader) {
				ItemScatterer.spawn(world, pos, trader.inventory);
				// update comparators
				world.updateComparators(pos, this);
			}
			super.onStateReplaced(state, world, pos, newState, moved);
		}
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return Block.createCuboidShape(1, 1, 1, 15, 15, 15);
	}
}
