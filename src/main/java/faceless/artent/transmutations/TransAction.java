package faceless.artent.transmutations;

import faceless.artent.api.functions.TriConsumer;
import net.minecraft.entity.player.PlayerEntity;
import java.util.function.BiConsumer;
import faceless.artent.transmutations.world.AlchemicalCircleEntity;
import net.minecraft.util.math.Direction;

public interface TransAction extends TriConsumer<Direction, AlchemicalCircleEntity, PlayerEntity> {
}
