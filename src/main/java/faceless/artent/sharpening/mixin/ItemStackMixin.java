package faceless.artent.sharpening.mixin;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import faceless.artent.api.AttributeUuids;
import faceless.artent.sharpening.api.SharpeningUtils;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin {
	@Inject(at = @At("TAIL"), method = "getAttributeModifiers", cancellable = true)
	public void getAttributeModifiers(EquipmentSlot slot, CallbackInfoReturnable<Multimap<EntityAttribute, EntityAttributeModifier>> cir) {
		var map = cir.getReturnValue();
		var newMap = HashMultimap.create(map);
		var stack = (ItemStack) (Object) this;
		var level = SharpeningUtils.getItemLevel(stack);
		if (level > 0 && slot.equals(EquipmentSlot.MAINHAND)) {
			if (stack.getItem() instanceof ArmorItem) {
				newMap.put(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, new EntityAttributeModifier(AttributeUuids.Levelup, "Levelup",
					0.1 * level, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
				newMap.put(EntityAttributes.GENERIC_ARMOR, new EntityAttributeModifier(AttributeUuids.Levelup, "Levelup",
					0.1 * level, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
			} else if (stack.getItem() instanceof ToolItem) {
				newMap.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(AttributeUuids.Levelup, "Levelup",
					0.1 * level, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
				newMap.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(AttributeUuids.Levelup, "Levelup",
					0.1 * level, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
			}
			cir.setReturnValue(newMap);
		}
	}
}