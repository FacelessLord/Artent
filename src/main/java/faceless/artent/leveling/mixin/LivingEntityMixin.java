package faceless.artent.leveling.mixin;

import faceless.artent.Artent;
import faceless.artent.leveling.api.*;
import faceless.artent.playerData.api.DataUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.MutableText;
import net.minecraft.text.PlainTextContent;
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
        return LevelingUtils.getMobBaseLevel(this.asEntity());
    }

    @Override
    public int getLevel() {
        return this.asEntity().getDataTracker().get(LEVEL);
    }

    @Override
    public void setLevel(int level) {
        this.asEntity().getDataTracker().set(LEVEL, level);
        updateLevelLabel();
        setupAttributes();
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
    public float getLevelHealthModifier() {
        return getLevelModifier();
    }

    @Override
    public float getLevelArmorModifier() {
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
        updateLevelLabel();
        setupAttributes();
    }

    @Override
    public boolean hasSpecialMobModifier() {
        return getSpecialMobType() == SpecialMobType.Common;
    }

    @Unique
    public float getSpecialMobModifier() {
        var type = getSpecialMobType();
        return LevelingUtils.getSpecialMobExperienceScaling(type) - 1;
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
    public float getSpecialHealthModifier() {
        return getSpecialMobModifier();
    }

    @Override
    public float getSpecialArmorModifier() {
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
        if (!(this.asEntity() instanceof PlayerEntity)) {
            var level = (int) Math.max(1, getBaseLevel() + (Math.random() * 2 - 1) * getLevelVariation());
            setLevel(level);
            var specialTypeRandom = new Random().nextFloat();
            var specialType = SpecialMobType.Common;

            if (specialTypeRandom > 0.8 && specialTypeRandom < 0.9)
                specialType = SpecialMobType.Wounded;
            if (specialTypeRandom > 0.9 && specialTypeRandom < 0.95)
                specialType = SpecialMobType.Cursed;
            if (specialTypeRandom > 0.95 && specialTypeRandom < 0.985)
                specialType = SpecialMobType.Demonic;
            if (specialTypeRandom > 0.985)
                specialType = SpecialMobType.Eldritch;
            setSpecialMobType(specialType);

            updateLevelLabel();
        }

        setupAttributes();
    }

    private void updateLevelLabel() {
        int level = getLevel();
        SpecialMobType specialType = getSpecialMobType();
        var specialTypeString = specialType != SpecialMobType.Common ? specialType.name().toLowerCase() : "";
        var labelText = MutableText.of(new PlainTextContent.Literal(""));
        if (specialTypeString.length() > 0)
            labelText = labelText
              .append(Text.translatable(Artent.MODID + ".specialType." + specialTypeString))
              .append(" ")
              .append(asEntity().getType().getName())
              .append(" ");

        labelText = labelText.append(Text.literal(String.valueOf(level)));

        this.asEntity().setCustomName(labelText);
    }


    @Inject(method = "drop", at = @At("TAIL"))
    protected void dropLoot(DamageSource damageSource, CallbackInfo ci) {
        if (damageSource.getAttacker() instanceof PlayerEntity player) {
            DataUtil.getHandler(player).onEntityKilled(asEntity());
        }
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    protected void initDataTracker(CallbackInfo ci) {
        var dataTracker = this.asEntity()
                              .getDataTracker();
        dataTracker.startTracking(LEVEL, getBaseLevel());
        dataTracker.startTracking(SPECIAL_MOB_TYPE, SpecialMobType.Common.ordinal());
    }

    @Unique
    public void setupAttributes() {
        var levelSpeedModifier = new EntityAttributeModifier(LevelingConstants.MOB_LEVEL_SPEED_MODIFIER,
                                                             "ARTENT.MOB_LEVEL_SPEED_MODIFIER",
                                                             getLevelSpeedModifier(),
                                                             EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
        var levelAttackModifier = new EntityAttributeModifier(LevelingConstants.MOB_LEVEL_ATTACK_MODIFIER,
                                                              "ARTENT.MOB_LEVEL_ATTACK_MODIFIER",
                                                              getLevelAttackModifier(),
                                                              EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
        var levelHealthModifier = new EntityAttributeModifier(LevelingConstants.MOB_LEVEL_HEALTH_MODIFIER,
                                                              "ARTENT.MOB_LEVEL_HEALTH_MODIFIER",
                                                              getLevelHealthModifier(),
                                                              EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
        var levelArmorModifier = new EntityAttributeModifier(LevelingConstants.MOB_LEVEL_ARMOR_MODIFIER,
                                                             "ARTENT.MOB_LEVEL_ARMOR_MODIFIER",
                                                             getLevelArmorModifier(),
                                                             EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
        var specialTypeSpeedModifier = new EntityAttributeModifier(LevelingConstants.SPECIAL_MOB_SPEED_MODIFIER,
                                                                   "ARTENT.SPECIAL_MOB_SPEED_MODIFIER",
                                                                   getSpecialSpeedModifier(),
                                                                   EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
        var specialTypeAttackModifier = new EntityAttributeModifier(LevelingConstants.SPECIAL_MOB_ATTACK_MODIFIER,
                                                                    "ARTENT.SPECIAL_MOB_ATTACK_MODIFIER",
                                                                    getSpecialAttackModifier(),
                                                                    EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
        var specialTypeHealthModifier = new EntityAttributeModifier(LevelingConstants.SPECIAL_MOB_HEALTH_MODIFIER,
                                                                    "ARTENT.SPECIAL_MOB_HEALTH_MODIFIER",
                                                                    getSpecialHealthModifier(),
                                                                    EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
        var specialTypeArmorModifier = new EntityAttributeModifier(LevelingConstants.SPECIAL_MOB_ARMOR_MODIFIER,
                                                                   "ARTENT.SPECIAL_MOB_ARMOR_MODIFIER",
                                                                   getSpecialArmorModifier(),
                                                                   EntityAttributeModifier.Operation.MULTIPLY_TOTAL);

        var speedAttributeInstance = this.asEntity().getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        var attackAttributeInstance = this.asEntity().getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE);
        var healthAttributeInstance = this.asEntity().getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        var armorAttributeInstance = this.asEntity().getAttributeInstance(EntityAttributes.GENERIC_ARMOR);

        if (speedAttributeInstance != null) {
            speedAttributeInstance.removeModifier(levelSpeedModifier.getId());
            speedAttributeInstance.removeModifier(specialTypeSpeedModifier.getId());
            speedAttributeInstance.addPersistentModifier(levelSpeedModifier);
            speedAttributeInstance.addPersistentModifier(specialTypeSpeedModifier);
        }
        if (attackAttributeInstance != null) {
            attackAttributeInstance.removeModifier(levelAttackModifier.getId());
            attackAttributeInstance.removeModifier(specialTypeAttackModifier.getId());
            attackAttributeInstance.addPersistentModifier(levelAttackModifier);
            attackAttributeInstance.addPersistentModifier(specialTypeAttackModifier);
        }
        if (healthAttributeInstance != null) {
            healthAttributeInstance.removeModifier(levelHealthModifier.getId());
            healthAttributeInstance.removeModifier(specialTypeHealthModifier.getId());
            healthAttributeInstance.addPersistentModifier(levelHealthModifier);
            healthAttributeInstance.addPersistentModifier(specialTypeHealthModifier);
        }
        if (armorAttributeInstance != null) {
            armorAttributeInstance.removeModifier(levelArmorModifier.getId());
            armorAttributeInstance.removeModifier(specialTypeArmorModifier.getId());
            armorAttributeInstance.addPersistentModifier(levelArmorModifier);
            armorAttributeInstance.addPersistentModifier(specialTypeArmorModifier);
        }
    }


    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        if (!nbt.contains("artent"))
            return;
        var artentTag = nbt.getCompound("artent");
        if (!(this.asEntity() instanceof PlayerEntity)) {
            var level = artentTag.getInt("level");
            var specialType = artentTag.getInt("specialType");

            setLevel(level);
            setSpecialMobType(SpecialMobType.fromInt(specialType));
        }
    }


    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        var artentTag = new NbtCompound();

        if (!(this.asEntity() instanceof PlayerEntity)) {
            artentTag.putInt("level", getLevel());
            artentTag.putInt("specialType", getSpecialMobType().ordinal());
        }

        nbt.put("artent", artentTag);
    }


    @Unique
    public LivingEntity asEntity() {
        return (LivingEntity) (Object) this;
    }
}
