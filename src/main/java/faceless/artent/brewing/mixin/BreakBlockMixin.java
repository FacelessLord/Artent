package faceless.artent.brewing.mixin;

import faceless.artent.objects.ModPotionEffects;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.class)
public class BreakBlockMixin {
    @Inject(at = @At("TAIL"), method = "calcBlockBreakingDelta", cancellable = true)
    public void calcBlockBreakingDelta(
      BlockState state,
      PlayerEntity player,
      BlockView world,
      BlockPos pos,
      CallbackInfoReturnable<Float> cir
    ) {
        if (player.hasStatusEffect(ModPotionEffects.LUMBERJACK)) {
            var potion = player.getStatusEffect(ModPotionEffects.LUMBERJACK);
            if ((state.isIn(BlockTags.LOGS) || state.isIn(BlockTags.LEAVES)) && potion != null) {
                var modifier = 3 << potion.getAmplifier();
                cir.setReturnValue(cir.getReturnValue() * modifier);
            }
        }
    }
}