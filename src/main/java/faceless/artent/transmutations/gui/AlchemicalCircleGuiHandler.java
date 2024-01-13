//package faceless.artent.transmutations.gui;
//
//import faceless.artent.objects.ModScreenHandlers;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.entity.player.PlayerInventory;
//import net.minecraft.item.ItemStack;
//import net.minecraft.network.PacketByteBuf;
//import net.minecraft.screen.ScreenHandler;
//import net.minecraft.screen.ScreenHandlerType;
//import net.minecraft.util.math.BlockPos;
//
//public class AlchemicalCircleGuiHandler extends ScreenHandler {
//
//	public BlockPos pos;
//
//	public AlchemicalCircleGuiHandler(int syncId, PlayerInventory inventory, PacketByteBuf buf) {
//		super(ModScreenHandlers.ALCHEMICAL_CIRCLE, syncId);
//		pos = buf.readBlockPos();
//	}
//
//	public AlchemicalCircleGuiHandler(ScreenHandlerType<?> type, int syncId) {
//		super(type, syncId);
//        pos = BlockPos.ORIGIN;
//	}
//
//	@Override
//	public ItemStack quickMove(PlayerEntity var1, int var2) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public boolean canUse(PlayerEntity var1) {
//		// TODO Auto-generated method stub
//		return true;
//	}
//	 
//    //this getter will be used by our Screen class
//    public BlockPos getPos() {
//        return pos;
//    }
//}
