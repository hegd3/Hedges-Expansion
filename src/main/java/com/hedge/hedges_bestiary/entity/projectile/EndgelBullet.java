package com.hedge.hedges_bestiary.entity.projectile;

import com.hedge.hedges_bestiary.entity.util.AttackHelpers;
import com.hedge.hedges_bestiary.registry.HBParticles;
import com.hedge.hedges_bestiary.util.WorldHelpers;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EndgelBullet extends GenericProjectile{

    private LivingEntity target;

    public EndgelBullet(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setNoGravity(true);
    }

    @Override
    protected void defineSynchedData() {

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
            motion = WorldHelpers.lerpVec3(0.08F, motion, v);
            this.setDeltaMovement(motion);
        }
        xRot = -((float) (Mth.atan2(motion.horizontalDistance(), motion.y) * (double) (180F / (float) Math.PI)) - 90.0F);
        yRot = -((float) (Mth.atan2(motion.z, motion.x) * (double) (180F / (float) Math.PI)) + 90.0F);

        this.setXRot(Mth.wrapDegrees(xRot));
        this.setYRot(Mth.wrapDegrees(yRot));
    }
    protected void onHitEntity(EntityHitResult hit) {
        super.onHitEntity(hit);
        hit.getEntity().hurt(this.damageSources().mobProjectile(this, (LivingEntity) this.getOwner()), this.getDamage());
        this.explode();
        this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        this.explode();
        this.discard();
    }

    private void explode() {
        if (!this.level().isClientSide) {
            List<LivingEntity> hit = AttackHelpers.projectileZoneHitbox(this, this.position(), 2, 2, 2, 10);
            for (LivingEntity entity : hit) {
                entity.hurt(this.damageSources().mobProjectile(this, (LivingEntity) this.getOwner()), this.getDamage() - (float)entity.distanceToSqr(this.position()));
            }
            this.playSound(SoundEvents.GENERIC_EXPLODE);
            this.level().broadcastEntityEvent(this, (byte) 39);
        }
    }

    @Override
    protected void onMaxAge() {
        this.explode();
        super.onMaxAge();
    }

    @Override
    public float getDamage() {
        return 10;
    }

    @Override
    public float getSpeed() {
        return 1f;
    }

    @Override
    public void trailParticles() {

        Vec3 v = getDeltaMovement();
        double length = v.length();
        int c = (int)Math.min(5, Math.round(length) * 3) + 1;
        float f = (float)length / c / 2;
        for (int i = 0; i < c; i++) {
            Vec3 p = v.scale(f * i);
            this.level().addParticle(HBParticles.ENDGEL_BULLET.get(), true, this.getX() + p.x,
                    this.getY() + p.y, this.getZ() + p.z, 0, 0, 0);
        }
    }

    @Override
    public int getLifespan() {
        return 30;
    }

    public void setTarget(LivingEntity target) {
        this.target = target;
    }

    @Override
    public void handleEntityEvent(byte pId) {
        if (pId == 39) {
            this.level().addParticle(HBParticles.ENDGEL_EXPLODE.get(), true, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
        } else {
            super.handleEntityEvent(pId);
        }
    }
}
