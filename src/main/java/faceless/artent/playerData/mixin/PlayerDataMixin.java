package faceless.artent.playerData.mixin;

import faceless.artent.api.inventory.InventoryUtils;
import faceless.artent.network.ArtentServerHook;
import faceless.artent.playerData.api.ArtentPlayerData;
import faceless.artent.trading.inventory.TraderSellInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerDataMixin implements ArtentPlayerData {
	@Unique
	public long artentMoney = 0;
	@Unique
	public TraderSellInventory traderSellInventory = new TraderSellInventory((PlayerEntity) (Object) this);

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
	public TraderSellInventory getTraderSellInventory() {
		return this.traderSellInventory;
	}

	@Override
	public void writeToNbt(NbtCompound compound) {
		var tag = new NbtCompound();
		tag.putLong("money", this.getMoney());

		InventoryUtils.writeInventoryNbt(tag, traderSellInventory.items, true);

		compound.put("artent.data", tag);
	}

	@Override
	public void readFromNbt(NbtCompound compound) {
		if (!compound.contains("artent.data"))
			return;
		var tag = compound.getCompound("artent.data");

		setMoney(tag.getLong("money"));
		Inventories.readNbt(tag, traderSellInventory.items);
	}
}
