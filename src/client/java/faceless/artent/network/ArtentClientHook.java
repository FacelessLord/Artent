package faceless.artent.network;

import faceless.artent.brewing.blockEntities.BrewingCauldronBlockEntity;
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
public class ArtentClientHook {

	public void loadClient() {
		System.out.println("AlchemicalNetworkHook client side load");
		ClientPlayNetworking
			.registerGlobalReceiver(
				ArtentServerHook.OPEN_CIRCLE_GUI_PACKET_ID,
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
		ClientPlayNetworking
			.registerGlobalReceiver(
				ArtentServerHook.SYNC_CAULDRON_PACKET_ID,
				(client, handler, buffer, responseHandler) -> {

					BlockPos pos = buffer.readBlockPos();
					var nbt = buffer.readNbt();

					// Client sided code
					client.execute(() -> {
						var world = client.player.getWorld();
						if (world == null)
							return;

						BlockEntity blockEntity = world.getBlockEntity(pos);
						if (!(blockEntity instanceof BrewingCauldronBlockEntity cauldron))
							return;

						cauldron.readNbt(nbt);
					});
				});

//		ClientPlayNetworking.registerGlobalReceiver(ArtentServerHook.EntitySpawnPacketID, (client, handler, byteBuf, responder) -> {
//			EntityType<?> et = Registries.ENTITY_TYPE.get(byteBuf.readVarInt());
//			UUID uuid = byteBuf.readUuid();
//			int entityId = byteBuf.readVarInt();
//			Vec3d pos = PacketBufUtil.readVec3d(byteBuf);
//			float pitch = PacketBufUtil.readAngle(byteBuf);
//			float yaw = PacketBufUtil.readAngle(byteBuf);
//
//			client.execute(() -> {
//				if (MinecraftClient.getInstance().world == null)
//					throw new IllegalStateException("Tried to spawn entity in a null world!");
//				Entity e = et.create(MinecraftClient.getInstance().world);
//				if (e == null)
//					throw new IllegalStateException("Failed to create instance of entity \"" + Registries.ENTITY_TYPE.getId(et) + "\"!");
//				e.updateTrackedPosition(pos.x, pos.y, pos.z);
//				e.setPos(pos.x, pos.y, pos.z);
//				e.setPitch(pitch);
//				e.setYaw(yaw);
//				e.setId(entityId);
//				e.setUuid(uuid);
//				MinecraftClient.getInstance().world.addEntity(e);
//			});
//		});
	}

	public static void packetSynchronizeCircle(AlchemicalCircleEntity entity) {
		NbtCompound tag = new NbtCompound();
		entity.writeNbt(tag);

		PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
		passedData.writeBlockPos(entity.getPos());
		passedData.writeNbt(tag);
		ClientPlayNetworking.send(ArtentServerHook.SYNCHRONIZE_CIRCLE, passedData);
	}
}
