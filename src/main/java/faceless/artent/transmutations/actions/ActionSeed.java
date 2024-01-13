package faceless.artent.transmutations.actions;

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

import faceless.artent.api.Color;
import faceless.artent.transmutations.Transmutation;

public class ActionSeed extends Transmutation {

	public ActionSeed(int level) {
		super("circle.seed", (e, p) -> {
		});
		this.setTickAction((e, p, tick) -> {
			final int height = 2 * level + 1, width = 4 * level + 4;
			World world = e.getWorld();

			return randomPoints(world, 64, e.getPos(), width, height, (pos, state) -> {
				Block b = state.getBlock();
				if (b instanceof FarmlandBlock && world.getBlockState(pos.up()).isAir()) {
					var center = e.getPos().toCenterPos();
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
										pos.up(),
										((AliasedBlockItem) seeds.getItem()).getBlock().getDefaultState());
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
