package faceless.artent.api;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class ChatUtils {

    public static void sendMessageToPlayer(World world, PlayerEntity player, String message) {
        if (world.isClient())
            player.sendMessage(Text.translatable(message), false);
    }
}