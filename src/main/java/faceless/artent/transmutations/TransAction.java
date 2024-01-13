package faceless.artent.transmutations;

import net.minecraft.entity.player.PlayerEntity;
import java.util.function.BiConsumer;
import faceless.artent.transmutations.world.AlchemicalCircleEntity;

public interface TransAction extends BiConsumer<AlchemicalCircleEntity, PlayerEntity> {
}
