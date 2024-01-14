package faceless.artent.transmutations;

import faceless.artent.api.functions.TetraFunction;
import faceless.artent.transmutations.world.AlchemicalCircleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Direction;

/**
 * Integer is count of ticks passed
 *
 * @returns whether you should stop transmutation
 */
public interface TransTickAction extends TetraFunction<Direction, AlchemicalCircleEntity, PlayerEntity, Integer, Boolean> {
}
