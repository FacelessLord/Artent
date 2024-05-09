package faceless.artent.sharpening.item.upgrades;

import faceless.artent.sharpening.item.EnhancerItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Formatting;

public class NetherFireStone extends EnhancerItem {
    public NetherFireStone(Item.Settings settings) {
        super(Formatting.RED, "upgrade/nether_fire_stone", settings);
    }

    @Override
    public void onEntityDamaged(ItemStack tool, LivingEntity attacker, LivingEntity target, float amount) {
        target.setOnFireFor(40);
    }

}