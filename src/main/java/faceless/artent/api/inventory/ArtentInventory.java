package faceless.artent.api.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;

public abstract class ArtentInventory implements Inventory {
	public DefaultedList<ItemStack> items;

	public ArtentInventory() {
		items = DefaultedList.ofSize(size(), ItemStack.EMPTY);
	}

	@Override
	public abstract int size();

	@Override
	public boolean isEmpty() {
		return items.stream().allMatch(ItemStack::isEmpty);
	}

	@Override
	public ItemStack getStack(int slot) {
		return items.get(slot);
	}

	public boolean canTakeStackFromSlot(@SuppressWarnings("unused") int slot) {
		return true;
	}

	public void onContentChanged() {
	}

	@Override
	public ItemStack removeStack(int slot, int amount) {
		if (!canTakeStackFromSlot(slot))
			return ItemStack.EMPTY;
		var stack = getStack(slot);
		if (stack.getCount() <= amount) {
			removeStack(slot);
			return stack;
		}
		var newStack = stack.copy();
		newStack.setCount(amount);
		stack.setCount(stack.getCount() - amount);
		onContentChanged();
		markDirty();
		return newStack;
	}

	protected void onRemoveStack(int slot) {
	}

	@Override
	public ItemStack removeStack(int slot) {
		var stack = getStack(slot);
		setStack(slot, ItemStack.EMPTY);
		onRemoveStack(slot);
		return stack;
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		items.set(slot, stack);
		onContentChanged();
		markDirty();
	}

	@Override
	public int getMaxCountPerStack() {
		return Inventory.super.getMaxCountPerStack();
	}

	@Override
	public void markDirty() {

	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return true;
	}

	@Override
	public void clear() {
		items.clear();
		onContentChanged();
		markDirty();
	}

	public void readNbt(NbtCompound nbt) {
		Inventories.readNbt(nbt, this.items);
	}

	public void writeNbt(NbtCompound tag) {
		InventoryUtils.writeNbt(tag, items, true);
	}
}
