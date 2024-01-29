package faceless.artent.playerData.api;

import faceless.artent.trading.inventory.TraderSellInventory;
import net.minecraft.nbt.NbtCompound;

public interface ArtentPlayerData {
	long getMoney();
	void setMoney(long money);
	void addMoney(long money);

	TraderSellInventory getTraderSellInventory();

	void writeToNbt(NbtCompound compound);
	void readFromNbt(NbtCompound compound);
}
