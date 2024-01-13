package faceless.artent.transmutations.actions;

import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Box;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Random;

import faceless.artent.api.Color;
import faceless.artent.api.functions.Lazy;
import faceless.artent.transmutations.Transmutation;

public class ActionBurningAir extends Transmutation {
	private static final int PACKET_SIZE = 200;
	private static final int CHECK_FREQUENCY = 20;
	public Random random = new Random();

	public Lazy<Map<Item, Integer>> fuelTimeMap = new Lazy<>(AbstractFurnaceBlockEntity::createFuelTimeMap);

	public ActionBurningAir() {
		super("circle.burning_air", (entity, playerEntity) -> {
		});
		this.setTickAction((e, p, tick) -> {
			int fuel = e.circleTag.getInt("furnaceFuel");
			var center = e.getPos().toCenterPos();
			Box box = new Box(center.add(-1.5f, -1.5f, -1.5f), center.add(1.5f, 1.5f, 1.5f));
			List<ItemEntity> entityList = e.getWorld().getEntitiesByType(EntityType.ITEM, box, ie -> true);

			fuel += entityList.stream().map(ei -> {
				int fuelValue = fuelTimeMap.get().getOrDefault(ei.getStack().getItem(), 0);
				if (fuelValue > 0) {
					int result = fuelValue * ei.getStack().getCount();
					ei.setDespawnImmediately();
					return result;
				}
				return 0;
			}).reduce(Integer::sum).orElse(0);

			fuel += e.boundEntities
					.stream()
					.map(Pair::getLeft)
					.filter(be -> be instanceof FurnaceBlockEntity)
					.map(f -> (FurnaceBlockEntity) f)
					.filter(f -> f.getStack(1).getCount() > 0)
					.map(f -> {
						int fuelValue = fuelTimeMap.get().getOrDefault(f.getStack(1).getItem(), 0);
						if (fuelValue > 0) {
							int result = fuelValue * f.getStack(1).getCount();
							f.setStack(1, ItemStack.EMPTY);
							return result;
						}
						return 0;
					})
					.reduce(Integer::sum)
					.orElse(0) * 3 / 2;
			if (tick % CHECK_FREQUENCY == 0) {
				fuel -= e.boundEntities
						.stream()
						.map(Pair::getLeft)
						.filter(be -> be instanceof FurnaceBlockEntity)
						.map(be -> (FurnaceBlockEntity) be)
						.filter(f -> getBurnTime(f) <= 0 && canAcceptRecipeOutput(f, getRecipeFor(f)))
						.map(f -> {
							addBurnTime(f, PACKET_SIZE + 21);
							return PACKET_SIZE;
						})
						.reduce(Integer::sum)
						.orElse(0);
			}

			e.circleTag.putInt("furnaceFuel", fuel);
			return fuel <= 0;
		});
		this.setPrepCol(new Color(255, 140, 80));
		this.setActCol(new Color(255, 80, 40));
	}

	private Recipe<?> getRecipeFor(FurnaceBlockEntity f) {
		if (f.hasWorld())
			return f
					.getWorld()
					.getRecipeManager()
					.getFirstMatch(RecipeType.SMELTING, f, f.getWorld())
					.map(e -> e.value())
					.orElse(null);
		return null;
	}

	private int getBurnTime(FurnaceBlockEntity f) {
		try {
			Field b = AbstractFurnaceBlockEntity.class.getDeclaredField("burnTime");
			b.setAccessible(true);
			return (int) b.get(f);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return 0;
	}

	private void addBurnTime(FurnaceBlockEntity f, int value) {
		try {
			Field b = AbstractFurnaceBlockEntity.class.getDeclaredField("burnTime");
			b.setAccessible(true);
			b.set(f, value);
			f
					.getWorld()
					.setBlockState(
							f.getPos(),
							f.getWorld().getBlockState(f.getPos()).with(AbstractFurnaceBlock.LIT, true),
							3);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public boolean canAcceptRecipeOutput(AbstractFurnaceBlockEntity inventory, Recipe<?> recipe) {
		if (!inventory.getStack(0).isEmpty() && recipe != null) {
			var registryManager = inventory.getWorld().getRegistryManager();
			ItemStack itemStack = recipe.getResult(registryManager);
			if (itemStack.isEmpty()) {
				return false;
			} else {
				ItemStack itemStack2 = inventory.getStack(2);
				if (itemStack2.isEmpty()) {
					return true;
				} else if (!ItemStack.areItemsEqual(itemStack2, itemStack)) {
					return false;
				} else if (itemStack2.getCount() < inventory.getMaxCountPerStack()
						&& itemStack2.getCount() < itemStack2.getMaxCount()) {
					return true;
				} else {
					return itemStack2.getCount() < itemStack.getMaxCount();
				}
			}
		} else {
			return false;
		}
	}
}