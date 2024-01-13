package faceless.artent.trasmutations;

import faceless.artent.Artent;
import faceless.artent.transmutations.CirclePart;
import faceless.artent.transmutations.gui.PartType;
import faceless.artent.transmutations.world.AlchemicalCircleEntity;
import faceless.artent.trasmutations.network.AlchemicalCircleClientHook;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class AlchemicalCircleGui extends Screen {
    public static final Identifier guiTexture = new Identifier(Artent.MODID, "textures/gui/alchemical_circle.png");
    public AlchemicalCircleEntity circle;
    protected int backgroundWidth = 176;
    protected int backgroundHeight = 166;
    
	public AlchemicalCircleGui(AlchemicalCircleEntity circle) {
		super(Text.translatable(Artent.MODID + ".gui.alchemical_circle.title"));
		this.circle = circle;
	}
	
	@Override
	protected void init() {
		super.init();

		System.out.println("Init gui");
		PartType[] types = PartType.values();
		int k = (width + backgroundWidth) / 2;
		int l = (height - backgroundHeight) / 2;
        int scaleFactor = (int) Math.floor(this.height / 180f);
        int i = 0;
        int j = 0;
        for (PartType type : types) {
        	var button = new PartTypeButton(k + j * 24 * scaleFactor, l + 24 * i, 24, type,
                    b -> {
						circle.addPart(type, hasShiftDown());
						AlchemicalCircleClientHook.packetSynchronizeCircle(circle);
					});
			addDrawableChild(button);
            i++;
            if (i > 6) {
                i = 0;
                j++;
            }
        }
	}

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderBackground(context, mouseX, mouseY, delta);
        this.drawBackground(context, delta, mouseX, mouseY);
    }

	protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
		int x = (width - backgroundWidth) / 2;
		int y = (height - backgroundHeight) / 2;
		context.drawTexture(AlchemicalCircleGui.guiTexture, x, y, backgroundWidth, backgroundHeight, 8, 8, 112, 112, 256, 256);
		

        for (CirclePart part : circle.parts) {
        	Identifier texture;
            if (part.reverse)
            	texture = part.part.itemTextureRev;
            else
            	texture = part.part.itemTexture;
        	
    		context.drawTexture(texture, x, y, backgroundWidth, backgroundHeight, 0, 0, 1024, 1024, 1024, 1024);
        }
	}
}
