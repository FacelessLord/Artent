package faceless.artent.trasmutations;

import faceless.artent.Artent;
import faceless.artent.network.ArtentClientHook;
import faceless.artent.transmutations.api.CirclePart;
import faceless.artent.transmutations.api.PartType;
import faceless.artent.transmutations.blockEntities.AlchemicalCircleEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class AlchemicalCircleGui extends Screen {
	public static final Identifier TEXTURE = new Identifier(Artent.MODID, "textures/gui/alchemical_circle.png");
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
		PartType[] types = PartType.values();
		var range = Math.max(backgroundWidth, backgroundHeight);
		int k = (width + range) / 2;
		int l = (height - range) / 2;
		int scaleFactor = (int) Math.floor(this.height / 180f);
		int i = 0;
		int j = 0;
		for (PartType type : types) {
			var button = new PartTypeButton(k + j * 24 * scaleFactor, l + 24 * i, 24, type,
				b -> {
					circle.addPart(type, (type.itemTexture != type.itemTextureRev) && hasShiftDown());
					ArtentClientHook.packetSynchronizeCircle(circle);
					updateCurrentParts();
				});
			addDrawableChild(button);
			i++;
			if (i > 6) {
				i = 0;
				j++;
			}
		}

		updateCurrentParts();
	}

	@Override
	public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
		super.renderBackground(context, mouseX, mouseY, delta);
		this.drawBackground(context);
	}

	private List<PartTypeButton> currentParts = new ArrayList<>();

	public void updateCurrentParts() {
		for (PartTypeButton currentPartButton : currentParts) {
			remove(currentPartButton);
		}
		currentParts.clear();

		var range = Math.max(backgroundWidth, backgroundHeight);
		int l = (height - range) / 2;

		int scaleFactor = (int) Math.floor(this.height / 180f);
		int i = 0;
		int j = 0;

		var drawnParts = circle.parts;

		int u = (width - range) / 2;
		for (CirclePart part : drawnParts) {
			var button = new PartTypeButton(u - (1 + j) * 24 * scaleFactor, l + 24 * i, 24, part.part,
				b -> {
					circle.removePart(part);
					ArtentClientHook.packetSynchronizeCircle(circle);
					updateCurrentParts();
				}, part.reverse);
			addDrawableChild(button);
			currentParts.add(button);
			i++;
			if (i > 6) {
				i = 0;
				j++;
			}
		}
	}

	protected void drawBackground(DrawContext context) {
		var range = Math.max(backgroundWidth, backgroundHeight);
		int x = (width - range) / 2;
		int y = (height - range) / 2;
		context.drawTexture(TEXTURE, x, y, range, range, 8, 8, 112, 112, 256, 256);


		for (CirclePart part : circle.parts) {
			Identifier texture;
			if (part.reverse)
				texture = part.part.blockTextureRev;
			else
				texture = part.part.blockTexture;

			context.drawTexture(texture, x, y, range, range, 0, 0, 1024, 1024, 1024, 1024);
		}
	}

	@Override
	public void close() {
		super.close();
		if (circle.parts.size() == 0)
			ArtentClientHook.packetRemoveCircle(circle.getPos());
	}
}
