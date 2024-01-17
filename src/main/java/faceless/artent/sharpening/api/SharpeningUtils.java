package faceless.artent.sharpening.api;

import net.minecraft.item.ItemStack;

public class SharpeningUtils {
	public static int getItemLevel(ItemStack stack) {
		if (stack.getItem() instanceof ISharpenable sharpenable) {
			return sharpenable.getLevel(stack);
		}
		return 0;
	}

	public static void setItemLevel(ItemStack stack, int level) {
		if (stack.getItem() instanceof ISharpenable sharpenable) {
			sharpenable.setLevel(stack, level);
		}
	}

	public static int getSlotsCount(ItemStack stack) {
		if (stack.getItem() instanceof ISharpenable sharpenable) {
			return sharpenable.getSlotCount(stack);
		}
		return 0;
	}

	public static void setSlotsCount(ItemStack stack, int slot) {
		if (stack.getItem() instanceof ISharpenable sharpenable) {
			sharpenable.setSlotCount(stack, slot);
		}
	}

	public static boolean addEnhancer(ItemStack stack, IEnhancer enhancer) {
		if (stack.getItem() instanceof ISharpenable sharpenable) {
			return sharpenable.addEnhancer(stack, enhancer);
		}
		return false;
	}

	public static IEnhancer[] getEnhancers(ItemStack stack) {
		if (stack.getItem() instanceof ISharpenable sharpenable) {
			return sharpenable.getSlots(stack);
		}
		return new IEnhancer[0];
	}

	public static boolean hasSlots(ItemStack stack) {
		return stack.getOrCreateNbt().contains("artent.slot.length");
	}
}