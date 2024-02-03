package faceless.artent.trading.priceDeterminators;

import faceless.artent.playerData.api.MoneyPouch;
import faceless.artent.trading.api.IItemStackPriceDeterminator;
import faceless.artent.trading.api.IPriceDeterminatorContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class ItemStackPriceDeterminator implements IItemStackPriceDeterminator {
	public static String NAME = "constant";

	@Override
	public MoneyPouch getSellPrice(ItemStack stack, PlayerEntity player, IPriceDeterminatorContext ctx) {
		return MoneyPouch.fromLong(5000L * stack.getCount());
	}

	@Override
	public MoneyPouch getBuyPrice(ItemStack stack, PlayerEntity player, IPriceDeterminatorContext ctx) {
		return new MoneyPouch(0, 0, stack.getCount());
	}

	public static class ConstantPriceDeterminatorContext implements IPriceDeterminatorContext {

		@Override
		public String getContextType() {
			return NAME;
		}

		@Override
		public void writeToNbt(NbtCompound tag) {

		}

		@Override
		public void readFromNbt(NbtCompound tag) {

		}
	}
}
