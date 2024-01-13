package faceless.artent.item;

import faceless.artent.transmutations.CircleHelper;
import faceless.artent.transmutations.world.AlchemicalCircleEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PaperSheet extends ArtentItem {
	public PaperSheet(Settings settings) {
		super("paper_sheet", settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		BlockEntity entity = world.getBlockEntity(pos);
		if (entity instanceof AlchemicalCircleEntity) {
			String formula = CircleHelper.createCircleFormula(((AlchemicalCircleEntity) entity).parts);
			context.getPlayer().sendMessage(Text.translatable(formula), true);
		}
		return super.useOnBlock(context);
	}
}
