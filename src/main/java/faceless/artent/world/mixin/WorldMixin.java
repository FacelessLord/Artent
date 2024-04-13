package faceless.artent.world.mixin;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import java.util.HashSet;

@Mixin(World.class)
public class WorldMixin {
    public HashSet<BlockPos> dimmedPositions = new HashSet<>();


}
