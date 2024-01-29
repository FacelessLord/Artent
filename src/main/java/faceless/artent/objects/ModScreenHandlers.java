package faceless.artent.objects;

import faceless.artent.sharpening.screenHandlers.SharpeningAnvilScreenHandler;
import faceless.artent.trading.screenHandlers.TraderScreenHandler;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;

public class ModScreenHandlers {
	public static ScreenHandlerType<SharpeningAnvilScreenHandler> SHARPENING_ANVIL_HANDLER = new ScreenHandlerType<>(SharpeningAnvilScreenHandler::new, FeatureFlags.VANILLA_FEATURES);
	public static ScreenHandlerType<TraderScreenHandler> TRADER_HANDLER = new ScreenHandlerType<>(TraderScreenHandler::new, FeatureFlags.VANILLA_FEATURES);
}
