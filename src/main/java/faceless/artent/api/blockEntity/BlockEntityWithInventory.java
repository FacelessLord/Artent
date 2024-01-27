package faceless.artent.api.blockEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

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
		onRemoveStack(slot);
		setStack(slot, ItemStack.EMPTY);
		return stack;
	}

	public abstract void onContentChanged();

	@Override
	public void setStack(int slot, ItemStack stack) {
		items.set(slot, stack);
		onContentChanged();
		markDirty();
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

	@Override
	public void markDirty() {
		super.markDirty();
		//noinspection DataFlowIssue
		world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
	}

	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}

	@Override
	public NbtCompound toInitialChunkDataNbt() {
		return createNbt();
	}

	// Custom "implementation" because Inventories.writeNbt doesn't save empty ItemStacks
	public static void writeInventoryNbt(NbtCompound nbt, DefaultedList<ItemStack> stacks, boolean setIfEmpty) {
		NbtList nbtList = new NbtList();
		for (int i = 0; i < stacks.size(); ++i) {
			ItemStack itemStack = stacks.get(i);
			NbtCompound nbtCompound = new NbtCompound();
			nbtCompound.putByte("Slot", (byte) i);
			itemStack.writeNbt(nbtCompound);
			nbtList.add(nbtCompound);
		}
		if (!nbtList.isEmpty() || setIfEmpty) {
			nbt.put("Items", nbtList);
		}
	}
}