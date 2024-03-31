package faceless.artent.spells.api;

import net.minecraft.nbt.NbtCompound;

public interface ISpellInventory {

    int getSize();
    Spell getSpell(int slot);

    void setSpell(Spell spell, int slot);

    void removeSpell(int slot);

    void readNbt(NbtCompound nbt);

    void writeNbt(NbtCompound nbt);

    boolean isDirty();
    void markDirty();

    void clearDirty();
}
