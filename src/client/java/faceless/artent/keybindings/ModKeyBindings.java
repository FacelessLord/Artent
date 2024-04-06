package faceless.artent.keybindings;

import faceless.artent.Artent;
import faceless.artent.network.ArtentClientHook;
import faceless.artent.registries.IRegistry;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ModKeyBindings implements IRegistry {
    private static KeyBinding spellIndexLeft;
    private static KeyBinding spellIndexRight;

    @Override
    public void register() {
        spellIndexLeft = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key." + Artent.MODID + ".spell_index.left",
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_LEFT_BRACKET, // The keycode of the key
                "category." + Artent.MODID
        ));
        spellIndexRight = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key." + Artent.MODID + ".spell_index.right",
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_RIGHT_BRACKET, // The keycode of the key
                "category." + Artent.MODID
        ));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (spellIndexLeft.wasPressed()) {
                ArtentClientHook.packetSpellIndexLeft(client.player);
            }
            if (spellIndexRight.wasPressed()) {
                ArtentClientHook.packetSpellIndexRight(client.player);
            }
        });
    }
}
