package faceless.artent.trading.blockEntities;

import faceless.artent.objects.ModBlockEntities;
import faceless.artent.trading.TraderInventory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class TraderBlockEntity extends BlockEntity {
	public TraderInventory inventory;
	public TraderBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.Trader, pos, state);
		inventory = new TraderInventory(this);
	}
}
