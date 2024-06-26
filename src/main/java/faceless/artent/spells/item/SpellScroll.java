package faceless.artent.spells.item;

import faceless.artent.api.item.ArtentItem;
import faceless.artent.registries.SpellRegistry;
import faceless.artent.spells.api.ISpellScroll;
import faceless.artent.spells.api.ScrollType;
import faceless.artent.spells.api.Spell;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpellScroll extends ArtentItem implements ISpellScroll {
    public static String SPELL_ID_KEY = "spell_id";

    public SpellScroll(Settings settings) {
        super(settings, "spell_scroll");
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public Spell getSpell(ItemStack stack) {
        var spellId = getSpellId(stack);
        return SpellRegistry.getSpell(spellId);
    }

    @Override
    public ScrollType getType(ItemStack stack) {
        return ScrollType.Common;
    }

    public String getSpellId(ItemStack stack) {
        var stackNbt = stack.getNbt();
        if (stackNbt == null) return null;
        return stackNbt.getString(SPELL_ID_KEY);
    }
}
