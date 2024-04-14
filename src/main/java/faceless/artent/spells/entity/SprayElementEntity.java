package faceless.artent.spells.entity;

import faceless.artent.spells.api.CasterStorage;
import faceless.artent.spells.api.ICaster;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Unique;

public class SprayElementEntity extends ThrownEntity {
	@Unique
	private static final TrackedData<Integer> SPRAY_ELEMENT = DataTracker.registerData(LivingEntity.class,
	  TrackedDataHandlerRegistry.INTEGER);
	@Unique
	private static final TrackedData<Integer> MOVEMENT_TYPE = DataTracker.registerData(LivingEntity.class,
	  TrackedDataHandlerRegistry.INTEGER);

	private ICaster caster;
	private ItemStack wandStack = ItemStack.EMPTY;
	private Vec3d startingPos;
	private int lifeTimeLeft = 10;

	public SprayElementEntity(EntityType<? extends ThrownEntity> type, World world) {
		super(type, world);
		this.setNoGravity(true);
	}

	@Override
	protected void initDataTracker() {
		getDataTracker().startTracking(SPRAY_ELEMENT, SprayElement.Fire.ordinal());
		getDataTracker().startTracking(MOVEMENT_TYPE, DirectMovent);
	}

	@Override
	public void tick() {
		super.tick();
		lifeTimeLeft--;
		if (lifeTimeLeft <= 0)
			discard();
	}

	@Override
	protected void onEntityHit(EntityHitResult entityHitResult) {
		if (getSprayElement() == SprayElement.Fire) {
			var entity = entityHitResult.getEntity();
			entity.setOnFireFor(20);

			if (entity instanceof LivingEntity living && caster instanceof LivingEntity livingCaster && living != livingCaster) {
				living.damage(getDamageSources().mobAttack(livingCaster), caster.getPotency());
			}
		}
		discard();
	}

	@Override
	protected void onBlockHit(BlockHitResult blockHitResult) {
		var blockPos = blockHitResult.getBlockPos();
		var blockState = this.getWorld().getBlockState(blockPos);
		var block = blockState.getBlock();

		if (getSprayElement() == SprayElement.Water) {
			if (block == Blocks.DIRT) {
				getWorld().setBlockState(blockPos, Blocks.GRASS_BLOCK.getDefaultState());
			}
//			if (block instanceof Fertilizable fertilizable && !getWorld().isClient) {
//				fertilizable.grow((ServerWorld) getWorld(), getWorld().random, blockPos, blockState);
//			} TODO
			for (int i = -1; i < 2; i++) {
				for (int j = -1; j < 2; j++) {
					for (int k = -1; k < 2; k++) {
						var offsetPos = blockPos.add(i, j, k);
						var offsetBlock = getWorld().getBlockState(offsetPos).getBlock();
						if (offsetBlock == Blocks.FIRE) {
							getWorld().setBlockState(offsetPos, Blocks.AIR.getDefaultState());
						}
					}
				}
			}
		}
		if (getSprayElement() == SprayElement.Fire) {
			for (int i = -1; i < 2; i++) {
				for (int j = -1; j < 2; j++) {
					for (int k = -1; k < 2; k++) {
						var offsetPos = blockPos.add(i, j, k);
						var offsetState = getWorld().getBlockState(offsetPos);
						var isAir = offsetState.isAir();
						var replaceable = offsetState.isReplaceable();
						if (isAir || replaceable) {
							getWorld().setBlockState(offsetPos, Blocks.FIRE.getDefaultState());
						}
					}
				}
			}
		}

		discard();
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

	public void setMovementType(int movementType) {
		getDataTracker().set(MOVEMENT_TYPE, movementType);

	}

	public int getMovementType() {
		return getDataTracker().get(MOVEMENT_TYPE);
	}

	@Override
	protected void readCustomDataFromNbt(NbtCompound nbt) {
		var sprayElement = SprayElement.fromInt(nbt.getInt("sprayElement"));
		setSprayElement(sprayElement);
		setMovementType(nbt.getInt("movementType"));
		lifeTimeLeft = nbt.getInt("lifeTimeLeft");
		if (nbt.contains("startingPosX")) {
			var startingPosX = nbt.getDouble("startingPosX");
			var startingPosY = nbt.getDouble("startingPosY");
			var startingPosZ = nbt.getDouble("startingPosZ");

			startingPos = new Vec3d(startingPosX, startingPosY, startingPosZ);
		}


		ItemStack wandStack = ItemStack.fromNbt(nbt.getCompound("Item"));
		if (nbt.contains("Caster")) {
			var casterUuid = nbt.getUuid("Caster");
			var player = CasterStorage.getCasterById(getWorld(), casterUuid);
			if (player != null && wandStack != null) {
				this.setCaster(player);
				this.setWandStack(wandStack);
			} else
				this.discard();
		}
	}

	@Override
	protected void writeCustomDataToNbt(NbtCompound nbt) {
		nbt.putInt("sprayElement", getSprayElement().ordinal());
		nbt.putInt("movementType", getMovementType());
		nbt.putInt("lifeTimeLeft", lifeTimeLeft);
		if (startingPos != null) {
			nbt.putDouble("startingPosX", startingPos.x);
			nbt.putDouble("startingPosY", startingPos.y);
			nbt.putDouble("startingPosZ", startingPos.z);
		}

		nbt.putUuid("Caster", getCaster().getCasterUuid());
		ItemStack wandStack = this.getWandStack();
		if (!wandStack.isEmpty()) {
			nbt.put("WandStack", wandStack.writeNbt(new NbtCompound()));
		}
	}

	public Vec3d getStartingPos() {
		return startingPos;
	}

	public void setStartingPos(Vec3d startingPos) {
		this.startingPos = startingPos;
	}

	public static enum SprayElement {
		Fire,
		Water;

		public static SprayElement fromInt(int id) {
			return switch (id) {
				case 0 -> Fire;
				case 1 -> Water;
				default -> Fire;
			};
		}
	}

	public static final int DirectMovent = 1;
	public static final int RotatingMovent = 2;
}
