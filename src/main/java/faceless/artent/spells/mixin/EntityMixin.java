package faceless.artent.spells.mixin;

import faceless.artent.spells.api.CasterStorage;
import faceless.artent.spells.api.ICaster;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
	@Inject(method = "discard", at = @At("TAIL"))
	public final void discard(CallbackInfo ci) {
		var entity = (Entity) (Object) this;
		if (entity instanceof ICaster caster)
			CasterStorage.removeCaster(entity.getWorld(), caster);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	public void tick(CallbackInfo ci) {
		var entity = (Entity) (Object) this;

		if (entity instanceof ICaster caster && entity.getWorld() != null && entity.getWorld().getTime() % 10 == 0)
			CasterStorage.putCaster(entity.getWorld(), caster);
	}
}
