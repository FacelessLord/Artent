package faceless.artent.trading.entity;

import faceless.artent.network.ArtentServerHook;
import faceless.artent.objects.ModEntities;
import faceless.artent.playerData.api.DataUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class CoinEntity extends Entity {
    private static final TrackedData<Integer> COIN_TYPE = DataTracker.registerData(CoinEntity.class,
                                                                                   TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Long> COUNT = DataTracker.registerData(CoinEntity.class,
                                                                            TrackedDataHandlerRegistry.LONG);

    public final float uniqueOffset;
    private int pickupDelay = 10;

    public CoinEntity(EntityType<? extends CoinEntity> type, World world) {
        super(type, world);
        this.uniqueOffset = this.random.nextFloat() * (float) Math.PI * 2.0f;
    }

    public CoinEntity(World world) {
        this(ModEntities.COIN_ENTITY, world);
    }

    @Override
    protected void initDataTracker() {
        this.getDataTracker().startTracking(COIN_TYPE, 0);
        this.getDataTracker().startTracking(COUNT, 1L);
    }

    public int getCoinType() {
        return this.getDataTracker().get(COIN_TYPE);
    }

    public long getCount() {
        return this.getDataTracker().get(COUNT);
    }

    public void setCoinType(int coinType) {
        this.getDataTracker().set(COIN_TYPE, coinType);
    }

    public void setCoinCount(long count) {
        this.getDataTracker().set(COUNT, count);
    }

    public float getRotation(float tickDelta) {
        return ((float) this.age + tickDelta) / 20.0f + this.uniqueOffset;
    }

    public int getRenderedAmount() {
        int i = 1;
        if (getCount() > 80) {
            i = 5;
        } else if (getCount() > 56) {
            i = 4;
        } else if (getCount() > 24) {
            i = 3;
        } else if (getCount() > 4) {
            i = 2;
        }
        return i;
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        setCoinCount(nbt.getInt("countCount"));
        setCoinType(nbt.getInt("coinType"));
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putInt("coinType", getCoinType());
        nbt.putLong("coinCount", getCount());
    }


    @Override
    public void tick() {
        if (this.getCount() == 0) {
            this.discard();
            return;
        }
        super.tick();
        if (this.pickupDelay > 0 && this.pickupDelay != Short.MAX_VALUE) {
            --this.pickupDelay;
        }
        if (getCount() >= 100 && getCoinType() < 2) {
            setCoinType(getCoinType() + 1);
            setCoinCount(1);
        }

        this.prevX = this.getX();
        this.prevY = this.getY();
        this.prevZ = this.getZ();
        Vec3d vec3d = this.getVelocity();
        if (!this.hasNoGravity()) {
            this.setVelocity(this.getVelocity().add(0.0, -0.04, 0.0));
        }
        if (this.getWorld().isClient) {
            this.noClip = false;
        } else {
            this.noClip = !this.getWorld().isSpaceEmpty(this, this.getBoundingBox().contract(1.0E-7));
            if (this.noClip) {
                this.pushOutOfBlocks(this.getX(),
                                     (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2.0,
                                     this.getZ());
            }
        }
        if (!this.isOnGround() ||
            this.getVelocity().horizontalLengthSquared() > (double) 1.0E-5f ||
            (this.age + this.getId()) % 4 == 0) {
            this.move(MovementType.SELF, this.getVelocity());
            float g = 0.98f;
            if (this.isOnGround()) {
                g = this.getWorld().getBlockState(this.getVelocityAffectingPos()).getBlock().getSlipperiness() * 0.98f;
            }
            this.setVelocity(this.getVelocity().multiply(g, 0.98, g));
            if (this.isOnGround()) {
                Vec3d vec3d2 = this.getVelocity();
                if (vec3d2.y < 0.0) {
                    this.setVelocity(vec3d2.multiply(1.0, -0.5, 1.0));
                }
            }
        }
        boolean bl = MathHelper.floor(this.prevX) != MathHelper.floor(this.getX()) ||
                     MathHelper.floor(this.prevY) != MathHelper.floor(this.getY()) ||
                     MathHelper.floor(this.prevZ) != MathHelper.floor(this.getZ());
        int i = bl ? 2 : 10;
        if (this.age % i == 1 && !this.getWorld().isClient) {
            this.tryMerge();
        }
        if (this.age != Short.MIN_VALUE) {
            ++this.age;
        }
        this.velocityDirty |= this.updateWaterState();
        if (!this.getWorld().isClient && this.getVelocity().subtract(vec3d).lengthSquared() > 0.01) {
            this.velocityDirty = true;
        }
        if (!this.getWorld().isClient && this.age >= 6000) {
            this.discard();
        }
    }

    private void tryMerge() {
        if (!this.canMerge()) {
            return;
        }
        List<CoinEntity> list =
          this.getWorld()
              .getEntitiesByClass(CoinEntity.class,
                                  this.getBoundingBox().expand(0.75, 0.25, 0.75),
                                  otherCoinEntity -> otherCoinEntity != this && otherCoinEntity.canMerge());
        for (CoinEntity coinEntity : list) {
            if (getCoinType() != coinEntity.getCoinType()) continue;
            this.tryMerge(coinEntity);
            if (!this.isRemoved()) continue;
            break;
        }
    }

    public static void merge(CoinEntity src, CoinEntity target) {
        var dTarget = 100 - target.getCount();
        var dSrc = Math.min(src.getCount(), dTarget);
        src.setCoinCount(src.getCount() - dSrc);
        target.setCoinCount(target.getCount() + dSrc);

        if (src.getCount() == 0) src.discard();
    }

    private void tryMerge(CoinEntity other) {
        if (getCount() < other.getCount()) {
            merge(this, other);
        } else {
            merge(other, this);
        }
    }

    private boolean canMerge() {
        return this.isAlive() && this.age != Short.MIN_VALUE && this.age < 6000 && getCount() < 100;
    }


    @Override
    public void onPlayerCollision(PlayerEntity player) {
        if (this.getWorld().isClient || this.pickupDelay != 0) {
            return;
        }
        var handler = DataUtil.getHandler(player);
        var type = getCoinType();
        var count = getCount();
        var money = (long) Math.pow(10, type * 2) * count;

        handler.addMoney(money);
        ArtentServerHook.packetSyncPlayerData(player);

        discard();
    }
}
