package faceless.artent.playerData.mixin;

import faceless.artent.api.inventory.InventoryUtils;
import faceless.artent.network.ArtentServerHook;
import faceless.artent.playerData.api.ArtentPlayerData;
import faceless.artent.trading.api.TradeInfo;
import faceless.artent.trading.block.Trader;
import faceless.artent.trading.inventory.TraderSellInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("ALL")
@Mixin(PlayerEntity.class)
public class PlayerDataMixin implements ArtentPlayerData {

	@Unique
	public long artentMoney = 0;

	@Unique
	public boolean canEditTrades = false;
	@Unique
	public TraderSellInventory traderSellInventory = new TraderSellInventory();
	@Unique
	public TradeInfo tradeInfo = Trader.getTradeInfo();

	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	private void onWriteEntityToNBT(NbtCompound compound, CallbackInfo ci) {
		this.writeToNbt(compound);
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	private void onReadEntityFromNBT(NbtCompound compound, CallbackInfo ci) {
		this.readFromNbt(compound);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void tick(CallbackInfo ci) {
		var player = (PlayerEntity) (Object) this;
		if (player.getWorld() != null && !player.getWorld().isClient && player.getWorld().getTime() % 10 == 1) {
			if (tradeInfo != null && tradeInfo.priceDeterminatorContext == null)
				setTradeInfo(null);

			ArtentServerHook.packetSyncPlayerData(player);
		}
	}


	@Override
	public long getMoney() {
		return artentMoney;
	}

	@Override
	public void setMoney(long money) {
		artentMoney = money;
	}

	@Override
	public void addMoney(long money) {
		artentMoney += money;
	}

	@Override
	public boolean canEditTrades() {
		return canEditTrades;
	}

	@Override
	public void setCanEditTrades(boolean canEdit) {
		canEditTrades = canEdit;
	}

	@Override
	public TraderSellInventory getTraderSellInventory() {
		return this.traderSellInventory;
	}

	@Override
	public TradeInfo getTradeInfo() {
		return tradeInfo;
	}

	@Override
	public void setTradeInfo(TradeInfo info) {
		tradeInfo = info;
	}

	@Override
	public void writeToNbt(NbtCompound compound) {
		var tag = new NbtCompound();
		tag.putLong("money", this.getMoney());
		tag.putBoolean("canEditTrades", this.canEditTrades());

		if (traderSellInventory == null)
			traderSellInventory = new TraderSellInventory();
		InventoryUtils.writeInventoryNbt(tag, traderSellInventory.items, true);
		if (tradeInfo != null) {
			var tradeInfoTag = new NbtCompound();
			tradeInfo.writeToNbt(tradeInfoTag);
			tag.put("tradeInfo", tradeInfoTag);
		}

		compound.put("artent.data", tag);
	}

	@Override
	public void readFromNbt(NbtCompound compound) {
		if (!compound.contains("artent.data"))
			return;
		var tag = compound.getCompound("artent.data");

		setMoney(tag.getLong("money"));
		setCanEditTrades(tag.getBoolean("canEditTrades"));
		try {
			Inventories.readNbt(tag, traderSellInventory.items);
		} catch (Exception e) {
			traderSellInventory = new TraderSellInventory();
		}

		if (!tag.contains("tradeInfo"))
			return;

		tradeInfo = new TradeInfo();
		var tradeInfoTag = tag.getCompound("tradeInfo");
		tradeInfo.readFromNbt(tradeInfoTag);
		var x = 1;
	}
}
