package com.hedge.hedges_bestiary.entity.living;

import com.hedge.hedges_bestiary.blocks.HEBlocks;
import com.hedge.hedges_bestiary.entity.AI.control.FlyingMoveControl;
import com.hedge.hedges_bestiary.entity.AI.goal.*;
import com.hedge.hedges_bestiary.entity.AI.navigation.MMPathNavigatorGround;
import com.hedge.hedges_bestiary.entity.AI.targeting.HBHurtByTargetGoal;
import com.hedge.hedges_bestiary.entity.types.*;
import com.hedge.hedges_bestiary.entity.util.EntityHelpers;
import com.hedge.hedges_bestiary.registry.HBEntities;
import com.hedge.hedges_bestiary.registry.HBParticles;
import com.hedge.hedges_bestiary.util.SmoothAnimationState;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
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
import java.util.stream.Stream;

public class ZappetEntity extends TamableFlyer implements HBGroupMob<ZappetEntity>, EggLayer {

    public static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(ZappetEntity.class, EntityDataSerializers.BOOLEAN);
    public final AnimationState idleAnimationState = new AnimationState();
    public final SmoothAnimationState shootAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState callAnimationState = new SmoothAnimationState();

    @Nullable
    private ZappetEntity leader;
    private int groupSize = 1;

    private float prevGlowProgress = 0.5f;
    private float glowProgress = 0.5f;
    private boolean pulse = false;





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
    protected boolean canOwnerMount(Player player) {
        return false;
    }

    @Override
    protected boolean canOwnerCommand(Player player) {
        return true;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(FLYING, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new HBSitWhenOrderedGoal(this, false));
        this.goalSelector.addGoal(2, new FlyerFollowOwnerGoal(this, 1.2D, 1.6D, 8.0f, 5.0f));
        this.goalSelector.addGoal(3, new FlockingGoal<>(this) {
            @Override
            public boolean canUse() {
                return !this.mob.isTame() &&  super.canUse();
            }

            @Override
            public boolean canContinueToUse() {
                return !this.mob.isTame() && super.canContinueToUse();
            }
        });
        this.goalSelector.addGoal(4, new SemiFlyerFlyingGoal<>(this, 1.0f, 25, 10, 20, 1600));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, LivingEntity.class, 10));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(8, new IdleAnimationGoal<>(this));
        this.goalSelector.addGoal(9, new DancingGoal(this));

        this.targetSelector.addGoal(2, new HBHurtByTargetGoal(this, true, TamableAnimal.class));
    }

    @Override
    public boolean isAlliedTo(Entity pEntity) {
        if (pEntity instanceof ZappetEntity zappet && zappet.getOwnerUUID() == this.getOwnerUUID()) {
            return true;
        }
        return super.isAlliedTo(pEntity);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            this.setUpAnimStates();
            this.tickGlow();
        } else {
            if (this.getAnimState() > 0) {
                this.animTicks++;
                switch (this.getAnimState()) {
                    case 1 -> {
                        if (this.animTicks > 20) {
                            this.resetAnimState();
                        }
                    }
                    case 2 -> {
                        if (this.animTicks > 15) {
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

    private void tickGlow() {
        this.prevGlowProgress = this.glowProgress;
        if (this.getAnimState() > 0) {
            if (!this.pulse) {

                if (this.glowProgress < 5.0F) {
                    Vec3 rand = EntityHelpers.getRandomVec3(0.6);
                    this.level().addParticle(HBParticles.ELECTRIC_SPARKS.get(), this.getX() + rand.x + rand.x,
                            this.getY() + rand.y + 0.5, this.getZ() + rand.z, rand.x, rand.y + 0.2, rand.z);
                    this.glowProgress += 0.5f;
                } else {
                    this.pulse = true;
                }
            } else {

                if (this.glowProgress > 1.0F) {
                    this.glowProgress -= 0.5f;
                }
                this.pulse = false;
            }
        } else {
            if (this.glowProgress > 0.5F) {
                this.glowProgress -= 0.5f;
            } else if (this.pulse) {
                this.pulse = false;
            }
        }
    }

    public float getGlowProgress(float partialTicks) {
        return (prevGlowProgress + (glowProgress - prevGlowProgress) * partialTicks) * 0.2F;
    }


    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        super.onSyncedDataUpdated(pKey);
        if (pKey == ANIM_STATE) {
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
    protected void dive() {

    }

    @Override
    protected void checkFallDamage(double pY, boolean pOnGround, BlockState pState, BlockPos pPos) {
    }

    @Override
    public void setUpAnimStates() {
        this.idleAnimationState.animateWhen(this.isAlive(), this.tickCount);
        this.sitAnimationState.animateWhen(this.isSitting() && !this.isDancing(), this.tickCount);
        this.danceAnimationState.animateWhen(this.isDancing(), this.tickCount);
        this.callAnimationState.animateWhen(this.getAnimState() == 1, this.tickCount);
        this.shootAnimationState.animateWhen(this.getAnimState() == 2, this.tickCount);
    }

    @Override
    public void switchNav(boolean flying) {
        if (flying) {
            this.moveControl = new FlyingMoveControl(this, 45, 20, 1.1f);
            this.lookControl = new SmoothSwimmingLookControl(this, 30);
            this.navigation = new FlyingPathNavigation(this, this.level());
        } else {
            this.lookControl = new LookControl(this);
            this.moveControl = new MoveControl(this);
            this.navigation = new MMPathNavigatorGround(this, this.level());
        }
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
    public boolean canBeFollowed() {
        if (this.getOwnerUUID() != null || this.isBaby()) {
            return false;
        }
        return HBGroupMob.super.canBeFollowed();
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
        pFollowers.limit((long)(this.getMaxGroupSize() - this.groupSize)).filter((zappet) -> {
            return zappet != this;
        }).forEach((zappet) -> {
            zappet.startFollowing(this);
        });
    }

    @Override
    public void playIdle() {
        this.setAnimState(1);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return HBEntities.ZAPPET.get().create(level);
    }

    @Override
    public BlockState getEgg() {
        return HEBlocks.ZAPPET_EGG.get().defaultBlockState();
    }
}
