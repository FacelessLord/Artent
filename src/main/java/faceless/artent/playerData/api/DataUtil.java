package faceless.artent.playerData.api;

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
		return new MoneyPouch(money % 100, (money / 100) % 100, money % 10000);
	}
}
