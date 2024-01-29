package faceless.artent.trading;

import faceless.artent.api.MiscUtils;
import faceless.artent.api.math.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class SellButton extends ButtonWidget {
	protected SellButton(int x, int y, int width, int height, PressAction onPress) {
		super(x, y, width, height, Text.translatable("artent.gui.trader.sell"), onPress, DEFAULT_NARRATION_SUPPLIER);
	}

	@Override
	protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
		var x = getX();
		var y = getY();
		int selected = MiscUtils.isInRange(mouseX, x, x + width) && MiscUtils.isInRange(mouseY, y, y + height) ? 1 : 0;

		context.drawTexture(TraderScreen.TEXTURE, x, y, width, height, 176, selected * 12, 41, 12, 256, 256);
		var textRenderer = MinecraftClient.getInstance().textRenderer;
		var message = this.getMessage();
		var messageWidth = textRenderer.getWidth(message);

		context.drawTextWithShadow(textRenderer, message, x + width / 2 - messageWidth / 2, y + 2, Color.White.asInt());
	}
}
