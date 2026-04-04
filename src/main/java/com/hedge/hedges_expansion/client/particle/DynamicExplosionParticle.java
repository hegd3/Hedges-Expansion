package com.hedge.hedges_expansion.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class DynamicExplosionParticle extends HugeExplosionParticle {
    public DynamicExplosionParticle(ClientLevel pLevel, double pX, double pY, double pZ, double pQuadSizeMultiplier, SpriteSet pSprites, int lifetime, float size) {
        super(pLevel, pX, pY, pZ, pQuadSizeMultiplier, pSprites);
        this.lifetime = lifetime;
        this.quadSize = size;
        this.setSpriteFromAge(pSprites);
    }

    @OnlyIn(Dist.CLIENT)
    public static class MurkExplosionProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public MurkExplosionProvider(SpriteSet pSprites) {
            this.sprites = pSprites;
        }

        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new DynamicExplosionParticle(pLevel, pX, pY, pZ, pXSpeed, this.sprites, 14, 6);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class MurkImpactProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public MurkImpactProvider(SpriteSet pSprites) {
            this.sprites = pSprites;
        }
        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new DynamicExplosionParticle(pLevel, pX, pY, pZ, pXSpeed, this.sprites, 5, 2.5f);
        }
    }
}
