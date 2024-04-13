package faceless.artent.playerData.mixin;

import faceless.artent.playerData.api.ArtentPlayerState;
import faceless.artent.spells.api.CasterStorage;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerMixin extends PlayerDataMixin {
    @Unique
    private ArtentPlayerState playerState = new ArtentPlayerState();

    @Override
    public ArtentPlayerState getPlayerState() {
        return playerState;
    }

    @Override
    public void readFromNbt(NbtCompound compound) {
        super.readFromNbt(compound);
        var player = (PlayerEntity) (Object) this;
        var playerStateTag = compound.getCompound("artent.data");
        playerState = ArtentPlayerState.createFromNbt(playerStateTag);

        CasterStorage.putCaster(player.getWorld(), this);
    }
}
