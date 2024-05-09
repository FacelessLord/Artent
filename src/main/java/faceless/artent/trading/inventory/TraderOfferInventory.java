package faceless.artent.trading.inventory;

import faceless.artent.api.inventory.ArtentInventory;
import faceless.artent.trading.api.TradeInfo;
import faceless.artent.trading.blockEntities.TraderBlockEntity;
import net.minecraft.item.ItemStack;

public class TraderOfferInventory extends ArtentInventory {
    private final TraderBlockEntity trader;
    public TradeInfo tradeInfo;

    public TraderOfferInventory(TraderBlockEntity trader) {
        this.trader = trader;
    }

    @Override
    public int size() {
        return 18;
    }

    @Override
    public void markDirty() {
        trader.markDirty();
    }

    @Override
    public boolean canTakeStackFromSlot(@SuppressWarnings("unused") int slot) {
        return true;
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        var stack = getStack(slot);
        if (stack.isEmpty())
            return ItemStack.EMPTY;
        return stack.copyWithCount(amount);
    }

    @Override
    protected void onRemoveStack(int slot) {
        super.onRemoveStack(slot);

        setStack(slot, tradeInfo.offer.get(slot).copy());
    }

    @Override
    public void clear() {
    }

    public void setTradeInfo(TradeInfo tradeInfo) {
        this.tradeInfo = tradeInfo;
        for (int i = 0; i < Math.min(size(), tradeInfo.offer.size()); i++) {
            setStack(i, tradeInfo.offer.get(i).copy());
        }
    }
}
