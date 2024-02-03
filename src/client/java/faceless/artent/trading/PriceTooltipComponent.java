package faceless.artent.trading;

import faceless.artent.hud.ArtentHudRenderer;
import faceless.artent.playerData.api.MoneyPouch;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;

public class PriceTooltipComponent implements TooltipComponent {
	private final MoneyPouch price;

	public PriceTooltipComponent(MoneyPouch price) {
		this.price = price;
	}

	@Override
	public int getHeight() {
		return 9;
	}

	@Override
	public int getWidth(TextRenderer textRenderer) {
		return textRenderer.getWidth(Text.literal("0")) *
				   (String.valueOf(price.asLong()).length() + 4);// Three 0 for coin icons, one 8 for gaps
	}

	public RenderInfo getRenderInfo(TextRenderer textRenderer) {
		var bronze = price.bronze();
		var silver = price.silver();
		var gold = price.gold();

		var baseLength = textRenderer.getWidth(Text.literal("0"));

		var bronzeText = Text.literal(String.valueOf(bronze));
		var silverText = Text.literal(String.valueOf(silver));
		var goldText = Text.literal(String.valueOf(gold));

		var bronzeLength = bronze == 0 ? 0 : baseLength * (String.valueOf(bronze).length());
		var silverLength = silver == 0 ? 0 : baseLength * (String.valueOf(silver).length());
		var goldLength = gold == 0 ? 0 : baseLength * (String.valueOf(gold).length());

		var length = bronzeLength + silverLength + goldLength;
		var coinSize = baseLength * 4 / 3;
		var gapSize = baseLength / 3;

		return new RenderInfo(bronzeText,
			silverText,
			goldText,
			bronzeLength,
			silverLength,
			goldLength,
			length,
			coinSize,
			gapSize);
	}

	@Override
	public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
		var renderInfo = getRenderInfo(textRenderer);

		var matrices = context.getMatrices();

		matrices.push();
		matrices.translate(x, y, 0);

		renderCoinPrice(renderInfo.bronzeLength,
			renderInfo.bronzeText,
			ArtentHudRenderer.BRONZE_COIN_TEXTURE,
			renderInfo,
			textRenderer,
			context);

		renderCoinPrice(renderInfo.silverLength,
			renderInfo.silverText,
			ArtentHudRenderer.SILVER_COIN_TEXTURE,
			renderInfo,
			textRenderer,
			context);

		renderCoinPrice(renderInfo.goldLength,
			renderInfo.goldText,
			ArtentHudRenderer.GOLD_COIN_TEXTURE,
			renderInfo,
			textRenderer,
			context);

		matrices.pop();
	}

	public void renderCoinPrice(int priceLength,
		Text priceText,
		Identifier coinTexture,
		RenderInfo renderInfo,
		TextRenderer textRenderer,
		DrawContext context) {

		if (priceLength <= 0)
			return;

		var matrices = context.getMatrices();
		var vertexConsumers = context.getVertexConsumers();

		matrices.scale(0.25f, 0.25f, 0.25f);
		context.drawTexture(coinTexture, 0, 0, 0, 0, 32, 32, 32, 32);
		matrices.scale(4, 4, 4);
		matrices.translate(renderInfo.coinSize, 0, 0);
		textRenderer.draw(priceText,
			0,
			0,
			Colors.WHITE,
			true,
			matrices.peek().getPositionMatrix(),
			vertexConsumers,
			TextRenderer.TextLayerType.NORMAL,
			0,
			0xF000F0);
		matrices.translate(priceLength + renderInfo.gapSize, 0, 0);
	}

	public record RenderInfo(Text bronzeText,
		Text silverText,
		Text goldText,
		int bronzeLength,
		int silverLength,
		int goldLength,
		int length,
		int coinSize,
		int gapSize) {

	}
}
