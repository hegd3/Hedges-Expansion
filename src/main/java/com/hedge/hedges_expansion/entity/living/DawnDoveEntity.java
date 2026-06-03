package com.hedge.hedges_expansion.entity.living;

import com.hedge.hedges_expansion.entity.AI.control.HEFlyingMoveControl;
import com.hedge.hedges_expansion.entity.AI.goal.GroupFollowLeaderGoal;
import com.hedge.hedges_expansion.entity.AI.goal.SemiFlyerFlyingGoal;
import com.hedge.hedges_expansion.entity.AI.navigation.MMPathNavigatorGround;
import com.hedge.hedges_expansion.entity.types.TamableFlyer;
import com.hedge.hedges_expansion.util.SmoothAnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
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
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class DawnDoveEntity extends TamableFlyer {

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
                .add(Attributes.MOVEMENT_SPEED, 0.2F);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(3, new SemiFlyerFlyingGoal<>(this, 1.0f, 35, 25, 20, 800));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, LivingEntity.class, 10));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    }

    /*
    @Override
    protected void tickRidden(Player pPlayer, Vec3 pTravelVector) {
        super.tickRidden(pPlayer, pTravelVector);
        Vec2 vec2 = this.getRiddenRotation(pPlayer);
        this.setRot(vec2.y, vec2.x);
        this.yRotO = this.yBodyRot = this.yHeadRot = this.getYRot();
        if (this.isControlledByLocalInstance()) {
            if (this.onGround()) {
                if (!this.isFlying() &&
            }
        }

    }

     */



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
    protected boolean canOwnerMount(Player player) {
        return true;
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
            this.moveControl = new HEFlyingMoveControl(this, 45, 8, 0.9f);
            this.lookControl = new SmoothSwimmingLookControl(this, 30);
            this.navigation = new FlyingPathNavigation(this, this.level());
        } else {
            this.lookControl = new LookControl(this);
            this.moveControl = new MoveControl(this);
            this.navigation = new MMPathNavigatorGround(this, this.level());
        }
    }
}
