package faceless.artent.playerData.api;

import net.minecraft.nbt.NbtCompound;

public interface ArtentPlayerData {
	long getMoney();
	void setMoney(long money);
	void addMoney(long money);

	void writeToNbt(NbtCompound compound);
	void readFromNbt(NbtCompound compound);
}
