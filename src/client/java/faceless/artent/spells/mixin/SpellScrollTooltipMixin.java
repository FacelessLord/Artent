package faceless.artent.spells.mixin;

import faceless.artent.spells.api.ICaster;
import faceless.artent.spells.item.SpellScroll;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Item.class)
public class SpellScrollTooltipMixin {
    @Inject(at = @At("TAIL"), method = "appendTooltip")
    public void appendTooltip(
      ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context, CallbackInfo ci
    ) {
        if (!(stack.getItem() instanceof SpellScroll scroll)) return;

        var formatting = Formatting.WHITE;
        var spellId = scroll.getSpellId(stack);
        var spell = scroll.getSpell(stack);
        if (spellId != null && spell == null) {
            tooltip.add(Text.translatable("artent.spell.null").formatted(Formatting.DARK_GRAY));
            return;
        }
        var minimalSpellPotency = scroll.getSpell(stack).settings.minimalPotency;


        var player = MinecraftClient.getInstance().player;
        if (player != null) {
            var caster = (ICaster) player;
            var playerPotency = caster.getPotency();

            formatting = playerPotency >= minimalSpellPotency ? Formatting.BLUE : Formatting.RED;
        }

        tooltip.add(Text.translatable("artent.spell." + spellId).formatted(formatting));
        tooltip.add(Text
                      .translatable("artent.spell.minimal_potency")
                      .append(minimalSpellPotency + "")
                      .formatted(formatting));
    }
}