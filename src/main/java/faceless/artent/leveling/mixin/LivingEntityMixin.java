package faceless.artent.leveling.mixin;

import faceless.artent.leveling.api.ILeveledMob;
import faceless.artent.leveling.api.ISpecialMob;
import faceless.artent.leveling.api.LevelingConstants;
import faceless.artent.leveling.api.SpecialMobType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Random;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements ILeveledMob, ISpecialMob {

    @Unique
    private static final TrackedData<Integer> LEVEL = DataTracker.registerData(LivingEntity.class,
            TrackedDataHandlerRegistry.INTEGER);
    @Unique
    private static final TrackedData<Integer> SPECIAL_MOB_TYPE = DataTracker.registerData(LivingEntity.class,
            TrackedDataHandlerRegistry.INTEGER);

    @Override
    public int getBaseLevel() {
        return 5;
    }

    @Override
    public int getLevel() {
        return this.asEntity().getDataTracker().get(LEVEL);
    }

    @Override
    public void setLevel(int level) {
        this.asEntity().getDataTracker().set(LEVEL, level);
    }

    @Override
    public int getLevelVariation() {
        return 4;
    }

    @Unique
    public float getLevelModifier() {
        var dLevel = getLevel() - getBaseLevel();
        return Math.max(1 + dLevel / 100f, 0) - 1;
    }

    @Override
    public float getLevelAttackModifier() {
        return getLevelModifier();
    }

    @Override
    public float getLevelSpeedModifier() {
        return getLevelModifier();
    }

    @Override
    public void makeLevelDrops(List<ItemStack> drops) {

    }

    @Override
    public float getLevelMoneyDropModifier() {
        return getLevelModifier();
    }

    @Override
    public float getXpModifier(int level) {
        return getLevelModifier();
    }

    @Override
    public SpecialMobType getSpecialMobType() {
        return SpecialMobType.fromInt(this.asEntity().getDataTracker().get(SPECIAL_MOB_TYPE));
    }

    @Override
    public void setSpecialMobType(SpecialMobType type) {
        this.asEntity().getDataTracker().set(SPECIAL_MOB_TYPE, type.ordinal());
    }

    @Override
    public boolean hasSpecialMobModifier() {
        return getSpecialMobType() == SpecialMobType.Common;
    }

    @Unique
    public float getSpecialMobModifier() {
        var type = getSpecialMobType();
        return switch (type) {
            case Common -> 1;
            case Cursed -> 1.3f;
            case Demonic -> 1.7f;
            case Eldritch -> 2.3f;
            case Weakened -> 0.7f;
            default -> 1;
        } - 1;
    }

    @Override
    public float getSpecialAttackModifier() {
        return getSpecialMobModifier();
    }

    @Override
    public float getSpecialSpeedModifier() {
        return getSpecialMobModifier();
    }

    @Override
    public void makeSpecialDrops(List<ItemStack> drops) {

    }

    public float getSpecialMoneyDropModifier() {
        return getSpecialMobModifier();
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void constructor(EntityType type, World world, CallbackInfo ci) {
        var level = (int) (getBaseLevel() + (Math.random() * 2 - 1) * getLevelVariation());
        setLevel(level);
        var specialTypeRandom = new Random().nextFloat();
        var specialType = SpecialMobType.Common;

        if (specialTypeRandom > 0.8 && specialTypeRandom < 0.9)
            specialType = SpecialMobType.Weakened;
        if (specialTypeRandom > 0.9 && specialTypeRandom < 0.95)
            specialType = SpecialMobType.Cursed;
        if (specialTypeRandom > 0.95 && specialTypeRandom < 0.985)
            specialType = SpecialMobType.Demonic;
        if (specialTypeRandom > 0.985)
            specialType = SpecialMobType.Eldritch;
        setSpecialMobType(specialType);

        var specialTypeString = specialType != SpecialMobType.Common ? specialType.name() : "";
        if (specialTypeString.length() > 0 && level != getBaseLevel())
            this.asEntity().setCustomName(Text.literal((specialTypeString + " " + level).trim()));

        setupAttributes();
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    protected void initDataTracker(CallbackInfo ci) {
        var dataTracker = this.asEntity()
                .getDataTracker();
        dataTracker.startTracking(LEVEL, getBaseLevel());
        dataTracker.startTracking(SPECIAL_MOB_TYPE, SpecialMobType.Common.ordinal());
    }

    //    @Inject(method = "updateAttributes", at = @At("TAIL"))
    @Unique
    public void setupAttributes() {
        var levelSpeedModifier = new EntityAttributeModifier(LevelingConstants.MOB_LEVEL_SPEED_MODIFIER, "ARTENT.MOB_LEVEL_SPEED_MODIFIER", getLevelSpeedModifier(), EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
        var levelAttackModifier = new EntityAttributeModifier(LevelingConstants.MOB_LEVEL_ATTACK_MODIFIER, "ARTENT.MOB_LEVEL_ATTACK_MODIFIER", getLevelAttackModifier(), EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
        var specialTypeSpeedModifier = new EntityAttributeModifier(LevelingConstants.SPECIAL_MOB_SPEED_MODIFIER, "ARTENT.SPECIAL_MOB_SPEED_MODIFIER", getSpecialSpeedModifier(), EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
        var specialTypeAttackModifier = new EntityAttributeModifier(LevelingConstants.SPECIAL_MOB_ATTACK_MODIFIER, "ARTENT.SPECIAL_MOB_ATTACK_MODIFIER", getSpecialAttackModifier(), EntityAttributeModifier.Operation.MULTIPLY_TOTAL);

        var speedAttributeInstance = this.asEntity().getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        var attackAttributeInstance = this.asEntity().getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE);

        if (speedAttributeInstance != null) {
            speedAttributeInstance.addPersistentModifier(levelSpeedModifier);
            speedAttributeInstance.addPersistentModifier(specialTypeSpeedModifier);
        }
        if (attackAttributeInstance != null) {
            attackAttributeInstance.addPersistentModifier(levelAttackModifier);
            attackAttributeInstance.addPersistentModifier(specialTypeAttackModifier);
        }
    }


    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        if (!nbt.contains("artent"))
            return;
        var artentTag = nbt.getCompound("artent");
        var level = artentTag.getInt("level");
        var specialType = artentTag.getInt("specialType");

        setLevel(level);
        setSpecialMobType(SpecialMobType.fromInt(specialType));
    }


    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        var artentTag = new NbtCompound();

        artentTag.putInt("level", getLevel());
        artentTag.putInt("specialType", getSpecialMobType().ordinal());

        nbt.put("artent", artentTag);
    }


    @Unique
    public LivingEntity asEntity() {
        return (LivingEntity) (Object) this;
    }
}
