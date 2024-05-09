package faceless.artent.sharpening.mixin;

import faceless.artent.api.CancellationToken;
import faceless.artent.sharpening.api.SharpeningUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndermanEntity.class)
public class EndermanMixin {
    @Inject(at = @At("HEAD"), method = "teleportRandomly", cancellable = true)
    protected void teleportRandomly(CallbackInfoReturnable<Boolean> cir) {
        var enderman = (EndermanEntity) (Object) this;
        if (enderman.getWorld().isClient() || !enderman.isAlive()) {
            cir.setReturnValue(false);
        }
        var pos = enderman.getPos();
        var box = Box.of(pos, 64, 64, 64);
        var toolCarriers = enderman
          .getWorld()
          .getEntitiesByClass(LivingEntity.class, box, e -> (e instanceof PlayerEntity || e instanceof MobEntity));
        toolCarriers.forEach(e -> {
            var item = SharpeningUtils.getCarriedItem(e);
            var teleportCancelled = SharpeningUtils
              .getNonEmptySlots(item)
              .anyMatch(enhancer -> {
                  var cancellationToken = new CancellationToken();
                  enhancer.beforeEndermanTeleported(item, e, enderman, cancellationToken);
                  return cancellationToken.isCancelled();
              });
            if (teleportCancelled) {
                cir.setReturnValue(false);
            }
        });
    }
}