package faceless.artent.sharpening.api;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public interface IEnhancer {
	Formatting getFormatting();

	Text getName();
}