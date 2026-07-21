package com.hedge.hedges_bestiary.entity.living;

import com.hedge.hedges_bestiary.HedgesBestiary;
import com.hedge.hedges_bestiary.blocks.HEBlocks;
import com.hedge.hedges_bestiary.entity.AI.control.FlyingMoveControl;
import com.hedge.hedges_bestiary.entity.AI.goal.*;
import com.hedge.hedges_bestiary.entity.AI.navigation.MMPathNavigatorGround;
import com.hedge.hedges_bestiary.entity.projectile.DawnDoveFireBall;
import com.hedge.hedges_bestiary.entity.types.AttackStateMob;
import com.hedge.hedges_bestiary.entity.types.EggLayer;
import com.hedge.hedges_bestiary.entity.types.KeybindUsingMount;
import com.hedge.hedges_bestiary.entity.types.TamableFlyer;
import com.hedge.hedges_bestiary.entity.util.AttackHelpers;
import com.hedge.hedges_bestiary.entity.util.MathHelpers;
import com.hedge.hedges_bestiary.message.MountedEntityKeyMessage;
import com.hedge.hedges_bestiary.registry.HBEntities;
import com.hedge.hedges_bestiary.registry.HBKeyMappings;
import com.hedge.hedges_bestiary.util.SmoothAnimationState;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
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

import java.util.List;

public class DawnDoveEntity extends TamableFlyer implements EggLayer, AttackStateMob, KeybindUsingMount {

    public final SmoothAnimationState glideAnimationState = new SmoothAnimationState(0.1f);
    public final SmoothAnimationState flyUpAnimationState = new SmoothAnimationState(0.1f);
    public final SmoothAnimationState flyForwardAnimationState = new SmoothAnimationState(0.1f);

    public final AnimationState biteAnimationState = new AnimationState();
    public final AnimationState shootAnimationState = new AnimationState();

    private float prevTrail;
    private float trail = 0.0f;

