package faceless.artent.trading;

import com.mojang.blaze3d.systems.RenderSystem;
import faceless.artent.api.math.Color;
import faceless.artent.network.ArtentClientHook;
import faceless.artent.playerData.api.MoneyPouch;
import faceless.artent.trading.screenHandlers.TraderScreenHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.tooltip.HoveredTooltipPositioner;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

import static faceless.artent.hud.ArtentHudRenderer.COINS;

public class TraderScreen extends HandledScreen<TraderScreenHandler> {
    public static final Identifier TEXTURE = new Identifier("artent", "textures/gui/trader_screen.png");

    public TraderScreen(TraderScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        backgroundHeight = (int) (backgroundWidth * 202f / 176f);
        super.init();

        addDrawableChild(new SellButton(
          width / 2 + 32, height / 2 - 12, 41, 12, button -> {
            ArtentClientHook.packetSellItems(handler.player, handler.getSellInventoryPrice());
        }));
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        context.drawTexture(TEXTURE, x, y, backgroundWidth, backgroundHeight, 0, 0, 176, 202, 256, 256);

        var itemsPrice = handler.getSellInventoryPrice();
        var pouch = MoneyPouch.fromLong(itemsPrice).asArray();
        var textRenderer = MinecraftClient.getInstance().textRenderer;

        MatrixStack matrices = context.getMatrices();

        matrices.push();
        matrices.translate(width / 2f + 32, height / 2f - 24, 0);
        matrices.scale(0.25f, 0.25f, 0.25f);

        for (int j = 0; j < 3; j++) {
            matrices.push();
            matrices.translate(0, -28 * j, 0);

            context.drawTexture(COINS[j], 0, 0, 0, 0, 32, 32, 32, 32);
            matrices.scale(2, 2, 2);
            context.drawTextWithShadow(textRenderer, String.valueOf(pouch[j]), 16, 4, Color.White.asInt());

            matrices.pop();
        }

        matrices.pop();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }

    private void renderTooltip(
      DrawContext context,
      TextRenderer textRenderer,
      List<TooltipComponent> components,
      int x,
      int y
    ) {
        try {
            var drawTooltip = DrawContext.class.getDeclaredMethod("drawTooltip",
                                                                  TextRenderer.class,
                                                                  List.class,
                                                                  int.class,
                                                                  int.class,
                                                                  TooltipPositioner.class);
            drawTooltip.setAccessible(true);
            drawTooltip.invoke(context, textRenderer, components, x, y, HoveredTooltipPositioner.INSTANCE);
        } catch (NoSuchMethodException e) {
            System.out.println("NoSuchMethodException");
        } catch (InvocationTargetException e) {
            System.out.println("InvocationTargetException");
        } catch (IllegalAccessException e) {
            System.out.println("IllegalAccessException");
        }
    }

    @Override
    protected void drawMouseoverTooltip(DrawContext context, int x, int y) {
        if (this.handler.getCursorStack().isEmpty() && this.focusedSlot != null && this.focusedSlot.hasStack()) {
            ItemStack itemStack = this.focusedSlot.getStack();
            var tooltipText = this.getTooltipFromItem(itemStack);
            var tooltipData = itemStack.getTooltipData(); // абсолютный долбоебизм

            List<TooltipComponent> list =
              tooltipText.stream()
                         .map(Text::asOrderedText)
                         .map(TooltipComponent::of)
                         .collect(
                           Collectors.toList());
            tooltipData.ifPresent(data -> list.add(1, TooltipComponent.of(data)));

            var itemPrice = this.focusedSlot.id < handler.traderOffers.size()
              ? handler.getStackBuyPrice(itemStack)
              : handler.getStackSellPrice(itemStack);
            if (itemPrice != null)
                list.add(new PriceTooltipComponent(itemPrice));

            renderTooltip(context, textRenderer, list, x, y);
        }
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
    }
}
