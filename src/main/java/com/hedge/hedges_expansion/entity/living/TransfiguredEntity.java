package com.hedge.hedges_expansion.entity.living;

import com.hedge.hedges_expansion.entity.AI.goal.GenericMeleeGoal;
import com.hedge.hedges_expansion.entity.types.HEMonster;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class TransfiguredEntity extends HEMonster {

    private static final EntityDataAccessor<Boolean> LEFT = SynchedEntityData.defineId(TransfiguredEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> ANGRY = SynchedEntityData.defineId(TransfiguredEntity.class, EntityDataSerializers.BOOLEAN);

    private int attackCD = 0;

    public int mouthProgress = 0;
    private boolean mouthSwitch = true;


    public TransfiguredEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }


    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 120.0D)
                .add(Attributes.ARMOR, 10D)
                .add(Attributes.ATTACK_DAMAGE, 8.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.7)
                .add(Attributes.FOLLOW_RANGE, 25F)
                .add(Attributes.MOVEMENT_SPEED, 0.17F);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, LivingEntity.class, 7));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0, 20));
        this.goalSelector.addGoal(1, new GenericMeleeGoal<>(this, 2) {
            @Override
            public void stop() {
                super.stop();
                this.mob.setAngry(false);
            }

            @Override
            public void start() {
                super.start();
                this.mob.setAngry(true);
            }
        });
        this.goalSelector.addGoal(0, new FloatGoal(this));

        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Villager.class, true));

    }

    @Override
    protected void serverTick() {
        this.attackCD = Math.max(this.attackCD - 1, 0);
        if (this.getAnimState() > 0) {
            this.animTicks++;
            LivingEntity target = this.getTarget();
            switch (this.getAnimState()) {
                case 1 -> {
                    if (this.animTicks == 15 && target != null && this.canHurtTarget(target)) {
                        this.doHurtTarget(target);
                    }  else if (this.animTicks > 22) {
                        this.resetAnimState();
                    }
                }
            }
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(LEFT, false);
        this.entityData.define(ANGRY, false);

    }

    @Override
    protected void clientTick() {
        super.clientTick();
        if (this.tickCount % 20 == 0) {
            if (!this.isAngry()) {
                if (mouthSwitch) {
                    this.mouthProgress = 1;
                } else {
                    this.mouthProgress = 0;
                }
            } else {
                if (mouthSwitch) {
                    this.mouthProgress = 1;
                } else {
                    this.mouthProgress = 2;
                }
            }
            this.mouthSwitch = !this.mouthSwitch;
        }
    }

    @Override
    public boolean canUseAttack(LivingEntity entity, double attackReach, double dist) {
        if (this.attackCD > 0 || this.getAnimState() > 0) {
            return false;
        }
        if (super.canUseAttack(entity, attackReach, dist)) {
            this.setLeft(this.getRandom().nextBoolean());
            return true;
        }
        return false;
    }

    public boolean swingingLeft() {
        return this.entityData.get(LEFT);
    }

    public void setLeft(boolean b) {
        this.entityData.set(LEFT, b);
    }

    public boolean isAngry() {
        return this.entityData.get(ANGRY);
    }

    public void setAngry(boolean b) {
        this.entityData.set(ANGRY, b);
    }

    private boolean canHurtTarget(LivingEntity entity) {
        return this.getAttackReachSqr(entity) >= this.distanceToSqr(entity);
    }

    @Override
    public void resetAnimState() {
        this.attackCD = 5;
        super.resetAnimState();
    }

    @Override
    public double getAttackReachSqr(LivingEntity entity) {
        return this.getBbWidth() * 1.8 * this.getBbWidth() * 1.8 + entity.getBbWidth();

    }
}