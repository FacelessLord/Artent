package faceless.artent.mobs.entity;

import faceless.artent.objects.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class WindmillEntity extends LivingEntity implements GeoEntity {
	protected static final RawAnimation ROTATION_ANIMATION = RawAnimation.begin().thenLoop("animation.windmill.baseRotation");
	private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

	public WindmillEntity(EntityType<? extends WindmillEntity> type, World world) {
		super(type, world);
	}

	public WindmillEntity(World world) {
		this(ModEntities.WINDMILL_ENTITY, world);
	}

	public static DefaultAttributeContainer.Builder createLivingAttributes() {
		return DefaultAttributeContainer
				   .builder()
				   .add(EntityAttributes.GENERIC_MAX_HEALTH)
				   .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE)
				   .add(EntityAttributes.GENERIC_MOVEMENT_SPEED)
				   .add(EntityAttributes.GENERIC_ARMOR)
				   .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS)
				   .add(EntityAttributes.GENERIC_MAX_ABSORPTION);
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
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
	public Arm getMainArm() {
		return Arm.RIGHT;
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>(this, "ROTATION", 5, this::rotationController));
	}

	protected PlayState rotationController(final AnimationState<WindmillEntity> event) {
		return event.setAndContinue(ROTATION_ANIMATION);
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.geoCache;
	}
}
