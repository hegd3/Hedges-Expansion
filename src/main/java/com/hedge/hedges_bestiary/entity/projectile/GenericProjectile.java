package com.hedge.hedges_bestiary.entity.projectile;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.ProjectileImpactEvent;

import java.util.Objects;

public abstract class GenericProjectile extends Projectile {

    Vec3 deltaMovementOld = Vec3.ZERO;
    private float damage;

    protected GenericProjectile(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public boolean isAlliedTo(Entity pEntity) {
        if (this.getOwner() != null) {
            if (pEntity == this.getOwner()) return true;
            return this.getOwner().isAlliedTo(pEntity);
        }
        return super.isAlliedTo(pEntity);
    }

    @Override
    public void shoot(double pX, double pY, double pZ, float pVelocity, float pInaccuracy) {
        Vec3 vec3 = (new Vec3(pX, pY, pZ)).normalize().add(this.random.triangle(0.0D, 0.0172275D * (double)pInaccuracy), this.random.triangle(0.0D, 0.0172275D * (double)pInaccuracy), this.random.triangle(0.0D, 0.0172275D * (double)pInaccuracy)).scale((double)pVelocity);
        this.setDeltaMovement(vec3);
        double d0 = vec3.horizontalDistance();
        this.setYRot((float)(Mth.atan2(vec3.x, vec3.z) * (double)(180F / (float)Math.PI)));
        this.setXRot((float)(Mth.atan2(vec3.y, d0) * (double)(180F / (float)Math.PI)) * 0.45f );
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }

    @Override
    public void shootFromRotation(Entity pShooter, float pX, float pY, float pZ, float pVelocity, float pInaccuracy) {
        this.setOwner(pShooter);
        float f = -Mth.sin(pY * ((float)Math.PI / 180F)) * Mth.cos(pX * ((float)Math.PI / 180F));
        float f1 = -Mth.sin((pX + pZ) * ((float)Math.PI / 180F));
        float f2 = Mth.cos(pY * ((float)Math.PI / 180F)) * Mth.cos(pX * ((float)Math.PI / 180F));
        this.shoot(f, f1, f2, pVelocity, pInaccuracy);
        Vec3 vec3 = pShooter.getDeltaMovement();
        this.setDeltaMovement(this.getDeltaMovement().add(vec3.x, pShooter.onGround() ? 0.0D : vec3.y, vec3.z));
    }

    @Override
    protected boolean canHitEntity(Entity target) {
        var owner = getOwner();
        return super.canHitEntity(target) && target != owner && (owner == null || (!owner.isAlliedTo(target)));
    }

    @Override
    public void checkDespawn() {
        if (this.level() instanceof ServerLevel serverLevel && !serverLevel.getChunkSource().chunkMap.getDistanceManager().inEntityTickingRange(this.chunkPosition().toLong())) {
            this.discard();
        }
    }

    @Override
    public void tick() {

        super.tick();
        if (tickCount == 1) {
            deltaMovementOld = getDeltaMovement();
        }
        if (tickCount > this.getLifespan()) {
            this.onMaxAge();
            return;
        }
        if (this.level().isClientSide) {
            trailParticles();
        }
        handleHitDetection();
        travel();
        deltaMovementOld = getDeltaMovement();
        rotateWithMotion();
    }

    protected void rotateWithMotion() {
        var motion = getDeltaMovement();
        double speed = motion.horizontalDistance();
        this.setYRot((float) Mth.atan2(motion.x, motion.z) * Mth.DEG_TO_RAD);
        this.setXRot((float) Mth.atan2(motion.y, speed) * Mth.DEG_TO_RAD);
        if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
        } else {
            this.xRotO = enforceRotationContinuity(this.xRotO, this.getXRot());
            this.yRotO = enforceRotationContinuity(this.yRotO, this.getYRot());
        }
    }

    protected static float enforceRotationContinuity(float currentRot, float targetRot) {
        while (targetRot - currentRot < -180.0F) {
            currentRot -= 360.0F;
        }

        while (targetRot - currentRot >= -180.0F) {
            currentRot += 360.0F;
        }

        return currentRot;
    }

    public void handleHitDetection() {
        HitResult result = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (result instanceof EntityHitResult entityHitResult) {
            result = new EntityHitResult(entityHitResult.getEntity(), entityHitResult.getEntity().getBoundingBox()
                    .clip(this.position(), this.position().add(this.getDeltaMovement())).orElse(this.position()));
        }
        if (result.getType() != HitResult.Type.MISS && !MinecraftForge.EVENT_BUS.post(new ProjectileImpactEvent(this, result))) {
            onHit(result);
        }
    }

    public void travel() {
        setPos(position().add(getDeltaMovement()));
        Vec3 motion = this.getDeltaMovement();
        float xRot = -((float) (Mth.atan2(motion.horizontalDistance(), motion.y) * (double) (180F / (float) Math.PI)) - 90.0F);
        float yRot = -((float) (Mth.atan2(motion.z, motion.x) * (double) (180F / (float) Math.PI)) + 90.0F);
        this.setXRot(Mth.wrapDegrees(xRot));
        this.setYRot(Mth.wrapDegrees(yRot));
        if (!this.isNoGravity()) {
            Vec3 vec34 = this.getDeltaMovement();
            this.setDeltaMovement(vec34.x, vec34.y - getDefaultGravity(), vec34.z);
        }
    }

    @Override
    public boolean shouldBeSaved() {
        return super.shouldBeSaved() && !Objects.equals(getRemovalReason(), RemovalReason.UNLOADED_TO_CHUNK);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("damage", this.getDamage());
        tag.putInt("age", tickCount);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.damage = tag.getFloat("damage");
        this.tickCount = tag.getInt("age");
    }

    protected void onMaxAge() {
        this.discard();
    }



    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getDamage() {
        return damage;
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    protected double getDefaultGravity() {
        return 0.05;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        return false;
    }

    public int getLifespan() {
        return 300;
    }


    public abstract float getSpeed();

    public abstract void trailParticles();

}
