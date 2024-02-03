package faceless.artent.trading.api;

import faceless.artent.playerData.api.MoneyPouch;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface IItemStackPriceDeterminator {
	MoneyPouch getSellPrice(ItemStack stack, IPriceDeterminatorContext ctx);

	MoneyPouch getBuyPrice(ItemStack stack, PlayerEntity player, IPriceDeterminatorContext ctx);

}
