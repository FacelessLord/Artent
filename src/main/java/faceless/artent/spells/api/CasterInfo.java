package faceless.artent.spells.api;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

public class CasterInfo {
    public int level = 1;
    public int mana = 0;

    private int spellBookIndex = 0;

    public int getMaxMana(PlayerEntity player) {
        return level * 40; // TODO
    }

    /**
     * @return mana to restore in one second
     */
    public int getManaRegenBase() {
        return level; // TODO
    }

    public int getSpellBookIndex() {
        return spellBookIndex;
    }

    public void setSpellBookIndex(int value) {
        spellBookIndex = value;
    }

    public void tickCaster(World world, PlayerEntity player) {
        mana = Math.min(mana + getManaRegenBase()*2, getMaxMana(player));
    }

    public void writeNbt(NbtCompound nbt) {
        var mageNbt = new NbtCompound();

        mageNbt.putInt("mana", mana);
        mageNbt.putInt("level", level);
        mageNbt.putInt("spellBookIndex", spellBookIndex);

        nbt.put("caster", mageNbt);
    }

    public void readNbt(NbtCompound nbt) {
        if (!nbt.contains("caster"))
            return;

        var mageNbt = nbt.getCompound("caster");

        this.level = mageNbt.getInt("level");
        this.mana = mageNbt.getInt("mana");
        this.spellBookIndex = mageNbt.getInt("spellBookIndex");
    }
}
