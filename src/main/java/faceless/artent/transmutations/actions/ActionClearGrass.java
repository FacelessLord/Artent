package faceless.artent.transmutations.actions;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import faceless.artent.api.math.Color;
import faceless.artent.transmutations.api.Transmutation;

public class ActionClearGrass extends Transmutation {
	public static Set<Block> grass = Arrays
			.stream(new Block[]{ Blocks.TALL_GRASS, Blocks.SEAGRASS, Blocks.TALL_SEAGRASS })
			.collect(Collectors.toSet());
	public static Set<Block> flowers = Arrays
			.stream(
					new Block[]{ Blocks.DANDELION, Blocks.POPPY, Blocks.ORANGE_TULIP, Blocks.PINK_TULIP,
							Blocks.RED_TULIP, Blocks.WHITE_TULIP, Blocks.SUNFLOWER, Blocks.ROSE_BUSH,
							Blocks.BLUE_ORCHID, Blocks.LILY_OF_THE_VALLEY, Blocks.AZURE_BLUET, Blocks.ALLIUM,
							Blocks.BELL, Blocks.DEAD_BUSH, Blocks.OXEYE_DAISY, Blocks.CORNFLOWER })
			.collect(Collectors.toSet());
	public static Set<Block> plants = Arrays
			.stream(
					new Block[]{ Blocks.ACACIA_LEAVES, Blocks.BIRCH_LEAVES, Blocks.DARK_OAK_LEAVES,
							Blocks.JUNGLE_LEAVES, Blocks.OAK_LEAVES, Blocks.OAK_LEAVES, Blocks.ACACIA_LOG,
							Blocks.BIRCH_LOG, Blocks.DARK_OAK_LOG, Blocks.JUNGLE_LOG, Blocks.OAK_LOG, Blocks.SPRUCE_LOG,
							Blocks.BAMBOO })
			.collect(Collectors.toSet());

	public ActionClearGrass(int level) {
		super("circle.clear_grass", (facing, e, p) -> {
		});
		this.setTickAction((facing, e, p, tick) -> {
			final int height = 2 * level + 1, width = 4 * level + 4;
			World world = e.getWorld();
			if (world == null)
				return false;

			BlockPos entityPos = e.getPos();
			boolean dirty = randomPoints(world, 64, entityPos, facing, width, height, (pos, state) -> {
				Block b = state.getBlock();
				if ((grass.contains(b) || flowers.contains(b) || plants.contains(b))) {
					world.breakBlock(pos, true);
					return true;
				}
				return false;
			}, Behaviour.DoAll);
			return !(dirty || tick < 625 * 2);
		});

		this.setPrepCol(new Color(80, 255, 80));
		this.setActCol(new Color(40, 255, 40));
	}
}