package faceless.artent.transmutations.actions;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;

import java.util.List;

import faceless.artent.transmutations.Transmutation;
import faceless.artent.transmutations.world.AlchemicalCircleEntity;

public class ActionCollectItems extends Transmutation {
	public ActionCollectItems(int level) {
		super("circle.item_collector", (e, p) -> {
		});
		this.setTickAction((e, p, tick) -> {
			final float heightRange = level + 1.5f, widthRange = 2 * level + 1.5f;
			var center = e.getPos().toCenterPos();
			Box box = new Box(
					center.add(-widthRange, -0.5f, -widthRange),
					center.add(widthRange, heightRange, widthRange));
			List<ItemEntity> entityList = e.getWorld().getEntitiesByType(EntityType.ITEM, box, ie -> true);

			if (entityList.size() <= 0)
				return false;
			ItemEntity itemEntity = entityList.get(0);
			ItemStack stack = itemEntity.getStack();
			e.boundEntities.stream().forEach(pair -> {
				if (!(pair.getLeft() instanceof Inventory))
					return;
				Inventory inv = ((Inventory) pair.getLeft());
				for (int i = 0; i < inv.size(); i++) {
					ItemStack invStack = inv.getStack(i);
					if (invStack.isEmpty()) {
						inv.setStack(i, stack);
						itemEntity.setDespawnImmediately();

						return;
					}
					if (invStack.getItem() == stack.getItem()
							&& invStack.getOrCreateNbt().equals(stack.getOrCreateNbt())
							&& invStack.getCount() < stack.getMaxCount()) {
						if (inv.getStack(i).getCount() + stack.getCount() <= stack.getMaxCount()) {
							stack.setCount(inv.getStack(i).getCount() + stack.getCount());
							inv.setStack(i, stack);
							itemEntity.setDespawnImmediately();
						} else {
							stack.setCount(inv.getStack(i).getCount() + stack.getCount() - stack.getMaxCount());
							inv.getStack(i).setCount(stack.getMaxCount());
						}

						return;
					}
				}
			});
			return false;
		});
	}

	public boolean canDropItem(AlchemicalCircleEntity entity, ItemStack stack) {
		return true;
	}
}
