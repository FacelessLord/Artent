package faceless.artent.playerData.mixin;

import faceless.artent.playerData.PersistentPlayersState;
import faceless.artent.playerData.api.ArtentPlayerState;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerMixin extends PlayerDataMixin {
    @Override
    public ArtentPlayerState getPlayerState() {
        return PersistentPlayersState.getPlayerState(this.asPlayer());
    }
}
