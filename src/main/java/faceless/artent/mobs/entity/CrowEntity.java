package faceless.artent.mobs.entity;

import faceless.artent.objects.ModEntities;
import faceless.artent.objects.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

import static net.minecraft.world.Heightmap.Type.WORLD_SURFACE;

public class CrowEntity extends AnimalEntity implements GeoEntity {
	protected static final RawAnimation FLIGHT_ANIMATION = RawAnimation.begin().thenLoop("animation.crow.flight");
	protected static final RawAnimation SIT_ANIMATION = RawAnimation.begin().thenPlay("animation.crow.sit");
	protected static final RawAnimation START_FLIGHT_ANIMATION = RawAnimation.begin()
																	 .thenPlay("animation.crow.startFlying");

	private static final TrackedData<String> ANIMATION_STATE = DataTracker.registerData(CrowEntity.class,
		TrackedDataHandlerRegistry.STRING);
	public static long FlockAdditionalTime = 15 * 20;

	private static final Ingredient BREEDING_INGREDIENT = Ingredient.ofItems(
		Items.WHEAT_SEEDS,
		Items.MELON_SEEDS,
		Items.PUMPKIN_SEEDS,
		Items.BEETROOT_SEEDS,
		Items.TORCHFLOWER_SEEDS,
		Items.PITCHER_POD);

	private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

	public long timeInState = 0;
	//	public CrowState state = CrowState.Sitting;
	public CrowState prevState = CrowState.Sitting;
	public boolean isInFlock = false;

	public BlockPos spawnPos = null;
	public PlayerEntity fatherPlayer = null;

	public CrowEntity(EntityType<? extends CrowEntity> type, World world) {
		super(type, world);
		this.setMovementSpeed(0.25f);
//		ignoreCameraFrustum = true;
	}

	public CrowState getAnimationState() {
		var state = this.getDataTracker().get(ANIMATION_STATE);
		return CrowState.valueOf(state);
	}

	public void setAnimationState(CrowState state) {
		this.getDataTracker().set(ANIMATION_STATE, state.name());
	}

	public CrowEntity(World world) {
		this(ModEntities.CROW_ENTITY, world);
	}

