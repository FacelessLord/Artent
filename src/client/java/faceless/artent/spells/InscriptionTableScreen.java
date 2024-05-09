package faceless.artent.spells;

import com.mojang.blaze3d.systems.RenderSystem;
import faceless.artent.Artent;
import faceless.artent.spells.api.ISpellInventoryItem;
import faceless.artent.spells.api.ISpellScroll;
import faceless.artent.spells.api.Spell;
import faceless.artent.spells.screenhandlers.InscriptionTableScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class InscriptionTableScreen extends HandledScreen<InscriptionTableScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(Artent.MODID, "textures/gui/spell_book.png");
    public static final Identifier SCROLL_BACKGROUND = new Identifier(Artent.MODID, "textures/gui/scroll.png");
    public static final Identifier PAPER_BACKGROUND = new Identifier(Artent.MODID, "textures/gui/paper.png");
    public static final Identifier BOOK_BACKGROUND = new Identifier(Artent.MODID, "textures/gui/spell_book_item.png");

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

        var spellContainerStack = handler.getSlot(0).getStack();
        if (!spellContainerStack.isEmpty() && spellContainerStack.getItem() instanceof ISpellInventoryItem item) {
            context.drawTexture(TEXTURE, x, y + 116, 176, 24, 0, 224, 176, 24, 256, 256);
            for (int i = 0; i < item.getSize(spellContainerStack); i++) {
                context.drawTexture(TEXTURE, x + 7 + i * 18, y + 3 + 116, 18, 18, 176, 224, 18, 18, 256, 256);
            }
        }
        RenderSystem.enableBlend();
        for (int i = 0; i < 3; i++) {
            var slot = handler.slots.get(i);
            if (slot.hasStack())
                continue;

            switch (i) {
                case 0 -> context.drawTexture(BOOK_BACKGROUND, slot.x + x, slot.y + y, 16, 16, 0, 0, 32, 32, 32, 32);
                case 1 -> context.drawTexture(PAPER_BACKGROUND, slot.x + x, slot.y + y, 16, 16, 0, 0, 32, 32, 32, 32);
                case 2 -> context.drawTexture(SCROLL_BACKGROUND, slot.x + x, slot.y + y, 16, 16, 0, 0, 32, 32, 32, 32);
            }
        }
    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        drawMouseoverTooltip(context, mouseX, mouseY);
    }

    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {

    }

    protected void drawMouseoverTooltip(DrawContext context, int x, int y) {
//        super.drawMouseoverTooltip(context, x, y);
        if (this.handler.getCursorStack().isEmpty() && this.focusedSlot != null && this.focusedSlot.hasStack()) {
            ItemStack itemStack = this.focusedSlot.getStack();
            var tooltip = this.getTooltipFromItem(itemStack);
            if (!(focusedSlot.inventory instanceof PlayerInventory) && focusedSlot.id > 3)
                tooltip.remove(0);
            context.drawTooltip(this.textRenderer, tooltip, itemStack.getTooltipData(), x, y);
        }
    }

    @Override
    protected void drawSlot(DrawContext context, Slot slot) {
        var itemStack = slot.getStack();
        var isBook = !(slot.inventory instanceof PlayerInventory) && slot.id > 3;
        if (!itemStack.isEmpty()
            && itemStack.getItem() instanceof ISpellScroll scroll
            && isBook
            && scroll.getSpell(itemStack) != null) {
            drawSpellInSlot(context, scroll.getSpell(itemStack), slot.x, slot.y);
            return;
        }
        super.drawSlot(context, slot);
    }

    public void drawSpellInSlot(DrawContext context, Spell spell, int x, int y) {
        if (spell == null)
            return;
        var spellIconPath = new Identifier(Artent.MODID, "textures/spell/" + spell.id + ".png");

        context.getMatrices().push();
        context.getMatrices().translate(x, y, 0);
        context.getMatrices().scale(0.5f, 0.5f, 1);
        context.drawTexture(spellIconPath, 0, 0, 0, 0, 0, 32, 32, 32, 32);
        context.getMatrices().pop();
    }

    @Override
    protected void init() {
        backgroundWidth = 201;
        backgroundHeight = (int) (backgroundWidth * 224f / 201f);
        super.init();
    }
}