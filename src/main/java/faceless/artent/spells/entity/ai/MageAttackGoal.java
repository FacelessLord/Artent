package faceless.artent.spells.entity.ai;

import faceless.artent.spells.api.Affinity;
import faceless.artent.spells.api.ICaster;
import faceless.artent.spells.api.ManaUtils;
import faceless.artent.spells.api.Spell;
import faceless.artent.spells.entity.MageEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Hand;

import java.util.List;

public class MageAttackGoal extends Goal {
	private final MageEntity mage;
	public ICaster caster;
	public List<Affinity> affinities;
	private final float squaredRange;
	private final float speed;

	public int castTime = 0;
	private int cooldown = -1;
	private int targetSeeingTicker;
	private boolean movingToLeft;
	private boolean backward;
	private int combatTicks = -1;

	private float oldYaw;
	private float oldPitch;

	public MageAttackGoal(MageEntity mage, ICaster caster, List<Affinity> affinities, float range, float speed) {
		this.mage = mage;
		this.caster = caster;
		this.affinities = affinities;
		this.squaredRange = range * range;
		this.speed = speed;
	}

	@Override
	public boolean canStart() {
		var spell = caster.getCurrentSpell();
		if (spell == null)
			return false;
		var neededMana = 0;
		if (spell.isSingleCastAction())
			neededMana = ManaUtils.evaluateManaToConsume(spell, this.affinities, Spell.ActionType.SingleCast);
		if (spell.isTickAction())
			neededMana += ManaUtils.evaluateManaToConsume(spell, this.affinities, Spell.ActionType.Tick) * 10;
		return caster.getMana() >= neededMana && mage.getTarget() != null;
	}

	@Override
	public void start() {
		super.start();
		castTime = 0;
	}

	@Override
	public boolean shouldContinue() {
		return this.canStart() || caster.getHealthProportion() > 0.3f;
	}

	@Override
	public boolean shouldRunEveryTick() {
		return true;
	}

	public void tick() {
		super.tick();
		castTime++;
		var spell = caster.getCurrentSpell();
		var target = mage.getTarget();
		var wand = mage.getMainHandStack();
		if (target == null || wand == null)
			return;

		double distanceSqr = mage.squaredDistanceTo(target.getX(), target.getY(), target.getZ());
		boolean canSeeTarget = mage.getVisibilityCache().canSee(target);

		boolean seenTargetBefore = this.targetSeeingTicker > 0;
		if (canSeeTarget != seenTargetBefore) {
			this.targetSeeingTicker = 0;
		}
		if (canSeeTarget) {
			this.targetSeeingTicker++;
		} else {
			this.targetSeeingTicker--;
		}
		// TODO squaredRange depends on current spell

		if (distanceSqr > this.squaredRange || this.targetSeeingTicker < 20) {
			mage.getNavigation().startMovingTo(target, this.speed);
			this.combatTicks = -1;
		} else {
			mage.getNavigation().stop();
			this.combatTicks++;
		}
		if (this.combatTicks >= 20) {
			if (mage.getRandom().nextFloat() < 0.3) {
				this.movingToLeft = !this.movingToLeft;
			}
			if (mage.getRandom().nextFloat() < 0.3) {
				this.backward = !this.backward;
			}
			this.combatTicks = 0;
		}
		if (this.combatTicks > -1) {
			if (distanceSqr > (this.squaredRange * 0.75f)) {
				this.backward = false;
			} else if (distanceSqr < (this.squaredRange * 0.25f)) {
				this.backward = true;
			}
			mage.getMoveControl().strafeTo(this.backward ? -0.5f : 0.5f, this.movingToLeft ? 0.5f : -0.5f);
			Entity entity = mage.getControllingVehicle();
			if (entity instanceof MobEntity mobEntity) {
				mobEntity.lookAtEntity(target, 30.0f, 30.0f);
			}
			mage.lookAtEntity(target, 30.0f, 30.0f);
		} else {
			mage.getLookControl().lookAt(target, 30.0f, 30.0f);
		}
		if (mage.isUsingItem()) {
			if (!canSeeTarget && this.targetSeeingTicker < -60) {
				mage.clearActiveItem();
			} else if (canSeeTarget) {
				if (mage.getItemUseTime() >= 20) {
					if (spell.isSingleCastAction()) {
						var castMana = ManaUtils.evaluateManaToConsume(spell, this.affinities, Spell.ActionType.Tick);
						if (caster.consumeMana(castMana)) {
							prepareDirectedCast(target);
							wand.onStoppedUsing(mage.getWorld(), mage, wand.getMaxUseTime() - castTime);
//						spell.action(caster, mage.getWorld(), mage.getMainHandStack(), castTime);
							afterCast();
						}
					}

					mage.clearActiveItem();
					this.cooldown = mage.getCurrentSpell().cooldown;
				}
				if (spell.isTickAction()) {
					var tickMana = ManaUtils.evaluateManaToConsume(spell, this.affinities, Spell.ActionType.Tick);
					if (caster.consumeMana(tickMana)) {
						prepareDirectedCast(target);
						wand.usageTick(mage.getWorld(), mage, wand.getMaxUseTime() - castTime);
						//				spell.spellTick(caster, mage.getWorld(), mage.getMainHandStack(), castTime);
						afterCast();
					}
				}
			}
		} else if (--this.cooldown <= 0 && this.targetSeeingTicker >= -60) {
			mage.setCurrentHand(Hand.MAIN_HAND);
		}
	}

	private void prepareDirectedCast(LivingEntity target) {
		var attackDir = target.getEyePos().subtract(mage.getPos());
		var pitch = (float) -(Math.atan2(attackDir.y - 2, Math.hypot(attackDir.x, attackDir.z)) * 180 / Math.PI);
		var yaw = (float) -(Math.atan2(attackDir.z, -attackDir.x) * 180 / Math.PI - 90);
		oldYaw = mage.getYaw();
		oldPitch = mage.getPitch();

		mage.setYaw(yaw);
		mage.setPitch(pitch);
	}

	private void afterCast() {
		mage.setYaw(oldYaw);
		mage.setPitch(oldPitch);
	}

	@Override
	public void stop() {
		super.stop();
		mage.clearActiveItem();
		this.targetSeeingTicker = 0;
		this.cooldown = -1;
	}
}
