package com.hedge.hedges_bestiary.entity.projectile;

import com.hedge.hedges_bestiary.client.particle.BansheeScreamParticleOptions;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class BansheeScream extends GenericProjectile{
    private static final EntityDataAccessor<Float> PARTICLEX_ROT = SynchedEntityData.defineId(BansheeScream.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> PARTICLEY_ROT = SynchedEntityData.defineId(BansheeScream.class, EntityDataSerializers.FLOAT);

    public BansheeScream(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setNoGravity(true);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(PARTICLEX_ROT, 0f);
        this.entityData.define(PARTICLEY_ROT, 0f);

    }

    @Override
    public void shootFromRotation(Entity pShooter, float pX, float pY, float pZ, float pVelocity, float pInaccuracy) {
        super.shootFromRotation(pShooter, pX, pY, pZ, pVelocity, pInaccuracy);
        this.setParticleXRot(-pShooter.getXRot());
        this.setParticleYRot(pShooter.getYRot());
    }

    protected void onHitEntity(EntityHitResult hit) {
        super.onHitEntity(hit);
        hit.getEntity().hurt(this.damageSources().mobProjectile(this, (LivingEntity) this.getOwner()), this.getDamage());

    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        if (this.level().getBlockState(blockHitResult.getBlockPos()).is(BlockTags.WOOL)) {
            this.discard();
        }
    }

    @Override
    public float getDamage() {
        return 10;
    }

    @Override
    public float getSpeed() {
        return 8f;
    }

    @Override
    public void trailParticles() {

        Vec3 v = getDeltaMovement();
        double length = v.length();
        int c = (int)Math.min(5, Math.round(length) * 3) + 1;
        float f = (float)length / c / 2;
        for (int i = 0; i < c; i++) {
            //Vec3 rand = EntityHelpers.getRandomVec3(0.02);
            Vec3 p = v.scale(f * i);
            BansheeScreamParticleOptions particle= new BansheeScreamParticleOptions(this.getParticleXRot(), this.getParticleYRot());
            this.level().addParticle(particle, this.getX() + p.x,
                    this.getY() + p.y, this.getZ() + p.z, 0, 0, 0);
        }
    }

    @Override
    public int getLifespan() {
        return 60;
    }

    public float getParticleXRot() {
        return this.entityData.get(PARTICLEX_ROT);
    }

    public float getParticleYRot() {
        return this.entityData.get(PARTICLEY_ROT);
    }

    public void setParticleXRot(float deg) {
        this.entityData.set(PARTICLEX_ROT, deg);
    }

    public void setParticleYRot(float deg) {
        this.entityData.set(PARTICLEY_ROT, deg);
    }
}
