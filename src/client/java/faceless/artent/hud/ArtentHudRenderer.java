package faceless.artent.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import faceless.artent.Artent;
import faceless.artent.api.math.Color;
import faceless.artent.playerData.api.DataUtil;
import faceless.artent.spells.api.ICaster;
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
        var casterInfo = DataUtil.getCasterInfo(ctx.player());
        var caster = (ICaster) ctx.player();

        if (!bookStack.isEmpty()) {
            var spellInventory = new ItemSpellInventory(bookStack);
            var spellInventorySize = spellInventory.getSize();

            matrices.push();
            matrices.translate(42, 0, -90.0f);

            context.drawTexture(SPELL_BOOK_HUD, 0, 0, 0, 0, 168, 24, 256, 256);

            var cooldown = 0;
            if (casterInfo.maxCooldown != 0) {
                cooldown = (18 * casterInfo.cooldown) / casterInfo.maxCooldown;
            }

            for (int j = 0; j < spellInventorySize; j++) {
                matrices.push();
                matrices.translate(3 + 18 * j, 3, 0);

                context.drawTexture(SPELL_BOOK_HUD, 0, 0, 0, 24, 18, 18, 256, 256);

                var spell = spellInventory.getSpell(j);
                if (spell != null && spell.spell != null) {
                    var spellIconPath = new Identifier(Artent.MODID, "textures/spell/" + spell.spell.id + ".png");
                    context.drawTexture(spellIconPath, 0, 0, 18, 18, 0, 0, 32, 32, 32, 32);

                    var potency = caster.getPotency();
                    RenderSystem.enableBlend();

                    if (potency < spell.spell.settings.minimalPotency) {
                        RenderSystem.clearColor(1, 0, 0, 0.5f);
                        context.setShaderColor(1, 0, 0, 0.5f);
                        context.drawTexture(SPELL_BOOK_HUD, 0, 0, 18, 24, 18, 18, 256, 256);
                        context.setShaderColor(1, 1, 1, 1);
                    } else if (cooldown != 0) {
                        RenderSystem.clearColor(1, 1, 1, 0.5f);
                        context.setShaderColor(1, 1, 1, 0.25f);
                        context.drawTexture(SPELL_BOOK_HUD, 0, 18 - cooldown, 18, 24, 18, cooldown, 256, 256);
                        context.setShaderColor(1, 1, 1, 1);
                    }
                    RenderSystem.disableBlend();
                }
                matrices.pop();
            }
            var spellIndex = DataUtil.getCasterInfo(ctx.player()).getSpellBookIndex() % spellInventory.getSize();

            context.drawTexture(SPELL_BOOK_HUD, 1 + 18 * spellIndex, 0, 168, 0, 22, 32, 256, 256);

            matrices.pop();
        }

        var maxMana = casterInfo.getMaxMana(ctx.player());
        var mana = casterInfo.mana;
        if (maxMana != 0) {
            matrices.push();

            matrices.translate(0, 42, -90.0f);

            context.drawTexture(SPELL_BOOK_HUD, 0, 0, 0, 42, 18, 127, 256, 256);

            if (mana == maxMana) // filled mana ball
                context.drawTexture(SPELL_BOOK_HUD, 0, 2, 32, 42, 18, 18, 256, 256);
            var fillPercentage = 1f * mana / maxMana;
            var manaHeight = (int) (109 * fillPercentage);
            var manaOffset = 214 - (int) ((ctx.player().getWorld().getTime() + fillPercentage * 112) % 214);
            // pt1
            var pt1Height = Math.min(manaOffset + manaHeight, 214) - manaOffset;
            context.drawTexture(SPELL_BOOK_HUD, 2, 125 - manaHeight, 18, 42 + manaOffset, 18, pt1Height, 256, 256);
            if (pt1Height < manaHeight) {
                var pt2Height = manaHeight - pt1Height;
                context.drawTexture(SPELL_BOOK_HUD, 2, 125 - manaHeight + pt1Height, 18, 42, 18, pt2Height, 256, 256);
            }
            RenderSystem.enableBlend();
            context.drawTexture(SPELL_BOOK_HUD, 0, 0, 50, 42, 18, 127, 256, 256);
            RenderSystem.disableBlend();
            matrices.pop();
        }

        {
            var heroInfo = DataUtil.getHeroInfo(ctx.player());

            matrices.push();

            matrices.translate(18, 42, -90.0f);

            var levelString = String.valueOf(heroInfo.getLevel());
            context.drawTextWithShadow(ctx.textRenderer(), levelString, 4 - 2 * levelString.length(), 2, 0xe9c02e);

            context.drawTexture(SPELL_BOOK_HUD, 0, 14, 68, 42 + 14, 9, 113, 256, 256);

            var fillPercentage = 1f * heroInfo.experience / heroInfo.getExperienceToLevel();
            var experienceHeight = (int) Math.min(109 * fillPercentage, 109);
            context.drawTexture(
              SPELL_BOOK_HUD,
              2,
              125 - experienceHeight,
              77,
              42 + 14 + 2 + 109 - experienceHeight,
              5,
              experienceHeight,
              256,
              256
            );
            matrices.pop();
        }
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
