package faceless.artent.sharpening.blockEntities;

import faceless.artent.api.blockEntity.BlockEntityWithInventory;
import faceless.artent.objects.ModBlockEntities;
import faceless.artent.objects.ModItems;
import faceless.artent.sharpening.api.IEnhancer;
import faceless.artent.sharpening.api.ISharpenable;
import faceless.artent.sharpening.api.SharpeningUtils;
import faceless.artent.sharpening.screenHandlers.SharpeningAnvilScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class SharpeningAnvilBlockEntity extends BlockEntityWithInventory implements NamedScreenHandlerFactory {
	public SharpeningAnvilBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.SharpeningAnvil, pos, state);
	}

	@Override
	public int size() {
		return 4;
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		Inventories.readNbt(nbt, items);
	}

	@Override
	public void writeNbt(NbtCompound nbt) {
		BlockEntityWithInventory.writeInventoryNbt(nbt, items, true);
		super.writeNbt(nbt);
	}

	@Override
	public void onContentChanged() {
		var target = getStack(0);
		var modifier = getStack(1);
		var hammer = getStack(2);

		if (!hammer.isEmpty() && !target.isEmpty() && !modifier.isEmpty() && target.getItem() instanceof ISharpenable sharpenable) {
			var level = sharpenable.getLevel(target);
			var resultStack = target.copy();
			var flag = false;
			if (modifier.getItem() == ModItems.StoneOfTheSea && level < 7 && modifier.getCount() >= getCatalystCount(level + 1)
				|| modifier.getItem() == ModItems.FortitudeSpiritStone && level < 12 && modifier.getCount() >= getCatalystCount(level + 1)
				|| modifier.getItem() == ModItems.AmberSphere && level < 15 && modifier.getCount() >= getCatalystCount(level + 1)) {
				SharpeningUtils.setItemLevel(resultStack, level + 1);
				flag = true;
			}
			if (modifier.getItem() instanceof IEnhancer enhancer) {
				flag = SharpeningUtils.addEnhancer(resultStack, enhancer);
			}
			if (flag)
				items.set(3, resultStack);
		} else
			items.set(3, ItemStack.EMPTY);
	}

	public static int getCatalystCount(int level) {
		if (level <= 7) {
			return (int) Math.ceil(level / 2f);
		}
		if (level <= 12) {
			return (int) Math.ceil(level / 2f) - 3;
		}
		if (level <= 15) {
			return level - 12;
		}
		return 0;
	}

	@Override
	public Text getDisplayName() {
		return Text.translatable("sharpening_anvil");
	}

	@Nullable
	@Override
	public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
		return new SharpeningAnvilScreenHandler(syncId, playerInventory, this, ScreenHandlerContext.create(world, pos));
	}
}