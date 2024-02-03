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
		var contextTag = new NbtCompound();
		priceDeterminatorContext.writeToNbt(contextTag);

		var offerTag = new NbtCompound();
		InventoryUtils.writeInventoryNbt(offerTag, offer, true);

		tag.put("context", contextTag);
		tag.put("offer", offerTag);
		tag.putString("contextType", priceDeterminatorContext.getContextType());
		tag.putString("determinatorType", priceDeterminatorType);
	}

	public void readFromNbt(NbtCompound tag) throws NullPointerException {
		var contextTag = tag.getCompound("context");
		var offerTag = tag.getCompound("offer");
		var contextType = tag.getString("contextType");
		priceDeterminatorType = tag.getString("determinatorType");
		var context = Artent.ItemPriceDeterminators.factories.get(contextType).create();

		context.readFromNbt(contextTag);
		Inventories.readNbt(offerTag, offer);
	}
}
