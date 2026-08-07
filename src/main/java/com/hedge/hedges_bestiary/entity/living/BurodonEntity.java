package com.hedge.hedges_bestiary.entity.living;

import com.hedge.hedges_bestiary.entity.AI.control.AdvancedTurner;
import com.hedge.hedges_bestiary.entity.AI.goal.*;
import com.hedge.hedges_bestiary.entity.AI.goal.specific.BurodonAttackGoal;
import com.hedge.hedges_bestiary.entity.AI.control.ATMLookControl;
import com.hedge.hedges_bestiary.entity.AI.control.ATMMoveControl;
import com.hedge.hedges_bestiary.entity.AI.navigation.MMPathNavigatorGround;
import com.hedge.hedges_bestiary.entity.AI.targeting.HBHurtByTargetGoal;
import com.hedge.hedges_bestiary.entity.AI.targeting.TargetMonstersGoal;
import com.hedge.hedges_bestiary.entity.AI.targeting.TargetPlayersGoal;
import com.hedge.hedges_bestiary.entity.types.HBTamableAnimal;
import com.hedge.hedges_bestiary.entity.types.AttackStateMob;
import com.hedge.hedges_bestiary.items.TreatItem;
import com.hedge.hedges_bestiary.registry.HBEntities;
import com.hedge.hedges_bestiary.util.SmoothAnimationState;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class BurodonEntity extends HBTamableAnimal implements AttackStateMob, AdvancedTurner {

    public final SmoothAnimationState biteAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState jumpAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState roarAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState yawnAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState sighAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState tailFlickAnimationState = new SmoothAnimationState();

    public final SmoothAnimationState airAnimationState = new SmoothAnimationState();


    private TurnType turnType;
    private boolean jumpAway = false;
    private Vec3 jumpVector;
    public int inAirTimer = 0;

    public float runProgress = 0;
    private int attackCD = 0;
    private int jumpCD = 0;
    private int roarCD = 0;

    public static final int BITE_ANIM = 1;
    public static final int JUMP_ANIM = 2;
    public static final int ROAR_ANIM = 3;
    public static final int YAWN_ANIM = 4;

    public BurodonEntity(EntityType<? extends BurodonEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new ATMMoveControl<>(this, 90);
        this.lookControl = new ATMLookControl<>(this, 90);
        this.setMaxUpStep(1);
    }

    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        return new MMPathNavigatorGround(this, pLevel);
    }

    public static AttributeSupplier.Builder bakeAttributes(){
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.6)
                .add(Attributes.FOLLOW_RANGE, 45F)
                .add(Attributes.MOVEMENT_SPEED, 0.25F);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        InteractionResult type = super.mobInteract(player, hand);
        if (!this.isTame() && itemStack.getItem() instanceof TreatItem treat && treat.getTier() > 0) {
            if (this.getAnimState() == ROAR_ANIM) {
                if (!this.level().isClientSide) {
                    if (!player.getAbilities().instabuild) {
                        itemStack.shrink(1);
                    }

                    this.level().broadcastEntityEvent(this, (byte) 7);
                    this.tame(player);
                    this.heal(this.getMaxHealth());
                }
                this.playSound(SoundEvents.GENERIC_EAT);
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }
        }
        return type;
    }

    @Override
    protected void registerGoals() {
        int i = 0;
        this.goalSelector.addGoal(i++, new FloatGoal(this));
        this.goalSelector.addGoal(i++, new HBSitWhenOrderedGoal(this));
        this.goalSelector.addGoal(i++, new AvoidTargetWhenLowGoal(this, 1.6D, 20, 15, 16, 7));
        this.goalSelector.addGoal(i++, new HBFollowOwnerGoal(this, 1.2D, 1.6D, 7.0f, 4.0f));
        this.goalSelector.addGoal(i++, new BurodonAttackGoal(this));
        this.goalSelector.addGoal(i++, new MoveToHomePosGoal(this));
        this.goalSelector.addGoal(i++, new NapGoal(this));
        this.goalSelector.addGoal(i++, new RandomlySitGoal(this));
        this.goalSelector.addGoal(i++, new LookAtPlayerGoal(this, LivingEntity.class, 7));
        this.goalSelector.addGoal(i++, new WaterAvoidingRandomStrollGoal(this, 1.0, 20));
        this.goalSelector.addGoal(i++, new IdleAnimationGoal<>(this));
        this.goalSelector.addGoal(i++, new DancingGoal(this));
        this.goalSelector.addGoal(i++, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(0, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(1, new HBHurtByTargetGoal(this, true, TamableAnimal.class));
        this.targetSelector.addGoal(2, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new TargetPlayersGoal(this));
        this.targetSelector.addGoal(4, new TargetMonstersGoal(this));
        this.targetSelector.addGoal(5, new NonTameRandomTargetGoal<>(this, Sheep.class, true, null));
        this.targetSelector.addGoal(6, new NonTameRandomTargetGoal<>(this, Player.class, true, null));

    }


    @Override
    public boolean isAlliedTo(Entity pEntity) {
        if (pEntity instanceof BurodonEntity burodon && burodon.getOwnerUUID() == this.getOwnerUUID()) {
            return true;
        }
        return super.isAlliedTo(pEntity);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.setUpAnimStates();
            if (this.inAir() || this.getAnimState() == JUMP_ANIM) {
                this.inAirTimer++;
            } else {
                this.inAirTimer = 0;
                if (this.getDeltaMovement().horizontalDistanceSqr() > 0.01) {
                    if (this.runProgress < 5) {
                        this.runProgress+=0.25f;
                    }
                } else if (this.runProgress > 0) {
                    this.runProgress-=0.25f;
                }
            }

        } else {
            if (this.tickCount % 200 == 0) {
                this.heal(10);
            }
            this.tickCooldowns();
            this.tickAnimState();
        }
    }

    private void tickCooldowns() {
        this.attackCD = Math.max(this.attackCD - 1, 0);
        this.jumpCD = Math.max(this.jumpCD - 1, 0);
        this.roarCD = Math.max(this.roarCD - 1, 0);
    }

    public boolean inAir() {
        return !this.isInFluidType() && !this.onGround();
    }

    private void tickAnimState() {
        if (this.getAnimState() > 0) {
            animTicks++;
            LivingEntity target = this.getTarget();
            switch (this.getAnimState()) {
                case BITE_ANIM -> {
                    if (this.animTicks == 7 && target != null && this.canHurtTarget(target)) {
                        this.doHurtTarget(target);
                    } else if (this.animTicks >= 15) {
                        this.attackCD = 5;
                        this.jumpAway = this.getRandom().nextBoolean();
                        this.resetAnimState();
                    }
                }
                case JUMP_ANIM-> {
                    this.navigation.stop();
                    if (this.getAnimTicks() < 5) {
                        if (target != null) {
                            if (this.jumpAway && jumpVector != null) {
                                this.getLookControl().setLookAt(jumpVector);
                            } else {
                                this.getLookControl().setLookAt(target, 30f, 30f);
                            }
                        }
                    }
                    else if (this.animTicks == 9) {
                        Vec3 v = this.getLookAngle();
                        this.setDeltaMovement(this.getDeltaMovement().add(v.x, 0.4, v.z).scale(1.5));
                    } else if (this.animTicks >= 15) {
                        this.jumpCD = 100;
                        this.jumpVector = null;
                        this.jumpAway = false;
                        this.resetAnimState();
                    }
                }
                case ROAR_ANIM -> {
                    this.navigation.stop();
                    if (target != null)
                        this.getLookControl().setLookAt(target);
                    if (this.animTicks >= 39) {
                        this.roarCD = 400;
                        this.resetAnimState();
                    }
                }
                case YAWN_ANIM -> {
                    if (this.animTicks >= 45 || target != null) {
                        this.resetAnimState();
                    }
                }
                case 5 -> {
                    if (this.animTicks >= 20 || target != null) {
                        this.resetAnimState();
                    }
                }
                case 6 -> {
                    if (this.animTicks >= 35 || target != null) {
                        this.resetAnimState();
                    }
                }
            }
        }
    }

    @Override
    public void resetAnimState() {
        super.resetAnimState();
        this.turnType = TurnType.NORMAL;
    }

    @Override
    public void playIdle() {
        this.setAnimState(this.getRandom().nextInt(3) + YAWN_ANIM);
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
    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pDimensions) {
        return this.getBbHeight()/1.38f;
    }

    @Override
    protected int calculateFallDamage(float pFallDistance, float pDamageMultiplier) {
        return 0;
    }

    @Override
    public void setUpAnimStates() {
        this.idleAnimationState.animateWhen(inAirTimer < 5 && keepsIdle(), this.tickCount);
        this.sitAnimationState.animateWhen(this.isSitting() && !this.isDancing(), this.tickCount);
        this.napAnimationState.animateWhen(this.isNapping(), this.tickCount);
        this.danceAnimationState.animateWhen(this.isDancing(), this.tickCount);
        int animState = this.getAnimState();
        this.airAnimationState.animateWhen(inAirTimer > 5 && keepsIdle(), this.tickCount);
        this.biteAnimationState.animateWhen(animState == 1, this.tickCount);
        this.jumpAnimationState.animateWhen(animState == 2, this.tickCount);
        this.roarAnimationState.animateWhen(animState == 3, this.tickCount);
        this.yawnAnimationState.animateWhen(animState == 4, this.tickCount);
        this.tailFlickAnimationState.animateWhen(animState == 5, this.tickCount);
        this.sighAnimationState.animateWhen(animState == 6, this.tickCount);

    }

    private boolean keepsIdle() {
        if (this.isSitting()) return false;
        return switch (this.getAnimState()) {
            case JUMP_ANIM, ROAR_ANIM -> false;
            default -> true;
        };
    }

    @Override
    public void setAttacking() {
        this.setAnimState(BITE_ANIM);
    }

    public boolean canRoar() {
        return this.roarCD == 0 && this.random.nextInt(6) == 0;
    }

    public boolean canJump(LivingEntity entity, double attackReach, double dist) {
        if (this.jumpCD > 0)
            return false;
        if (this.getHealth() < 15) {
            this.jumpAway = true;
        }
        if (this.jumpAway) {
            jumpVector = DefaultRandomPos.getPosAway(this, 10, 7, entity.position());
            return jumpVector != null;
        }
        return attackReach * 24 >= dist && attackReach * 6 <= dist;
    }

    @Override
    public boolean canUseAttack(LivingEntity entity, double attackReach, double dist) {
        return this.attackCD == 0 && attackReach >= dist;
    }

    @Override
    public double getAttackReachSqr(LivingEntity entity) {
        return this.getBbWidth() * 2.2 * this.getBbWidth() * 2.2 + entity.getBbWidth();

    }

    private boolean canHurtTarget(LivingEntity entity) {
        return this.hasLineOfSight(entity) && this.getAttackReachSqr(entity) >= this.distanceToSqr(entity);
    }


    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return HBEntities.BURODON.get().create(level);
    }

    @Override
    public void setTurnType(TurnType turnType) {
        this.turnType = turnType;
    }

    @Override
    public TurnType getTurnType() {
        return this.turnType;
    }
}
