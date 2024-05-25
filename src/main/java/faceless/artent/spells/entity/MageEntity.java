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
    protected static final RawAnimation WALK_LEGS_ANIMATION = RawAnimation.begin().thenPlay("animation.mage.walk_legs");
    protected static final RawAnimation WALK_HANDS_ANIMATION = RawAnimation
      .begin()
      .thenPlay("animation.mage.walk_hands");
    protected static final RawAnimation CAST_ANIMATION = RawAnimation.begin().thenPlay("animation.mage.cast");
    protected static final RawAnimation START_CAST_ANIMATION = RawAnimation
      .begin()
      .thenPlay("animation.mage.start_cast");
    private static final TrackedData<Integer> MANA = DataTracker.registerData(
      MageEntity.class,
      TrackedDataHandlerRegistry.INTEGER
    );

    private static final int MAX_MANA_BASE = 100;
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    public Spell[] spells;
    public UUID mageId;
    private MageAttackGoal mageAttackGoal;

    public MageEntity(EntityType<MageEntity> entityType, World world) {
        super(entityType, world);
        mageId = UUID.randomUUID();
        spells = new Spell[]{ModSpells.Gust, ModSpells.Flamethrower, ModSpells.LightSword};
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1));
        this.goalSelector.add(6, new LookAtEntityGoal(this, SkeletonEntity.class, 16.0f));
        this.goalSelector.add(6, new LookAroundGoal(this));
        mageAttackGoal = new MageAttackGoal(this, this, new ArrayList<>(0), 32, 1);
        this.goalSelector.add(4, mageAttackGoal);
        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(
          3,
          new ActiveTargetGoal<>(this, SkeletonEntity.class, false)
        ); // TODO select mobs not in a team
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, ZombieEntity.class, false));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, SpiderEntity.class, false));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, CreeperEntity.class, false));
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        getDataTracker().startTracking(MANA, 0);
    }

    public static DefaultAttributeContainer.Builder createLivingAttributes() {
        return DefaultAttributeContainer
          .builder()
          .add(EntityAttributes.GENERIC_MAX_HEALTH)
          .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE)
          .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25f)
          .add(EntityAttributes.GENERIC_ARMOR)
          .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS)
          .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32)
          .add(EntityAttributes.GENERIC_MAX_ABSORPTION);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "HANDS", 5, this::handsController));
        controllers.add(new AnimationController<>(this, "LEGS", 5, this::legsController));
    }

    protected PlayState handsController(final AnimationState<MageEntity> event) {
        var mage = event.getAnimatable();
        if (mage.isUsingItem()) {
            if (mage.getItemUseTime() < 10) return event.setAndContinue(START_CAST_ANIMATION);
            return event.setAndContinue(CAST_ANIMATION);
        }

        if (mage.getVelocity().length() > 0.2) return event.setAndContinue(WALK_HANDS_ANIMATION);

        return event.setAndContinue(STAND_ANIMATION);
    }

    protected PlayState legsController(final AnimationState<MageEntity> event) {
        var mage = event.getAnimatable();
        if (mage.getVelocity().length() > 0.1) return event.setAndContinue(WALK_LEGS_ANIMATION);
        return event.setAndContinue(STAND_ANIMATION);
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

    @Override
    public void setCooldown(int time) {
        mageAttackGoal.setCooldown(time);
    }

    @Override
    public int getCooldown() {
        return mageAttackGoal == null ? 0 : mageAttackGoal.getCooldown();
    }

    @Override
    public void tick() {
        super.tick();
        if (getMana() < getMaxMana()) {
            setMana(Math.min(getMaxMana(), getMana() + ((ILeveledMob) this).getLevel()));
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putUuid("mageId", mageId);
        nbt.putInt("mana", getMana());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        mageId = nbt.contains("mageId") ? nbt.getUuid("mageId") : UUID.randomUUID();
        setMana(nbt.getInt("mana"));
    }

    public int getMaxMana() {
        return MAX_MANA_BASE * ((ILeveledMob) this).getLevel();
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
        return ModSpells.SmallFireball;
    }

    public void setMana(int mana) {
        getDataTracker().set(MANA, mana);
    }

    @Override
    public void shootAt(LivingEntity target, float pullProgress) {

    }
}
