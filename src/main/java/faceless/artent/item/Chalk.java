package faceless.artent.item;

import faceless.artent.objects.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

public class Chalk extends ArtentItem {
	public Chalk() {
		super("chalk/chalk");
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		BlockPos circlePos = context.getBlockPos().add(0, 1, 0);
		BlockState blockState = context.getWorld().getBlockState(circlePos);
		if (blockState.isAir()) {
			BlockState circleState = ModBlocks.alchemicalCircle.getDefaultState();
			context.getWorld().setBlockState(circlePos, circleState);
		}
		return super.useOnBlock(context);
	}
}
