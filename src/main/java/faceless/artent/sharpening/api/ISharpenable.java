package faceless.artent.sharpening.api;


import faceless.artent.registries.EnhancerRegistry;
import net.minecraft.item.ItemStack;

public interface ISharpenable {
    default int getLevel(ItemStack stack) {
        return stack.getOrCreateNbt().getInt("artent.level");
    }

    default void setLevel(ItemStack stack, int level) {
        stack.getOrCreateNbt().putInt("artent.level", level);
    }

    default int getSlotCount(ItemStack stack) {
        return stack.getOrCreateNbt().getInt("artent.slot.length");
    }

    default int getLastSlotId(ItemStack stack) {
        return stack.getOrCreateNbt().getInt("artent.slot.last");
    }

    default void setLastSlotId(ItemStack stack, int slot) {
        stack.getOrCreateNbt().putInt("artent.slot.last", slot);
    }

    default IEnhancer[] getSlots(ItemStack stack) {
        var count = getSlotCount(stack);
        var enhancers = new IEnhancer[count];
        for (int i = 0; i < count; i++) {
            enhancers[i] = getSlot(stack, i);
        }
        return enhancers;
    }

    default IEnhancer getSlot(ItemStack stack, int slot) {
        var enhancerId = stack.getOrCreateNbt().getInt("artent.slot" + slot);
        if (EnhancerRegistry.IdsToEnhancer.containsKey(enhancerId))
            return EnhancerRegistry.IdsToEnhancer.get(enhancerId);
        return null;
    }

    default boolean addEnhancer(ItemStack stack, IEnhancer enhancer) {
        var enhancerId = EnhancerRegistry.EnhancerToIds.get(enhancer);
        var slot = this.getLastSlotId(stack);
        if (slot < getSlotCount(stack)) {
            stack.getOrCreateNbt().putInt("artent.slot" + slot, enhancerId);
            setLastSlotId(stack, slot + 1);
            return true;
        }
        return false;
    }

    default void setSlotCount(ItemStack stack, int slot) {
        stack.getOrCreateNbt().putInt("artent.slot.length", slot);
    }
}