package faceless.artent.hud;

import faceless.artent.Artent;
import faceless.artent.api.math.Color;
import faceless.artent.playerData.api.DataUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class ArtentHudRenderer {
	private static final Identifier POUCH_TEXTURE = new Identifier(Artent.MODID, "hud/pouch");
	private static final Identifier GOLD_COIN_TEXTURE = new Identifier(Artent.MODID, "hud/coin_gold");
	private static final Identifier SILVER_COIN_TEXTURE = new Identifier(Artent.MODID, "hud/coin_silver");
	private static Identifier BRONZE_COIN_TEXTURE = new Identifier(Artent.MODID, "textures/hud/coin_bronze.png");

	public static void render(DrawContext context, float tickDelta, ArtentHudContext ctx) {
		var playerData = DataUtil.getHandler(ctx.player());
		var i = ctx.scaledWidth() / 2;

		context.getMatrices().push();
		context.getMatrices().translate(i + 91 + 8, ctx.scaledHeight() - 22 + 3, -90.0f);
		context.getMatrices().scale(0.5f, 0.5f, 0.5f);
//		context.drawGuiTexture(BRONZE_COIN_TEXTURE, 0, 0, 32, 32);
		context.drawTexture(BRONZE_COIN_TEXTURE, 0, 0, 0, 0, 32, 32, 32, 32);
		context.getMatrices().scale(2, 2, 2);
		context.drawTextWithShadow(ctx.textRenderer(), String.valueOf(playerData.getMoney()), 16, 4, Color.White.asInt());

		context.getMatrices().pop();
	}
}
