package faceless.artent.trading.blockEntities;

import faceless.artent.objects.ModBlockEntities;
import faceless.artent.playerData.api.DataUtil;
import faceless.artent.trading.inventory.TraderOfferInventory;
import faceless.artent.trading.screenHandlers.TraderScreenHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class TraderBlockEntity extends BlockEntity implements NamedScreenHandlerFactory {
	public TraderOfferInventory offerInventory;

	public TraderBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.Trader, pos, state);
		offerInventory = new TraderOfferInventory(this);
	}

	@Override
	public Text getDisplayName() {
		return Text.translatable("artent.trader.title");
	}

	@Nullable
	@Override
	public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
		var traderSellInventory = DataUtil.getTraderSellInventory(player);

		return new TraderScreenHandler(syncId, playerInventory, offerInventory, traderSellInventory, ScreenHandlerContext.create(world, pos));
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		offerInventory.readNbt(nbt);
	}

	@Override
	public void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		offerInventory.writeNbt(nbt);
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
