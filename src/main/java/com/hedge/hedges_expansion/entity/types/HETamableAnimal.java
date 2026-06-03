package com.hedge.hedges_expansion.entity.types;

import com.hedge.hedges_expansion.util.SmoothAnimationState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class HETamableAnimal extends TamableAnimal implements AnimStateMob, IdleAnimMob {
    public final SmoothAnimationState sitAnimationState = new SmoothAnimationState(0.25f);
    public final SmoothAnimationState idleAnimationState = new SmoothAnimationState();

    protected static final EntityDataAccessor<Integer> ANIM_STATE = SynchedEntityData.defineId(HETamableAnimal.class, EntityDataSerializers.INT);

    protected static final EntityDataAccessor<Boolean> IS_SITTING = SynchedEntityData.defineId(HETamableAnimal.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Integer> TAME_COMMAND = SynchedEntityData.defineId(HETamableAnimal.class, EntityDataSerializers.INT);

    protected int animTicks = 0;

    public HETamableAnimal(EntityType<? extends HETamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public boolean isAlliedTo(Entity pEntity) {
        if (this.isTame() && pEntity instanceof OwnableEntity e) {
            if (e.getOwner() == this.getOwner()) {
                return true;
            }
        }
        return super.isAlliedTo(pEntity);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        InteractionResult type = super.mobInteract(player, hand);

        if (!type.consumesAction()) {
            return this.interactTameCommands(player, hand);
        }
        return type;
    }

    public InteractionResult interactTameCommands(Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (this.isTame() && this.isOwnedBy(player) && !this.isFood(itemstack)) {
            if (this.canOwnerCommand(player)) {
                this.setCommand(this.getCommand() + 1);
                if (this.getCommand() == 3) {
                    this.setCommand(0);
                }
                player.displayClientMessage(Component.translatable("entity.hedges_expansion.all.command_" + this.getCommand(), this.getName()), true);
                this.setOrderedToSit(this.getCommand() == 1);
                return InteractionResult.SUCCESS;
            } else if (this.canOwnerMount(player)) {
                if (!level().isClientSide && player.startRiding(this)) {
                    return InteractionResult.CONSUME;
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    protected abstract boolean canOwnerMount(Player player);

    protected abstract boolean canOwnerCommand(Player player);



    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ANIM_STATE, 0);
        this.entityData.define(IS_SITTING, false);
        this.entityData.define(TAME_COMMAND, 0);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setSitting(pCompound.getBoolean("Is_Sitting"));
        this.setCommand(pCompound.getInt("Tame_Command"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putBoolean("Is_Sitting", this.isSitting());
        pCompound.putInt("Tame_Command", this.getCommand());
    }

    @Override
    protected void updateWalkAnimation(float pPartialTick) {
        float f;
        if (this.getPose() == Pose.STANDING) {
            f = Math.min(pPartialTick * 6f, 1f);
        } else {
            f = 0;
        }

        this.walkAnimation.update(f, 0.2f);
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.isInvulnerableTo(pSource)) {
            return false;
        } else {
            Entity entity = pSource.getEntity();
            if (!this.level().isClientSide) {
                this.setOrderedToSit(false);
            }

            if (entity != null && !(entity instanceof Player) && !(entity instanceof AbstractArrow)) {
                pAmount = (pAmount + 1.0F) / 2.0F;
            }

            return super.hurt(pSource, pAmount);
        }
    }

    @Override
    public void setAnimState(int i) {
        this.entityData.set(ANIM_STATE, i);
    }

    @Override
    public int getAnimState() {
        return this.entityData.get(ANIM_STATE);
    }

    @Override
    public int getAnimTicks() {
        return this.animTicks;
    }

    public void resetAnimState() {
        this.animTicks = 0;
        this.setAnimState(0);
    }

    @Override
    public void setUpAnimStates() {
        this.idleAnimationState.animateWhen(this.isAlive(), this.tickCount);
        this.sitAnimationState.animateWhen(this.isSitting(), this.tickCount);

    }

    public void setCommand(int i) {
        this.entityData.set(TAME_COMMAND, i);
    }

    public int getCommand() {
        return this.entityData.get(TAME_COMMAND);
    }

    protected Vec2 getRiddenRotation(LivingEntity pEntity) {
        return new Vec2(pEntity.getXRot() * 0.5F, pEntity.getYRot());
    }

    public void setSitting(boolean b) {
        this.entityData.set(IS_SITTING, b);
    }

    public boolean isSitting() {
        return this.entityData.get(IS_SITTING);
    }

    @Override
    public boolean canPlayIdle() {
        return this.tickCount % 20 == 0 && this.getTarget() == null && this.getAnimState() == 0;
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return null;
    }
}
