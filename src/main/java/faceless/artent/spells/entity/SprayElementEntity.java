package faceless.artent.spells.entity;

import faceless.artent.registries.SpellRegistry;
import faceless.artent.spells.api.CasterStorage;
import faceless.artent.spells.api.ICaster;
import faceless.artent.spells.api.Spell;
import faceless.artent.spells.spells.SpraySpell;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Unique;

public class SprayElementEntity extends ThrownEntity {
    @Unique
    private static final TrackedData<Integer> SPRAY_ELEMENT = DataTracker.registerData(
      SprayElementEntity.class,
      TrackedDataHandlerRegistry.INTEGER
    );
    @Unique
    private static final TrackedData<Integer> MOVEMENT_TYPE = DataTracker.registerData(
      SprayElementEntity.class,
      TrackedDataHandlerRegistry.INTEGER
    );
    @Unique
    private static final TrackedData<Integer> LIFE_TIME_LEFT = DataTracker.registerData(
      SprayElementEntity.class,
      TrackedDataHandlerRegistry.INTEGER
    );
    private static final TrackedData<String> SPELL = DataTracker.registerData(
      SprayElementEntity.class,
      TrackedDataHandlerRegistry.STRING
    );

    public static final int DirectMovement = 1;
    public static final int RotatingMovement = 2;

    private ICaster caster;
    private ItemStack wandStack = ItemStack.EMPTY;

    public SprayElementEntity(EntityType<? extends ThrownEntity> type, World world) {
        super(type, world);
        this.setNoGravity(true);
    }

    @Override
    protected void initDataTracker() {
        getDataTracker().startTracking(SPRAY_ELEMENT, SprayElement.Fire.ordinal());
        getDataTracker().startTracking(MOVEMENT_TYPE, DirectMovement);
        getDataTracker().startTracking(LIFE_TIME_LEFT, 10);
        this.getDataTracker().startTracking(SPELL, "");
    }

    @Override
    public void tick() {
        super.tick();
        setLifeTimeLeft(getLifeTimeLeft() - 1);
        var spellId = getSpellId();
        if (getLifeTimeLeft() <= 0 ||
            spellId == null ||
            spellId.isEmpty() ||
            !(SpellRegistry.getSpell(spellId) instanceof SpraySpell spray)) {
            discard();
            return;
        }
        var world = getWorld();
        var blockPos = getBlockPos();
        var blockState = world.getBlockState(blockPos);

        var projectiles = world.getEntitiesByClass(
          ProjectileEntity.class,
          Box.of(getPos(), 3, 3, 3),
          e -> !(e instanceof SprayElementEntity)
        );

        projectiles.forEach(Entity::discard);

        if (spray.projectileTick(world, caster, blockState, blockPos)) {
            discard();
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        var spell = SpellRegistry.getSpell(getSpellId());
        if (getWorld().isClient ||
            caster == null ||
            entityHitResult.getEntity() == null ||
            entityHitResult.getEntity() == caster ||
            !(spell instanceof SpraySpell spray)) {
            discard();
            return;
        }

        spray.onProjectileEntityHit(caster, getWorld(), wandStack, entityHitResult.getEntity());
        discard();
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        var spell = SpellRegistry.getSpell(getSpellId());
        if (getWorld().isClient || caster == null || !(spell instanceof SpraySpell spray)) {
            discard();
            return;
        }

        var blockPos = blockHitResult.getBlockPos();
        var world = getWorld();
        var blockState = this.getWorld().getBlockState(blockPos);

        if (!world.isClient) {
            var center = blockPos.toCenterPos();
            var offset = getPos().subtract(center);
            var dir = getHitDirection(offset);
            spray.onProjectileBlockHit(caster, world, wandStack, blockState, blockPos, dir);
        }

        discard();
    }

    private static Direction getHitDirection(Vec3d offset) {
        var dir = Direction.UP;

        var ax = Math.abs(offset.x);
        var ay = Math.abs(offset.y);
        var az = Math.abs(offset.z);
        var axisVec = new Vec3d(0, 1, 0);
        if (ax > ay && ax > az) axisVec = new Vec3d(offset.x, 0, 0);
        if (ay > ax && ay > az) axisVec = new Vec3d(0, offset.y, 0);
        if (az > ay && az > ax) axisVec = new Vec3d(0, 0, offset.z);
        axisVec = axisVec.normalize();

        dir = Direction.fromVector((int) axisVec.x, (int) axisVec.y, (int) axisVec.z);
        return dir;
    }

    //region Properties
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

    public void setSprayElement(SprayElement element) {
        getDataTracker().set(SPRAY_ELEMENT, element.ordinal());
    }

    public SprayElement getSprayElement() {
        return SprayElement.fromInt(getDataTracker().get(SPRAY_ELEMENT));
    }

    public void setLifeTimeLeft(int time) {
        getDataTracker().set(LIFE_TIME_LEFT, time);
    }

    public int getLifeTimeLeft() {
        return getDataTracker().get(LIFE_TIME_LEFT);
    }

    public void setMovementType(int movementType) {
        getDataTracker().set(MOVEMENT_TYPE, movementType);

        if (movementType == RotatingMovement) {
            setLifeTimeLeft(10);
        }
    }

    public int getMovementType() {
        return getDataTracker().get(MOVEMENT_TYPE);
    }

    //endregion

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        setSpellId(nbt.getString("spell"));
        var sprayElement = SprayElement.fromInt(nbt.getInt("sprayElement"));
        setSprayElement(sprayElement);
        setMovementType(nbt.getInt("movementType"));
        setLifeTimeLeft(nbt.getInt("lifeTimeLeft"));

        ItemStack wandStack = ItemStack.fromNbt(nbt.getCompound("wandStack"));
        if (nbt.contains("caster")) {
            var casterUuid = nbt.getUuid("caster");
            var player = CasterStorage.getCasterById(getWorld(), casterUuid);
            if (player != null && wandStack != null) {
                this.setCaster(player);
                this.setWandStack(wandStack);
            } else this.discard();
        }
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putString("spell", getSpellId());
        nbt.putInt("sprayElement", getSprayElement().ordinal());
        nbt.putInt("movementType", getMovementType());
        nbt.putInt("lifeTimeLeft", getLifeTimeLeft());

        nbt.putUuid("caster", getCaster().getCasterUuid());
        ItemStack wandStack = this.getWandStack();
        if (!wandStack.isEmpty()) {
            nbt.put("wandStack", wandStack.writeNbt(new NbtCompound()));
        }
    }

    public enum SprayElement {
        Fire, Water, Air, Cold; // Icd -> Cold

        public static SprayElement fromInt(int id) {
            return switch (id) {
                case 0 -> Fire;
                case 1 -> Water;
                case 2 -> Air;
                case 3 -> Cold;
                default -> Fire;
            };
        }
    }
}
