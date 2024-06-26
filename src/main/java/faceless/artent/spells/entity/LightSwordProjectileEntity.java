package faceless.artent.spells.entity;

import faceless.artent.objects.ModEntities;
import faceless.artent.objects.ModSpells;
import faceless.artent.registries.SpellRegistry;
import faceless.artent.spells.api.CasterStorage;
import faceless.artent.spells.api.ICaster;
import faceless.artent.spells.api.Spell;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

import java.util.Objects;

public class LightSwordProjectileEntity extends ThrownEntity {
    private ICaster caster;
    private ItemStack wandStack = ItemStack.EMPTY;
    private static final TrackedData<String> SPELL = DataTracker.registerData(LightSwordProjectileEntity.class,
                                                                              TrackedDataHandlerRegistry.STRING);

    public LightSwordProjectileEntity(
      EntityType<? extends LightSwordProjectileEntity> entityType,
      World world
    ) {
        super(entityType, world);
        this.setNoGravity(true);
    }

    public LightSwordProjectileEntity(World world) {
        super(ModEntities.LIGHT_SWORD, world);
        this.setNoGravity(true);
    }

    @Override
    protected void initDataTracker() {
        this.getDataTracker().startTracking(SPELL, "");
    }

    public String getSpellId() {
        return getDataTracker().get(SPELL);
    }

    public void setSpell(Spell spell) {
        getDataTracker().set(SPELL, spell.id);
    }

    public void setSpellId(String spellId) {
        getDataTracker().set(SPELL, spellId);
    }

    public ICaster getCaster() {
        return caster;
    }

    public void setCaster(ICaster caster) {
        this.caster = caster;
    }

    public ItemStack getWandStack() {
        return wandStack;
    }

    public void setWandStack(ItemStack wandStack) {
        this.wandStack = wandStack;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putString("Spell", getSpellId());
        nbt.putUuid("Caster", getCaster().getCasterUuid());
        ItemStack wandStack = this.getWandStack();
        if (!wandStack.isEmpty()) {
            nbt.put("WandStack", wandStack.writeNbt(new NbtCompound()));
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        setSpellId(nbt.getString("Spell"));
        ItemStack wandStack = ItemStack.fromNbt(nbt.getCompound("Item"));
        var casterUuid = nbt.getUuid("Caster");

        var player = CasterStorage.getCasterById(getWorld(), casterUuid);
        if (player != null && wandStack != null) {
            this.setCaster(player);
            this.setWandStack(wandStack);
        } else
            this.discard();
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        // Player is LivingEntity and ICaster
        //noinspection EqualsBetweenInconvertibleTypes
        if (!entityHitResult.getEntity().getWorld().isClient && entityHitResult.getEntity() != caster) {
            if (!(entityHitResult.getEntity() instanceof LivingEntity living))
                return;

            var damageSources = this.getDamageSources();
            var damageSource = this.caster instanceof LivingEntity casterAttacker
              ? damageSources.mobProjectile(this, casterAttacker)
              : damageSources.magic();
            var damageCoeff = 1;
            if (Objects.equals(this.getSpellId(), ModSpells.GilgameshLightStorm.id))
                damageCoeff = 4;

            living.damage(damageSource, 7 * damageCoeff);
            if (living instanceof MobEntity mob && mob.isUndead())
                living.damage(damageSource, 3 * damageCoeff);

//            if (getWorld().getBlockState(getBlockPos()).isAir())
//                getWorld().setBlockState(this.getBlockPos(), ModBlocks.LightBlock.getDefaultState());
            this.discard();
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        var pos = blockHitResult.getBlockPos().offset(blockHitResult.getSide());
//        if (getWorld().isClient && getWorld().getBlockState(pos).isAir())
//            getWorld().setBlockState(pos, ModBlocks.LightBlock.getDefaultState());
        this.discard();
    }

    @Override
    public void tick() {
        super.tick();
        var world = getWorld();
        if (world.isClient && world.random.nextBoolean()) {
            var spell = SpellRegistry.getSpell(this.getSpellId());
            if (spell == null) {
                return;
            }

            world.addParticle(new DustParticleEffect(ModSpells.MakeLight.settings.color, 1),
                              getParticleX(0.05f),
                              this.getRandomBodyY(),
                              getParticleZ(0.05f),
                              0,
                              0,
                              0);
        }

        if (getVelocity().length() < 0.05)
            discard();
    }
}
