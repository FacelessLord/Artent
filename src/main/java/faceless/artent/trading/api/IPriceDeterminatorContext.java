package faceless.artent.trading.api;

import net.minecraft.nbt.NbtCompound;

public interface IPriceDeterminatorContext {
	String getContextType();

	void writeToNbt(NbtCompound tag);

	void readFromNbt(NbtCompound tag);
}
