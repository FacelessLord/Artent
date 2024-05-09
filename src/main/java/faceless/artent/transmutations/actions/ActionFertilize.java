package faceless.artent.transmutations.actions;

import faceless.artent.api.math.Color;
import faceless.artent.transmutations.api.Transmutation;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public class ActionFertilize extends Transmutation {

    public ActionFertilize(int level) {
        super("circle.fertilize", (facing, e, p) -> {
        });
        this.setTickAction((facing, e, p, tick) -> {
            final int height = 2 * level + 1, width = 4 * level + 4;
            World world = e.getWorld();
            if (world == null)
                return false;

            return randomPoints(world, 16, e.getPos(), facing, width, height, (pos, state) -> {
                Block b = state.getBlock();
                if (b == Blocks.WATER) {
                    e.circleTag.putInt("moisture", e.circleTag.getInt("moisture") + 10);
                    world.removeBlock(pos, false);
                }
                if (e.circleTag.getInt("moisture") > 0) {
                    if (b instanceof FarmlandBlock) {
                        int i = state.get(FarmlandBlock.MOISTURE);
                        world.setBlockState(pos, state.with(FarmlandBlock.MOISTURE, Math.min(i + 3, 7)), 2);
                        e.circleTag.putInt("moisture", e.circleTag.getInt("moisture") - 1);
                    }
                    if (!world.isClient && (b instanceof CropBlock || b instanceof StemBlock)) {
                        ((Fertilizable) b).grow((ServerWorld) world, world.random, pos, state);
                        e.circleTag.putInt("moisture", e.circleTag.getInt("moisture") - 1);
                    }
                }
                return false;
            }, Behaviour.DoAll);
        });
        this.setPrepCol(new Color(80, 255, 80));
        this.setActCol(new Color(40, 255, 40));
    }
}