    private int attackCD;
    private int shootCD;

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
        this.goalSelector.addGoal(3, new RandomlySitGoal(this));
        this.goalSelector.addGoal(4, new SemiFlyerFlyingGoal<>(this, 1.0f, 45, 25, 20, 800));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, LivingEntity.class, 10));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(8, new DancingGoal(this));

        this.targetSelector.addGoal(2, new HBHurtByTargetGoal(this));
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

                if (HBKeyMappings.MOUNT_ABILITY_KEY.isDown()) {
                    HedgesBestiary.sendMSGToServer(new MountedEntityKeyMessage(this.getId(), rider.getId(), 2));
                } else if (Minecraft.getInstance().options.keyAttack.isDown()) {
                    HedgesBestiary.sendMSGToServer(new MountedEntityKeyMessage(this.getId(), rider.getId(), 1));
                }


                this.setSpeed(flag ? speed * 7 : speed);
            } else if (rider instanceof Player) {
                setDeltaMovement(Vec3.ZERO);
                return;
            }
        }
        super.travel(vec3d);
    }

    @Override
    protected void tickRidden(Player pPlayer, Vec3 pTravelVector) {
        super.tickRidden(pPlayer, pTravelVector);
        float turnSpeed = 5.0F;
        float currentYaw = this.getYRot();
        float targetYaw = pPlayer.getYRot();
        float deltaYaw = Mth.wrapDegrees(targetYaw - currentYaw);

        float newYaw = currentYaw + Mth.clamp(deltaYaw, -turnSpeed, turnSpeed);
        this.setYRot(newYaw);
        this.setYHeadRot(pPlayer.getYHeadRot());
        this.setXRot(Mth.clamp(pPlayer.getXRot(), -45, 45));
        if (this.isFlying()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0, -0.004, 0));
            if (this.onGround()) {
                this.setFlying(false);
            }
        }
    }

    @Override
    public void push(Entity pEntity) {
        if (this.hasControllingPassenger() && pEntity.getVehicle() == null) {
            pEntity.startRiding(this);
        }
        super.push(pEntity);
    }

    protected Vec3 getRiddenInput(Player pPlayer, Vec3 pTravelVector) {

        float f1 = pPlayer.zza * 0.5F;
        if (f1 <= 0.0F)
            f1 *= 0.25F;

        return new Vec3(!this.isFlying() ? pPlayer.xxa * 0.5f : 0, 0.0, f1);


    }

    @Override
    public void positionRider(Entity passenger, Entity.MoveFunction moveFunc) {
        if (this.isPassengerOfSameVehicle(passenger) && passenger instanceof LivingEntity && !this.touchingUnloadedChunk()) {
            final float angle = (MathHelpers.STARTING_ANGLE * this.yBodyRot);
            float flight = this.getFlyProgress(1.0F);
            double targetY = this.getY() + passenger.getBbHeight() + 0.25F * flight;
            double extraX = 0;
            double extraZ = 0;
            if (this.getPassengers().size() > 1) {
                int i = this.getPassengers().indexOf(passenger);
                if (i == 0) {
                    extraX = 0.5f * Mth.sin(Mth.PI + angle);
                    extraZ = 0.5f * Mth.cos(angle);
                } else {
                    extraX = -0.5f * Mth.sin(Mth.PI + angle);
                    extraZ = -0.5f * Mth.cos(angle);
                }
            } else {
                extraX = 0.5f * Mth.sin(Mth.PI + angle);
                extraZ = 0.5f * Mth.cos(angle);
            }


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
    protected boolean canAddPassenger(Entity pPassenger) {
        return this.getPassengers().size() < 2;
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
        } else {
            this.attackCD = Math.max(this.attackCD - 1, 0);
            this.shootCD = Math.max(this.shootCD - 1, 0);
            if (this.getAnimState() > 0) {
                this.animTicks++;
                switch(this.getAnimState()) {
                    case 1 -> {
                        if (animTicks == 8) {
                            List<LivingEntity> hit = AttackHelpers.zoneHitbox(this, this.getLookAngle().scale(1.2), 2, 2, 2, 2);
                            for (LivingEntity e : hit) {
                                this.doHurtTarget(e);
                            }
                        } else if (animTicks > 15) {
                            this.resetAnimState();
                        }
                    } case 2 -> {
                        if (animTicks == 13) {
                            DawnDoveFireBall fireball = HBEntities.DAWN_DOVE_FIREBALL.get().create(this.level());
                            fireball.setOwner(this);
                            fireball.moveTo(this.getEyePosition().add(this.getLookAngle().scale(1.2)));
                            fireball.shootFromRotation(this, Mth.clamp(this.getXRot(), -45, 45), this.getYRot(), 0.0f, 3, 0);
                            this.level().addFreshEntity(fireball);
                        } else if (animTicks > 23) {
                            this.resetAnimState();
                            this.shootCD = 100;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void resetAnimState() {
        super.resetAnimState();
        this.attackCD +=5;
        this.shootCD +=5;
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
        this.biteAnimationState.animateWhen(this.getAnimState() == 1, this.tickCount);
        this.shootAnimationState.animateWhen(this.getAnimState() == 2, this.tickCount);
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

    @Override
    public void setAttacking() {
        this.setAnimState(1);
    }

    @Override
    public boolean canUseAttack(LivingEntity entity, double attackReach, double dist) {
        return this.attackCD == 0 && attackReach >= dist;
    }

    @Override
    public double getAttackReachSqr(LivingEntity entity) {
        return this.getBbWidth() * 2.2 * this.getBbWidth() * 2.2 + entity.getBbWidth();
    }

    @Override
    public void onKeyPacket(Entity keyPresser, int type) {
        if (type == 1 && this.attackCD == 0) {
            this.setAnimState(1);
        } else if (type == 2 && this.shootCD == 0) {
            this.setAnimState(2);
        }
    }
}
