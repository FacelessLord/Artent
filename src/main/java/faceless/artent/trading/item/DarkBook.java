package faceless.artent.trading.item;

import faceless.artent.api.item.INamed;
import faceless.artent.network.ArtentServerHook;
import faceless.artent.playerData.api.DataUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class DarkBook extends Item implements INamed {
	public DarkBook(Settings settings) {
		super(settings);
	}

	@Override
	public String getId() {
		return "dark_book";
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		var handler = DataUtil.getHandler(user);
		var canEditTrades = handler.canEditTrades();
		if (!world.isClient) {
			handler.setCanEditTrades(!canEditTrades);
			ArtentServerHook.packetSyncPlayerData(user);
		} else {
			user.sendMessage(Text.literal("CanEditTrades: " + !canEditTrades));
		}
		return super.use(world, user, hand);
	}
}
