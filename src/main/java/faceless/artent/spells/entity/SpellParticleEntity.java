package faceless.artent.spells.entity;

import faceless.artent.registries.SpellRegistry;
import faceless.artent.spells.api.CasterStorage;
import faceless.artent.spells.api.ICaster;
import faceless.artent.spells.api.Spell;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;

public class SpellParticleEntity extends ThrownEntity {
    private ICaster caster;
    private ItemStack wandStack = ItemStack.EMPTY;
    private static final TrackedData<String> SPELL = DataTracker.registerData(SpellParticleEntity.class,
            TrackedDataHandlerRegistry.STRING);

    public SpellParticleEntity(EntityType<? extends SpellParticleEntity> entityType,
                               World world) {
        super(entityType, world);
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
        ItemStack wandStack = ItemStack.fromNbt(nbt.getCompound("WandStack"));
        var casterUuid = nbt.getUuid("Caster");

        var player = CasterStorage.getCasterById(getWorld(), casterUuid);
        if (player != null && wandStack != null) {
            this.setCaster(player);
            this.setWandStack(wandStack);
        } else
            this.discard();
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        var world = getWorld();
        if (!world.isClient) {
            var spell = SpellRegistry.getSpell(this.getSpellId());
            if (spell == null || getCaster() == null) {
                discard();
                return;
            }

            spell.blockCast(getCaster(),
                    getWorld(),
                    getWandStack(),
                    blockHitResult.getBlockPos(),
                    blockHitResult.getSide(),
                    1);
            this.discard();
        }
    }

    @Override
    public void onRemoved() {
        super.onRemoved();
    }

    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
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

            world.addParticle(new DustParticleEffect(spell.color, 1),
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
