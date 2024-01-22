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

import java.util.ArrayList;
import java.util.List;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	// No need to serialize this value
	@Unique
	private final List<StatusEffectInstance> statusEffectQueue = new ArrayList<>();

	@Inject(at = @At("HEAD"), method = "tickStatusEffects")
	protected void tickStatusEffects(CallbackInfo ci) {
		var living = get();
		if (living.getWorld() != null && !living.getWorld().isClient) {
			for (StatusEffectInstance effect : statusEffectQueue) {
				living.addStatusEffect(effect);
			}
			statusEffectQueue.clear();
		}
	}

	@Inject(at = @At("HEAD"), method = "onStatusEffectRemoved")
	protected void onStatusEffectRemoved(StatusEffectInstance effect, CallbackInfo ci) {
		var living = get();
		if (!living.getWorld().isClient) {
			var type = effect.getEffectType();
			if (type instanceof ArtentStatusEffect artentStatusEffect) {
				artentStatusEffect.onEffectRemoved(living, living.getAttributes(), effect.getAmplifier(), statusEffectQueue);
				this.updateAttributes();
			}
		}
	}

	@Inject(at = @At("HEAD"), method = "onStatusEffectUpgraded")
	protected void onStatusEffectUpgraded(StatusEffectInstance effect, boolean reapplyEffect, @Nullable Entity source, CallbackInfo ci) {
		var living = get();
		if (reapplyEffect && !living.getWorld().isClient) {
			var type = effect.getEffectType();
			if (!(type instanceof ArtentStatusEffect artentStatusEffect)) {
				return;
			}
			artentStatusEffect.onEffectRemoved(living, living.getAttributes(), effect.getAmplifier(), statusEffectQueue);
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
