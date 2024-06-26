package faceless.artent.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;

@Environment(value = EnvType.CLIENT)
public class WaterParticle
  extends AbstractSlowingParticle {
    WaterParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
        super(clientWorld, d, e, f, g, h, i);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void move(double dx, double dy, double dz) {
        this.setBoundingBox(this.getBoundingBox().offset(dx, dy, dz));
        this.repositionFromBoundingBox();
    }

    @Override
    public float getSize(float tickDelta) {
        float f = ((float) this.age + tickDelta) / (float) this.maxAge;
        return this.scale * (1.0f - f * f * 0.5f);
    }

    @Override
    public int getBrightness(float tint) {
        float f = ((float) this.age + tint) / (float) this.maxAge;
        f = MathHelper.clamp(f, 0.0f, 1.0f);
        int i = super.getBrightness(tint);
        int j = i & 0xFF;
        int k = i >> 16 & 0xFF;
        if ((j += (int) (f * 15.0f * 16.0f)) > 240) {
            j = 240;
        }
        return j | k << 16;
    }

    @Environment(EnvType.CLIENT)
    public static class SmallFactory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public SmallFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(
          DefaultParticleType defaultParticleType,
          ClientWorld clientWorld,
          double d,
          double e,
          double f,
          double g,
          double h,
          double i
        ) {
            WaterParticle waterParticle = new WaterParticle(clientWorld, d, e, f, g, h, i);
            waterParticle.setSprite(this.spriteProvider);
            waterParticle.scale(0.5F);
            return waterParticle;
        }
    }

    @Environment(value = EnvType.CLIENT)
    public static class Factory
      implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(
          DefaultParticleType defaultParticleType,
          ClientWorld clientWorld,
          double d,
          double e,
          double f,
          double g,
          double h,
          double i
        ) {
            WaterParticle waterParticle = new WaterParticle(clientWorld, d, e, f, g, h, i);
            waterParticle.setSprite(this.spriteProvider);
            return waterParticle;
        }
    }
}