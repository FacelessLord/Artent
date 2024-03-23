package faceless.artent.playerData.api;

import faceless.artent.Artent;
import faceless.artent.spells.api.ICaster;
import faceless.artent.trading.inventory.TraderSellInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class DataUtil {
	@NotNull
	public static ArtentPlayerData getHandler(PlayerEntity player) {
		return (ArtentPlayerData) player;
	}

	public static MoneyPouch getMoneyPouch(PlayerEntity player) {
		var handler = getHandler(player);
		var money = handler.getMoney();
		return MoneyPouch.fromLong(money);
	}

	public static TraderSellInventory getTraderSellInventory(PlayerEntity player) {
		var handler = getHandler(player);
		return handler.getTraderSellInventory();
	}

	public static MoneyPouch getSellPrice(ItemStack stack, PlayerEntity player) {
		var tradeInfo = DataUtil.getHandler(player).getTradeInfo();
		var determinator = Artent.ItemPriceDeterminators.determinators.get(tradeInfo.priceDeterminatorType);
		var determinatorContext = tradeInfo.priceDeterminatorContext;

		return determinator.getSellPrice(stack, determinatorContext);
	}

	public static MoneyPouch getBuyPrice(ItemStack stack, PlayerEntity player) {
		var tradeInfo = DataUtil.getHandler(player).getTradeInfo();
		var determinator = Artent.ItemPriceDeterminators.determinators.get(tradeInfo.priceDeterminatorType);
		var determinatorContext = tradeInfo.priceDeterminatorContext;

		return determinator.getBuyPrice(stack, player, determinatorContext);
	}

	public static ICaster asCaster(PlayerEntity player) {
		return (ICaster) player;
	}
}
