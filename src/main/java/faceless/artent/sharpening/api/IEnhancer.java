package faceless.artent.sharpening.api;

import faceless.artent.api.CancellationToken;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public interface IEnhancer {
    Formatting getFormatting();

    Text getName();

    default void onEntityDamaged(ItemStack tool, LivingEntity attacker, LivingEntity target, float amount) {
    }

    default void beforeEndermanTeleported(
      ItemStack tool,
      LivingEntity attacker,
      EndermanEntity enderman,
      CancellationToken token
    ) {
    }

}