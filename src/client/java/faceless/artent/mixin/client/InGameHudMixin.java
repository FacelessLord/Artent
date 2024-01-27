package faceless.artent.mixin.client;

import faceless.artent.hud.ArtentHudContext;
import faceless.artent.hud.ArtentHudRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
	@Shadow
	private Random random;
	@Shadow
	private MinecraftClient client;
	@Shadow
	private ItemRenderer itemRenderer;
	@Shadow
	private int scaledWidth;
	@Shadow
	private int scaledHeight;

	@Inject(method = "render", at = @At("TAIL"))
	public void render(DrawContext context, float tickDelta, CallbackInfo ci) {
		var hud = (InGameHud) (Object) this;
		if (this.client.options.hudHidden) return;

		var ctx = new ArtentHudContext(
			this.client,
			hud.getTextRenderer(),
			this.getCameraPlayer(),
			scaledWidth,
			scaledHeight,
			random
		);
		ArtentHudRenderer.render(context, tickDelta, ctx);
	}


	@Shadow
	private PlayerEntity getCameraPlayer() {
		return null;
	}
}
