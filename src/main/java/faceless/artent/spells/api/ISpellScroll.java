package faceless.artent.spells.api;

import net.minecraft.item.ItemStack;

public interface ISpellScroll {
    Spell getSpell(ItemStack stack);

    ScrollType getType(ItemStack stack);
}
