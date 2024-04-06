package faceless.artent.spells.api;

import faceless.artent.registries.SpellRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class ItemSpellInventory implements ISpellInventory {
    public static final String SPELL_INVENTORY_KEY = "spell_inventory";
    public static final String INVENTORY_SIZE_KEY = "inventory_size";
    public static final String INVENTORY_SLOT_SPELL_KEY = "spell_";
    public static final String INVENTORY_SLOT_TYPE_KEY = "spell_type_";
    private NbtCompound tag;
    private boolean dirty = false;

    public ItemSpellInventory(ItemStack stack) {
        if (!(stack.getItem() instanceof ISpellInventoryItem spellInventoryItem))
            return;
        this.tag = getOrInitSpellNbt(stack.getOrCreateNbt(), spellInventoryItem.getSize(stack));
    }

    @Override
    public int getSize() {
        if (tag == null)
            return 0;
        return tag.getInt(INVENTORY_SIZE_KEY);
    }

    @Override
    public ScrollStack getSpell(int slot) {
        if (tag == null || !tag.contains(INVENTORY_SLOT_SPELL_KEY + slot))
            return null;
        var spellId = tag.getString(INVENTORY_SLOT_SPELL_KEY + slot);
        var scrollTypeId = tag.getInt(INVENTORY_SLOT_TYPE_KEY + slot);
        var spell = SpellRegistry.getSpell(spellId);
        var scrollType = scrollTypeId < ScrollTypes.ScrollTypes.length ? ScrollTypes.ScrollTypes[scrollTypeId] : ScrollType.Common;

        return new ScrollStack(spell, scrollType);
    }

    @Override
    public void setSpell(int slot, ScrollStack stack) {
        var size = getSize();
        if (slot > size) {
            System.err.println("Spell Inventory slot overflow. Slot: " + slot + ", inventorySize: " + size);
            return;
        }
        if (stack != null) {
            tag.putString(INVENTORY_SLOT_SPELL_KEY + slot, stack.spell.id);
            tag.putInt(INVENTORY_SLOT_TYPE_KEY + slot, ScrollTypes.getId(stack.scrollType));
        } else {
            tag.remove(INVENTORY_SLOT_SPELL_KEY + slot);
            tag.remove(INVENTORY_SLOT_TYPE_KEY + slot);
        }
        markDirty();
    }

    @Override
    public void removeSpell(int slot) {
        var size = getSize();
        if (slot > size) {
            System.err.println("Spell Inventory slot overflow. Slot: " + slot + ", inventorySize: " + size);
            return;
        }
        tag.remove(INVENTORY_SLOT_SPELL_KEY + slot);
        tag.remove(INVENTORY_SLOT_TYPE_KEY + slot);
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
