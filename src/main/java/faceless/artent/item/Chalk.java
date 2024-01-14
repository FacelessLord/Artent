package faceless.artent.item;

import faceless.artent.objects.ModBlocks;
import faceless.artent.transmutations.world.AlchemicalCircleBlock;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class Chalk extends ArtentItem {
	public Chalk() {
		super("chalk/chalk");
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		var blockSide = context.getSide();
		BlockPos circlePos = context.getBlockPos().add(blockSide.getVector());

		BlockState circleState = ModBlocks.alchemicalCircle
			.getDefaultState()
			.with(AlchemicalCircleBlock.FACING, blockSide);
		context.getWorld().setBlockState(circlePos, circleState);

		return super.useOnBlock(context);
	}
}
