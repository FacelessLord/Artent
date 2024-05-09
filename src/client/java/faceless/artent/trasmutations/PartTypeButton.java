package faceless.artent.trasmutations;

import faceless.artent.transmutations.api.PartType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.ScreenTexts;

import java.util.Optional;

import static net.minecraft.client.gui.screen.Screen.hasShiftDown;

@Environment(EnvType.CLIENT)
public class PartTypeButton extends ButtonWidget {
    private final PartType type;
    private Optional<Boolean> reversed = Optional.empty();

    protected PartTypeButton(int x, int y, int width, PartType type, PressAction onPress) {
        //noinspection SuspiciousNameCombination
        super(x, y, width, width, ScreenTexts.EMPTY, onPress, DEFAULT_NARRATION_SUPPLIER);
        this.type = type;
    }

    protected PartTypeButton(int x, int y, int width, PartType type, PressAction onPress, boolean reversed) {
        //noinspection SuspiciousNameCombination
        super(x, y, width, width, ScreenTexts.EMPTY, onPress, DEFAULT_NARRATION_SUPPLIER);
        this.type = type;
        this.reversed = Optional.of(reversed);
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        var x = getX();
        var y = getY();

        int selected = this.isMouseOver(mouseX, mouseY) ? 1 : 0;

        context.drawTexture(AlchemicalCircleGui.TEXTURE, x, y, width, height, 128f, selected * 24f, 24, 24, 256, 256);
        var partTypeTexture = reversed.orElse(hasShiftDown()) ? type.itemTextureRev : type.itemTexture;
        context.drawTexture(partTypeTexture, x, y, width, height, 0, 0, 64, 64, 64, 64);
    }
}
