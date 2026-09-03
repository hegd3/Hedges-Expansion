package com.hedge.hedges_bestiary.potion;

import com.hedge.hedges_bestiary.client.HBSounds;
import com.hedge.hedges_bestiary.registry.HBParticles;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class VolatileEffect extends MobEffect {
    public VolatileEffect() {
        super(MobEffectCategory.HARMFUL, 0xF01F1F);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (pLivingEntity.level().isClientSide) {
            pLivingEntity.level().addParticle(HBParticles.MURK_EXPLODE.get(), true, pLivingEntity.getX(), pLivingEntity.getY(0.5), pLivingEntity.getZ(), 0.0D, 0.0D, 0.0D);
        }
        pLivingEntity.hurt(pLivingEntity.damageSources().explosion(null, null), 7 + pAmplifier);

    }

    @Override
    public boolean isDurationEffectTick(int duration, int pAmplifier) {
        return duration == 1;
    }
}
