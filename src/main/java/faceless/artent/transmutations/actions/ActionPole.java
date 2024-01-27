package faceless.artent.transmutations.actions;

import faceless.artent.api.DirectionUtils;
import faceless.artent.api.math.Color;
import faceless.artent.objects.ModBlocks;
import faceless.artent.transmutations.api.Transmutation;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

public class ActionPole extends Transmutation {
	public ActionPole(int level) {
		super("circle.pole", (facing, e, p) -> {
			final int heightRange = level + 3, widthRange = 2 * level;
			var circle = e.getPos();
			var world = e.getWorld();
			if (world == null)
				return;

			for (int j = 1; j <= heightRange; j++) {
				for (int i = -widthRange; i <= widthRange; i++) {
					for (int k = -widthRange; k <= widthRange; k++) {
						var rotated = DirectionUtils.applyDirection(new int[]{ i, -j, k }, facing);

						var blockPos = circle.add(rotated[0], rotated[1], rotated[2]);
						var state = world.getBlockState(blockPos);
						var block = state.getBlock();
						if (block == Blocks.AIR || block == Blocks.CAVE_AIR || block == Blocks.VOID_AIR) {
							continue;
						}

						var newPos = blockPos;
						int l = heightRange;
						for (; l > 0; l--) {
							var newPosTemp = blockPos.offset(facing, l);
							var tempState = world.getBlockState(newPosTemp);
							var targetBlock = tempState.getBlock();
							if (targetBlock == Blocks.AIR || targetBlock == Blocks.CAVE_AIR || targetBlock == Blocks.VOID_AIR || targetBlock == ModBlocks.AlchemicalCircle) {
								newPos = newPosTemp;
								break;
							}
						}
						if (l == 0)
							continue;

						// immediately creates blockEntity and writes it to chunk
						world.setBlockState(newPos, state, Block.NOTIFY_ALL);
						var entity = world.getBlockEntity(blockPos);
						if (entity != null) {
							var tag = entity.createNbt();

							var newEntity = world.getBlockEntity(newPos);
							if (newEntity == null) {
								System.out.println("new entity is not created on setBlockState");
								continue;
							}

							newEntity.readNbt(tag);
						}

						world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL);
					}
				}
			}

			var state = world.getBlockState(circle);
			if (state == null)
				return;

			world.setBlockState(circle, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL);
		});

		this.setPrepCol(new Color(80, 80, 255));
		this.setActCol(new Color(40, 40, 255));
	}
}
