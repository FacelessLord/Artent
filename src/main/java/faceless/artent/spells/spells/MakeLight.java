package faceless.artent.spells.spells;

import faceless.artent.objects.ModBlocks;
import faceless.artent.spells.api.ICaster;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class MakeLight extends ProjectileSpell {
    public MakeLight() {
        super("lightbulb", 3);
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
        var lightBlockState = ModBlocks.LightBlock.getDefaultState();
        world.setBlockState(blockPos, lightBlockState);
    }
}
