package faceless.artent.transmutations.actions;

import faceless.artent.api.math.Color;
import faceless.artent.transmutations.api.Transmutation;
import net.minecraft.block.Block;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.block.PlantBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

public class ActionSeed extends Transmutation {

	public ActionSeed(int level) {
		super("circle.seed", (facing, e, p) -> {
		});
		this.setTickAction((facing, e, p, tick) -> {
			final int height = 2 * level + 1, width = 4 * level + 4;
			World world = e.getWorld();
			if (world == null)
				return false;

			return randomPoints(world, 64, e.getPos(), facing, width, height, (pos, state) -> {
				Block b = state.getBlock();
				if (b instanceof FarmlandBlock && world.getBlockState(pos.up()).isAir()) {
					var center = e.getPos().toCenterPos();
					// box is symmetrical so no need for applying direction
					Box box = new Box(center.add(-1.5f, -1.5f, -1.5f), center.add(1.5f, 1.5f, 1.5f));
					List<ItemEntity> entityList = e
						.getWorld()
						.getEntitiesByType(EntityType.ITEM, box, ie -> stackIsSeed(ie.getStack()));
					if (!entityList.isEmpty()) {
						ItemEntity seedsEntity = entityList.get(0);
						ItemStack seeds = seedsEntity.getStack();
						if (seeds.isEmpty())
							return true;

						world
							.setBlockState(
								// seeds grow on top only
								pos.up(),
								((AliasedBlockItem) seeds.getItem()).getBlock().getDefaultState()
							);
					}
				}
				return false;
			}, Behaviour.StopOnTrue);
		});
		this.setPrepCol(new Color(80, 255, 80));
		this.setActCol(new Color(40, 255, 40));
	}

	public static boolean stackIsSeed(ItemStack stack) {
		return stack.getItem() instanceof AliasedBlockItem
			&& ((AliasedBlockItem) stack.getItem()).getBlock() instanceof PlantBlock;
	}
}
