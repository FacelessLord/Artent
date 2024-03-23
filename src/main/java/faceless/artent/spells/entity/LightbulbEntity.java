package faceless.artent.spells.entity;

import faceless.artent.spells.api.CasterStorage;
import faceless.artent.spells.api.ICaster;
import faceless.artent.spells.spells.MakeLight;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import org.joml.Vector3f;

public class LightbulbEntity extends ThrownEntity {
	private ICaster caster;
	private ItemStack wandStack = ItemStack.EMPTY;
	private static final TrackedData<String> SPELL = DataTracker.registerData(LightbulbEntity.class,
		TrackedDataHandlerRegistry.STRING);

	public LightbulbEntity(EntityType<? extends LightbulbEntity> entityType,
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
	protected void onBlockHit(BlockHitResult blockHitResult) {
		super.onBlockHit(blockHitResult);
		// TODO: var spell = spellRegistry.getSpell(this.getSpellId());
		var spell = new MakeLight();
		if (spell == null) {
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

	@Override
	public void tick() {
		super.tick();
		var world = getWorld();
		if (world.isClient && world.random.nextBoolean()) {
			world.addParticle(new DustParticleEffect(new Vector3f(1, 1, 0.5f), 1),
				getParticleX(0.05f),
				this.getRandomBodyY(),
				getParticleZ(0.05f),
				0,
				0,
				0);
		}
	}
}
