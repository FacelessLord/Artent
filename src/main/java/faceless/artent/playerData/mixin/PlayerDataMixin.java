package faceless.artent.playerData.mixin;

import faceless.artent.api.MiscUtils;
import faceless.artent.leveling.api.ILeveledMob;
import faceless.artent.leveling.api.ISpecialMob;
import faceless.artent.leveling.api.LevelingUtils;
import faceless.artent.network.ArtentServerHook;
import faceless.artent.playerData.api.ArtentPlayerData;
import faceless.artent.playerData.api.ArtentPlayerState;
import faceless.artent.playerData.api.HeroInfo;
import faceless.artent.spells.api.CasterInfo;
import faceless.artent.spells.api.CasterStorage;
import faceless.artent.spells.api.ICaster;
import faceless.artent.spells.api.Spell;
import faceless.artent.spells.item.WandItem;
import faceless.artent.trading.api.TradeInfo;
import faceless.artent.trading.inventory.TraderSellInventory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@SuppressWarnings("ALL")
@Mixin(PlayerEntity.class)
public abstract class PlayerDataMixin implements ArtentPlayerData, ICaster {

    @Override
    public abstract ArtentPlayerState getPlayerState();

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void onWriteEntityToNBT(NbtCompound compound, CallbackInfo ci) {
        this.writeToNbt(compound);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void onReadEntityFromNBT(NbtCompound compound, CallbackInfo ci) {
        this.readFromNbt(compound);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        var player = (PlayerEntity) (Object) this;
        var casterInfo = getCasterInfo();
        {
            if (casterInfo.cooldown > 0)
                setCooldown(casterInfo.cooldown - 1);
        }

        if (player.getWorld() != null && !player.getWorld().isClient) {
            casterInfo.tickCaster(player.getWorld(), player);
            if (player.getWorld().getTime() % 10 == 1) {
                if (getPlayerState().tradeInfo != null && getPlayerState().tradeInfo.priceDeterminatorContext == null)
                    setTradeInfo(null);

                ArtentServerHook.packetSyncPlayerData(player);
            }
        }
    }

    @Override
    public void onEntityKilled(LivingEntity living) {
        var heroInfo = getHeroInfo();
        var special = false;
        var specialMobScalingFactor = 1f;
        if (living instanceof ISpecialMob specialMob) {
            special = true;
            specialMobScalingFactor = LevelingUtils.getSpecialMobExperienceScaling(specialMob.getSpecialMobType());
        }
        var leveled = false;
        if (living instanceof ILeveledMob leveledMob) {
            leveled = true;
            var mobLevel = leveledMob.getLevel();
            var experienceScaling = LevelingUtils.getExperienceScalingByMobLevel(heroInfo.getLevel(), mobLevel);
            if (Double.isInfinite(experienceScaling)) {
                experienceScaling = LevelingUtils.getExperienceScalingByMobLevel(
                  heroInfo.getLevel(),
                  heroInfo.getLevel() + 5
                ) *
                                    (mobLevel - heroInfo.getLevel());
            }
            var mobsToLevel = LevelingUtils.getSameLevelMobsToLevel(heroInfo.getLevel());
            var xpPerSameLevelMob = heroInfo.getExperienceToLevel() / mobsToLevel;
            var earnedXp = (int) (xpPerSameLevelMob * experienceScaling * specialMobScalingFactor);
            System.out.println(heroInfo.getLevel() +
                               " " +
                               mobLevel +
                               " " +
                               experienceScaling +
                               " " +
                               xpPerSameLevelMob +
                               " " +
                               earnedXp);
            heroInfo.addExperience(this.asPlayer(), earnedXp);
        }

        if (!leveled) {
            heroInfo.addExperience(this.asPlayer(), (int) (living.getMaxHealth() * specialMobScalingFactor / 2));
        }
    }

    @Override
    public void restoreMana() {
        getCasterInfo().restoreMana(this.asPlayer());
        ArtentServerHook.packetSyncPlayerData(this.asPlayer());
    }

    @Override
    public long getMoney() {
        return getPlayerState().artentMoney;
    }

    @Override
    public void setMoney(long money) {
        getPlayerState().artentMoney = money;
    }

    @Override
    public void addMoney(long money) {
        getPlayerState().artentMoney += money;
    }

    @Override
    public boolean canEditTrades() {
        return getPlayerState().canEditTrades;
    }

    @Override
    public void setCanEditTrades(boolean canEdit) {
        getPlayerState().canEditTrades = canEdit;
    }

    @Override
    public TraderSellInventory getTraderSellInventory() {
        return getPlayerState().traderSellInventory;
    }

    @Override
    public TradeInfo getTradeInfo() {
        return getPlayerState().tradeInfo;
    }

    @Override
    public void setTradeInfo(TradeInfo info) {
        getPlayerState().tradeInfo = info;
    }

    @Override
    public HeroInfo getHeroInfo() {
        return getPlayerState().heroInfo;
    }

    @Override
    public void setHeroInfo(HeroInfo info) {
        getPlayerState().heroInfo = info;
    }

    @Override
    public CasterInfo getCasterInfo() {
        return getPlayerState().casterInfo;
    }

    @Override
    public void setCasterInfo(CasterInfo info) {
        getPlayerState().casterInfo = info;
    }

    @Override
    public void writeToNbt(NbtCompound compound) {
        var playerStateTag = new NbtCompound();
        getPlayerState().writeToNbt(playerStateTag);
        compound.put("artent.data", playerStateTag);
    }

    @Override
    public void readFromNbt(NbtCompound compound) {
        var player = (PlayerEntity) (Object) this;
        CasterStorage.putCaster(player.getWorld(), this);
    }

    @Override
    public int getMana() {
        return getCasterInfo().mana;
    }

    @Override
    public float getHealthProportion() {
        var player = asPlayer();
        return player.getHealth() / player.getMaxHealth();
    }

    @Override
    public Spell getCurrentSpell() {
        var player = this.asPlayer();
        var mainHand = WandItem.getSelectedSpell(player, Hand.MAIN_HAND);
        var offHand = WandItem.getSelectedSpell(player, Hand.OFF_HAND);
        if (mainHand == null && offHand == null)
            return null;
        if (mainHand != null)
            return mainHand;
        return offHand;
    }

    @Override
    public boolean consumeMana(int mana) {
        var player = (PlayerEntity) (Object) this;
        var casterInfo = getCasterInfo();
        if (casterInfo.mana >= mana) {
            casterInfo.mana = MiscUtils.clamp(casterInfo.mana - mana, 0, casterInfo.getMaxMana(player));
            return true;
        }
        return false;
    }

    @Override
    public int getPotency() {
        return getHeroInfo().getLevel();
    }

    @Override
    public UUID getCasterUuid() {
        var player = (PlayerEntity) (Object) this;
        return player.getUuid();
    }

    @Override
    public Vec3d getCasterPosition() {
        return asPlayer().getPos();
    }

    @Override
    public Vec3d getCasterRotation() {
        return asPlayer().getRotationVector();
    }

    @Override
    public int getCooldown() {
        var casterInfo = getCasterInfo();
        return casterInfo.cooldown;
    }

    @Override
    public void setCooldown(int time) {
        var casterInfo = getCasterInfo();
        casterInfo.cooldown = time;

        if (casterInfo.maxCooldown == 0)
            casterInfo.maxCooldown = time;
        if (time == 0)
            casterInfo.maxCooldown = 0;
    }

    public PlayerEntity asPlayer() {
        return (PlayerEntity) (Object) this;
    }
}
