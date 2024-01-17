package faceless.artent.registries;

import faceless.artent.objects.ModScreenHandlers;
import faceless.artent.sharpening.SharpeningAnvilScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class ScreenRegistry implements IRegistry {

	@Override
	public void register() {
//		HandledScreens.register(ModScreenHandlers.ALCHEMICAL_CIRCLE, AlchemicalCircleGui::new);
		HandledScreens.register(ModScreenHandlers.SHARPENING_ANVIL_HANDLER, SharpeningAnvilScreen::new);
	}

}
