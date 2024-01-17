package faceless.artent.sharpening.mixin;

import faceless.artent.sharpening.api.ISharpenable;
import net.minecraft.item.ToolItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ToolItem.class)
public class ToolMixin implements ISharpenable {
}