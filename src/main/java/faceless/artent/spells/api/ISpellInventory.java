package faceless.artent.spells.api;

import net.minecraft.nbt.NbtCompound;

public interface ISpellInventory {

    int getSize();

    ScrollStack getSpell(int slot);

    void setSpell(int slot, ScrollStack spell);

    void removeSpell(int slot);

    void readNbt(NbtCompound nbt);

    void writeNbt(NbtCompound nbt);

    boolean isDirty();

    void markDirty();

    void clearDirty();
}
