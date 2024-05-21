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
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BaseSpellProjectile extends ThrownEntity {
    private ICaster caster;
    public ItemStack wandStack = ItemStack.EMPTY;
    public static final TrackedData<String> SPELL = DataTracker.registerData(
      BaseSpellProjectile.class,
      TrackedDataHandlerRegistry.STRING
    );

    public BaseSpellProjectile(
      EntityType<? extends BaseSpellProjectile> entityType, World world
    ) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker() {
        this.getDataTracker().startTracking(SPELL, "");
    }

    public String getSpellId() {
        return getDataTracker().get(SPELL);
    }

    public void setSpellId(String spellId) {
        getDataTracker().set(SPELL, spellId);
    }

    public ICaster getCaster() {
        return caster;
    }

    public void setSpell(Spell spell) {
        getDataTracker().set(SPELL, spell.id);
    }

    public @Nullable Spell getSpell() {
        return SpellRegistry.getSpell(this.getSpellId());
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
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        var world = getWorld();
        if (!world.isClient) {
            var spell = SpellRegistry.getSpell(this.getSpellId());
            if (spell == null || getCaster() == null) {
                discard();
                return;
            }

            spell.onProjectileBlockHit(
              getCaster(),
              getWorld(),
              getWandStack(),
              getWorld().getBlockState(blockHitResult.getBlockPos()),
              blockHitResult.getBlockPos(),
              blockHitResult.getSide()
            );
            this.discard();
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        var world = getWorld();
        if (!world.isClient) {
            var spell = SpellRegistry.getSpell(this.getSpellId());
            if (spell == null || getCaster() == null) {
                discard();
                return;
            }

            spell.onProjectileEntityHit(getCaster(), getWorld(), getWandStack(), entityHitResult.getEntity());
            this.discard();
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putString("spell", getSpellId());
        nbt.putUuid("caster", getCaster().getCasterUuid());
        ItemStack wandStack = this.getWandStack();
        if (!wandStack.isEmpty()) {
            nbt.put("wandStack", wandStack.writeNbt(new NbtCompound()));
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        setSpellId(nbt.getString("spell"));
        ItemStack wandStack = ItemStack.fromNbt(nbt.getCompound("wandStack"));
        var casterUuid = nbt.getUuid("caster");

        var player = CasterStorage.getCasterById(getWorld(), casterUuid);
        if (player != null && wandStack != null) {
            this.setCaster(player);
            this.setWandStack(wandStack);
        } else this.discard();
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
        var spell = getSpell();
        if (spell == null || (this.hasNoGravity() && getVelocity().length() < 0.05)) {
            discard();
        }
    }
}