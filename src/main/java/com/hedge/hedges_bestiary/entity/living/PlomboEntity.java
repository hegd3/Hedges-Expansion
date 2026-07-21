package com.hedge.hedges_bestiary.entity.living;

import com.hedge.hedges_bestiary.entity.AI.control.ATMBodyRotControl;
import com.hedge.hedges_bestiary.entity.AI.control.ATMLookControl;
import com.hedge.hedges_bestiary.entity.AI.control.ATMMoveControl;
import com.hedge.hedges_bestiary.entity.AI.goal.*;
import com.hedge.hedges_bestiary.entity.AI.goal.specific.PlomboAttackGoal;
import com.hedge.hedges_bestiary.entity.AI.navigation.MMPathNavigatorGround;
import com.hedge.hedges_bestiary.entity.types.HBTamableAnimal;
import com.hedge.hedges_bestiary.entity.types.AdvancedTurningMob;
import com.hedge.hedges_bestiary.entity.types.AttackStateMob;
import com.hedge.hedges_bestiary.entity.util.AttackHelpers;
import com.hedge.hedges_bestiary.entity.util.EntityHelpers;
import com.hedge.hedges_bestiary.registry.HBEntities;
import com.hedge.hedges_bestiary.util.SmoothAnimationState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

public class PlomboEntity extends HBTamableAnimal implements AttackStateMob, AdvancedTurningMob {

    private static final EntityDataAccessor<Boolean> LEFT = SynchedEntityData.defineId(PlomboEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SCRATCHING = SynchedEntityData.defineId(PlomboEntity.class, EntityDataSerializers.BOOLEAN);


    public final AnimationState biteAnimationState = new AnimationState();
    public final AnimationState multiAttackAnimationState = new AnimationState();
    public final SmoothAnimationState scratchAnimationState = new SmoothAnimationState(0.2f);

    public final SmoothAnimationState sniffAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState yawnAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState earflickAnimationState = new SmoothAnimationState();

    private int attackCD = 0;
    private int multiAttackCD = 0;


    public PlomboEntity(EntityType<? extends PlomboEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.lookControl = new ATMLookControl<>(this);
        this.moveControl = new ATMMoveControl<>(this);
        this.setMaxUpStep(1.0f);
    }

    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        return new MMPathNavigatorGround(this, pLevel);
    }

