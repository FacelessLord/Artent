package faceless.artent.sharpening.item;

import faceless.artent.sharpening.api.IEnhancer;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class EnhancerItem extends Item implements IEnhancer {
	private final Formatting formatting;
	private final String nameKey;

	public EnhancerItem(Formatting formatting, String nameKey, Settings settings) {
		super(settings);
		this.formatting = formatting;
		this.nameKey = nameKey;
	}

	@Override
	public Formatting getFormatting() {
		return formatting;
	}

	@Override
	public Text getName() {
		return Text.translatable(nameKey);
	}
}