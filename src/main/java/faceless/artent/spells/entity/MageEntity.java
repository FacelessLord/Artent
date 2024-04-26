package faceless.artent.spells.entity;

import faceless.artent.objects.ModItems;
import faceless.artent.objects.ModSpells;
import faceless.artent.spells.api.ICaster;
import faceless.artent.spells.api.Spell;
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
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.player.PlayerEntity;
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

	private final BowAttackGoal<MageEntity> bowAttackGoal = new BowAttackGoal<MageEntity>(this, 1.0, 20, 15.0f);
	public UUID mageId;
	private int maxMana = 100;
	private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
	public Spell[] spells;

	public MageEntity(EntityType<MageEntity> entityType, World world) {
		super(entityType, world);
		mageId = UUID.randomUUID();
		spells = new Spell[]{ModSpells.Gust, ModSpells.Flamethrower, ModSpells.LightSword};
	}

	@Override
	protected void initGoals() {
		this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0));
		this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
		this.goalSelector.add(6, new LookAroundGoal(this));
		this.targetSelector.add(1, new RevengeGoal(this));
		this.targetSelector.add(2, new ActiveTargetGoal<PlayerEntity>((MobEntity) this, PlayerEntity.class, true)); // TODO select mobs not in a team
		this.targetSelector.add(3, new ActiveTargetGoal<IronGolemEntity>((MobEntity) this, IronGolemEntity.class, true));
		this.targetSelector.add(3, new ActiveTargetGoal<TurtleEntity>(this, TurtleEntity.class, 10, true, false, TurtleEntity.BABY_TURTLE_ON_LAND_FILTER));
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
		  .add(EntityAttributes.GENERIC_MAX_ABSORPTION)
		  .add(EntityAttributes.GENERIC_FOLLOW_RANGE);
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
			case Stand -> event.setAndContinue(WALK_ANIMATION);
			case Walk -> event.setAndContinue(WALK_ANIMATION);
			case Cast -> event.setAndContinue(WALK_ANIMATION);
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
		return switch (slot){
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

	@Override
	public void tick() {
		super.tick();
		var currentAnimationState = getAnimationState();
		if (getVelocity().length() > 0.2 && currentAnimationState != MageAnimationState.Cast) {
			setAnimationState(MageAnimationState.Walk);
		}
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putUuid("mageId", mageId);
		nbt.putInt("mana", getMana());
		nbt.putInt("maxMana", getMaxMana());
		nbt.putString("animationState", getAnimationState().name());
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		mageId = nbt.getUuid("mageId");
		setMana(nbt.getInt("mana"));
		setMaxMana(nbt.getInt("maxMana"));
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
		return maxMana;
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
		return ModSpells.LightSword;
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
