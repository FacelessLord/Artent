package faceless.artent.spells.api;

import faceless.artent.registries.SpellRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class ItemSpellInventory implements ISpellInventory {
    public static final String SPELL_INVENTORY_KEY = "spell_inventory";
    public static final String INVENTORY_SIZE_KEY = "inventory_size";
    public static final String INVENTORY_SLOT_KEY = "spell_";
    private NbtCompound tag;
    private boolean dirty = false;

    public ItemSpellInventory(ItemStack stack) {
        if (!(stack.getItem() instanceof ISpellInventoryItem spellInventoryItem))
            return;
        this.tag = getOrInitSpellNbt(stack.getOrCreateNbt(), spellInventoryItem.getLevel());

    }

    @Override
    public int getSize() {
        if (tag == null)
            return 0;
        return tag.getInt(INVENTORY_SIZE_KEY);
    }

    @Override
    public Spell getSpell(int slot) {
        if (tag == null)
            return null;
        var spellId = tag.getString(INVENTORY_SLOT_KEY + slot);
        return SpellRegistry.getSpell(spellId);
    }

    @Override
    public void setSpell(Spell spell, int slot) {
        var size = getSize();
        if (slot > size) {
            System.err.println("Spell Inventory slot overflow. Slot: " + slot + ", inventorySize: " + size);
            return;
        }
        tag.putString(INVENTORY_SLOT_KEY + slot, spell.id);
        markDirty();
    }

    @Override
    public void removeSpell(int slot) {
        var size = getSize();
        if (slot > size) {
            System.err.println("Spell Inventory slot overflow. Slot: " + slot + ", inventorySize: " + size);
            return;
        }
        tag.remove(INVENTORY_SLOT_KEY + slot);
        markDirty();
    }

    @Override
    public void readNbt(NbtCompound nbt) {

    }

    @Override
    public void writeNbt(NbtCompound nbt) {

    }

    @Override
    public boolean isDirty() {
        return dirty;
    }

    @Override
    public void markDirty() {
        dirty = true;
    }

    @Override
    public void clearDirty() {
        dirty = false;
    }

    public static NbtCompound getOrInitSpellNbt(NbtCompound tag, int baseSize) {
        if (tag.contains(SPELL_INVENTORY_KEY))
            return tag.getCompound(SPELL_INVENTORY_KEY);
        var spellTag = new NbtCompound();
        spellTag.putInt(INVENTORY_SIZE_KEY, baseSize);
        tag.put(SPELL_INVENTORY_KEY, spellTag);
        return spellTag;
    }
}
