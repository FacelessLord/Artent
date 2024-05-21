package faceless.artent.spells.spells;

import faceless.artent.objects.ModEntities;
import faceless.artent.spells.api.ICaster;
import faceless.artent.spells.api.SpellSettings;
import faceless.artent.spells.entity.BaseSpellProjectile;
import faceless.artent.spells.entity.SmallFireballEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class SmallFireball extends ProjectileSpell {
    public SmallFireball(SpellSettings settings) {
        super("small_fireball", settings);
    }

    @Override
    public BaseSpellProjectile createProjectile(ICaster caster, World world, ItemStack stack) {
        return new SmallFireballEntity(ModEntities.SMALL_FIREBALL, world);
    }

    @Override
    public void onProjectileEntityHit(ICaster caster, World world, ItemStack stack, Entity entity) {
        super.onProjectileEntityHit(caster, world, stack, entity);
        if (entity instanceof LivingEntity living) {
            living.setOnFire(true);
            living.setOnFireFor(1);
            living.damage(createDamageSource(world, caster), 4);
        }
    }
}
