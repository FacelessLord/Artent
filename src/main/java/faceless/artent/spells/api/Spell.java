package faceless.artent.spells.api;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.joml.Vector3f;

public class Spell {
    public final int type;
    public String id;
    public int baseCost;
    public Vector3f color = new Vector3f(1, 1, 1);

    public Spell(String id, int type, int baseCost) {
        this.type = type;
        this.id = id;
        this.baseCost = baseCost;
    }

    public Spell setColor(float r, float g, float b) {
        this.color = new Vector3f(r, g, b);
        return this;
    }

    public float getRecoilChance(LivingEntity player, World world) {
        return 0;
    }

    public void action(ICaster caster, World world, ItemStack stack, int castTime) {

    }

    public SpellActionResult spellTick(ICaster caster, World world, ItemStack stack, int actionTime) {
        return SpellActionResult.Continue(0);
    }

    public void blockCast(ICaster caster,
                          World world,
                          ItemStack stack,
                          BlockPos blockPos,
                          Direction hitSide,
                          int actionTime) {

    }

    public void onRecoil(ICaster caster, World world, ItemStack stack, int actionTime) {

    }

    public static class ActionType {
        public static final int Tick = 1;
        public static final int SingleCast = 2;
        public static final int BlockCast = 4;
    }
}
