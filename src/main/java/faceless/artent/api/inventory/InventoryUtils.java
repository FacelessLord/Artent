package faceless.artent.api.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.collection.DefaultedList;

public class InventoryUtils {

	// Custom "implementation" because Inventories.writeNbt doesn't save empty ItemStacks which can break renders
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
