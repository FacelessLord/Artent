package faceless.artent.transmutations.api;

import faceless.artent.api.functions.TriConsumer;
import faceless.artent.transmutations.blockEntities.AlchemicalCircleEntity;
import net.minecraft.entity.player.PlayerEntity;

/**
 * Integer is count of ticks passed
 */
public interface RenderTickAction extends TriConsumer<AlchemicalCircleEntity, PlayerEntity, Integer> {
}
