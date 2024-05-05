package faceless.artent.spells.entity;

import faceless.artent.leveling.api.ILeveledMob;
import faceless.artent.objects.ModItems;
import faceless.artent.objects.ModSpells;
import faceless.artent.spells.api.ICaster;
import faceless.artent.spells.api.Spell;
import faceless.artent.spells.entity.ai.MageAttackGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Arm;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.UUID;

public class MageEntity extends HostileEntity implements ICaster, GeoEntity, RangedAttackMob {
	protected static final RawAnimation STAND_ANIMATION = RawAnimation.begin().thenLoop("animation.mage.stand");
	protected static final RawAnimation WALK_ANIMATION = RawAnimation.begin().thenPlay("animation.mage.walk");
	protected static final RawAnimation CAST_ANIMATION = RawAnimation.begin().thenPlay("animation.mage.cast");

	private static final TrackedData<String> ANIMATION_STATE = DataTracker.registerData(MageEntity.class,
	  TrackedDataHandlerRegistry.STRING);
	private static final TrackedData<Integer> MANA = DataTracker.registerData(MageEntity.class,
	  TrackedDataHandlerRegistry.INTEGER);

	public UUID mageId;
	private int maxMana = 100;
	private boolean isCasting = false;
	private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
	public Spell[] spells;

	public MageEntity(EntityType<MageEntity> entityType, World world) {
		super(entityType, world);
		mageId = UUID.randomUUID();
		spells = new Spell[]{ModSpells.Gust, ModSpells.Flamethrower, ModSpells.LightSword};
	}

	@Override
	protected void initGoals() {
		this.goalSelector.add(5, new WanderAroundFarGoal(this, 1f));
		this.goalSelector.add(6, new LookAtEntityGoal(this, SkeletonEntity.class, 16.0f));
		this.goalSelector.add(6, new LookAroundGoal(this));
		this.goalSelector.add(4, new MageAttackGoal(this, this, new ArrayList<>(0), 8, 1));
		this.targetSelector.add(1, new RevengeGoal(this));
		this.targetSelector.add(3, new ActiveTargetGoal<>(this, SkeletonEntity.class, false)); // TODO select mobs not in a team
		this.targetSelector.add(3, new ActiveTargetGoal<>(this, ZombieEntity.class, false));
		this.targetSelector.add(3, new ActiveTargetGoal<>(this, SpiderEntity.class, false));
		this.targetSelector.add(3, new ActiveTargetGoal<>(this, CreeperEntity.class, false));
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		getDataTracker().startTracking(MANA, 0);
		getDataTracker().startTracking(ANIMATION_STATE, MageAnimationState.Stand.name());
	}

	public static DefaultAttributeContainer.Builder createLivingAttributes() {
		return DefaultAttributeContainer
		  .builder()
		  .add(EntityAttributes.GENERIC_MAX_HEALTH)
		  .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE)
		  .add(EntityAttributes.GENERIC_MOVEMENT_SPEED)
		  .add(EntityAttributes.GENERIC_ARMOR)
		  .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS)
		  .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32)
		  .add(EntityAttributes.GENERIC_MAX_ABSORPTION);
	}


	public MageAnimationState getAnimationState() {
		return MageAnimationState.valueOf(getDataTracker().get(ANIMATION_STATE));
	}

	public void setAnimationState(MageAnimationState state) {
		getDataTracker().set(ANIMATION_STATE, state.name());
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>(this, "ROTATION", 5, this::rotationController));
	}

	protected PlayState rotationController(final AnimationState<MageEntity> event) {
		return switch (getAnimationState()) {
			case Stand -> event.setAndContinue(STAND_ANIMATION);
			case Walk -> event.setAndContinue(WALK_ANIMATION);
			case Cast -> event.setAndContinue(CAST_ANIMATION);
			default -> event.setAndContinue(STAND_ANIMATION);
		};
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.geoCache;
	}

	@Override
	public Iterable<ItemStack> getArmorItems() {
		return new ArrayList<>(0);
	}

	@Override
	public ItemStack getEquippedStack(EquipmentSlot slot) {
		return switch (slot) {
			case MAINHAND -> new ItemStack(ModItems.StaffOfLight, 1);
			case OFFHAND -> new ItemStack(ModItems.ApprenticeSpellbook, 1);
			default -> ItemStack.EMPTY;
		};

	}

	@Override
	public void equipStack(EquipmentSlot slot, ItemStack stack) {

	}

	@Override
	public Arm getMainArm() {
		return Arm.RIGHT;
	}

	@Override
	public boolean consumeMana(int mana) {
		if (this.getMana() >= mana) {
			setMana(this.getMana() - mana);
			return true;
		}
		return false;
	}

	@Override
	public void restoreMana() {
		setMana(getMaxMana());
	}

	@Override
	public int getPotency() {
		return 2;
	}

	@Override
	public UUID getCasterUuid() {
		return mageId;
	}

	@Override
	public Vec3d getCasterRotation() {
		return getRotationVector();
	}

	@Override
	public Vec3d getCasterPosition() {
		return getPos();
	}

	public void setIsCasting(boolean isCasting) {
		this.isCasting = isCasting;
	}

	@Override
	public void tick() {
		super.tick();
		if (getMana() < getMaxMana()) {
			setMana(Math.min(getMaxMana(), getMana() + ((ILeveledMob) this).getLevel()));
		}
		if (isUsingItem()) {
			setAnimationState(MageAnimationState.Cast);
		} else if (getVelocity().length() > 0.2) {
			setAnimationState(MageAnimationState.Walk);
		} else {
			setAnimationState(MageAnimationState.Stand);
		}
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putUuid("mageId", mageId);
		nbt.putInt("mana", getMana());
		nbt.putInt("maxMana", getMaxMana());
		nbt.putString("animationState", getAnimationState().name());
		nbt.putBoolean("isCasting", isCasting);
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		mageId = nbt.contains("mageId") ? nbt.getUuid("mageId") : UUID.randomUUID();
		setMana(nbt.getInt("mana"));
		setMaxMana(nbt.getInt("maxMana"));
		isCasting = nbt.getBoolean("isCasting");
		var stateString = nbt.getString("animationState");
		try {
			if (stateString.length() > 0) {
				var state = MageAnimationState.valueOf(stateString);
				setAnimationState(state);
			} else {
				setAnimationState(MageAnimationState.Stand);
			}
		} catch (Exception e) {
			setAnimationState(MageAnimationState.Stand);
			System.err.println("Can't parse mage animation state: " + stateString);
		}
	}

	public int getMaxMana() {
		return 100 * ((ILeveledMob) this).getLevel();
	}

	public void setMaxMana(int maxMana) {
		this.maxMana = maxMana;
	}

	@Override
	public int getMana() {
		return getDataTracker().get(MANA);
	}

	@Override
	public float getHealthProportion() {
		return getHealth() / getMaxHealth();
	}

	@Override
	public Spell getCurrentSpell() {
		return ModSpells.Flamethrower;
	}

	public void setMana(int mana) {
		getDataTracker().set(MANA, mana);
	}

	@Override
	public void shootAt(LivingEntity target, float pullProgress) {

	}

	public static enum MageAnimationState {
		Stand,
		Walk,
		Cast
	}
}
