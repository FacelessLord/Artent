package faceless.artent.sharpening.mixin;

import faceless.artent.sharpening.api.SharpeningUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class AttackMixin {
	@Inject(method = "tryAttack", at = @At("TAIL"))
	public void tryAttack(Entity target, CallbackInfoReturnable<Boolean> cir) {
	}

	@Inject(method = "damage", at = @At("TAIL"))
	public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		var living = (LivingEntity) (Object) this;
		var attacker = source.getAttacker();
		if (!(attacker instanceof LivingEntity livingAttacker))
			return;

		var attackerItem = SharpeningUtils.getCarriedItem(livingAttacker);
		if (attackerItem != null && !attackerItem.isEmpty()) {
			SharpeningUtils
				.getNonEmptySlots(attackerItem)
				.forEach(e -> e.onEntityDamaged(attackerItem, livingAttacker, living, amount));
		}

//        checkBarrierEffects(living, livingAttacker);
//        checkBarrierEffects(livingAttacker, living);

//        if (livingAttacker.hasStatusEffect(ModPotionEffects.VAMPIRISM)) {
//            var potion = livingAttacker.getStatusEffect(ModPotionEffects.VAMPIRISM);
//            if (potion != null)
//                livingAttacker.heal(amount * 0.3f * (1 + potion.getAmplifier()));
//        }
//        if (livingAttacker.hasStatusEffect(ModPotionEffects.FERMENTED_VAMPIRISM)) {
//            var potion = livingAttacker.getStatusEffect(ModPotionEffects.FERMENTED_VAMPIRISM);
//            if (potion != null)
//                livingAttacker.heal(amount * 0.6f * (1 + potion.getAmplifier()));
//        }
	}

//    private void checkBarrierEffects(LivingEntity living, LivingEntity livingAttacker) {
//        if (living.hasStatusEffect(ModPotionEffects.LIQUID_FLAME)) {
//            var potion = living.getStatusEffect(ModPotionEffects.LIQUID_FLAME);
//            if (potion != null)
//                livingAttacker.setOnFireFor((potion.getAmplifier() + 1) * 20);
//        }
//        if (living.hasStatusEffect(ModPotionEffects.HOLY_WATER)) {
//            var potion = living.getStatusEffect(ModPotionEffects.HOLY_WATER);
//            if (potion != null && livingAttacker.getGroup() == EntityGroup.UNDEAD) {
//                livingAttacker.timeUntilRegen = 0;
//                livingAttacker.damage(DamageSource.MAGIC, 4 * (1 + potion.getAmplifier()));
//            }
//        }
//        if (living.hasStatusEffect(ModPotionEffects.FERMENTED_HOLY_WATER)) {
//            var potion = living.getStatusEffect(ModPotionEffects.FERMENTED_HOLY_WATER);
//            if (potion != null && livingAttacker.getGroup() == EntityGroup.UNDEAD) {
//                livingAttacker.timeUntilRegen = 0;
//                livingAttacker.damage(DamageSource.MAGIC, 10 * (1 + potion.getAmplifier()));
//            }
//        }
//    }

//    @ModifyArg(method = "damage", index = 1, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;applyDamage(Lnet/minecraft/entity/damage/DamageSource;F)V"))
//    public float damageBeforeApply(float amount) {
//        var living = (LivingEntity) (Object) this;
//        if (living.hasStatusEffect(ModPotionEffects.STONE_SKIN)) {
//            var potion = living.getStatusEffect(ModPotionEffects.STONE_SKIN);
//            if (potion != null)
//                return Math.max(amount * (1 - 0.4f * (1 + potion.getAmplifier())), 0);
//        }
//        return amount;
//    }
}