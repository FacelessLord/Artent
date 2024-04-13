package faceless.artent.playerData.api;

import faceless.artent.spells.api.CasterInfo;
import faceless.artent.trading.api.TradeInfo;
import faceless.artent.trading.inventory.TraderSellInventory;
import net.minecraft.nbt.NbtCompound;

public interface ArtentPlayerData {
    ArtentPlayerState getPlayerState();

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

    CasterInfo getCasterInfo();

    void setCasterInfo(CasterInfo info);

    void writeToNbt(NbtCompound compound);

    void readFromNbt(NbtCompound compound);
}
