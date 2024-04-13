package faceless.artent.trading.api;

import faceless.artent.Artent;
import faceless.artent.api.inventory.InventoryUtils;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;

public class TradeInfo {
    public DefaultedList<ItemStack> offer = DefaultedList.ofSize(18, ItemStack.EMPTY);
    public IPriceDeterminatorContext priceDeterminatorContext;
    public String priceDeterminatorType;

    public void writeNbt(NbtCompound tag) {
        var tradeInfoTag = new NbtCompound();

        var contextTag = new NbtCompound();
        if (priceDeterminatorContext != null)
            priceDeterminatorContext.writeToNbt(contextTag);

        var offerTag = new NbtCompound();
        InventoryUtils.writeNbt(offerTag, offer, true);

        tradeInfoTag.put("context", contextTag);
        tradeInfoTag.put("offer", offerTag);
        if (priceDeterminatorContext != null && priceDeterminatorType != null) {
            tradeInfoTag.putString("contextType", priceDeterminatorContext.getContextType());
            tradeInfoTag.putString("determinatorType", priceDeterminatorType);
        }

        tag.put("tradeInfo", tradeInfoTag);
    }

    public void readNbt(NbtCompound tag) throws NullPointerException {
        if (!tag.contains("tradeInfo"))
            return;
        var tradeInfoTag = tag.getCompound("tradeInfo");

        var contextTag = tradeInfoTag.getCompound("context");
        var offerTag = tradeInfoTag.getCompound("offer");
        var contextType = tradeInfoTag.getString("contextType");
        priceDeterminatorType = tradeInfoTag.getString("determinatorType");
        var contextFactory = Artent.ItemPriceDeterminators.factories.get(contextType);
        if (contextFactory != null) {
            var context = contextFactory.create();
            context.readFromNbt(contextTag);
            priceDeterminatorContext = context;
        }
        Inventories.readNbt(offerTag, offer);
    }
}
