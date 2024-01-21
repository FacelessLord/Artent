package faceless.artent.sharpening.item.upgrades;

import faceless.artent.sharpening.item.EnhancerItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Formatting;

public class PoisonBottle extends EnhancerItem {
	public PoisonBottle(Settings settings) {
		super(Formatting.GREEN, "upgrade/poison_bottle", settings);
	}

	@Override
	public void onEntityDamaged(ItemStack tool, LivingEntity attacker, LivingEntity target, float amount) {
		var poisonEffect = new StatusEffectInstance(StatusEffects.POISON, 50);
		target.addStatusEffect(poisonEffect, attacker);
	}
}