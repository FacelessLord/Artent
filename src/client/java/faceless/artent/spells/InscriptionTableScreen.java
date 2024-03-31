package faceless.artent.spells;

import com.mojang.blaze3d.systems.RenderSystem;
import faceless.artent.spells.screenhandlers.InscriptionTableScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class InscriptionTableScreen extends HandledScreen<InscriptionTableScreenHandler> {
	private static final Identifier TEXTURE = new Identifier("artent", "textures/gui/spell_book.png");

	public InscriptionTableScreen(InscriptionTableScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	@Override
	protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
//        RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, TEXTURE);
		int x = (width - backgroundWidth) / 2;
		int y = (height - backgroundHeight) / 2;
		context.drawTexture(TEXTURE, x, y, backgroundWidth, backgroundHeight, 0, 0, 201, 224, 256, 256);

	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		drawMouseoverTooltip(context, mouseX, mouseY);
	}

	@Override
	protected void init() {
		backgroundWidth = 201;
		backgroundHeight = (int) (backgroundWidth * 224f / 201f);
		super.init();
//		// Center the title
//		titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
	}
}