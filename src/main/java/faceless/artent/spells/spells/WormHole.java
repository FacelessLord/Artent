package faceless.artent.spells.spells;

import faceless.artent.api.DirectionUtils;
import faceless.artent.objects.ModBlocks;
import faceless.artent.spells.api.ICaster;
import faceless.artent.spells.api.Spell;
import faceless.artent.spells.blockEntity.VoidBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class WormHole extends Spell {
	public WormHole() {
		super("wormhole", ActionType.BlockCast, 8);
	}

	@Override
	public void blockCast(ICaster caster, World world, ItemStack stack, BlockPos blockPos, Direction hitSide, int actionTime) {
		voidLayer(world, blockPos, hitSide.getOpposite());
	}

	public static void voidLayer(World world, BlockPos blockPos, Direction hitSide) {
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				var baseVec = new int[]{i, 0, j};
				var transformedVec = DirectionUtils.applyDirection(baseVec, hitSide);
				var offsetPos = blockPos.add(transformedVec[0], transformedVec[1], transformedVec[2]);

				voidBlock(world, offsetPos, hitSide, 8, blockPos);
			}
		}
	}

	public static void voidBlock(World world, BlockPos offsetPos, Direction direction, int depth, BlockPos centerPos) {
		var blockState = world.getBlockState(offsetPos);
		NbtCompound tileState = new NbtCompound();
		if (!world.getBlockState(offsetPos).isOpaqueFullCube(world, offsetPos))
			return;
		if (blockState.hasBlockEntity()) {
			var blockEntity = world.getBlockEntity(offsetPos);
			if (blockEntity != null) {
				tileState = blockEntity.createNbt();
				blockEntity.readNbt(new NbtCompound()); // TODO
			}
		}
		world.setBlockState(offsetPos, ModBlocks.VoidBlock.getDefaultState(), Block.NOTIFY_ALL_AND_REDRAW | Block.SKIP_DROPS);
		var voidEntity = world.getBlockEntity(offsetPos);
		if (voidEntity instanceof VoidBlockEntity vbe) {
			vbe.replacedBlockTag = NbtHelper.fromBlockState(blockState);
			vbe.replacedEntityTag = tileState;
			vbe.centerPos = centerPos;
			vbe.depth = depth;
			vbe.direction = direction;
		} else {
			System.err.println("AA");
		}
	}
}
