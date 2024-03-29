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

	public void writeToNbt(NbtCompound tag) {
		var tradeInfoTag = new NbtCompound();

		var contextTag = new NbtCompound();
		priceDeterminatorContext.writeToNbt(contextTag);

		var offerTag = new NbtCompound();
		InventoryUtils.writeInventoryNbt(offerTag, offer, true);

		tradeInfoTag.put("context", contextTag);
		tradeInfoTag.put("offer", offerTag);
		tradeInfoTag.putString("contextType", priceDeterminatorContext.getContextType());
		tradeInfoTag.putString("determinatorType", priceDeterminatorType);

		tag.put("tradeInfo", tradeInfoTag);
	}

	public void readFromNbt(NbtCompound tag) throws NullPointerException {
		if (!tag.contains("tradeInfo"))
			return;
		var tradeInfoTag = tag.getCompound("tradeInfo");

		var contextTag = tradeInfoTag.getCompound("context");
		var offerTag = tradeInfoTag.getCompound("offer");
		var contextType = tradeInfoTag.getString("contextType");
		priceDeterminatorType = tradeInfoTag.getString("determinatorType");
		var context = Artent.ItemPriceDeterminators.factories.get(contextType).create();

		context.readFromNbt(contextTag);
		Inventories.readNbt(offerTag, offer);
	}
}
