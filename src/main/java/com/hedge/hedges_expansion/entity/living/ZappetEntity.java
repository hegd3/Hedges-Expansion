package com.hedge.hedges_expansion.entity.living;

import com.hedge.hedges_expansion.entity.AI.control.HEFlyingMoveControl;
import com.hedge.hedges_expansion.entity.AI.goal.GroupFollowLeaderGoal;
import com.hedge.hedges_expansion.entity.AI.goal.SemiFlyerCircleWanderGoal;
import com.hedge.hedges_expansion.entity.AI.goal.SemiFlyerFlyingGoal;
import com.hedge.hedges_expansion.entity.AI.navigation.MMPathNavigatorGround;
import com.hedge.hedges_expansion.entity.types.HEAnimStateAnimal;
import com.hedge.hedges_expansion.entity.types.HEGroupMob;
import com.hedge.hedges_expansion.entity.types.HESemiFlyer;
import com.hedge.hedges_expansion.util.SmoothAnimationState;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Stream;

public class ZappetEntity extends HEAnimStateAnimal implements HESemiFlyer, HEGroupMob<ZappetEntity> {

    public static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(ZappetEntity.class, EntityDataSerializers.BOOLEAN);
    public final AnimationState idleAnimationState = new AnimationState();
    public final SmoothAnimationState shootAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState callAnimationState = new SmoothAnimationState();

    @Nullable
    private ZappetEntity leader;
    private int groupSize = 1;

    private int groundTicks = 0;
    private boolean isLanding = false;

    private float flyProgress;
    private float prevFlyProgress;
    private float flightPitch = 0;
    private float prevFlightPitch = 0;
    private float flightRoll = 0;
    private float prevFlightRoll = 0;




