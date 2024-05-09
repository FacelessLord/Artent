package faceless.artent.registries;

import faceless.artent.Artent;
import faceless.artent.objects.ModScreenHandlers;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public final class ScreenHandlerRegistry implements IRegistry {

    public void register() {
        register(ModScreenHandlers.SHARPENING_ANVIL, "sharpening_anvil");
        register(ModScreenHandlers.TRADER_HANDLER, "trader");
        register(ModScreenHandlers.INSCRIPTION_TABLE, "inscription_table");
    }

    public void register(ScreenHandlerType<?> item, String screenId) {
        Registry.register(Registries.SCREEN_HANDLER, new Identifier(Artent.MODID, screenId), item);
    }
}
