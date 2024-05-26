package faceless.artent.sharpening.mixin;

import faceless.artent.sharpening.api.SharpeningUtils;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class SharpenableItem {
    @Inject(at = @At("TAIL"), method = "getName(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/text/Text;", cancellable = true)
    public void getName(ItemStack stack, CallbackInfoReturnable<Text> cir) {
        var level = SharpeningUtils.getItemLevel(stack);
        if (level > 0)
            cir.setReturnValue(cir.getReturnValue().copy().append(" +" + level));
    }

    @Inject(at = @At("TAIL"), method = "inventoryTick")
    public void inventoryTick(
      ItemStack stack,
      World world,
      Entity entity,
      int slot,
      boolean selected,
      CallbackInfo ci
    ) {
        if (!SharpeningUtils.hasSlots(stack)) {
            SharpeningUtils.setSlotsCount(stack, world.random.nextInt(3));
        }
    }
}