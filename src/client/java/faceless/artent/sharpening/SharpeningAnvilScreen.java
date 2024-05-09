package faceless.artent.sharpening;

import com.mojang.blaze3d.systems.RenderSystem;
import faceless.artent.sharpening.screenHandlers.SharpeningAnvilScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SharpeningAnvilScreen extends HandledScreen<SharpeningAnvilScreenHandler> {
    private static final Identifier TEXTURE = new Identifier("artent", "textures/gui/anvil.png");

    public SharpeningAnvilScreen(SharpeningAnvilScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
//        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        context.drawTexture(TEXTURE, x, y, backgroundWidth, backgroundHeight, 0, 0, 176, 166, 256, 256);

    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void init() {
        super.init();
//		// Center the title
//		titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
    }
}