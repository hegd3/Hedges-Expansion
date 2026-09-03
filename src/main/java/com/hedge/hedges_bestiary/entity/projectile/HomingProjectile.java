package com.hedge.hedges_bestiary.entity.projectile;


import com.hedge.hedges_bestiary.util.WorldHelpers;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;


public abstract class HomingProjectile extends GenericProjectile {

    protected LivingEntity target;
    protected HomingProjectile(EntityType<? extends GenericProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.target != null && !this.target.isAlive()) {
            this.target = null;
        }
    }

    public void travel() {
        setPos(position().add(getDeltaMovement()));
        Vec3 motion = this.getDeltaMovement();
        float xRot;
        float yRot;
        if (this.target != null) {
            Vec3 v = target.position().subtract(this.position()).normalize().scale(3);
            motion = motion.lerp(v, this.getLerpSpeed());
            this.setDeltaMovement(motion);
        }
        xRot = -((float) (Mth.atan2(motion.horizontalDistance(), motion.y) * (double) (180F / (float) Math.PI)) - 90.0F);
        yRot = -((float) (Mth.atan2(motion.z, motion.x) * (double) (180F / (float) Math.PI)) + 90.0F);

        this.setXRot(Mth.wrapDegrees(xRot));
        this.setYRot(Mth.wrapDegrees(yRot));
    }

    protected float getLerpSpeed() {
        return 0.08F;
    }

    public void setTarget(LivingEntity target) {
        this.target = target;
    }

}
