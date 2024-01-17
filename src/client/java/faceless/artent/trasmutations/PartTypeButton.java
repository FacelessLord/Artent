package faceless.artent.trasmutations;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.ScreenTexts;

import static net.minecraft.client.gui.screen.Screen.hasShiftDown;

import faceless.artent.api.MiscUtils;
import faceless.artent.transmutations.api.PartType;

@Environment(EnvType.CLIENT)
public class PartTypeButton extends ButtonWidget {
	private final PartType type;

	protected PartTypeButton(int x, int y, int width, PartType type, PressAction onPress) {
		//noinspection SuspiciousNameCombination
		super(x, y, width, width, ScreenTexts.EMPTY, onPress, DEFAULT_NARRATION_SUPPLIER);
		this.type = type;
	}

	@Override
	protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
//    	super.renderWidget(context, mouseX, mouseY, delta);
		var x = getX();
		var y = getY();

		int selected = MiscUtils.isInRange(mouseX, x, x + width) && MiscUtils.isInRange(mouseY, y, y + height) ? 1 : 0;

		context.drawTexture(AlchemicalCircleGui.TEXTURE, x, y, width, height, 128f, selected * 24f, 24, 24, 256, 256);
		var partTypeTexture = hasShiftDown() ? type.itemTextureRev : type.itemTexture;
		context.drawTexture(partTypeTexture, x, y, width, height, 0, 0, 64, 64, 64, 64);
	}
}
