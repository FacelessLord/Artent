package faceless.artent.trading.api;

import faceless.artent.playerData.api.MoneyPouch;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class ItemStackPriceDeterminator implements IItemStackPriceDeterminator {
	public static IItemStackPriceDeterminator INSTANCE = new ItemStackPriceDeterminator();

	@Override
	public MoneyPouch getSellPrice(ItemStack stack, PlayerEntity player) {
		return MoneyPouch.fromLong(5000L * stack.getCount());
	}

	@Override
	public MoneyPouch getBuyPrice(ItemStack stack, PlayerEntity player) {
		return new MoneyPouch(0, 0, 1);
	}
}
