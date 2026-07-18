package com.hedge.hedges_bestiary.entity.living;

import com.hedge.hedges_bestiary.blocks.HEBlocks;
import com.hedge.hedges_bestiary.entity.AI.control.FlyingMoveControl;
import com.hedge.hedges_bestiary.entity.AI.goal.*;
import com.hedge.hedges_bestiary.entity.AI.navigation.MMPathNavigatorGround;
import com.hedge.hedges_bestiary.entity.types.EggLayer;
import com.hedge.hedges_bestiary.entity.types.TamableFlyer;
import com.hedge.hedges_bestiary.entity.util.MathHelpers;
import com.hedge.hedges_bestiary.registry.HBEntities;
import com.hedge.hedges_bestiary.util.SmoothAnimationState;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class DawnDoveEntity extends TamableFlyer implements EggLayer {

    public final SmoothAnimationState glideAnimationState = new SmoothAnimationState(0.1f);
    public final SmoothAnimationState flyUpAnimationState = new SmoothAnimationState(0.1f);
    public final SmoothAnimationState flyForwardAnimationState = new SmoothAnimationState(0.1f);
    private float prevTrail;
    private float trail = 0.0f;

    public DawnDoveEntity(EntityType<? extends DawnDoveEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static AttributeSupplier.Builder bakeAttributes(){
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 60.0D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.2D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.4)
                .add(Attributes.FOLLOW_RANGE, 35F)
                .add(Attributes.MOVEMENT_SPEED, 0.15F);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(0, new HBSitWhenOrderedGoal(this, false));
        this.goalSelector.addGoal(2, new FlyerFollowOwnerGoal(this, 1.2D, 1.6D, 8.0f, 5.0f));
        this.goalSelector.addGoal(3, new SemiFlyerFlyingGoal<>(this, 1.0f, 35, 25, 20, 800));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, LivingEntity.class, 10));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(7, new DancingGoal(this));
    }

    @Override
    public void travel(Vec3 vec3d) {


        if (isControlledByLocalInstance() && getControllingPassenger() != null && getControllingPassenger() instanceof Player rider) {
            boolean flag = this.isFlying();
            float speed = (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED);


            if (isControlledByLocalInstance()) {

                if (Minecraft.getInstance().options.keyJump.isDown()) {
                    this.setDeltaMovement(this.getDeltaMovement().add(0, 0.03, 0));
                    if (!flag) {
                        this.setFlying(true);
                    }

                } else if (Minecraft.getInstance().options.keySprint.isDown() && flag) {
                    this.setDeltaMovement(this.getDeltaMovement().add(0, -0.03, 0));
                }


                this.setSpeed(flag ? speed * 7 : speed);
            } else if (rider instanceof Player) {
                setDeltaMovement(Vec3.ZERO);
                return;
            }
        }
        super.travel(vec3d);
    }
    protected void tickRidden(Player pPlayer, Vec3 pTravelVector) {
        super.tickRidden(pPlayer, pTravelVector);
        float turnSpeed = 5.0F;
        float currentYaw = this.getYRot();
        float targetYaw = pPlayer.getYRot();
        float deltaYaw = Mth.wrapDegrees(targetYaw - currentYaw);

        float newYaw = currentYaw + Mth.clamp(deltaYaw, -turnSpeed, turnSpeed);
        this.setYRot(newYaw);
        this.setYHeadRot(pPlayer.getYHeadRot());
        this.setXRot(pPlayer.getXRot() * 0.5f);
        if (this.isFlying()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0, -0.004, 0));
            if (this.onGround()) {
                this.setFlying(false);
            }
        }
    }

    protected Vec3 getRiddenInput(Player pPlayer, Vec3 pTravelVector) {

        //float f = pPlayer.xxa * 0.5F;
        float f1 = pPlayer.zza * 0.5F;
        if (f1 <= 0.0F)
            f1 *= 0.25F;

        return new Vec3(!this.isFlying() ? pPlayer.xxa * 0.5f : 0, 0.0, f1);


    }

    @Override
    public void positionRider(Entity passenger, Entity.MoveFunction moveFunc) {
        /*
        final float radius = 0.5F;
        final float angle = (MathHelpers.STARTING_ANGLE * this.yBodyRot);
        final double extraX = radius * Mth.sin(Mth.PI + angle);
        final double extraZ = radius * Mth.cos(angle);
        final double extraY = 2 + Mth.sin(-this.getFlightPitch(1.0f));

        passenger.setPos(this.getX() + extraX, this.getY() + extraY, this.getZ() + extraZ);

         */
        if (this.isPassengerOfSameVehicle(passenger) && passenger instanceof LivingEntity && !this.touchingUnloadedChunk()) {
            final float angle = (MathHelpers.STARTING_ANGLE * this.yBodyRot);
            float flight = this.getFlyProgress(1.0F);
            //Vec3 seatOffset = new Vec3(0F, 0.0F, 0.2F - 1.5F * flight).xRot((float) Math.toRadians(this.getXRot())).yRot((float) Math.toRadians(-this.yBodyRot));
            double targetY = this.getY() + passenger.getBbHeight() + 0.25F * flight;
            double extraX = 0.5f * Mth.sin(Mth.PI + angle);
            double extraZ = 0.5f * Mth.cos(angle);

            passenger.setYBodyRot(this.yBodyRot);
            passenger.fallDistance = 0.0F;
            moveFunc.accept(passenger, this.getX() + extraX, targetY, this.getZ() + extraZ);
        } else {
            super.positionRider(passenger, moveFunc);
        }

    }



    @Override
    public LivingEntity getControllingPassenger() {
        Entity entity = this.getFirstPassenger();
        if (entity instanceof Player) {
            return (Player) entity;
        } else {
            return null;
        }
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity pPassenger) {
        if (this.isFlying() && pPassenger == this.getFirstPassenger()) {
            this.setFlying(false);
        }
        return super.getDismountLocationForPassenger(pPassenger);
    }

    @Override
    protected boolean canOwnerMount(Player player) {
        return !this.isBaby();
    }

    @Override
    protected boolean canOwnerCommand(Player player) {
        return player.isShiftKeyDown();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.setUpAnimStates();
            this.tickTrailYaw();
        }
    }

    private void tickTrailYaw() {
        this.prevTrail = this.trail;
        this.trail += (-(this.yBodyRot - this.yBodyRotO) - this.trail) * 0.15F;
    }

    public float getTrailYaw(float partialTick) {
        return (this.prevTrail + (this.trail - this.prevTrail) * partialTick);
    }

    @Override
    protected void dive() {

    }

    @Override
    public void setUpAnimStates() {
        boolean flying = this.isFlying();
        Vec3 delta = this.getDeltaMovement();
        this.idleAnimationState.animateWhen(!flying, this.tickCount);
        this.sitAnimationState.animateWhen(this.isSitting() && !this.isVehicle() && !this.isDancing(), this.tickCount);
        this.danceAnimationState.animateWhen(this.isDancing() && !this.isVehicle(), this.tickCount);
        this.flyUpAnimationState.animateWhen(flying && (delta.y >= 0 || delta.horizontalDistanceSqr() < 0.002), this.tickCount);
        this.flyForwardAnimationState.animateWhen(flying && delta.horizontalDistanceSqr() >= 0.002 && flyUpAnimationState.isStarted(), this.tickCount);
        this.glideAnimationState.animateWhen(flying && !flyUpAnimationState.isStarted(), this.tickCount);
    }

    @Override
    public void playIdle() {

    }

    @Override
    protected void switchNav(boolean flying) {
        if (flying) {
            this.moveControl = new FlyingMoveControl(this, 45, 8, 1.6f);
            this.lookControl = new SmoothSwimmingLookControl(this, 30);
            this.navigation = new FlyingPathNavigation(this, this.level());
        } else {
            this.lookControl = new LookControl(this);
            this.moveControl = new MoveControl(this);
            this.navigation = new MMPathNavigatorGround(this, this.level());
        }
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return HBEntities.DAWN_DOVE.get().create(level);
    }

    @Override
    public BlockState getEgg() {
        return HEBlocks.DAWN_DOVE_EGG.get().defaultBlockState();
    }
}
