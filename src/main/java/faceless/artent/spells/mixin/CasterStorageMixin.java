package faceless.artent.spells.mixin;

import faceless.artent.spells.api.CasterStorage;
import faceless.artent.spells.api.IContainsCasterStorage;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@SuppressWarnings("ALL")
@Mixin(World.class)
public class CasterStorageMixin implements IContainsCasterStorage {
	@Unique
	private CasterStorage casterStorage = new CasterStorage();

	@Unique
	@Override
	public CasterStorage getCasterStorage() {
		return this.casterStorage;
	}
}
