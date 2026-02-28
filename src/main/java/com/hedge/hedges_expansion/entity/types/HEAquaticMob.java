package com.hedge.hedges_expansion.entity.types;

import com.hedge.hedges_expansion.entity.util.AnimStateMob;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class HEAquaticMob extends WaterAnimal implements AnimStateMob {

    private static final EntityDataAccessor<Integer> ANIM_STATE = SynchedEntityData.defineId(HEAquaticMob.class, EntityDataSerializers.INT);

    public final AnimationState abilityAnimationState = new AnimationState();
    public final AnimationState idleAnimationState = new AnimationState();
    protected int animTicks;

    public HEAquaticMob(EntityType<? extends HEAquaticMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ANIM_STATE, 0);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.clientTick();
        } else {
            this.serverTick();
        }
    }

    protected void clientTick() {
        setUpAnimStates();
    }


    protected void flop() {
        if (!this.isInWater() && this.onGround() && this.verticalCollision) {
            this.setDeltaMovement(this.getDeltaMovement().add((double)((this.random.nextFloat() * 2.0F - 1.0F) * 0.05F), (double)0.4F, (double)((this.random.nextFloat() * 2.0F - 1.0F) * 0.05F)));
            this.setOnGround(false);
            this.hasImpulse = true;
            this.playSound(this.getFlopSound(), this.getSoundVolume(), this.getVoicePitch());
        }
    }

    protected SoundEvent getFlopSound() {
        return SoundEvents.TROPICAL_FISH_FLOP;
    }


    protected void serverTick() {

    }

    @Override
    protected void updateWalkAnimation(float pPartialTick) {
        float f;
        if (this.getPose() == Pose.STANDING) {
            f = Math.min(pPartialTick * 6f, 1f);
        } else {
            f = 0;
        }

        this.walkAnimation.update(f, 0.2f);
    }

    @Override
    protected SoundEvent getSwimSound() {
        return SoundEvents.DOLPHIN_SWIM;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {

    }


    public void travel(Vec3 pTravelVector) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), pTravelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
        } else {
            super.travel(pTravelVector);
        }

    }

    @Override
    public void onAboveBubbleCol(boolean pDownwards) {

    }

    @Override
    public void onInsideBubbleColumn(boolean pDownwards) {
    }

    @Override
    public void setAnimState(int i) {
        this.entityData.set(ANIM_STATE, i);
    }

    @Override
    public int getAnimState() {
        return this.entityData.get(ANIM_STATE);
    }

    @Override
    public int getAnimTicks() {
        return this.animTicks;
    }

    @Override
    public void setUpAnimStates() {
        this.idleAnimationState.animateWhen(this.isAlive(), this.tickCount);

        this.abilityAnimationState.animateWhen(this.getAnimState() > 0, this.tickCount);
    }

    public void resetAnimState() {
        this.animTicks = 0;
        this.setAnimState(0);
    }
}
