package faceless.artent.brewing.mixin;

import faceless.artent.objects.ModPotionEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class FeatherFallMixin {
	@Inject(method = "tick", at = @At("RETURN"))
	private void onTick(CallbackInfo ci) {
		var living = (LivingEntity) (Object) this;

		// These effects here because they need to be able to remove effects and effectQueue can't help here
		if (living.hasStatusEffect(ModPotionEffects.ANTIDOTE)) {
			var potion = living.getStatusEffect(ModPotionEffects.ANTIDOTE);
			if (potion == null)
				return;
			var level = potion.getAmplifier() + 1;
			var duration = potion.getDuration();
			checkAndClearPoison(living, level, duration);
		}
		if (living.hasStatusEffect(ModPotionEffects.FERMENTED_ANTIDOTE)) {
			var potion = living.getStatusEffect(ModPotionEffects.ANTIDOTE);
			if (potion == null)
				return;
			var level = potion.getAmplifier() + 1;
			checkAndClearPoison(living, level, 0);
			living.removeStatusEffect(ModPotionEffects.FERMENTED_ANTIDOTE);
		}
	}

	private void checkAndClearPoison(LivingEntity living, int level, int duration) {
		if (!living.hasStatusEffect(StatusEffects.POISON) || duration % 15 != 0)
			return;
		var poison = living.getStatusEffect(StatusEffects.POISON);
		if (poison != null && poison.getAmplifier() + 1 <= level) {
			living.removeStatusEffect(StatusEffects.POISON);
		}
	}
}