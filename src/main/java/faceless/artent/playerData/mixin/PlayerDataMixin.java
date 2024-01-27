package faceless.artent.playerData.mixin;

import faceless.artent.playerData.api.ArtentPlayerData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerDataMixin implements ArtentPlayerData {
	@Unique
	public long artentMoney = 0;

	@Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
	private void onWriteEntityToNBT(NbtCompound compound, CallbackInfo ci) {
		this.writeToNbt(compound);
	}

	@Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
	private void onReadEntityFromNBT(NbtCompound compound, CallbackInfo ci) {
		this.readFromNbt(compound);
	}

	@Override
	public long getMoney() {
		return artentMoney;
	}

	@Override
	public void setMoney(long money) {
		artentMoney = money;
	}

	@Override
	public void addMoney(long money) {
		artentMoney += money;
	}

	@Override
	public void writeToNbt(NbtCompound compound) {
		var tag = new NbtCompound();
		tag.putLong("money", this.getMoney());

		compound.put("artent.data", tag);
	}

	@Override
	public void readFromNbt(NbtCompound compound) {
		if (!compound.contains("artent.data"))
			return;
		var tag = compound.getCompound("artent.data");

		setMoney(tag.getLong("artent.money"));
	}
}
