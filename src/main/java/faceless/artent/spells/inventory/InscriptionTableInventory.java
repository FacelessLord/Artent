package faceless.artent.spells.inventory;

import faceless.artent.api.inventory.ArtentInventory;
import faceless.artent.spells.api.ISpellInventoryItem;
import faceless.artent.spells.api.ItemSpellInventory;
import faceless.artent.spells.api.ScrollTypes;
import faceless.artent.spells.blockEntity.InscriptionTableBlockEntity;
import net.minecraft.item.ItemStack;

public class InscriptionTableInventory extends ArtentInventory {
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
        if (slot != 0) {
            super.setStack(slot, stack);
            return;
        }

        var bookStack = getStack(slot);

        // Save spells into book taken
        if (!bookStack.isEmpty() && bookStack.getItem() instanceof ISpellInventoryItem) {
            var bookInventory = new ItemSpellInventory(bookStack);
            for (int i = 0; i < bookInventory.getSize(); i++) {
                var scrollStack = getStack(4 + i);
                var scroll = ScrollTypes.createScrollFromItem(scrollStack);
                bookInventory.setSpell(i, scroll);

                setStack(4 + i, ItemStack.EMPTY);
            }
        }
        super.setStack(slot, stack);

        bookStack = getStack(slot);
        // Fill inventory from inserted book
        if (!bookStack.isEmpty() && bookStack.getItem() instanceof ISpellInventoryItem) {
            var bookInventory = new ItemSpellInventory(bookStack);

            for (int i = 0; i < bookInventory.getSize(); i++) {
                var spellStack = bookInventory.getSpell(i);
                var scrollStack = ScrollTypes.createItemFromScroll(spellStack);

                setStack(4 + i, scrollStack);
            }
        }
    }

    @Override
    public void markDirty() {
        super.markDirty();
        table.markDirty();
    }
}
