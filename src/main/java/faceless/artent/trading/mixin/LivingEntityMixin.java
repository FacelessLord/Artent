package faceless.artent.trading.mixin;

import faceless.artent.playerData.api.DataUtil;
import faceless.artent.playerData.api.MoneyPouch;
import faceless.artent.trading.entity.CoinEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "drop", at = @At("TAIL"))
    protected void dropLoot(DamageSource damageSource, CallbackInfo ci) {
        var living = (LivingEntity) (Object) this;
        var money = (long) living.getMaxHealth() * 4;
        var pouch = MoneyPouch.fromLong(money);
        var world = living.getWorld();
        var pos = living.getPos();

        if (pouch.bronze() != 0)
            spawnCoin(0, (int) pouch.bronze(), world, pos);
        if (pouch.silver() != 0)
            spawnCoin(1, (int) pouch.silver(), world, pos);
        if (pouch.gold() != 0)
            spawnCoin(2, (int) pouch.gold(), world, pos);

        if (damageSource.getAttacker() instanceof PlayerEntity player) {
            DataUtil.getHeroInfo(player).addExperience(player, (int) (living.getMaxHealth() / 2));
        }
    }

    @Unique
    private void spawnCoin(int type, int count, World world, Vec3d pos) {
        var coin = new CoinEntity(world);
        coin.setPosition(pos);
        coin.setCoinType(type);
        coin.setCoinCount(count);
        world.spawnEntity(coin);
    }
}
