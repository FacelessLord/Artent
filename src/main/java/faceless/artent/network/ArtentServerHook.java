package faceless.artent.network;

import faceless.artent.Artent;
import faceless.artent.brewing.blockEntities.BrewingCauldronBlockEntity;
import faceless.artent.objects.ModBlocks;
import faceless.artent.transmutations.blockEntities.AlchemicalCircleEntity;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class ArtentServerHook {

	public static final Identifier OPEN_CIRCLE_GUI_PACKET_ID = new Identifier(Artent.MODID, "packet.open.circle");
	public static final Identifier SYNC_CAULDRON_PACKET_ID = new Identifier(Artent.MODID, "packet.sync.cauldron");
	public static final Identifier SYNCHRONIZE_CIRCLE = new Identifier(Artent.MODID, "packet.sync.circle");
	public static final Identifier REMOVE_CIRCLE = new Identifier(Artent.MODID, "packet.close.circle");

//	public static final Identifier EntitySpawnPacketID = new Identifier(Artent.MODID, "entity_spawn_packet");

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
		ServerPlayNetworking.registerGlobalReceiver(REMOVE_CIRCLE, (server, player, handler, buffer, sender) -> {
			BlockPos pos = buffer.readBlockPos();

			// Server sided code
			server.execute(() -> {
				var world = player.getWorld();
				if (world == null)
					return;
				var state = world.getBlockState(pos);
				if (state.getBlock() != ModBlocks.AlchemicalCircle)
					return;

				world.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL);
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

	public static void packetSyncCauldron(PlayerEntity player, BrewingCauldronBlockEntity entity) {
		if (player.getWorld().isClient)
			return;

		PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
		passedData.writeBlockPos(entity.getPos());
		var nbt = entity.createNbt();
		passedData.writeNbt(nbt);
		ServerPlayNetworking.send((ServerPlayerEntity) player, SYNC_CAULDRON_PACKET_ID, passedData);
	}

}
