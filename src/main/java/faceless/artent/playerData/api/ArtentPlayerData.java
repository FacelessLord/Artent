package faceless.artent.playerData.api;

import faceless.artent.trading.api.TradeInfo;
import faceless.artent.trading.inventory.TraderSellInventory;
import net.minecraft.nbt.NbtCompound;

public interface ArtentPlayerData {
	long getMoney();

	void setMoney(long money);

	void addMoney(long money);

	boolean canEditTrades();

	void setCanEditTrades(boolean canEdit);

	TraderSellInventory getTraderSellInventory();

	TradeInfo getTradeInfo();

	void setTradeInfo(TradeInfo info);
	HeroInfo getHeroInfo();

	void setHeroInfo(HeroInfo info);

	void writeToNbt(NbtCompound compound);

	void readFromNbt(NbtCompound compound);
}
