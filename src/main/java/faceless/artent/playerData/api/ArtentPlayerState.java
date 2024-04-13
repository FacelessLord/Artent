package faceless.artent.playerData.api;

import faceless.artent.api.inventory.InventoryUtils;
import faceless.artent.spells.api.CasterInfo;
import faceless.artent.trading.api.TradeInfo;
import faceless.artent.trading.block.Trader;
import faceless.artent.trading.inventory.TraderSellInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Unique;

public class ArtentPlayerState {

    @Unique
    public long artentMoney = 0;

    @Unique
    public boolean canEditTrades = false;
    @Unique
    public TraderSellInventory traderSellInventory = new TraderSellInventory();
    @Unique
    public TradeInfo tradeInfo = Trader.getTradeInfo();

    @Unique
    public HeroInfo heroInfo = new HeroInfo();
    @Unique
    public CasterInfo casterInfo = new CasterInfo();

    public void writeToNbt(NbtCompound tag) {
        tag.putLong("money", artentMoney);
        tag.putBoolean("canEditTrades", canEditTrades);

        if (traderSellInventory == null)
            traderSellInventory = new TraderSellInventory();
        InventoryUtils.writeNbt(tag, traderSellInventory.items, true);
        if (tradeInfo != null) {
            tradeInfo.writeNbt(tag);
        }
        if (casterInfo != null) {
            casterInfo.writeNbt(tag);
        }
        if (heroInfo != null) {
            heroInfo.writeNbt(tag);
        }
    }

    public void readFromNbt(NbtCompound tag) {
        artentMoney = tag.getLong("money");
        canEditTrades = tag.getBoolean("canEditTrades");
        try {
            Inventories.readNbt(tag, traderSellInventory.items);
        } catch (Exception e) {
            traderSellInventory = new TraderSellInventory();
        }

        tradeInfo = new TradeInfo();
        tradeInfo.readNbt(tag);
        casterInfo = new CasterInfo();
        casterInfo.readNbt(tag);
        heroInfo = new HeroInfo();
        heroInfo.readNbt(tag);
    }

    public static ArtentPlayerState createFromNbt(NbtCompound tag) {
        var state = new ArtentPlayerState();
        if (tag != null)
            state.readFromNbt(tag);
        return state;
    }

}
