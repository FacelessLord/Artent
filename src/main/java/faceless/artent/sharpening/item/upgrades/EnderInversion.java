package faceless.artent.sharpening.item.upgrades;

import faceless.artent.api.CancellationToken;
import faceless.artent.sharpening.item.EnhancerItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Formatting;

public class EnderInversion extends EnhancerItem {
    public EnderInversion(Settings settings) {
        super(Formatting.AQUA, "upgrade/ender_inversion", settings);
    }

    @Override
    public void onEntityDamaged(ItemStack tool, LivingEntity attacker, LivingEntity target, float amount) {
        if (target instanceof EndermanEntity enderman) {
            enderman.damage(attacker.getDamageSources().magic(), 4);
        }
    }

    @Override
    public void beforeEndermanTeleported(ItemStack tool, LivingEntity attacker, EndermanEntity enderman, CancellationToken cancellationToken) {
        enderman.damage(attacker.getDamageSources().magic(), 2);
        cancellationToken.setCancelled();
    }
}