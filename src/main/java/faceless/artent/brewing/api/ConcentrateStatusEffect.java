package faceless.artent.brewing.api;

import faceless.artent.api.math.Color;
import faceless.artent.objects.ModPotionEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;

public class ConcentrateStatusEffect extends StatusEffect {

	private boolean isInstant = true;

	public ConcentrateStatusEffect(StatusEffectCategory category, Color color) {
		super(category, color.toHex());
	}

	public ConcentrateStatusEffect(StatusEffectCategory category, Color color, boolean isInstant) {
		super(category, color.toHex());
		this.isInstant = isInstant;
	}

	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
	}

	public void applyInstantEffect(@Nullable Entity source, @Nullable Entity attacker, LivingEntity target, int amplifier, double proximity) {
		if (this == ModPotionEffects.FERMENTED_SATURATION) {
			if (target instanceof PlayerEntity player)
				player.getHungerManager().add(10 * amplifier, 5);
		}
		if (this == ModPotionEffects.INSTANT_HEALING) {
			target.heal(8f * amplifier);
		}
		if (this == ModPotionEffects.INSTANT_HARM) {
			target.damage(target.getDamageSources().magic(), 4 * amplifier);
		}
		if (this == ModPotionEffects.FERMENTED_LIQUID_FLAME) {
			target.setFireTicks(200 * amplifier);
		}
	}

	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return super.canApplyUpdateEffect(duration, amplifier);
	}

	@Override
	public boolean isInstant() {
		return isInstant;
	}
}