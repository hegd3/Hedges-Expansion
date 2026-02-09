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

}
