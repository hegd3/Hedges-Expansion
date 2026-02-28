package com.hedge.hedges_expansion.entity.projectile;

import com.hedge.hedges_expansion.entity.util.AttackHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class WaveEntity extends Entity {

    private static final EntityDataAccessor<Float> Y_ROT = SynchedEntityData.defineId(WaveEntity.class, EntityDataSerializers.FLOAT);

    public final AnimationState travelAnimationState = new AnimationState();
    @Nullable
    private LivingEntity owner;
    @Nullable
    private UUID ownerUUID;

    private static final int maxLife = 100;
    private int lifeTicks = 0;
    private int lSteps;
    private double lx;
    private double ly;
    private double lz;
    private double lyr;
    private double lxr;
    private double lxd;
    private double lyd;
    private double lzd;


    public WaveEntity(EntityType<? extends WaveEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public void setOwner(@Nullable LivingEntity living) {
        this.owner = living;
        this.ownerUUID = living == null ? null : living.getUUID();
    }

    @Nullable
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUUID != null && this.level() instanceof ServerLevel) {
            Entity entity = ((ServerLevel) this.level()).getEntity(this.ownerUUID);
            if (entity instanceof LivingEntity) {
                this.owner = (LivingEntity) entity;
            }
        }

        return this.owner;
    }

    @Override
    public boolean isAlliedTo(Entity pEntity) {
        if (pEntity == this.owner) {
            return true;
        }
        else if (this.owner != null) {
            return owner.isAlliedTo(pEntity);
        }
        return super.isAlliedTo(pEntity);
    }

    public void shoot(LivingEntity pOwner, double xPos, double yPos, double zPos, Vec3 vect, int num, int angle) {
        this.setOwner(pOwner);
        this.setPos(xPos, yPos, zPos);
        this.setYRot(-(float) (Mth.atan2(vect.x, vect.z) * (double) (180F / (float) Math.PI)) + (num * angle));
        this.lifeTicks = 0;
    }

    public void shoot(LivingEntity pOwner, float angle) {
        this.setOwner(pOwner);
        this.moveTo(pOwner.position());
        this.setYRot(angle);
        this.lifeTicks = 0;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return (Packet<ClientGamePacketListener>) NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        super.tick();
        //this.prevGrowProgress = this.growProgress;
        //this.growProgress += 0.005F;

        if (!this.isNoGravity()) {
            {
                if (this.isInFluidType()) {
                    this.addDeltaMovement(new Vec3(0.0D, (double) 0.04F, 0.0D));
                } else if (!this.onGround()) {
                    this.addDeltaMovement(new Vec3(0.0D, (double) -0.04F, 0.0D));
                }
            }
        }
        if (this.level().isClientSide) {
            this.travelAnimationState.animateWhen(this.tickCount >= 0, this.tickCount);
            if (this.lSteps > 0) {
                double d5 = this.getX() + (this.lx - this.getX()) / (double) this.lSteps;
                double d6 = this.getY() + (this.ly - this.getY()) / (double) this.lSteps;
                double d7 = this.getZ() + (this.lz - this.getZ()) / (double) this.lSteps;

                this.setYRot(Mth.wrapDegrees((float) this.lyr));
                this.setXRot(this.getXRot() + (float) (this.lxr - (double) this.getXRot()) / (float) this.lSteps);
                --this.lSteps;
                this.setPos(d5, d6, d7);
            }
        }

        else {

            this.reapplyPosition();
            this.setRot(this.getYRot(), this.getXRot());

            if (this.owner == null && ownerUUID != null) {
                Entity possibleOwner = ((ServerLevel) this.level()).getEntity(ownerUUID);
                if (possibleOwner instanceof LivingEntity entity)
                    this.owner = entity;
            }

            float f = Math.min((maxLife - this.lifeTicks) / 100F, 0.7F);
            Vec3 directionVec = new Vec3(0, 0, f * f * 0.2F).yRot((float) Math.toRadians(-this.getYRot()));
            Vec3 vec3 = this.getDeltaMovement().scale(0.9F).add(directionVec);
            this.move(MoverType.SELF, vec3);
            this.setDeltaMovement(vec3.multiply((double) 0.99F, (double) 0.98F, (double) 0.99F));
            this.aoeAttack();
            if (this.lifeTicks >= maxLife) {
                this.discard();
                }
            }
            this.lifeTicks++;
    }

    public float getYRot() {
        return this.entityData.get(Y_ROT);
    }

    public void setYRot(float f) {
        this.entityData.set(Y_ROT, f);
    }

    private void aoeAttack() {
        AABB hitZone = this.getBoundingBox().inflate(0.5f, 0.5f, 0.5f);
        for (LivingEntity entity : this.level().getEntitiesOfClass(LivingEntity.class, hitZone)) {
            this.doHurt(entity);
        }
    }

    protected boolean doHurt(LivingEntity target) {
        if (target.hurt(target.damageSources().mobProjectile(this, this.owner), 6.0f)) {
            target.knockback(0.1D + 0.5D * 2, Mth.sin(this.getYRot() * Mth.DEG_TO_RAD), -Mth.cos(this.getYRot() * Mth.DEG_TO_RAD));
            return true;
        }
        return false;
    }

    @Override
    public void lerpTo(double x, double y, double z, float yr, float xr, int steps, boolean b) {
        this.lx = x;
        this.ly = y;
        this.lz = z;
        this.lyr = yr;
        this.lxr = xr;
        this.lSteps = steps;
        this.setDeltaMovement(this.lxd, this.lyd, this.lzd);
    }

    @Override
    public void lerpMotion(double lerpX, double lerpY, double lerpZ) {
        this.lxd = lerpX;
        this.lyd = lerpY;
        this.lzd = lerpZ;
        this.setDeltaMovement(this.lxd, this.lyd, this.lzd);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {

    }

    @Override
    protected void playSwimSound(float pVolume) {
    }

    public float getStepHeight() {
        return 2F;
    }

    @Override
    public boolean canCollideWith(Entity other) {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void doWaterSplashEffect() {
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(Y_ROT, 0F);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {

        if (compound.hasUUID("OwnerUUID"))
            this.ownerUUID = compound.getUUID("OwnerUUID");
        this.lifeTicks = compound.getInt("liveticks");

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        if (this.ownerUUID != null) {
            compoundTag.putUUID("Owner", this.ownerUUID);
        }
        compoundTag.putInt("liveticks", this.lifeTicks);

    }
}
