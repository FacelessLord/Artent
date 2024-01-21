package faceless.artent.sharpening.item;

import faceless.artent.api.item.INamed;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;

public class SmithingHammer extends SwordItem implements INamed {
	public SmithingHammer(Settings settings) {
		super(ToolMaterials.IRON, 3, -2.4f, settings);
	}

	@Override
	public String getId() {
		return "smithing_hammer";
	}
}