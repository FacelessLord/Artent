package faceless.artent.network;

import faceless.artent.Artent;
import faceless.artent.brewing.blockEntities.BrewingCauldronBlockEntity;
import faceless.artent.objects.ModBlocks;
import faceless.artent.objects.ModItems;
import faceless.artent.playerData.PersistentPlayersState;
import faceless.artent.playerData.api.DataUtil;
import faceless.artent.transmutations.blockEntities.AlchemicalCircleEntity;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class ArtentServerHook {

    public static final Identifier OPEN_CIRCLE_GUI_PACKET_ID = new Identifier(Artent.MODID, "packet.circle.open");
    public static final Identifier SYNC_CAULDRON_PACKET_ID = new Identifier(Artent.MODID, "packet.cauldron.sync");
    public static final Identifier SYNCHRONIZE_CIRCLE = new Identifier(Artent.MODID, "packet.circle.sync");
    public static final Identifier DAMAGE_CHALK_PACKET_ID = new Identifier(Artent.MODID, "packet.chalk.damage");
    public static final Identifier REMOVE_CIRCLE_PACKET_ID = new Identifier(Artent.MODID, "packet.circle.close");
    public static final Identifier SYNC_PLAYER_DATA_PACKET_ID = new Identifier(Artent.MODID, "packet.player.sync");
    public static final Identifier SELL_ITEMS_PACKET_ID = new Identifier(Artent.MODID, "packet.player.sell");
    public static final Identifier SPELL_INDEX_LEFT = new Identifier(Artent.MODID, "packet.caster.spell_index.left");
    public static final Identifier SPELL_INDEX_RIGHT = new Identifier(Artent.MODID, "packet.caster.spell_index.right");

    public void load() {
        System.out.println("ArtentServerHook load");
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
        ServerPlayNetworking.registerGlobalReceiver(REMOVE_CIRCLE_PACKET_ID,
                                                    (server, player, handler, buffer, sender) -> {
                                                        BlockPos pos = buffer.readBlockPos();

                                                        // Server sided code
                                                        server.execute(() -> {
                                                            var world = player.getWorld();
                                                            if (world == null)
                                                                return;
                                                            var state = world.getBlockState(pos);
                                                            if (state.getBlock() != ModBlocks.AlchemicalCircle)
                                                                return;

                                                            world.setBlockState(pos,
                                                                                Blocks.AIR.getDefaultState(),
                                                                                Block.NOTIFY_ALL);
                                                        });
                                                    });
        ServerPlayNetworking.registerGlobalReceiver(DAMAGE_CHALK_PACKET_ID,
                                                    (server, player, handler, buffer, sender) -> {
                                                        var playerUuid = buffer.readUuid();

                                                        if (!player.getUuid().equals(playerUuid))
                                                            return;
                                                        // Server sided code
                                                        server.execute(() -> {
                                                            var world = player.getWorld();
                                                            if (world == null)
                                                                return;

                                                            if (damageChalk(player, Hand.MAIN_HAND)) {
                                                                player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
                                                            } else if (damageChalk(player, Hand.OFF_HAND)) {
                                                                player.setStackInHand(Hand.OFF_HAND, ItemStack.EMPTY);
                                                            }
                                                        });
                                                    });
        ServerPlayNetworking.registerGlobalReceiver(SELL_ITEMS_PACKET_ID, (server, player, handler, buffer, sender) -> {
            var playerUuid = buffer.readUuid();
            var money = buffer.readLong();

            if (!player.getUuid().equals(playerUuid))
                return;
            // Server sided code
            server.execute(() -> {

                var dataHandler = DataUtil.getHandler(player);
                dataHandler.getTraderSellInventory().clear();
                dataHandler.addMoney(money);

                packetSyncPlayerData(player);
            });
        });
        ServerPlayNetworking.registerGlobalReceiver(SPELL_INDEX_LEFT, (server, player, handler, buffer, sender) -> {
            var playerUuid = buffer.readUuid();
            if (!player.getUuid().equals(playerUuid))
                return;

            // Server sided code
            server.execute(() -> {
                var casterInfo = DataUtil.getCasterInfo(player);
                casterInfo.setSpellBookIndex((casterInfo.getSpellBookIndex() - 1 + 18) % 18);
                packetSyncPlayerData(player);

                player.stopUsingItem();
            });
        });
        ServerPlayNetworking.registerGlobalReceiver(SPELL_INDEX_RIGHT, (server, player, handler, buffer, sender) -> {
            var playerUuid = buffer.readUuid();

            if (!player.getUuid().equals(playerUuid))
                return;
            // Server sided code
            server.execute(() -> {
                var casterInfo = DataUtil.getCasterInfo(player);
                casterInfo.setSpellBookIndex((casterInfo.getSpellBookIndex() + 1) % 18);
                packetSyncPlayerData(player);
                player.stopUsingItem();
            });
        });
    }

    public static boolean damageChalk(PlayerEntity player, Hand hand) {
        var mainHand = player.getStackInHand(hand);
        return !player.isCreative()
               &&
               !mainHand.isEmpty()
               &&
               mainHand.getItem() == ModItems.Chalk
               &&
               mainHand.damage(1,
                               player.getWorld().getRandom(),
                               player instanceof ServerPlayerEntity serverPlayer ? serverPlayer : null);
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

    public static void packetSyncPlayerData(PlayerEntity player) {
        var handler = DataUtil.getHandler(player);
        var nbt = new NbtCompound();

        handler.writeToNbt(nbt);

        PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
        passedData.writeNbt(nbt);

        ServerPlayNetworking.send((ServerPlayerEntity) player, SYNC_PLAYER_DATA_PACKET_ID, passedData);

        if (player.getWorld().isClient) {
            return;
        }

        var server = player.getWorld().getServer();
        if (server == null)
            return;

        var serverState = PersistentPlayersState.getServerState(server);
        serverState.setPlayerState(player, handler.getPlayerState());
    }

}