    public ZappetEntity(EntityType<? extends ZappetEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static AttributeSupplier.Builder bakeAttributes(){
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.2D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.3)
                .add(Attributes.FOLLOW_RANGE, 35F)
                .add(Attributes.MOVEMENT_SPEED, 0.2F);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(FLYING, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new GroupFollowLeaderGoal<>(this) {
            @Override
            public void tick() {
                super.tick();
                if (this.mob.tickCount % 10 == 0) {
                    if (this.mob.getLeader().isFlying() && !this.mob.isFlying()) {
                        this.mob.setFlying(true);
                        this.mob.isLanding = this.mob.getLeader().isLanding;
                    }
                }
            }

        });
        this.goalSelector.addGoal(3, new SemiFlyerFlyingGoal<>(this, 1.0f, 25, 10, 20, 1600));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, LivingEntity.class, 10));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }

    @Override
    public void tick() {
        super.tick();

        this.prevFlyProgress = this.flyProgress;
        this.prevFlightPitch = this.flightPitch;
        this.prevFlightRoll = this.flightRoll;

        this.tickFlight();
        this.tickRotation((float) this.getDeltaMovement().y * 2 * -(float) (180F / (float) Math.PI));
        if (this.level().isClientSide()) {
            this.setUpAnimStates();
            if (this.getAnimState() > 0) {
                this.animTicks++;
            }
        } else {
            if (this.onGround()) {
                this.groundTicks++;
            } else {
                this.groundTicks = 0;
            }
            if (this.getAnimState() > 0) {
                this.animTicks++;
                switch (this.getAnimState()) {
                    case 1 -> {
                        if (this.animTicks > 15) {
                            this.resetAnimState();
                        }
                    }
                    case 2 -> {
                        if (this.animTicks > 20) {
                            this.resetAnimState();
                        }
                    }
                }
            }
        }
        if (this.hasFollowers() && this.level().random.nextInt(200) == 1) {
            List<? extends ZappetEntity> list = this.level().getEntitiesOfClass(this.getClass(), this.getBoundingBox().inflate(10.0D, 10.0D, 10.0D));
            if (list.size() <= 1) {
                this.groupSize = 1;
            }
        }
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        super.onSyncedDataUpdated(pKey);
        if (pKey == FLYING) {
            switchNav(this.isFlying());
        } else if (pKey == ANIM_STATE) {
            this.animTicks = 0;
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        Vec3 vec3 = this.getDeltaMovement();
        if (!this.isFlying() && !this.onGround() && vec3.y < 0.0D) {
            this.setDeltaMovement(vec3.multiply(1.0D, 0.6D, 1.0D));
        }
    }

    @Override
    protected void updateWalkAnimation(float pPartialTick) {
        float f;
        if (this.isFlying() && this.getDeltaMovement().y < 0 && this.getXRot() > 0) {
            f = 0;
        }
        else if (this.getPose() == Pose.STANDING) {
            f = Math.min(pPartialTick * 6f, 1f);
        } else {
            f = 0;
        }

        this.walkAnimation.update(f, 0.2f);
    }

    @Override
    public void travel(Vec3 pTravelVector) {
        if (this.isEffectiveAi() && this.isFlying()) {
            this.moveRelative(this.getSpeed(), pTravelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
        } else {
            super.travel(pTravelVector);
        }
    }

    @Override
    protected void checkFallDamage(double pY, boolean pOnGround, BlockState pState, BlockPos pPos) {
    }

    @Override
    public void setUpAnimStates() {
        this.idleAnimationState.animateWhen(this.isAlive(), this.tickCount);
        this.shootAnimationState.animateWhen(this.getAnimState() == 1, this.tickCount);
        this.callAnimationState.animateWhen(this.getAnimState() == 2, this.tickCount);
    }

    @Override
    public boolean isFlying() {
        return this.entityData.get(FLYING);
    }

    @Override
    public void setFlying(boolean b) {
        this.entityData.set(FLYING, b);
    }

    @Override
    public boolean isLandNav() {
        return this.navigation instanceof MMPathNavigatorGround;
    }

    @Override
    public boolean isLanding() {
        return this.isLanding;
    }

    @Override
    public void setLanding(boolean b) {
        this.isLanding = b;
    }

    @Override
    public int getGroundTicks() {
        return this.groundTicks;
    }

    public void tickFlight() {
        if (this.isFlying() && flyProgress < 5F) this.flyProgress++;
        if (!this.isFlying() && flyProgress > 0F) this.flyProgress--;

        if (this.isFlying()) {
            this.setNoGravity(true);
            if (groundTicks > 0) this.setFlying(false);
            if (this.isLandNav()) {
                this.switchNav(false);
            }
        } else {
            this.setNoGravity(false);
            if (!this.isLandNav()) {
                this.switchNav(true);
            }
        }

        if (groundTicks > 0) groundTicks--;

        if (!level().isClientSide) {
            if (this.isFlying() && this.isAlive() && !this.isVehicle()) {
                if (this.isLanding) this.setDeltaMovement(this.getDeltaMovement().add(0, -0.1D, 0));
                if (horizontalCollision && !this.isLanding && !this.isInWater()) {
                    this.setDeltaMovement(this.getDeltaMovement().add(0, 0.05D, 0));
                }
            }
            if (this.isFlying() && this.flyProgress > 40 && this.onGround()) this.setFlying(false);
        }
    }

    private void switchNav(boolean flying) {
        if (flying) {
            //his.setDeltaMovement(this.getDeltaMovement().add(0, 0.15, 0));
            this.moveControl = new HEFlyingMoveControl(this, 45, 20, 1.1f);
            this.lookControl = new SmoothSwimmingLookControl(this, 30);
            this.navigation = new FlyingPathNavigation(this, this.level());
        } else {
            this.lookControl = new LookControl(this);
            this.moveControl = new MoveControl(this);
            this.navigation = new MMPathNavigatorGround(this, this.level());
        }
    }

    public void tickRotation(float yMov) {
        this.flightPitch = yMov;
        float threshold = 1F;
        boolean flag = false;
        if (isFlying() && this.yRotO - this.getYRot() > threshold) {
            flightRoll += 10;
            flag = true;
        }
        if (isFlying() && this.yRotO - this.getYRot() < -threshold) {
            flightRoll -= 10;
            flag = true;
        }
        if (!flag) {
            if (flightRoll > 0) this.flightRoll = Math.max(flightRoll - 5, 0);
            if (flightRoll < 0) this.flightRoll = Math.min(flightRoll + 5, 0);
        }
        this.flightRoll = Mth.clamp(flightRoll, -60, 60);
    }

    public float getFlightPitch(float partialTick) {
        return (prevFlightPitch + (flightPitch - prevFlightPitch) * partialTick);
    }

    public float getFlightRoll(float partialTick) {
        return (prevFlightRoll + (flightRoll - prevFlightRoll) * partialTick);
    }

    public float getFlyProgress(float partialTick) {
        return (prevFlyProgress + (flyProgress - prevFlyProgress) * partialTick) * 0.2F;
    }

    @Override
    public ZappetEntity getLeader() {
        return this.leader;
    }

    @Override
    public void setLeader(ZappetEntity leader) {
        this.leader = leader;
    }

    @Override
    public void pathToLeader() {
        if (this.isFollower()) {
            this.getNavigation().moveTo(this.leader, 1.2f);
        }
    }

    @Override
    public boolean inRangeOfLeader() {
        return this.distanceToSqr(this.leader) <= 400.0D;
    }

    @Override
    public int getGroupSize() {
        return this.groupSize;
    }

    @Override
    public int getMaxGroupSize() {
        return 20;
    }

    @Override
    public void addFollower() {
        this.groupSize++;
    }

    @Override
    public void removeFollower() {
        this.groupSize--;
    }

    @Override
    public void addFollowers(Stream<ZappetEntity> pFollowers) {
        pFollowers.limit((long)(this.getMaxGroupSize() - this.groupSize)).filter((p_27538_) -> {
            return p_27538_ != this;
        }).forEach((p_27536_) -> {
            p_27536_.startFollowing(this);
        });
    }
}
