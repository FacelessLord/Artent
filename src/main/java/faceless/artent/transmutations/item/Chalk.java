package faceless.artent.transmutations.item;

import faceless.artent.api.item.ArtentItem;
import faceless.artent.network.ArtentServerHook;
import faceless.artent.objects.ModBlocks;
import faceless.artent.transmutations.block.AlchemicalCircleBlock;
import faceless.artent.transmutations.blockEntities.AlchemicalCircleEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;

public class Chalk extends ArtentItem {
	public Chalk(Settings settings) {
		super(settings, "chalk/chalk");
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		var blockSide = context.getSide();
		BlockPos circlePos = context.getBlockPos().add(blockSide.getVector());
		var world = context.getWorld();
		if (world == null)
			return ActionResult.FAIL;

		BlockState circleState = ModBlocks.AlchemicalCircle
			.getDefaultState()
			.with(AlchemicalCircleBlock.FACING, blockSide);
		world.setBlockState(circlePos, circleState, Block.NOTIFY_ALL);

		BlockEntity blockEntity = world.getBlockEntity(circlePos);
		if (!(blockEntity instanceof AlchemicalCircleEntity circle))
			return ActionResult.FAIL;

		var player = context.getPlayer();
		if (player == null)
			return ActionResult.FAIL;

		ArtentServerHook.packetOpenCircleGui(player, circle);
		return ActionResult.SUCCESS;
	}
}
