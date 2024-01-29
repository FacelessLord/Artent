package faceless.artent.hud;

import faceless.artent.Artent;
import faceless.artent.api.math.Color;
import faceless.artent.playerData.api.DataUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class ArtentHudRenderer {
	private static final Identifier POUCH_TEXTURE = new Identifier(Artent.MODID, "hud/pouch");
	public static final Identifier GOLD_COIN_TEXTURE = new Identifier(Artent.MODID, "textures/hud/coin_gold.png");
	public static final Identifier SILVER_COIN_TEXTURE = new Identifier(Artent.MODID, "textures/hud/coin_silver.png");
	public static final Identifier BRONZE_COIN_TEXTURE = new Identifier(Artent.MODID, "textures/hud/coin_bronze.png");

	public static final Identifier[] COINS = new Identifier[]{
		BRONZE_COIN_TEXTURE,
		SILVER_COIN_TEXTURE,
		GOLD_COIN_TEXTURE
	};

	public static void render(DrawContext context, float tickDelta, ArtentHudContext ctx) {
		var pouch = DataUtil.getMoneyPouch(ctx.player()).asArray();
		var i = ctx.scaledWidth() / 2;

		MatrixStack matrices = context.getMatrices();

		matrices.push();
		matrices.translate(i + 91 + 8, ctx.scaledHeight() - 22+14, -90.0f);
		matrices.scale(0.25f, 0.25f, 0.25f);

		for (int j = 0; j < 3; j++) {
			matrices.push();
			matrices.translate(0, -28 * j, 0);

			context.drawTexture(COINS[j], 0, 0, 0, 0, 32, 32, 32, 32);
			matrices.scale(2, 2, 2);
			context.drawTextWithShadow(ctx.textRenderer(), String.valueOf(pouch[j]), 16, 4, Color.White.asInt());

			matrices.pop();
		}

		matrices.pop();
	}
}
