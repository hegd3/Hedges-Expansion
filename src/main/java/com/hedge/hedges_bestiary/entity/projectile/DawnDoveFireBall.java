package com.hedge.hedges_bestiary.entity.projectile;


import com.hedge.hedges_bestiary.entity.util.AttackHelpers;
import com.hedge.hedges_bestiary.entity.util.EntityHelpers;
import com.hedge.hedges_bestiary.registry.HBParticles;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class DawnDoveFireBall extends GenericProjectile {

    public DawnDoveFireBall(EntityType<? extends DawnDoveFireBall> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setNoGravity(true);
    }

    protected void onHitEntity(EntityHitResult hit) {
        super.onHitEntity(hit);
        Entity entity = hit.getEntity();
        if (this.getOwner() != null && this.getOwner().isAlliedTo(entity))  {
            return;
        }
        if (entity.hurt(this.damageSources().mobProjectile(this, (LivingEntity) this.getOwner()), this.getDamage())) {
            this.explode();
            this.discard();
        }

    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        this.explode();
        this.discard();
    }

    @Override
    protected void onMaxAge() {
        this.explode();
        super.onMaxAge();
    }

    private void explode() {
        if (!this.level().isClientSide()) {
            List<LivingEntity> hit = AttackHelpers.projectileZoneHitbox(this.getOwner() != null ? this.getOwner() : this, this.position(), 5, 5, 5, 20);
            for (LivingEntity entity : hit) {
                entity.setRemainingFireTicks(60);
                entity.hurt(this.damageSources().mobProjectile(this, (LivingEntity) this.getOwner()), this.getDamage() - (float)entity.distanceToSqr(this.position()));
            }
            this.level().broadcastEntityEvent(this, (byte)39);
        }
    }

    @Override
    public void handleEntityEvent(byte pId) {
        if (pId == 39) {
            this.level().addParticle(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
        } else {
            super.handleEntityEvent(pId);
        }
    }

    @Override
    public float getDamage() {
        return 15;
    }

    @Override
    public float getSpeed() {
        return 2f;
    }

    @Override
    public void trailParticles() {
        Vec3 v = getDeltaMovement();
        double length = v.length();
        int c = (int)Math.min(20, Math.round(length) * 3) + 1;
        float f = (float)length / c / 2;
        for (int i = 0; i < c; i++) {
            Vec3 rand = EntityHelpers.getRandomVec3(0.02);
            Vec3 p = v.scale(f * i);
            this.level().addParticle(HBParticles.FIREBALL.get(), this.getX() + rand.x + p.x,
                    this.getY() + rand.y + p.y, this.getZ() + rand.z + p.z, rand.x, rand.z, rand.y);
        }
    }

    @Override
    public int getLifespan() {
        return 2000;
    }

    @Override
    protected void defineSynchedData() {

    }
}
