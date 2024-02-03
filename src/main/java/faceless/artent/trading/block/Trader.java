package faceless.artent.trading.block;

import com.mojang.serialization.MapCodec;
import faceless.artent.api.item.INamed;
import faceless.artent.network.ArtentServerHook;
import faceless.artent.objects.ModBlocks;
import faceless.artent.objects.ModItems;
import faceless.artent.playerData.api.DataUtil;
import faceless.artent.sharpening.block.SharpeningAnvil;
import faceless.artent.trading.api.TradeInfo;
import faceless.artent.trading.blockEntities.TraderBlockEntity;
import faceless.artent.trading.priceDeterminators.ItemStackPriceDeterminator;
import faceless.artent.trading.screenHandlers.TraderScreenHandler;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
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

			var blockEntity = world.getBlockEntity(pos);
			if (!(blockEntity instanceof TraderBlockEntity trader))
				return;

			var tradeInfo = getTradeInfo();
			var handler = DataUtil.getHandler(player);
			handler.setTradeInfo(tradeInfo);
			ArtentServerHook.packetSyncPlayerData(player);

			trader.setTradeInfo(tradeInfo);

			//This will call the createScreenHandlerFactory method from BlockWithEntity, which will return our blockEntity cast to
			//a namedScreenHandlerFactory. If your block class does not extend BlockWithEntity, it needs to implement createScreenHandlerFactory.
			NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);

			if (screenHandlerFactory != null) {
				//With this call the server will request the client to open the appropriate Screenhandler
				player.openHandledScreen(screenHandlerFactory);
			}
		}
	}

	public static TradeInfo getTradeInfo() {
		var tradeInfo = new TradeInfo();
		tradeInfo.offer.set(0, new ItemStack(ModItems.AmberSphere));
		tradeInfo.offer.set(1, new ItemStack(ModItems.FortitudeSpiritStone));
		tradeInfo.offer.set(2, new ItemStack(ModItems.StoneOfTheSea));
		tradeInfo.offer.set(4, new ItemStack(ModItems.PhiloStone));
		tradeInfo.offer.set(6, new ItemStack(ModItems.SmithingHammer));
		tradeInfo.offer.set(7, new ItemStack(ModBlocks.SharpeningAnvil.Item));
		tradeInfo.offer.set(7, new ItemStack(ModItems.NetherFireStone));
		tradeInfo.priceDeterminatorContext = new ItemStackPriceDeterminator.ConstantPriceDeterminatorContext();
		tradeInfo.priceDeterminatorType = ItemStackPriceDeterminator.NAME;

		return tradeInfo;
	}

	@Override
	@Nullable
	public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
		var blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof TraderBlockEntity trader)
			return new SimpleNamedScreenHandlerFactory((syncId, inventory, player) -> {
				var traderSellInventory = DataUtil.getTraderSellInventory(player);
				return new TraderScreenHandler(
					syncId,
					inventory,
					trader.offerInventory,
					traderSellInventory,
					ScreenHandlerContext.create(world, pos));
			}, Text.translatable("gui.artent.trader"));
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
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof TraderBlockEntity trader) {
				ItemScatterer.spawn(world, pos, trader.offerInventory);
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
