package faceless.artent.brewing.api;

import faceless.artent.api.math.Color;
import faceless.artent.objects.ModPotionEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.world.Heightmap.Type.MOTION_BLOCKING_NO_LEAVES;

public class ArtentStatusEffect extends StatusEffect {
	private boolean isInstant = false;

	public ArtentStatusEffect(StatusEffectCategory category, Color color) {
		super(category, color.toHex());
	}

	public ArtentStatusEffect(StatusEffectCategory category, Color color, boolean isInstant) {
		super(category, color.toHex());
		this.isInstant = isInstant;
	}

	public void onEffectRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		if (this == ModPotionEffects.FLIGHT && entity instanceof ServerPlayerEntity player) {
			player.interactionManager.getGameMode().setAbilities(player.getAbilities());
			player.sendAbilitiesUpdate();
		}
		if (this == ModPotionEffects.BERSERK) {
			onRemoved(entity.getAttributes());
			// TODO status effect queue
			entity.addStatusEffect(new StatusEffectInstance(ModPotionEffects.BERSERK_RECOIL, 30*20, amplifier, true, true));
		}
		if (this == ModPotionEffects.FLIGHT && entity instanceof ServerPlayerEntity player) {
			player.getAbilities().allowFlying = false;
			player.sendAbilitiesUpdate();
		}
	}

	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		if (entity == null)
			return;
		if (this == ModPotionEffects.FLIGHT && entity instanceof ServerPlayerEntity player) {
			player.getAbilities().allowFlying = true;
			player.sendAbilitiesUpdate();
		}
		if (this == ModPotionEffects.BERSERK) {
			super.applyUpdateEffect(entity, amplifier);
		}
		if (this == ModPotionEffects.FREEZING) {
			entity.extinguish();
		}
		if (this == ModPotionEffects.FAST_SWIMMING) {
			if (entity.isTouchingWater())
				onApplied(entity.getAttributes(), amplifier);
			else
				onRemoved(entity.getAttributes());
		}
		if (this == ModPotionEffects.REGENERATION) {
			if (entity.getGroup() == EntityGroup.UNDEAD) {
				entity.timeUntilRegen = 20;
				entity.damage(entity.getDamageSources().magic(), 1);
			} else
				entity.heal(1f);
		}
	}

	public void applyInstantEffect(@Nullable Entity source, @Nullable Entity attacker, LivingEntity target, int amplifier, double proximity) {
		if (this == ModPotionEffects.SURFACE_TELEPORTATION) {
			var pos = target.getBlockPos();
			var world = target.getWorld();
			if (world == null)
				return;

			var topPosition = world.getTopPosition(MOTION_BLOCKING_NO_LEAVES, pos);
			var diff = topPosition.subtract(pos).getY();
			if (diff > 0 && diff < 64 * (amplifier + 1)) {
				target.teleport(topPosition.getX(), topPosition.getY() + 1, topPosition.getZ());
			}
		}
	}

	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return super.canApplyUpdateEffect(duration, amplifier)
			|| this == ModPotionEffects.FLIGHT
			|| this == ModPotionEffects.FREEZING && duration % 10 == 0
			|| this == ModPotionEffects.BERSERK && duration == 600
			|| this == ModPotionEffects.REGENERATION && ((20 >> amplifier) == 0 || duration % (20 >> amplifier) == 0)
			|| this == ModPotionEffects.FAST_SWIMMING && (duration % 10 == 0);
	}

	@Override
	public boolean isInstant() {
		return isInstant;
	}
}