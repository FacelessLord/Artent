package faceless.artent.network;

import faceless.artent.brewing.blockEntities.BrewingCauldronBlockEntity;
import faceless.artent.playerData.api.DataUtil;
import faceless.artent.transmutations.blockEntities.AlchemicalCircleEntity;
import faceless.artent.trasmutations.AlchemicalCircleGui;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
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
				ArtentServerHook.SYNC_PLAYER_DATA_PACKET_ID,
				(client, handler, buffer, responseHandler) -> {
					var nbt = buffer.readNbt();
					if (client.player == null)
						return;
//
					// Client sided code
					client.execute(() -> {
						var artentHandler = DataUtil.getHandler(client.player);
						artentHandler.readFromNbt(nbt);
						var x = 1;
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
						if (client.player == null)
							return;

						var world = client.player.getWorld();
						if (world == null)
							return;

						BlockEntity blockEntity = world.getBlockEntity(pos);
						if (!(blockEntity instanceof BrewingCauldronBlockEntity cauldron))
							return;

						cauldron.readNbt(nbt);
					});
				});
	}

	public static void packetSynchronizeCircle(AlchemicalCircleEntity entity) {
		NbtCompound tag = new NbtCompound();
		entity.writeNbt(tag);

		PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
		passedData.writeBlockPos(entity.getPos());
		passedData.writeNbt(tag);
		ClientPlayNetworking.send(ArtentServerHook.SYNCHRONIZE_CIRCLE, passedData);
	}

	public static void packetDamageChalk(PlayerEntity player) {
		PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
		passedData.writeUuid(player.getUuid());
		ClientPlayNetworking.send(ArtentServerHook.DAMAGE_CHALK_PACKET_ID, passedData);
	}

	public static void packetRemoveCircle(BlockPos pos) {
		PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
		passedData.writeBlockPos(pos);
		ClientPlayNetworking.send(ArtentServerHook.REMOVE_CIRCLE_PACKET_ID, passedData);
	}

	public static void packetSellItems(PlayerEntity player, long money) {
		PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
		passedData.writeUuid(player.getUuid());
		passedData.writeLong(money);
		ClientPlayNetworking.send(ArtentServerHook.SELL_ITEMS_PACKET_ID, passedData);
	}
}
