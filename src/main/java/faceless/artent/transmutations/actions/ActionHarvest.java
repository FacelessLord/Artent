package faceless.artent.transmutations.actions;

import faceless.artent.api.Color;
import faceless.artent.transmutations.Transmutation;
import net.minecraft.block.*;
import net.minecraft.world.World;

public class ActionHarvest extends Transmutation {

	public ActionHarvest(int level) {
		super("circle.harvest", (e, p) -> {
		});
		this.setTickAction((e, p, tick) -> {
			final int height = 2 * level + 1, width = 4 * level + 4;
			World world = e.getWorld();

			return randomPoints(world, 64, e.getPos(), width, height, (pos, state) -> {
				Block b = state.getBlock();
				BlockState cropState = world.getBlockState(pos.up());
				if (b instanceof FarmlandBlock && !cropState.isAir()) {
					Block crop = cropState.getBlock();
					if (crop instanceof CropBlock) {
						if (((CropBlock) crop).isMature(cropState)) {
							world.breakBlock(pos, true);
						}
					}
					if (crop == Blocks.MELON || crop == Blocks.PUMPKIN) {
						world.breakBlock(pos, true);
					}
				}
				return false;
			}, Behaviour.DoAll);
		});
		this.setPrepCol(new Color(80, 255, 80));
		this.setActCol(new Color(40, 255, 40));
	}
}
