package faceless.artent.brewing.entity;

import faceless.artent.brewing.api.AlchemicalPotionUtil;
import faceless.artent.objects.ModEntities;
import faceless.artent.objects.ModItems;
import net.minecraft.block.AbstractCandleBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.passive.AxolotlEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class ThrowablePotionPhialEntity extends ThrownItemEntity {
	public static final Predicate<LivingEntity> WATER_HURTS = LivingEntity::hurtByWater;

	public ThrowablePotionPhialEntity(EntityType<? extends ThrowablePotionPhialEntity> entityType, World world) {
		super(entityType, world);
	}

	public ThrowablePotionPhialEntity(World world, LivingEntity owner) {
		super(ModEntities.POTION_PHIAL, owner, world);
	}

	public ThrowablePotionPhialEntity(World world, double x, double y, double z) {
		super(ModEntities.POTION_PHIAL, x, y, z, world);
	}

	@Override
	protected Item getDefaultItem() {
		return ModItems.PotionPhialExplosive;
	}

	@Override
	protected float getGravity() {
		return 0.05f;
	}

	@Override
	protected void onBlockHit(BlockHitResult blockHitResult) {
		super.onBlockHit(blockHitResult);
		if (this.getWorld().isClient) {
			return;
		}
		ItemStack itemStack = this.getStack();
		Potion potion = AlchemicalPotionUtil.getPotion(itemStack);
		List<StatusEffectInstance> list = AlchemicalPotionUtil.getPotionEffects(itemStack);
		Direction direction = blockHitResult.getSide();
		BlockPos blockPos = blockHitResult.getBlockPos();
		BlockPos blockPos2 = blockPos.offset(direction);
		if (potion == Potions.WATER && list.isEmpty()) {
			this.extinguishFire(blockPos2);
			this.extinguishFire(blockPos2.offset(direction.getOpposite()));
			for (Direction direction2 : Direction.Type.HORIZONTAL) {
				this.extinguishFire(blockPos2.offset(direction2));
			}
		}
	}

	@Override
	protected void onCollision(HitResult hitResult) {
		super.onCollision(hitResult);
		if (this.getWorld().isClient) {
			return;
		}
		ItemStack itemStack = this.getStack();
		Potion potion = AlchemicalPotionUtil.getPotion(itemStack);
		List<StatusEffectInstance> list = AlchemicalPotionUtil.getPotionEffects(itemStack);
		if (potion == Potions.WATER && list.isEmpty()) {
			this.damageEntitiesHurtByWater();
		} else if (!list.isEmpty()) {
			this.applySplashPotion(list, hitResult.getType() == HitResult.Type.ENTITY ? ((EntityHitResult) hitResult).getEntity() : null);
		}
		int i = potion.hasInstantEffect() ? WorldEvents.INSTANT_SPLASH_POTION_SPLASHED : WorldEvents.SPLASH_POTION_SPLASHED;
		this.getWorld().syncWorldEvent(i, this.getBlockPos(), AlchemicalPotionUtil.getColor(itemStack));
		this.discard();
	}

	private void damageEntitiesHurtByWater() {
		Box box = this.getBoundingBox().expand(4.0, 2.0, 4.0);
		List<LivingEntity> list = this.getWorld().getEntitiesByClass(LivingEntity.class, box, WATER_HURTS);
		if (!list.isEmpty()) {
			for (LivingEntity livingEntity : list) {
				double d = this.squaredDistanceTo(livingEntity);
				if (!(d < 16.0) || !livingEntity.hurtByWater()) continue;
				livingEntity.damage(livingEntity.getDamageSources().magic(), 1.0f);
			}
		}
		List<AxolotlEntity> list2 = this.getWorld().getNonSpectatingEntities(AxolotlEntity.class, box);
		for (AxolotlEntity d : list2) {
			d.hydrateFromPotion();
		}
	}

	private void applySplashPotion(List<StatusEffectInstance> statusEffects, @Nullable Entity entity) {
		Box box = this.getBoundingBox().expand(4.0, 2.0, 4.0);
		List<LivingEntity> list = this.getWorld().getNonSpectatingEntities(LivingEntity.class, box);
		if (!list.isEmpty()) {
			Entity entity2 = this.getEffectCause();
			for (LivingEntity livingEntity : list) {
				double d;
				if (!livingEntity.isAffectedBySplashPotions() || !((d = this.squaredDistanceTo(livingEntity)) < 16.0))
					continue;
				double e = 1.0 - Math.sqrt(d) / 4.0;
				if (livingEntity == entity) {
					e = 1.0;
				}
				for (StatusEffectInstance statusEffectInstance : statusEffects) {
					StatusEffect statusEffect = statusEffectInstance.getEffectType();
					if (statusEffect.isInstant()) {
						statusEffect.applyInstantEffect(this, this.getOwner(), livingEntity, statusEffectInstance.getAmplifier(), e);
						continue;
					}
					int i = (int) (e * (double) statusEffectInstance.getDuration() + 0.5);
					if (i <= 20) continue;
					livingEntity.addStatusEffect(new StatusEffectInstance(statusEffect, i, statusEffectInstance.getAmplifier(), statusEffectInstance.isAmbient(), statusEffectInstance.shouldShowParticles()), entity2);
				}
			}
		}
	}

	private void extinguishFire(BlockPos pos) {
		var world = getWorld();
		BlockState blockState = world.getBlockState(pos);
		if (blockState.isIn(BlockTags.FIRE)) {
			world.removeBlock(pos, false);
		} else if (AbstractCandleBlock.isLitCandle(blockState)) {
			AbstractCandleBlock.extinguish(null, blockState, world, pos);
		} else if (CampfireBlock.isLitCampfire(blockState)) {
			world.syncWorldEvent(null, WorldEvents.FIRE_EXTINGUISHED, pos, 0);
			CampfireBlock.extinguish(this.getOwner(), world, pos, blockState);
			world.setBlockState(pos, blockState.with(CampfireBlock.LIT, false), Block.NOTIFY_ALL);
		}
	}
}