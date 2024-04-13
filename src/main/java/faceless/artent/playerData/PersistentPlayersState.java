package faceless.artent.playerData;

import faceless.artent.Artent;
import faceless.artent.playerData.api.ArtentPlayerState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.UUID;

public class PersistentPlayersState extends PersistentState {
    HashMap<UUID, ArtentPlayerState> playersData = new HashMap<>();

    public void setPlayerState(PlayerEntity player, ArtentPlayerState state) {
        playersData.put(player.getUuid(), state);
    }

    public static ArtentPlayerState getPlayerState(LivingEntity player) {
        PersistentPlayersState serverState = getServerState(player.getWorld().getServer());

        // Either get the player by the uuid, or we don't have data for him yet, make a new player state
        return serverState.playersData.computeIfAbsent(player.getUuid(), uuid -> new ArtentPlayerState());
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtCompound playersNbt = new NbtCompound();
        playersData.forEach((uuid, playerData) -> {
            NbtCompound playerNbt = new NbtCompound();
            playerData.writeToNbt(playerNbt);
            playersNbt.put(uuid.toString(), playerNbt);
        });
        nbt.put("players", playersNbt);
        return nbt;
    }

    public static PersistentPlayersState createFromNbt(NbtCompound tag) {
        var persistentState = new PersistentPlayersState();
        if (!tag.contains("players"))
            return persistentState;
        var playersTag = tag.getCompound("players");
        for (var uuidString : playersTag.getKeys()) {
            var uuid = UUID.fromString(uuidString);
            var data = ArtentPlayerState.createFromNbt(playersTag.getCompound(uuidString));
            persistentState.playersData.put(uuid, data);
        }

        return persistentState;
    }

    private static Type<PersistentPlayersState> Type = new Type<>(
            PersistentPlayersState::new, // If there's no 'PersistentPlayersState' yet create one
            PersistentPlayersState::createFromNbt, // If there is a 'PersistentPlayersState' NBT, parse it with 'createFromNbt'
            null // Supposed to be an 'DataFixTypes' enum, but we can just pass null
    );

    public static PersistentPlayersState getServerState(MinecraftServer server) {
        // (Note: arbitrary choice to use 'World.OVERWORLD' instead of 'World.END' or 'World.NETHER'.  Any work)
        PersistentStateManager persistentStateManager = server.getWorld(World.OVERWORLD).getPersistentStateManager();

        // The first time the following 'getOrCreate' function is called, it creates a brand new 'PersistentPlayersState' and
        // stores it inside the 'PersistentStateManager'. The subsequent calls to 'getOrCreate' pass in the saved
        // 'PersistentPlayersState' NBT on disk to our function 'PersistentPlayersState::createFromNbt'.
        PersistentPlayersState state = persistentStateManager.getOrCreate(Type, Artent.MODID);

        // If state is not marked dirty, when Minecraft closes, 'writeNbt' won't be called and therefore nothing will be saved.
        // Technically it's 'cleaner' if you only mark state as dirty when there was actually a change, but the vast majority
        // of mod writers are just going to be confused when their data isn't being saved, and so it's best just to 'markDirty' for them.
        // Besides, it's literally just setting a bool to true, and the only time there's a 'cost' is when the file is written to disk when
        // there were no actual change to any of the mods state (INCREDIBLY RARE).
        state.markDirty();

        return state;
    }
}
