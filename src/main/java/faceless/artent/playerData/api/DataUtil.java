package faceless.artent.playerData.api;

import faceless.artent.trading.inventory.TraderSellInventory;
import net.minecraft.entity.player.PlayerEntity;
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
}
