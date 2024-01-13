package faceless.artent.transmutations;

import faceless.artent.api.functions.TriFunction;
import faceless.artent.transmutations.world.AlchemicalCircleEntity;
import net.minecraft.entity.player.PlayerEntity;

/**
 * Integer is count of ticks passed
 *
 * @returns whether you should stop transmutation
 */
public interface TransTickAction extends TriFunction<AlchemicalCircleEntity, PlayerEntity, Integer, Boolean> {
}
