package faceless.artent.leveling.mixin;

import faceless.artent.leveling.api.SpecialMobType;
import faceless.artent.network.ArtentServerHook;
import faceless.artent.playerData.api.DataUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin {
    @Override
    public int getBaseLevel() {
        return 1;
    }

    @Override
    public int getLevel() {
        return DataUtil.getHeroInfo(this.asPlayer()).getLevel();
    }

    @Override
    public void setLevel(int level) {
        DataUtil.getHeroInfo(this.asPlayer()).setLevel(level);
        ArtentServerHook.packetSyncPlayerData(this.asPlayer());
    }

    @Override
    public int getLevelVariation() {
        return 0;
    }

    @Override
    public float getLevelModifier() {
        return 1;
//        var dLevel = getLevel() - getBaseLevel(); TODO custom player leveling
//        return Math.max(1 + dLevel / 100f, 0) - 1;
    }

    @Override
    public SpecialMobType getSpecialMobType() {
        return SpecialMobType.Common;
//        return SpecialMobType.fromInt(this.asEntity().getDataTracker().get(SPECIAL_MOB_TYPE));
    }

    @Override
    public void setSpecialMobType(SpecialMobType type) {
    }

    @Override
    public boolean hasSpecialMobModifier() {
        return false;
    }

    @Override
    public void makeSpecialDrops(List<ItemStack> drops) {

    }

    public float getSpecialMoneyDropModifier() {
        return getSpecialMobModifier();
    }

    //    @Inject(method = "updateAttributes", at = @At("TAIL"))
    @Override
    public void setupAttributes() {
    }

    @Unique
    public PlayerEntity asPlayer() {
        return (PlayerEntity) (Object) this;
    }
}
