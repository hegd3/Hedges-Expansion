package com.hedge.hedges_bestiary.entity.living;

import com.hedge.hedges_bestiary.entity.AI.control.ATMBodyRotControl;
import com.hedge.hedges_bestiary.entity.AI.control.ATMLookControl;
import com.hedge.hedges_bestiary.entity.AI.control.ATMMoveControl;
import com.hedge.hedges_bestiary.entity.AI.goal.*;
import com.hedge.hedges_bestiary.entity.AI.goal.specific.GruinAttackGoal;
import com.hedge.hedges_bestiary.entity.AI.navigation.MMPathNavigatorGround;
import com.hedge.hedges_bestiary.entity.types.HBTamableAnimal;
import com.hedge.hedges_bestiary.entity.types.AdvancedTurningMob;
import com.hedge.hedges_bestiary.entity.types.AttackStateMob;
import com.hedge.hedges_bestiary.entity.util.AttackHelpers;
import com.hedge.hedges_bestiary.entity.util.EntityHelpers;
import com.hedge.hedges_bestiary.registry.HBEntities;
import com.hedge.hedges_bestiary.util.SmoothAnimationState;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GruinEntity extends HBTamableAnimal implements AttackStateMob, AdvancedTurningMob {

    private static final EntityDataAccessor<Boolean> LEFT = SynchedEntityData.defineId(GruinEntity.class, EntityDataSerializers.BOOLEAN);


    public final AnimationState biteAnimationState = new AnimationState();
    public final AnimationState swipeAnimationState = new AnimationState();
    public final AnimationState multiAttackAnimationState = new AnimationState();
    public final SmoothAnimationState sniffAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState yawnAnimationState = new SmoothAnimationState();

    private int attackCD = 0;
    private int multiAttackCD = 0;


    public GruinEntity(EntityType<? extends GruinEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.lookControl = new ATMLookControl<>(this);
        this.moveControl = new ATMMoveControl<>(this);
    }

    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        return new MMPathNavigatorGround(this, pLevel);
    }

    public static AttributeSupplier.Builder bakeAttributes(){
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 60.0D)
                .add(Attributes.ATTACK_DAMAGE, 8.0D)
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
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new HBSitWhenOrderedGoal(this));
        this.goalSelector.addGoal(2, new AvoidTargetWhenLowGoal(this, 1.3f, 20, 30, 20, 3));
        this.goalSelector.addGoal(3, new GruinAttackGoal(this));
        this.goalSelector.addGoal(4, new HBFollowOwnerGoal(this, 1.2D, 1.3D, 7.0f, 4.0f));

        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, LivingEntity.class, 8));

        this.goalSelector.addGoal(6, new RandomStrollGoal(this, 1));

        this.goalSelector.addGoal(7, new IdleAnimationGoal<>(this));

        this.targetSelector.addGoal(0, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
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
                    if (this.animTicks == 8 && target != null && this.canHurtTarget(target, this.getAttackReachSqr(target), this.distanceToSqr(target))) {
                        this.doHurtTarget(target);
                    } else if (this.animTicks >= 17) {
                        this.resetAnimState();
                    }
                }
                case 2 -> {
                    if (this.animTicks == 11 && target != null && this.canHurtTarget(target, this.getAttackReachSqr(target), this.distanceToSqr(target))) {
                        this.doHurtTarget(target);
                    } else if (this.animTicks >= 19) {
                        this.resetAnimState();
                    }
                }
                case 3 -> {
                    this.getNavigation().stop();
                    if (this.animTicks < 10 && target != null) {
                        this.lookAt(target, 15f, 30f);
                        this.getLookControl().setLookAt(target, 15f, 30f);
                    } else if (this.animTicks == 10 || this.animTicks == 24) {
                        this.addDeltaMovement(EntityHelpers.bodyAngle(this).scale(0.4));
                    }
                    else if (this.animTicks == 16 || this.animTicks == 30) {
                        List<LivingEntity> hit = AttackHelpers.zoneHitbox(this, EntityHelpers.bodyAngle(this).scale(1.6), 2.5, 2, 2.5, 8);
                        for (LivingEntity entity : hit) {
                            AttackHelpers.betterHurt(this, entity, 1.8f, 0.8f);
                        }
                    } else if (this.animTicks >= 45) {
                        this.resetAnimState();
                        this.multiAttackCD = 100;
                    }
                }
                case 4, 5 -> {
                    if (this.getTarget() != null || this.getAnimTicks() >= 41) {
                        this.resetAnimState();
                    }
                }

            }
        }
    }

    @Override
    public void setUpAnimStates() {
        super.setUpAnimStates();
        this.biteAnimationState.animateWhen(this.getAnimState() == 1, this.tickCount);
        this.swipeAnimationState.animateWhen(this.getAnimState() == 2, this.tickCount);
        this.multiAttackAnimationState.animateWhen(this.getAnimState() == 3, this.tickCount);
        this.sniffAnimationState.animateWhen(this.getAnimState() == 4, this.tickCount);
        this.yawnAnimationState.animateWhen(this.getAnimState() == 5, this.tickCount);
    }



    @Override
    public void resetAnimState() {
        super.resetAnimState();
        this.attackCD = 5;
        this.multiAttackCD += 5;
    }

    @Override
    public boolean shouldTurnWholeBody() {
        return switch(this.getAnimState()) {
            case 2, 3 -> true;
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
        int i = this.getRandom().nextInt(2) + 1;
        if (i == 2) {
            this.setLeft(!this.swingingLeft());
        }
        this.setAnimState(i);
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

    @Override
    protected BodyRotationControl createBodyControl() {
        return new ATMBodyRotControl<>(this);
    }

    @Override
    public void playIdle() {
        this.setAnimState(this.getRandom().nextInt(2) + 4);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return HBEntities.GRUIN.get().create(level);
    }
}
