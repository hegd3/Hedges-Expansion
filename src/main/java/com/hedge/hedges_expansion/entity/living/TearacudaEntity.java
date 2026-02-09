package com.hedge.hedges_expansion.entity.living;

import com.hedge.hedges_expansion.client.particle.SmokeParticleOptions;
import com.hedge.hedges_expansion.entity.AI.control.HESwimmingMoveControl;
import com.hedge.hedges_expansion.entity.AI.goal.SchoolingGoal;
import com.hedge.hedges_expansion.entity.AI.goal.SchoolingMobRandomSwimGoal;
import com.hedge.hedges_expansion.entity.AI.goal.TearacudaAttackGoal;
import com.hedge.hedges_expansion.entity.AI.navigation.FluidPathNavigation;
import com.hedge.hedges_expansion.entity.types.HESchoolingMob;
import com.hedge.hedges_expansion.entity.util.AttackHelpers;
import com.hedge.hedges_expansion.entity.util.AttackStateMob;
import com.hedge.hedges_expansion.entity.util.EntityHelpers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class TearacudaEntity extends HESchoolingMob implements AttackStateMob {

    public final AnimationState biteAnimationState = new AnimationState();
    public final AnimationState frenzyAnimationState = new AnimationState();

    private int frenzyCD = 0;
    private int attackCD = 0;
    private int jumpCD = 0;
    public int groundTimer = 0;

    public TearacudaEntity(EntityType<? extends WaterAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new HESwimmingMoveControl(this, 40, 8, 0.02f, 0.1f);
        this.lookControl = new SmoothSwimmingLookControl(this, 8);
    }

    public static AttributeSupplier.Builder bakeAttributes(){
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.2D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.3)
                .add(Attributes.FOLLOW_RANGE, 35F)
                .add(Attributes.MOVEMENT_SPEED, 2F);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(2, new SchoolingMobRandomSwimGoal(this, 1.0f, 10, 10, 5, true));
        this.goalSelector.addGoal(1, new SchoolingGoal(this));
        this.goalSelector.addGoal(0, new TearacudaAttackGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Animal.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this).setAlertOthers());
    }

    @Override
    protected void clientTick() {
        super.clientTick();
        if (!this.isInFluidType() && this.onGround()) {
            this.groundTimer = 10;
        } else {
            this.groundTimer = Math.max(groundTimer - 1, 0);
        }
    }

    @Override
    protected void serverTick() {
        super.serverTick();
        this.attackCD = Math.max(this.attackCD - 1, 0);
        this.frenzyCD = Math.max(this.frenzyCD - 1, 0);
        this.jumpCD = Math.max(this.jumpCD - 1, 0);
        if (!this.isInFluidType() && !this.onGround()) {
            Vec3 vec3 = this.getDeltaMovement();
            if (vec3.y * vec3.y < (double)0.03F && this.getXRot() != 0.0F) {
                this.setXRot(Mth.rotLerp(0.2F, this.getXRot(), 0.0F));
            } else if (vec3.length() > (double)1.0E-5F) {
                double d0 = vec3.horizontalDistance();
                double d1 = Math.atan2(-vec3.y, d0) * (double)(180F / (float)Math.PI);
                this.setXRot((float)d1);
            }
        }
        if (this.getAnimState() > 0) {
            animTicks++;
            LivingEntity target = this.getTarget();
            switch (this.getAnimState()) {
                case 1 -> {
                    if (this.animTicks == 8) {
                        if (target != null && AttackHelpers.singleTargetHitbox(this, target, this.getLookAngle(), 1, 1, 2)) {
                            this.doHurtTarget(target);
                        }
                    } else if (this.animTicks >= 13) {
                        this.resetAnimState();
                        this.attackCD = 5;
                    }
                }
                case 2 -> {

                    if (this.animTicks >= 100 || target == null) {
                        this.resetAnimState();
                        this.frenzyCD = 200;
                    } else if (this.animTicks % 5 == 0 && this.isInFluidType()) {
                        Vec3 v = this.getLookAngle();
                        this.setDeltaMovement(v.scale(0.6));
                        List<LivingEntity> hit = AttackHelpers.zoneHitbox(this, v, 2, 2, 2, 5);
                        for (LivingEntity entity : hit) {
                            if (AttackHelpers.betterHurt(this, entity, 0.5f)) {
                                Vec3 rand = EntityHelpers.getRandomVec3(0.5);
                                for (int i = 0; i < 3; i++) {
                                    ((ServerLevel) this.level()).sendParticles(
                                            new SmokeParticleOptions(1 + (float)rand.x, 30, 0xE8675D),
                                            entity.getX(), entity.getY(), entity.getZ(),
                                            1,
                                            rand.x, rand.y, rand.z, 0.01);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        return new FluidPathNavigation(this, pLevel);
    }

    @Override
    public void setUpAnimStates() {
        super.setUpAnimStates();
        this.biteAnimationState.animateWhen(this.getAnimState() == 1, this.tickCount);
        this.frenzyAnimationState.animateWhen(this.getAnimState() == 2, this.tickCount);

    }

    @Override
    public int getMaxSchoolSize() {
        return 15;
    }

    @Override
    public boolean isAlliedTo(Entity pEntity) {
        if (pEntity instanceof TearacudaEntity && pEntity.getTeam() == this.getTeam()) {
            return true;
        }
        return super.isAlliedTo(pEntity);
    }


    @Override
    public void setAttacking() {
        this.setAnimState(1);
    }

    public boolean canJump(double attackReach, double dist) {
        if (this.jumpCD == 0 && (!this.isInFluidType() || EntityHelpers.closeToSurface(this, 3)) && attackReach * 5 <= dist) {
            this.jumpCD = 60;
            return true;
        }
        return false;
    }

    public boolean canFrenzy(double attackReach, double dist) {
        if (this.isFollower()) {
            return this.getLeader().getAnimState() == 2;
        } else {
            return this.frenzyCD == 0 && attackReach * 10 >= dist;
        }
    }

    @Override
    public boolean canUseAttack(LivingEntity entity, double attackReach, double dist) {
        return this.attackCD == 0 && attackReach >= dist;
    }

    @Override
    public double getAttackReachSqr(LivingEntity entity) {
        return this.getBbWidth() * 2.2 * this.getBbWidth() * 2.2 + entity.getBbWidth();
    }
}
