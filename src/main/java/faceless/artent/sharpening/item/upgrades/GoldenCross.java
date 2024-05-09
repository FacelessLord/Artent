package faceless.artent.sharpening.item.upgrades;

import faceless.artent.sharpening.item.EnhancerItem;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Formatting;

public class GoldenCross extends EnhancerItem {
    public GoldenCross(Settings settings) {
        super(Formatting.GOLD, "upgrade/golden_cross", settings);
    }

    @Override
    public void onEntityDamaged(ItemStack tool, LivingEntity attacker, LivingEntity target, float amount) {
        if (target.getGroup() == EntityGroup.UNDEAD) {
            target.timeUntilRegen = 0;
            target.damage(attacker.getDamageSources().magic(), amount * 0.2f);
        }
    }
}