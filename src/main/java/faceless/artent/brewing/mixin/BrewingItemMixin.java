package faceless.artent.brewing.mixin;

import faceless.artent.brewing.api.IBrewable;
import net.minecraft.entity.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

//@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(ItemEntity.class)
public class BrewingItemMixin implements IBrewable {
    @Unique
    private int brewingTime = 0;

    @Override
    public int getBrewingTime() {
        return brewingTime;
    }

    @Override
    public void setBrewingTime(int time) {
        brewingTime = time;
    }
}