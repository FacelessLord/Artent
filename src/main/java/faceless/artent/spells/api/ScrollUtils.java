package faceless.artent.spells.api;

import faceless.artent.objects.ModItems;
import faceless.artent.spells.item.SpellScroll;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ScrollUtils {
    public static ScrollType[] ScrollTypes = {ScrollType.Common, ScrollType.Forbidden, ScrollType.Holy};
    public static Item[] ScrollItems = {ModItems.SpellScroll, ModItems.SpellScroll, ModItems.SpellScroll};

    public static int getId(ScrollType type) {
        for (int i = 0; i < ScrollTypes.length; i++) {
            if (ScrollTypes[i] == type)
                return i;
        }
        return 0;
    }

    public static Item getItemByType(ScrollType type) {
        return ScrollItems[getId(type)];
    }

    public static ItemStack createItemFromScroll(ScrollStack scrollStack) {
        if (scrollStack == null)
            return ItemStack.EMPTY;

        var item = getItemByType(scrollStack.scrollType);
        var itemStack = new ItemStack(item, 1);
        itemStack.getOrCreateNbt().putString(SpellScroll.SPELL_ID_KEY, scrollStack.spell.id);
        return itemStack;
    }

    public static ScrollStack createScrollFromItem(ItemStack itemStack) {
        if (itemStack.isEmpty() || !(itemStack.getItem() instanceof ISpellScroll scroll))
            return null;
        var type = scroll.getType(itemStack);
        var spell = scroll.getSpell(itemStack);
        return new ScrollStack(spell, type);
    }
}
