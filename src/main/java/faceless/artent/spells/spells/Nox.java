package faceless.artent.spells.spells;

import faceless.artent.objects.ModBlocks;
import faceless.artent.spells.api.ICaster;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class Nox extends ProjectileSpell {
    public Nox() {
        super("nox", 1);
    }

    @Override
    public void blockCast(
            ICaster caster,
            World world,
            ItemStack stack,
            BlockPos hitPos,
            Direction hitSide,
            int actionTime) {
        var blockPos = hitPos.offset(hitSide);

        var range = 8;

        for (int i = -range; i <= range; i++) {
            for (int j = -range; j <= range; j++) {
                for (int k = -range; k <= range; k++) {
                    var pos = blockPos.add(i, j, k);
                    var blockState = world.getBlockState(pos);
                    var block = blockState.getBlock();
                    if (block == ModBlocks.LightBlock) {
                        if (caster.consumeMana(-2))
                            world.setBlockState(pos, Blocks.AIR.getDefaultState());
                    }
                    if (block == Blocks.TORCH || block == Blocks.WALL_TORCH || block == Blocks.GLOWSTONE) {
                        if (caster.consumeMana(2))
                            world.breakBlock(pos, true);
                    }
                }
            }
        }
    }
}
