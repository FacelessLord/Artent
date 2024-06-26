package faceless.artent.spells.spells;

import faceless.artent.spells.api.ICaster;
import faceless.artent.spells.api.Spell;
import faceless.artent.spells.api.SpellSettings;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Gust extends Spell {
    public Gust(SpellSettings settings) {
        super("gust", settings);
    }

    @Override
    public void action(ICaster caster, World world, ItemStack stack, int castTime) {
        super.action(caster, world, stack, castTime);

        var rotation = caster.getCasterRotation();
        var coefficient = 1.25f + Math.min(castTime / 20f, 3f);

        var entityPos = caster.getCasterPosition();
        var scaleVec = new Vec3d(1, 1, 1).multiply(coefficient * 3);

        var box = new Box(entityPos.subtract(scaleVec), entityPos.add(scaleVec));

        var entities = world.getEntitiesByClass(Entity.class, box, e -> {
            var targetPos = e.getPos();
            var targetVec = targetPos.subtract(entityPos);
            var dot = rotation.dotProduct(targetVec);
            return dot > 0.1;
        });

        for (var e : entities) {
            e.setVelocity(rotation.multiply(coefficient));
            e.velocityDirty = true;
        }
    }
}
