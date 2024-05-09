package faceless.artent.transmutations.actions;

import faceless.artent.api.DirectionUtils;
import faceless.artent.transmutations.api.Transmutation;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;

import java.util.List;

public class ActionCollectItems extends Transmutation {
    public ActionCollectItems(int level) {
        super("circle.item_collector", (facing, e, p) -> {
        });
        this.setTickAction((facing, e, p, tick) -> {
            final float heightRange = level + 1.5f, widthRange = 2 * level + 1.5f;
            var center = e.getPos().toCenterPos();
            var min = DirectionUtils.applyDirection(new float[]{-widthRange, -0.5f, -widthRange}, facing);
            var max = DirectionUtils.applyDirection(new float[]{widthRange, heightRange, widthRange}, facing);
            Box box = new Box(
              center.add(min[0], min[1], min[2]),
              center.add(max[0], max[1], max[2]));
            var world = e.getWorld();

            if (world == null)
                return false;

            List<ItemEntity> entityList = world.getEntitiesByType(EntityType.ITEM, box, ie -> true);

            if (entityList.size() == 0)
                return false;
            ItemEntity itemEntity = entityList.get(0);
            ItemStack stack = itemEntity.getStack();
            e.boundEntities.forEach(pair -> {
                if (!(pair.getLeft() instanceof Inventory inv))
                    return;
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
}
