package faceless.artent.spells.api;

import faceless.artent.playerData.api.DataUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

public class CasterInfo {
    public int mana = 0;

    private int spellBookIndex = 0;

    public int getMaxMana(PlayerEntity player) {
        var heroInfo = DataUtil.getHeroInfo(player);
        return heroInfo.getLevel() * 40; // TODO
    }

    /**
     * @return mana to restore in one second
     */
    public int getManaRegenBase(PlayerEntity player) {
        var heroInfo = DataUtil.getHeroInfo(player);
        return heroInfo.getLevel(); // TODO
    }

    public void restoreMana(PlayerEntity player) {
        mana = getMaxMana(player);
    }

    public int getSpellBookIndex() {
        return spellBookIndex;
    }

    public void setSpellBookIndex(int value) {
        spellBookIndex = value;
    }

    public void tickCaster(World world, PlayerEntity player) {
        mana = Math.min(mana + getManaRegenBase(player) * 2, getMaxMana(player));
    }

    public void writeNbt(NbtCompound nbt) {
        var mageNbt = new NbtCompound();

        mageNbt.putInt("mana", mana);
        mageNbt.putInt("spellBookIndex", spellBookIndex);

        nbt.put("caster", mageNbt);
    }

    public void readNbt(NbtCompound nbt) {
        if (!nbt.contains("caster"))
            return;

        var mageNbt = nbt.getCompound("caster");

        this.mana = mageNbt.getInt("mana");
        this.spellBookIndex = mageNbt.getInt("spellBookIndex");
    }
}
