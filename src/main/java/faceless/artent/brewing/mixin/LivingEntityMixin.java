package faceless.artent.brewing.mixin;

import faceless.artent.brewing.api.ArtentStatusEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@Inject(at = @At("HEAD"), method = "onStatusEffectRemoved")
	protected void onStatusEffectRemoved(StatusEffectInstance effect, CallbackInfo ci) {
		var living = get();
		if (!living.getWorld().isClient) {
			var type = effect.getEffectType();
			if (type instanceof ArtentStatusEffect artentStatusEffect) {
				artentStatusEffect.onEffectRemoved(living, living.getAttributes(), effect.getAmplifier());
				this.updateAttributes();
			}
		}
	}

	@Shadow
	private boolean effectsChanged = true;

	@Inject(at = @At("HEAD"), method = "onStatusEffectUpgraded")
	protected void onStatusEffectUpgraded(StatusEffectInstance effect, boolean reapplyEffect, @Nullable Entity source, CallbackInfo ci) {
		this.effectsChanged = true;
		var living = get();
		if (reapplyEffect && !living.getWorld().isClient) {
			var type = effect.getEffectType();
			if (!(type instanceof ArtentStatusEffect artentStatusEffect)) {
				return;
			}
			artentStatusEffect.onEffectRemoved(living, living.getAttributes(), effect.getAmplifier());
			this.updateAttributes();
		}
	}

	@Shadow
	private void updateAttributes() {
	}

	@Unique
	private LivingEntity get() {
		return (LivingEntity) (Object) this;
	}
}
