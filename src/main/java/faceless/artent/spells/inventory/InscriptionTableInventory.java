package faceless.artent.spells.inventory;

import faceless.artent.api.inventory.ArtentInventory;
import faceless.artent.spells.api.*;
import faceless.artent.spells.blockEntity.InscriptionTableBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class InscriptionTableInventory extends ArtentInventory {
    public static final int SPELL_SLOTS_OFFSET = 4;

    public InscriptionTableBlockEntity table;

    public InscriptionTableInventory(InscriptionTableBlockEntity table) {
        this.table = table;
    }

    @Override
    public int size() {
        return 13;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        if (slot == 0) {
            onUpdateBookSlot(slot, stack);
        }
        if (slot == 3 && stack.isEmpty()) {
            var paperStack = getStack(1);
            paperStack.decrement(1);
            super.setStack(1, paperStack);
        }
        super.setStack(slot, stack);
        if (slot == 1 || slot == 2 || slot == 3) {
            updateCopiedScrollSlot();
        }
    }

    private void updateCopiedScrollSlot() {
        var paperStack = getStack(1);
        var scrollStack = getStack(2);
        if (!paperStack.isEmpty() && !scrollStack.isEmpty()
                && paperStack.getItem() == Items.PAPER
                && scrollStack.getItem() instanceof ISpellScroll scroll
                && scroll.getType(scrollStack) == ScrollType.Common) {
            super.setStack(3, scrollStack.copyWithCount(1));
        } else {
            super.setStack(3, ItemStack.EMPTY);
        }
    }

    private void onUpdateBookSlot(int slot, ItemStack stack) {
        var bookStack = getStack(slot);

        // Save spells into book taken
        if (!bookStack.isEmpty() && bookStack.getItem() instanceof ISpellInventoryItem) {
            var bookInventory = new ItemSpellInventory(bookStack);
            for (int i = 0; i < bookInventory.getSize(); i++) {
                var scrollStack = getStack(SPELL_SLOTS_OFFSET + i);
                var scroll = ScrollUtils.createScrollFromItem(scrollStack);
                bookInventory.setSpell(i, scroll);

                setStack(SPELL_SLOTS_OFFSET + i, ItemStack.EMPTY);
            }
        }
        super.setStack(slot, stack);

        bookStack = getStack(slot);
        // Fill inventory from inserted book
        if (!bookStack.isEmpty() && bookStack.getItem() instanceof ISpellInventoryItem) {
            var bookInventory = new ItemSpellInventory(bookStack);

            for (int i = 0; i < bookInventory.getSize(); i++) {
                var spellStack = bookInventory.getSpell(i);
                var scrollStack = ScrollUtils.createItemFromScroll(spellStack);

                setStack(SPELL_SLOTS_OFFSET + i, scrollStack);
            }
        }
    }

    @Override
    public void markDirty() {
        super.markDirty();
        table.markDirty();
    }
}