    public static AttributeSupplier.Builder bakeAttributes(){
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 80.0D)
                .add(Attributes.ATTACK_DAMAGE, 7.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.8D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.89)
                .add(Attributes.FOLLOW_RANGE, 35F)
                .add(Attributes.MOVEMENT_SPEED, 0.22F);
    }

    @Override
    protected boolean canOwnerMount(Player player) {
        return false;
    }

    @Override
    protected boolean canOwnerCommand(Player player) {
        return player.isShiftKeyDown();
    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(LEFT, false);
        this.entityData.define(SCRATCHING, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new HBSitWhenOrderedGoal(this));
        this.goalSelector.addGoal(2, new AvoidTargetWhenLowGoal(this, 1.3f, 20, 30, 20, 3));
        this.goalSelector.addGoal(3, new PlomboAttackGoal(this));
        this.goalSelector.addGoal(4, new HBFollowOwnerGoal(this, 1.2D, 1.3D, 7.0f, 4.0f));
        this.goalSelector.addGoal(5, new PlomboScratchLeavesGoal(this));
        this.goalSelector.addGoal(6, new NapGoal(this, NapGoal.SleepType.CATHERMAL, false));
        this.goalSelector.addGoal(7, new RandomlySitGoal(this, 200, 400));
        this.goalSelector.addGoal(8, new RandomStrollGoal(this, 1));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, LivingEntity.class, 8));
        this.goalSelector.addGoal(10, new IdleAnimationGoal<>(this, 50));
        this.goalSelector.addGoal(11, new DancingGoal(this));

        this.targetSelector.addGoal(0, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(2, new HBHurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Monster.class, true));
    }

    @Override
    public boolean canFreeze() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.setUpAnimStates();
        } else {
            if (this.tickCount % 200 == 0) {
                this.heal(20);
            }
            this.multiAttackCD = Math.max(this.multiAttackCD - 1, 0);
            this.attackCD = Math.max(this.attackCD - 1, 0);
            this.tickAnimState();
        }
    }

    private void tickAnimState() {
        if (this.getAnimState() > 0) {
            animTicks++;
            LivingEntity target = this.getTarget();
            switch (this.getAnimState()) {
                case 1 -> {
                    if (this.animTicks == 10 && target != null && this.canHurtTarget(target, this.getAttackReachSqr(target), this.distanceToSqr(target))) {
                        this.doHurtTarget(target);
                    } else if (this.animTicks >= 21) {
                        this.resetAnimState();
                    }
                }
                case 2 -> {
                    this.getNavigation().stop();
                    if (this.animTicks < 10 && target != null) {
                        this.lookAt(target, 15f, 30f);
                        this.getLookControl().setLookAt(target, 15f, 30f);
                    } else if (this.animTicks == 15 || this.animTicks == 24 || this.animTicks == 44) {
                        this.addDeltaMovement(EntityHelpers.bodyAngle(this).scale(0.4));
                    }
                    else if (this.animTicks == 20 || this.animTicks == 30) {
                        List<LivingEntity> hit = AttackHelpers.zoneHitbox(this, EntityHelpers.bodyAngle(this).scale(1.3), 2, 2, 2, 8);
                        for (LivingEntity entity : hit) {
                            AttackHelpers.betterHurt(this, entity, 1.2f, 0.8f);
                        }
                    } else if (this.animTicks == 48) {
                        List<LivingEntity> hit = AttackHelpers.zoneHitbox(this, EntityHelpers.bodyAngle(this).scale(1.3), 2, 2, 2, 8);
                        for (LivingEntity entity : hit) {
                            if (!AttackHelpers.blockBreak(this, entity)) {
                                AttackHelpers.betterHurt(this, entity, 2f, 1f);
                            }
                        }
                    }

                    else if (this.animTicks >= 70) {
                        this.resetAnimState();
                        this.multiAttackCD = 60;
                    }
                }
                case 3 -> {
                    if (this.getTarget() != null || this.getAnimTicks() >= 20) {
                        this.resetAnimState();
                        this.setLeft(!this.swingingLeft());
                    }
                }
                case 4 -> {
                    if (this.getTarget() != null || this.getAnimTicks() >= 41) {
                        this.resetAnimState();
                    }
                }
                case 5 -> {
                    if (this.getTarget() != null || this.getAnimTicks() >= 38) {
                        this.resetAnimState();
                    }
                }

            }
        }
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public void setUpAnimStates() {
        super.setUpAnimStates();
        this.scratchAnimationState.animateWhen(this.isScratching(), this.tickCount);
        int animState = this.getAnimState();
        this.biteAnimationState.animateWhen(animState == 1, this.tickCount);
        this.multiAttackAnimationState.animateWhen(animState == 2, this.tickCount);
        this.earflickAnimationState.animateWhen(animState == 3, this.tickCount);
        this.sniffAnimationState.animateWhen(animState == 4, this.tickCount);
        this.yawnAnimationState.animateWhen(animState == 5, this.tickCount);
    }

    @Override
    @javax.annotation.Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @javax.annotation.Nullable SpawnGroupData pSpawnData, @javax.annotation.Nullable CompoundTag pDataTag) {
        if (pReason == MobSpawnType.CHUNK_GENERATION || pReason == MobSpawnType.NATURAL) {
            long dayTime = this.level().getDayTime();
            if ((dayTime < 12000 || dayTime > 18000) && dayTime < 23000 && dayTime > 6000) {
                this.setNapping(true);
            }
        }

        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

        @Override
    public boolean isInvulnerableTo(DamageSource source) {

        return source.is(DamageTypes.SWEET_BERRY_BUSH) || super.isInvulnerableTo(source);
    }

    @Override
    public void resetAnimState() {
        super.resetAnimState();
        this.attackCD = 5;
        this.multiAttackCD += 5;
    }

    @Override
    public boolean shouldTurnWholeBody() {
        if (this.isScratching()) {
            return true;
        }
        return switch(this.getAnimState()) {
            case 2 -> true;
            default -> false;
        };
    }

    @Override
    public boolean shouldLockAngle() {
        return false;
    }

    @Override
    public boolean shouldInstantTurn() {
        return false;
    }

    @Override
    public float getTurnSpeed() {
        return 25;
    }

    @Override
    public void setAttacking() {
        this.setAnimState(1);
    }

    public boolean canUseMultiAttack(double attackReach, double dist) {
        return this.multiAttackCD == 0 && attackReach * 1.3 >= dist;
    }

    @Override
    public boolean canUseAttack(LivingEntity entity, double attackReach, double dist) {
        return this.attackCD == 0 && this.canHurtTarget(entity, attackReach, dist);
    }

    @Override
    public double getAttackReachSqr(LivingEntity entity) {
        return this.getBbWidth() * 1.8 * this.getBbWidth() * 1.8 + entity.getBbWidth();
    }

    private boolean canHurtTarget(LivingEntity entity, double attackreach, double dist) {
        return this.hasLineOfSight(entity) && attackreach >= dist;
    }

    public boolean swingingLeft() {
        return this.entityData.get(LEFT);
    }

    public void setLeft(boolean b) {
        this.entityData.set(LEFT, b);
    }

    public boolean isScratching() {
        return this.entityData.get(SCRATCHING);
    }

    public void setScratching(boolean b) {
        this.entityData.set(SCRATCHING, b);
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new ATMBodyRotControl<>(this);
    }

    @Override
    public void playIdle() {
        this.setAnimState(this.getRandom().nextInt(3) + 3);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return HBEntities.PLOMBO.get().create(level);
    }

    static class PlomboScratchLeavesGoal extends MoveToBlockGoal {

        private final PlomboEntity plombo;
        private int ticksScratching;
        public PlomboScratchLeavesGoal(PlomboEntity plombo) {
            super(plombo, 1.2f, 8, 5);
            this.plombo = plombo;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));

        }



        @Override
        public void tick() {

            if (this.plombo.isScratching()) {
                this.ticksScratching++;
                if (this.ticksScratching % 15 == 0) {
                    BlockState state = plombo.level().getBlockState(blockPos);
                    plombo.level().levelEvent(2001, blockPos, Block.getId(state));
                }
            } else if (this.isReachedTarget()) {
                this.plombo.getNavigation().stop();
                this.plombo.setScratching(true);
                this.plombo.getLookControl().setLookAt(blockPos.getX(), blockPos.getY(), blockPos.getZ());
            } else {
                this.plombo.getLookControl().setLookAt(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                super.tick();
            }
        }

        public double acceptedDistance() {
            return 4D;
        }


        @Override
        public boolean canUse() {

            return !this.plombo.isBaby() && !this.plombo.isNapping() && !this.plombo.isSitting() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            if (this.plombo.isNapping() || this.plombo.isSitting()) {
                return false;
            }
            if (this.plombo.isScratching()) {
                return this.ticksScratching < 200 && this.isValidTarget(this.plombo.level(), this.blockPos) && blockPos.closerToCenterThan(this.mob.position(), this.acceptedDistance());
            }
            return super.canContinueToUse();
        }

        @Override
        public void start() {
            super.start();
            this.ticksScratching = 0;
        }

        @Override
        public void stop() {
            if (this.plombo.isScratching()) {
                this.plombo.setScratching(false);
            }
            super.stop();
        }

        @Override
        protected boolean isValidTarget(LevelReader level, BlockPos pPos) {
            BlockState state = level.getBlockState(pPos);
            return (state.is(BlockTags.LEAVES));
        }

        @Override
        protected @NotNull BlockPos getMoveToTarget() {
            BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos().set(blockPos);
            while (plombo.level().getBlockState(mutable.below()).isAir()) {
                mutable.move(Direction.DOWN);
            }
            return mutable;
        }
    }

}
