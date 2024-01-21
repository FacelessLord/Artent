package faceless.artent.api.blockEntity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

public abstract class BlockEntityWithInventory extends BlockEntity implements Inventory {
	public DefaultedList<ItemStack> items;

	public BlockEntityWithInventory(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		items = DefaultedList.ofSize(size(), ItemStack.EMPTY);
	}

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

	@Override
	public ItemStack removeStack(int slot, int amount) {
		if (!canTakeStackFromSlot(slot))
			return ItemStack.EMPTY;
		var stack = getStack(slot);
		if (stack.getCount() < amount) {
			setStack(slot, ItemStack.EMPTY);
			return stack;
		}
		var newStack = stack.copy();
		newStack.setCount(amount);
		stack.setCount(stack.getCount() - amount);
		markDirty();
		return newStack;
	}

	@Override
	public ItemStack removeStack(int slot) {
		var stack = getStack(slot);
		setStack(slot, ItemStack.EMPTY);
		return stack;
	}

	public abstract void onContentChanged();

	@Override
	public void setStack(int slot, ItemStack stack) {
		items.set(slot, stack);
		markDirty();
	}

	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return true;
	}

	@Override
	public void clear() {
		items.clear();
		markDirty();
	}

	@Override
	public void markDirty() {
		super.markDirty();
		onContentChanged();
	}
}