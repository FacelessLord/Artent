package faceless.artent.hud;

import faceless.artent.Artent;
import faceless.artent.api.math.Color;
import faceless.artent.playerData.api.DataUtil;
import faceless.artent.spells.api.ISpellInventoryItem;
import faceless.artent.spells.api.ItemSpellInventory;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

public class ArtentHudRenderer {
    private static final Identifier POUCH_TEXTURE = new Identifier(Artent.MODID, "hud/pouch");
    public static final Identifier GOLD_COIN_TEXTURE = new Identifier(Artent.MODID, "textures/hud/coin_gold.png");
    public static final Identifier SILVER_COIN_TEXTURE = new Identifier(Artent.MODID, "textures/hud/coin_silver.png");
    public static final Identifier BRONZE_COIN_TEXTURE = new Identifier(Artent.MODID, "textures/hud/coin_bronze.png");
    public static final Identifier SPELL_BOOK_HUD = new Identifier(Artent.MODID, "textures/hud/spell_book_hud.png");

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
        matrices.translate(i + 91 + 8, ctx.scaledHeight() - 22 + 14, -90.0f);
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

        var bookStack = getBookStack(ctx);
        if (bookStack.isEmpty())
            return;

        var spellInventory = new ItemSpellInventory(bookStack);
        var spellInventorySize = spellInventory.getSize();

        matrices.push();
        matrices.translate(42, 0, -90.0f);

        context.drawTexture(SPELL_BOOK_HUD, 0, 0, 0, 0, 168, 24, 256, 256);

        for (int j = 0; j < spellInventorySize; j++) {
            matrices.push();
            matrices.translate(3 + 18 * j, 3, 0);

            context.drawTexture(SPELL_BOOK_HUD, 0, 0, 0, 24, 18, 18, 256, 256);

            var spell = spellInventory.getSpell(j);
            if (spell != null) {
                var spellIconPath = new Identifier(Artent.MODID, "textures/spell/" + spell.spell.id + ".png");
                context.drawTexture(spellIconPath, 0, 0, 18, 18, 0, 0, 32, 32, 32, 32);
            }
            matrices.pop();
        }
        var spellIndex = DataUtil.getCasterInfo(ctx.player()).getSpellBookIndex() % spellInventory.getSize();

        context.drawTexture(SPELL_BOOK_HUD, 1 + 18 * spellIndex, 0, 168, 0, 22, 32, 256, 256);

        matrices.pop();

    }

    private static ItemStack getBookStack(ArtentHudContext ctx) {
        var mainStack = ctx.player().getStackInHand(Hand.MAIN_HAND);
        if (!mainStack.isEmpty() && mainStack.getItem() instanceof ISpellInventoryItem)
            return mainStack;
        var offStack = ctx.player().getStackInHand(Hand.OFF_HAND);
        if (!offStack.isEmpty() && offStack.getItem() instanceof ISpellInventoryItem)
            return offStack;
        return ItemStack.EMPTY;
    }
}
