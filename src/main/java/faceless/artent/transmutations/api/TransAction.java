package faceless.artent.transmutations.api;

import faceless.artent.api.functions.TriConsumer;
import faceless.artent.transmutations.blockEntities.AlchemicalCircleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Direction;

public interface TransAction extends TriConsumer<Direction, AlchemicalCircleEntity, PlayerEntity> {
}
