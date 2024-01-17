package faceless.artent.transmutations.item;

import faceless.artent.api.item.ArtentItem;
import faceless.artent.objects.ModBlocks;
import faceless.artent.transmutations.block.AlchemicalCircleBlock;
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
		var blockSide = context.getSide();
		BlockPos circlePos = context.getBlockPos().add(blockSide.getVector());

		BlockState circleState = ModBlocks.AlchemicalCircle
			.getDefaultState()
			.with(AlchemicalCircleBlock.FACING, blockSide);
		context.getWorld().setBlockState(circlePos, circleState);

		return super.useOnBlock(context);
	}
}
