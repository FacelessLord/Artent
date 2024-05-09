package faceless.artent.sharpening.inventory;

import faceless.artent.api.inventory.ArtentInventory;
import faceless.artent.objects.ModItems;
import faceless.artent.sharpening.api.IEnhancer;
import faceless.artent.sharpening.api.ISharpenable;
import faceless.artent.sharpening.api.SharpeningUtils;
import faceless.artent.sharpening.blockEntity.SharpeningAnvilBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Arrays;

public class SharpeningAnvilInventory extends ArtentInventory {
    public SharpeningAnvilBlockEntity anvil;

    public SharpeningAnvilInventory(SharpeningAnvilBlockEntity anvil) {
        this.anvil = anvil;
    }

    @Override
    public int size() {
        return 4;
    }

    @Override
    public void markDirty() {
        super.markDirty();
        anvil.markDirty();
    }

    public static boolean checkCatalysts(ItemStack modifier, int level) {
        var item = modifier.getItem();

        var catalystLevel = getCatalystLevel(item);
        var catalystCount = getCatalystCount(level + 1, catalystLevel);
        return modifier.getCount() >= catalystCount && catalystCount != 0;
    }

    public static int getCatalystCount(int level, int catalystLevel) {
        if (level <= 7) {
            if (catalystLevel == 1)
                return (int) Math.ceil(level / 2f);
            return 1;
        }
        if (level <= 12) {
            if (catalystLevel == 2)
                return (int) Math.ceil(level / 2f) - 3;
            if (catalystLevel > 2)
                return 1;
        }
        if (level <= 15) {
            if (catalystLevel == 3)
                return level - 12;
            if (catalystLevel > 3)
                return 1;
        }
        return 0;
    }

    @Override
    public void onContentChanged() {
        var target = getStack(0);
        var modifier = getStack(1);
        var hammer = getStack(2);

        if (hammer.isEmpty() ||
            target.isEmpty() ||
            modifier.isEmpty() ||
            !(target.getItem() instanceof ISharpenable sharpenable)) {
            items.set(3, ItemStack.EMPTY);
            return;
        }

        var level = sharpenable.getLevel(target);
        var resultStack = target.copy();
        var flag = false;
        if (Arrays.stream(ModItems.Catalysts).anyMatch(i -> i == modifier.getItem())) {
            if (checkCatalysts(modifier, level)) {
                SharpeningUtils.setItemLevel(resultStack, level + 1);
                flag = true;
            } else {
                items.set(3, ItemStack.EMPTY);
                return;
            }
        }

        if (modifier.getItem() instanceof IEnhancer enhancer) {
            flag = SharpeningUtils.addEnhancer(resultStack, enhancer);
        }
        if (flag) items.set(3, resultStack);
    }

    @Override
    protected void onRemoveStack(int slot) {
        var stack = getStack(slot);
        if (slot == 3 && !stack.isEmpty() && stack.getItem() instanceof ISharpenable sharpenable) {
            var level = sharpenable.getLevel(stack);
            this.getStack(0).decrement(1);
            var modifier = this.getStack(1);
            var modifierItem = modifier.getItem();
            if (isCatalyst(modifierItem)) {
                this.getStack(1).decrement(getCatalystCount(level, getCatalystLevel(modifierItem)));
            } else if (modifierItem instanceof IEnhancer) {
                this.getStack(1).decrement(1);
            }
        }
        var target = getStack(0);
        var modifier = getStack(1);
        var hammer = getStack(2);
        if (hammer.isEmpty() ||
            target.isEmpty() ||
            modifier.isEmpty() ||
            !(target.getItem() instanceof ISharpenable sharpenable)) {
            items.set(3, ItemStack.EMPTY);
        }
    }

    private static int getCatalystLevel(Item item) {
        return item == ModItems.StoneOfTheSea ? 1 : item == ModItems.FortitudeSpiritStone ? 2 : item ==
                                                                                                ModItems.AmberSphere ? 3 : 1;
    }

    public static boolean isCatalyst(Item modifierItem) {
        return modifierItem == ModItems.StoneOfTheSea ||
               modifierItem == ModItems.FortitudeSpiritStone ||
               modifierItem == ModItems.AmberSphere;
    }
}
