package faceless.artent.sharpening.api;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

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
		return stack.hasNbt() && stack.getOrCreateNbt().contains("artent.slot.length");
	}

	public static Stream<IEnhancer> getNonEmptySlots(ItemStack stack) {
		if (stack.getItem() instanceof ISharpenable sharpenable) {
			return Arrays.stream(sharpenable.getSlots(stack)).filter(Objects::nonNull);
		}
		return Stream.<IEnhancer>builder().build();
	}

	public static ItemStack getCarriedItem(LivingEntity livingAttacker) {
		var attackerItem = ItemStack.EMPTY;
		if (livingAttacker instanceof MobEntity mob) {
			attackerItem = mob.getEquippedStack(mob.getActiveHand() == Hand.OFF_HAND ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND);
		}
		if (livingAttacker instanceof PlayerEntity player) {
			attackerItem = player.getEquippedStack(player.getActiveHand() == Hand.OFF_HAND ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND);
		}
		if (SharpeningUtils.hasSlots(attackerItem))
			return attackerItem;
		return ItemStack.EMPTY;
	}
}