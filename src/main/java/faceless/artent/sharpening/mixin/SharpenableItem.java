package faceless.artent.sharpening.mixin;

import faceless.artent.api.MiscUtils;
import faceless.artent.sharpening.api.SharpeningUtils;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.List;

@Mixin(Item.class)
public class SharpenableItem {
	@Inject(at = @At("TAIL"), method = "getName(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/text/Text;", cancellable = true)
	public void getName(ItemStack stack, CallbackInfoReturnable<Text> cir) {
		var level = SharpeningUtils.getItemLevel(stack);
		if (level > 0)
			cir.setReturnValue(cir.getReturnValue().copy().append(" +" + level));
	}

	@Inject(at = @At("TAIL"), method = "inventoryTick")
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected, CallbackInfo ci) {
		if (!SharpeningUtils.hasSlots(stack)) {
			SharpeningUtils.setSlotsCount(stack, world.random.nextInt(3));
		}
	}

	@Inject(at = @At("TAIL"), method = "appendTooltip")
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context, CallbackInfo ci) {
		var enhancers = SharpeningUtils.getEnhancers(stack);

		if (enhancers.length > 0) {
			if (context.isAdvanced()) {
				var i = 1;
				tooltip.add(Text.translatable("text.artent.slots"));
				for (var enchancer : enhancers) {
					var formatting = enchancer != null ? enchancer.getFormatting() : Formatting.GRAY;
					var enhancerName = enchancer != null
						? enchancer.getName().copy().formatted(enchancer.getFormatting())
						: Text.translatable("text.artent.enhancer.null");
					tooltip.add(Text.translatable(i + ". ").formatted(formatting).append(enhancerName));
					i++;
				}
			} else {
				var slotsString = Arrays.stream(enhancers).map(e -> {
					if (e != null)
						return Text.translatable(MiscUtils.FILLED_CIRCLE).formatted(e.getFormatting());
					return Text.translatable(MiscUtils.EMPTY_CIRCLE).formatted(Formatting.WHITE);
				}).reduce(MutableText::append).orElse(Text.translatable(MiscUtils.EMPTY_CIRCLE));
				tooltip.add(slotsString);
			}
		}
	}
}