	public static DefaultAttributeContainer.Builder createLivingAttributes() {
		return DefaultAttributeContainer
				   .builder()
				   .add(EntityAttributes.GENERIC_MAX_HEALTH)
				   .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE)
				   .add(EntityAttributes.GENERIC_MOVEMENT_SPEED)
				   .add(EntityAttributes.GENERIC_ARMOR)
				   .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS)
				   .add(EntityAttributes.GENERIC_MAX_ABSORPTION)
				   .add(EntityAttributes.GENERIC_FOLLOW_RANGE);
	}

	@Override
	public boolean cannotDespawn() {
		return true;
	}

	@Override
	public boolean isBreedingItem(ItemStack stack) {
		return BREEDING_INGREDIENT.test(stack);
	}

	@Nullable
	@Override
	public PassiveEntity createChild(ServerWorld world, PassiveEntity parent) {
		var entity = new CrowEntity(world);
		entity.setPosition(entity.getPos());

		return entity;
	}

	@Override
	public boolean shouldRender(double distance) {
		return true;
	}

	public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
		return true;
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();

		this.getDataTracker().startTracking(ANIMATION_STATE, CrowState.Sitting.name());
	}

	@Override
	public Iterable<ItemStack> getArmorItems() {
		return List.of();
	}

	@Override
	public ItemStack getEquippedStack(EquipmentSlot slot) {
		return ItemStack.EMPTY;
	}

	@Override
	public void equipStack(EquipmentSlot slot, ItemStack stack) {

	}

	@Override
	public void tick() {
		if (spawnPos == null)
			spawnPos = this.getBlockPos();

//		if (age % 4 == 0)
		updateState();
		super.tick();
	}

	@Override
	public Arm getMainArm() {
		return Arm.RIGHT;
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>(this, "ROTATION", 5, this::rotationController));
	}

	protected PlayState rotationController(final AnimationState<CrowEntity> event) {
		return switch (getAnimationState()) {
			case Sitting -> event.setAndContinue(SIT_ANIMATION);
			case Flying, Landing -> event.setAndContinue(FLIGHT_ANIMATION);
			case StartFlight -> event.setAndContinue(START_FLIGHT_ANIMATION);
			default -> event.setAndContinue(SIT_ANIMATION);
		};
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.geoCache;
	}

	public BlockPos getNestPos() {
		if (fatherPlayer != null)
			return fatherPlayer.getBlockPos();
		return spawnPos;
	}

	public void updateState() {
		timeInState++;
		var state = getAnimationState();
		if (state == CrowState.Sitting && timeInState >= 3 * 20) // TODO make more
			setState(CrowState.StartFlight);
		if (state == CrowState.StartFlight && timeInState >= 20 / 2) {
			setState(CrowState.Flying);
			if (!hasNoGravity())
				setNoGravity(true);
		}
		if (state == CrowState.Flying) {
			if (timeInState >= 120 * 20 + (isInFlock ? FlockAdditionalTime : 0)) {
				setState(CrowState.Landing);
				return;
			}
			if (!hasNoGravity())
				setNoGravity(true);
			var velocity = getRotationVector().multiply(0.25f);

			var topPosition = getAverageTopHeight() + 20;

			var targetHeightRange = this.getBlockY() - topPosition;
			var dy = calculateDyByHeightRange(targetHeightRange);
			dy = -dy * dy * dy;
			velocity = velocity.add(0, dy * this.getMovementSpeed(), 0);

			var spawnVec = getNestPos().toCenterPos().subtract(this.getPos());
			if (spawnVec.length() > 10 && spawnVec.dotProduct(this.getRotationVector()) < 0) {
				this.setRotation(this.getYaw() + 1f, this.getPitch());
			}

			// interpolation between previous and next calculated velocity;
			velocity = velocity.normalize().multiply(this.getMovementSpeed()).multiply(0.66f)
						   .add(this.getVelocity().multiply(0.34f));

			var box = Box.of(this.getPos(), 15, 7, 15);
			var flock = getWorld().getEntitiesByType(ModEntities.CROW_ENTITY, box, e -> {
				var dirVec = e.getPos().subtract(this.getPos()).normalize();
				return e != this && dirVec.dotProduct(this.getRotationVector()) > -0.5;
			});
			this.isInFlock = flock.size() > 3;
			if (flock.size() > 0) {
				var alignmentVelocity = new Vec3d(0, 0, 0);
				var averageFlockPos = new Vec3d(0, 0, 0);
				for (var crow : flock) {
					var dPos = crow.getPos().subtract(this.getPos());
					if (dPos.length() < 2 && dPos.normalize().dotProduct(this.getRotationVector()) > 0.5) {
						var dYaw = this.getYaw() - crow.getYaw();
						var dYawNormalized = dYaw > 2 ? 2 : dYaw < -2 ? -2 : 0;
						this.setYaw(this.getYaw() + dYawNormalized);
						alignmentVelocity = alignmentVelocity.add(dPos.normalize()
																	  .multiply(-0.3f * getMovementSpeed()));
					} else {
						var dYaw = crow.getYaw() - this.getYaw();
						var dYawNormalized = dYaw > 1 ? 1 : dYaw < -1 ? -1 : 0;
						this.setYaw(this.getYaw() + dYawNormalized);
						alignmentVelocity = alignmentVelocity.add(velocity.multiply(1 / 0.8f));
					}
//					alignmentVelocity = alignmentVelocity.add(crow.getRotationVector());
					averageFlockPos = averageFlockPos.add(crow.getPos());
				}
				alignmentVelocity = alignmentVelocity.normalize().multiply(this.getMovementSpeed());
				var cohesionVelocity = averageFlockPos.multiply(1f / flock.size()).subtract(this.getPos())
										   .normalize().multiply(this.getMovementSpeed());

				velocity = velocity.multiply(0.5f)
							   .add(alignmentVelocity.multiply(0.2f))
							   .add(cohesionVelocity.multiply(0.3f));
			}
			this.setVelocity(velocity);
		}
		if (state == CrowState.Landing) {
			var spawnVec = getNestPos().toCenterPos().subtract(this.getPos());

			if (spawnVec.length() < 2) {
				var nestPos = getNestPos().down();
				if (!getWorld().getBlockState(nestPos).isAir()) {
					fallDistance = 0;
					setState(CrowState.Landed);
					if (hasNoGravity())
						this.setNoGravity(false);
					return;
				}
			}
			var yaw = MathHelper.wrapDegrees(this.getYaw() + 360f);
			var nestYaw = MathHelper.wrapDegrees((float) (MathHelper.atan2(
				spawnVec.z,
				spawnVec.x) * 57.2957763671875) - 90.0F);
			var dYaw = (nestYaw - yaw);
			if (dYaw > 180)
				dYaw -= 360;
			if (dYaw < -180)
				dYaw += 360;
			var dYawNorm = dYaw > 2
							   ? 2
							   : dYaw < -2 ? -2 : 0;
			this.setYaw(yaw + dYawNorm);

			var velocity = getRotationVector().multiply(0.25f);

			var distanceToNest = spawnVec.length();
			var x = distanceToNest > 20 ? 1 : distanceToNest / 20;
			var interpolationValue = (float) (3 * x * x * x - 2 * x * x);

			var flightTopPosition = getAverageTopHeight() + 20;

			// interpolating between top and nest position;
			var targetHeightRange = this.getBlockY() - flightTopPosition;
			var topSig = calculateDyByHeightRange(targetHeightRange);
			topSig = -topSig * topSig * topSig;
			var farVelocity = velocity.add(0, topSig * this.getMovementSpeed(), 0);

			var nestHeight = this.getNestPos().getY() + 0.5f;
			var nestHeightRange = this.getBlockY() - nestHeight;
			var nestSig = calculateDyByHeightRange(nestHeightRange);
			var nearVelocity = velocity.add(0, -nestSig * this.getMovementSpeed(), 0);

			velocity = farVelocity.multiply(interpolationValue).add(nearVelocity.multiply(1 - interpolationValue));

			this.setVelocity(velocity);
		}
		if (state == CrowState.Landed && timeInState >= 20 / 2)
			setState(CrowState.Sitting);
	}

	public float calculateDyByHeightRange(float heightRange) {
		var exp = (float) Math.exp(-heightRange / 3);

		return (1 - exp) / (1 + exp);
	}

	public float getAverageTopHeight() {
		var center = getWorld().getTopPosition(WORLD_SURFACE, this.getBlockPos());
		var plus_plus = getWorld().getTopPosition(WORLD_SURFACE, this.getBlockPos().add(2, 0, 2));
		var plus_minus = getWorld().getTopPosition(WORLD_SURFACE, this.getBlockPos().add(2, 0, -2));
		var minus_plus = getWorld().getTopPosition(WORLD_SURFACE, this.getBlockPos().add(-2, 0, 2));
		var minus_minus = getWorld().getTopPosition(WORLD_SURFACE, this.getBlockPos().add(-2, 0, -2));

		return (
			center.getY()
				+ plus_plus.getY()
				+ plus_minus.getY()
				+ minus_plus.getY()
				+ minus_minus.getY()
		) / 5f;
	}

	@Override
	public ActionResult interactAt(PlayerEntity player, Vec3d hitPos, Hand hand) {
		if (player != null) {
			if (player.getStackInHand(hand).isOf(ModItems.CrowStaff)) {
				this.fatherPlayer = player;
			}
		}

		return super.interactAt(player, hitPos, hand);
	}

	public void setState(CrowState state) {
		if (state == this.getAnimationState())
			return;
		this.prevState = this.getAnimationState();
		this.setAnimationState(state);
		this.timeInState = 0;
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		setAnimationState(ParseEnum(nbt.getString("animationState"), CrowState.class, CrowState.Sitting));
		prevState = ParseEnum(nbt.getString("prevAnimationState"), CrowState.class, CrowState.Sitting);
		timeInState = nbt.getLong("timeInState");
		isInFlock = nbt.getBoolean("isInFlock");
		var spawnPos1 = nbt.getIntArray("spawnPos");
		if (spawnPos1.length > 0)
			spawnPos = new BlockPos(spawnPos1[0], spawnPos1[1], spawnPos1[2]);
		else
			spawnPos = null;
	}

	public static <T extends Enum<T>> T ParseEnum(String value, Class<T> classT, T defaultValue) {
		if (value != null && !value.equals(""))
			return T.valueOf(classT, value);
		return defaultValue;
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putString("animationState", getAnimationState().name());
		nbt.putString("prevAnimationState", prevState.name());
		nbt.putLong("timeInState", timeInState);
		nbt.putBoolean("isInFlock", isInFlock);
		nbt.putIntArray("spawnPos", new int[]{ spawnPos.getX(), spawnPos.getY(), spawnPos.getZ() });
	}

	public enum CrowState {
		Sitting,
		StartFlight,
		Landing,
		Landed,
		Flying
	}
}
