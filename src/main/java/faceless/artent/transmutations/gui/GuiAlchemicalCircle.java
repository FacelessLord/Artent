//package faceless.artent.transmutations.gui;
//
//import com.faceless.artent.Core;
//import com.faceless.artent.common.transmutations.world.AlchemicalCircleEntity;
//import com.mojang.blaze3d.systems.RenderSystem;
//
//import faceless.artent.Artent;
//import faceless.artent.transmutations.api.CirclePart;
//import net.fabricmc.api.EnvType;
//import net.fabricmc.api.Environment;
//import net.minecraft.client.MinecraftClient;
//import net.minecraft.client.gui.screen.Screen;
//import net.minecraft.text.TranslatableText;
//import net.minecraft.util.Identifier;
//
//import static com.faceless.artent.api.common.inventory.GuiHelper.drawRectangle;
//
//@Environment(EnvType.CLIENT)
//public class GuiAlchemicalCircle extends Screen {
//    public static final Identifier guiTexture = new Identifier(Artent.MODID, "textures/gui/alchemical_circle.png");
//    private final AlchemicalCircleBlockEntity entity;
//
//    public GuiAlchemicalCircle(String title, AlchemicalCircleEntity entity) {
//        super(Text.translatable(title));
//        this.entity = entity;
//    }
//
//    @Override
//    public void init(MinecraftClient client, int width, int height) {
//        super.init(client, width, height);
//        PartType[] types = PartType.values();
//        int k = this.width / 2;
//        int l = this.height / 2;
//        int scaleFactor = (int) Math.floor(this.height / 180f);
//        int i = 0;
//        int j = 0;
//        for (PartType type : types) {
//            addButton(new PartTypeButton(k + 64 * scaleFactor + j * 24 * scaleFactor, l - 64 * scaleFactor + 24 * i * scaleFactor, 24 * scaleFactor, type,
//                    b -> {
//                        entity.addPart(type, hasShiftDown());
//                        AlchemicalCircleNetworkHook.packetSynchronizeCircle(entity);
//                    }));
//            i++;
//            if (i > 4) {
//                i = 0;
//                j++;
//            }
//        }
//    }
//
//    @Override
//    public void render(int mouseX, int mouseY, float delta) {
//        super.render(mouseX, mouseY, delta);
//        int k = this.width / 2;
//        int l = this.height / 2;
//        int scaleFactor = (int) Math.floor(this.height / 180f);
//        RenderSystem.scalef(scaleFactor, scaleFactor, 1);
//        RenderSystem.enableDepthTest();
//
//        MinecraftClient.getInstance().getTextureManager().bindTexture(guiTexture);
//        drawRectangle(k, l, 0, 64, 64, 256, 256, 0, 0, 128, 128);
//
//        for (CirclePart part : entity.parts) {
//            if (part.reverse)
//                MinecraftClient.getInstance().getTextureManager().bindTexture(part.part.itemTextureRev);
//            else
//                MinecraftClient.getInstance().getTextureManager().bindTexture(part.part.itemTexture);
//            drawRectangle(k, l, 0, 56, 56, 1, 1, 0, 0, 1, 1);
//        }
//    }
//}
