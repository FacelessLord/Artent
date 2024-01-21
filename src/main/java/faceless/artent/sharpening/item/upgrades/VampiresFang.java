package faceless.artent.sharpening.item.upgrades;

import faceless.artent.sharpening.item.EnhancerItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Formatting;

public class VampiresFang extends EnhancerItem {
	public VampiresFang(Settings settings) {
		super(Formatting.DARK_RED, "upgrade/vampires_fang", settings);
	}

	@Override
	public void onEntityDamaged(ItemStack tool, LivingEntity attacker, LivingEntity target, float amount) {
		attacker.heal(amount * 0.2f);
	}
}