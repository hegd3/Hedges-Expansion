package com.hedge.hedges_expansion.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BansheeScreamParticle extends AngledParticle {
    private static final float START_R = 94F / 255F;
    private static final float START_G = 1;
    private static final float START_B = 77F / 255F;

    private static final float END_R = 20F / 255F;
    private static final float END_G = 40F / 255F;
    private static final float END_B = 120F / 255F;

    public BansheeScreamParticle(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, SpriteSet pSprites, float xRot, float yRot) {
        super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, pSprites, 10, 0.80f, 0.95f, xRot, yRot);
        this.setColor(START_R, START_G, START_B);

    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        float progress = (float)this.age / (float)this.lifetime;

        this.rCol = Mth.lerp(progress, START_R, END_R);
        this.gCol = Mth.lerp(progress, START_G, END_G);
        this.bCol = Mth.lerp(progress, START_B, END_B);
        if (this.alpha > 0.01F) {
            this.alpha -= 0.05F;
            this.quadSize-= 0.1F;
        }
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.setSpriteFromAge(this.sprites);
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<BansheeScreamParticleOptions> {
        private final SpriteSet sprites;

        public Provider(SpriteSet pSprites) {
            this.sprites = pSprites;
        }

        public Particle createParticle(BansheeScreamParticleOptions options, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            BansheeScreamParticle p = new BansheeScreamParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, this.sprites, options.getXRot(), options.getYRot());
            p.pickSprite(this.sprites);
            return p;
        }

    }
}
