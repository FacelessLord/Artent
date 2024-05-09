package faceless.artent.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.random.Random;

public record ArtentHudContext(
  MinecraftClient client,
  TextRenderer textRenderer,
  PlayerEntity player,
  int scaledWidth,
  int scaledHeight,
  Random random
) {
}
