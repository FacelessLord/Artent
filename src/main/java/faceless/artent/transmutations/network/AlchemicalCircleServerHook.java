package faceless.artent.transmutations.network;

import faceless.artent.Artent;
import faceless.artent.transmutations.blockEntities.AlchemicalCircleEntity;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class AlchemicalCircleServerHook {

	public static final Identifier OPEN_CIRCLE_GUI_PACKET_ID = new Identifier(Artent.MODID, "packet.open.circle");
	public static final Identifier SYNCHRONIZE_CIRCLE = new Identifier(Artent.MODID, "packet.sync.circle");

	public void load() {
		System.out.println("AlchemicalNetworkHook server side load");

		ServerPlayNetworking.registerGlobalReceiver(SYNCHRONIZE_CIRCLE, (server, player, handler, buffer, sender) -> {
			BlockPos pos = buffer.readBlockPos();
			NbtCompound tag = buffer.readNbt();

			// Server sided code
			server.execute(() -> {
				BlockEntity blockEntity = player.getWorld().getBlockEntity(pos);
				if (blockEntity == null)
					return;

				blockEntity.readNbt(tag);
				blockEntity.markDirty();
			});
		});
	}

	public static void packetOpenCircleGui(PlayerEntity player, AlchemicalCircleEntity entity) {
		if (player.getWorld().isClient)
			return;

		PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
		passedData.writeBlockPos(entity.getPos());
		ServerPlayNetworking.send((ServerPlayerEntity) player, OPEN_CIRCLE_GUI_PACKET_ID, passedData);
	}

}
