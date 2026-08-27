package com.hedge.hedges_bestiary.entity.types;

import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;

public interface EggLayer {

    public BlockState getEgg();

    public boolean hasEgg();

    public void setHasEgg(boolean b);

    default boolean laysMultipleEggs() {
        return false;
    }


    default void tickDig() {
        LivingEntity entity = (LivingEntity)this;
        entity.walkAnimation.update(0.8F, 0.4F);

        float radius = entity.getBbWidth() * 0.55F;
        float particleCount = (5 + entity.getRandom().nextInt(5)) * radius;
        for (int i1 = 0; i1 < particleCount; i1++) {
            double motionX = (entity.getRandom().nextFloat() - 0.5F) * 0.7D;
            double motionY = entity.getRandom().nextFloat() * 0.7D + 0.8F;
            double motionZ = (entity.getRandom().nextFloat() - 0.5F) * 0.7D;
            float angle = (0.01745329251F * (entity.yBodyRot + (i1 / particleCount) * 360F));
            double extraX = radius * Mth.sin((float) (Math.PI + angle));
            double extraZ = radius * Mth.cos(angle);
            BlockState groundState = entity.level().getBlockState(entity.blockPosition().below());
            if (groundState.isSolid()) {
                entity.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, groundState), true, entity.getX() + extraX, entity.getY(), entity.getZ() + extraZ, motionX, motionY, motionZ);
            }
        }
    }
}
