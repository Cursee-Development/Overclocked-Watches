package io.github.jason13official.overclocked_watches.impl.common.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.SuspendedTownParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class WatchGrowthParticle extends SingleQuadParticle {

  public WatchGrowthParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet, double xSpeed, double ySpeed, double zSpeed) {
    super(level, x, y, z, xSpeed, ySpeed, zSpeed, spriteSet.first());

    float f = this.random.nextFloat() * 0.1F + 0.2F;
    this.rCol = f;
    this.gCol = f;
    this.bCol = f;
    this.setSize(0.02F, 0.02F);
    this.quadSize *= this.random.nextFloat() * 0.6F + 0.5F;
    this.xd *= 0.019999999552965164;
    this.yd *= 0.019999999552965164;
    this.zd *= 0.019999999552965164;
    this.lifetime = (int) (20.0 / (Math.random() * 0.8 + 0.2));
  }

  @Override
  protected SingleQuadParticle.Layer getLayer() {
    return SingleQuadParticle.Layer.TRANSLUCENT;
  }

  /**
   * @see SuspendedTownParticle
   */
  public static class HappyVillagerParticleCopiedProvider implements ParticleProvider<SimpleParticleType> {

    private final SpriteSet sprites;

    public HappyVillagerParticleCopiedProvider(SpriteSet sprites) {
      this.sprites = sprites;
    }

    public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
      WatchGrowthParticle suspendedTownParticle = new WatchGrowthParticle(level, x, y, z, this.sprites, xSpeed, ySpeed, zSpeed);
      suspendedTownParticle.setSpriteFromAge(this.sprites);
      suspendedTownParticle.setColor(1.0F, 1.0F, 1.0F);
      return suspendedTownParticle;
    }
  }
}
