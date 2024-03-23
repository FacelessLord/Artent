package faceless.artent.spells.api;

import net.minecraft.nbt.NbtCompound;

public class CasterInfo {
	public int level = 1;
	public int mana = 0;

	public int getMaxManaBase() {
		return level * 40; // TODO
	}

	/**
	 * @return mana to restore in one second
	 */
	public int getManaRegenBase() {
		return level; // TODO
	}

	public void writeNbt(NbtCompound nbt) {
		var mageNbt = new NbtCompound();

		mageNbt.putInt("mana", mana);
		mageNbt.putInt("level", level);

		nbt.put("mage", mageNbt);
	}

	public void readNbt(NbtCompound nbt) {
		if (!nbt.contains("mage"))
			return;

		var mageNbt = nbt.getCompound("mage");

		this.level = mageNbt.getInt("level");
		this.mana = mageNbt.getInt("mana");
	}
}
