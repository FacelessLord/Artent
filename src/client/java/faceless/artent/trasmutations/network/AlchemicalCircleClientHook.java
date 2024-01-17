package faceless.artent.trasmutations.network;

import faceless.artent.transmutations.network.AlchemicalCircleServerHook;
import faceless.artent.transmutations.blockEntities.AlchemicalCircleEntity;
import faceless.artent.trasmutations.AlchemicalCircleGui;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

@Environment(EnvType.CLIENT)
public class AlchemicalCircleClientHook {

	public void loadClient() {
		System.out.println("AlchemicalNetworkHook client side load");
		ClientPlayNetworking
			.registerGlobalReceiver(
				AlchemicalCircleServerHook.OPEN_CIRCLE_GUI_PACKET_ID,
				(client, handler, buffer, responseHandler) -> {

					BlockPos pos = buffer.readBlockPos();
					if (client.player == null)
						return;

					BlockEntity blockEntity = client.player.getWorld().getBlockEntity(pos);
//
					// Client sided code
					client.execute(() -> {
						AlchemicalCircleGui gui = new AlchemicalCircleGui((AlchemicalCircleEntity) blockEntity);
						client.setScreen(gui);
					});
				});
	}

	public static void packetSynchronizeCircle(AlchemicalCircleEntity entity) {
		NbtCompound tag = new NbtCompound();
		entity.writeNbt(tag);

		PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
		passedData.writeBlockPos(entity.getPos());
		passedData.writeNbt(tag);
		ClientPlayNetworking.send(AlchemicalCircleServerHook.SYNCHRONIZE_CIRCLE, passedData);
	}
}
