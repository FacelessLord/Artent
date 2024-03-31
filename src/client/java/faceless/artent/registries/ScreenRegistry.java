package faceless.artent.registries;

import faceless.artent.objects.ModScreenHandlers;
import faceless.artent.sharpening.SharpeningAnvilScreen;
import faceless.artent.spells.InscriptionTableScreen;
import faceless.artent.trading.TraderScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class ScreenRegistry implements IRegistry {

	@Override
	public void register() {
		HandledScreens.register(ModScreenHandlers.SHARPENING_ANVIL, SharpeningAnvilScreen::new);
		HandledScreens.register(ModScreenHandlers.TRADER_HANDLER, TraderScreen::new);
		HandledScreens.register(ModScreenHandlers.INSCRIPTION_TABLE, InscriptionTableScreen::new);
	}

}
