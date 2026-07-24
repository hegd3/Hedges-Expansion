package com.hedge.hedges_bestiary.entity.types;

import com.hedge.hedges_bestiary.HedgesBestiary;
import com.hedge.hedges_bestiary.entity.living.PlomboEntity;
import com.hedge.hedges_bestiary.entity.util.EntityHelpers;
import com.hedge.hedges_bestiary.message.DanceJukeboxMessage;
import com.hedge.hedges_bestiary.registry.HBParticles;
import com.hedge.hedges_bestiary.util.SmoothAnimationState;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.datafix.fixes.BlockEntityJukeboxFix;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Optional;

public abstract class HBTamableAnimal extends TamableAnimal implements AnimStateMob, IdleAnimMob {
    public final SmoothAnimationState sitAnimationState = new SmoothAnimationState(0.25f);
    public final SmoothAnimationState napAnimationState = new SmoothAnimationState(0.1f);
    public final SmoothAnimationState idleAnimationState = new SmoothAnimationState();
    public final SmoothAnimationState danceAnimationState = new SmoothAnimationState();

    protected static final EntityDataAccessor<BlockPos> HOME_POS = SynchedEntityData.defineId(HBTamableAnimal.class, EntityDataSerializers.BLOCK_POS);
    protected static final EntityDataAccessor<Integer> ANIM_STATE = SynchedEntityData.defineId(HBTamableAnimal.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Integer> TAME_COMMAND = SynchedEntityData.defineId(HBTamableAnimal.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Boolean> IS_SITTING = SynchedEntityData.defineId(HBTamableAnimal.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> IS_DANCING = SynchedEntityData.defineId(HBTamableAnimal.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> IS_NAPPING = SynchedEntityData.defineId(HBTamableAnimal.class, EntityDataSerializers.BOOLEAN);

    protected int animTicks = 0;


    @Nullable
    private BlockPos jukebox;

    public HBTamableAnimal(EntityType<? extends HBTamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public boolean isAlliedTo(Entity pEntity) {
        if (this.isTame()) {
            if (pEntity instanceof OwnableEntity e) {
                if (e.getOwnerUUID() == this.getOwnerUUID() || e.getOwner() == this.getOwner()) {
                    return true;
                }
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
    public void tick() {
        super.tick();
        this.tickNap();
    }

    protected void tickNap() {
        if (this.isNapping() && this.level().isClientSide() && this.tickCount % 30 == 0) {
            Vec3 rand = this.getEyePosition().add(EntityHelpers.getRandomVec3(0.6));
            this.level().addParticle(HBParticles.SLEEP.get(), rand.x, rand.y, rand.z, rand.x, 0.1, rand.z);
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(HOME_POS, BlockPos.ZERO);
        this.entityData.define(ANIM_STATE, 0);
        this.entityData.define(IS_SITTING, false);
        this.entityData.define(IS_DANCING, false);
        this.entityData.define(IS_NAPPING, false);
        this.entityData.define(TAME_COMMAND, 0);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setSitting(pCompound.getBoolean("Is_Sitting"));
        this.setNapping(pCompound.getBoolean("Is_Napping"));
        this.setCommand(pCompound.getInt("Tame_Command"));
        int i = pCompound.getInt("HomePosX");
        int j = pCompound.getInt("HomePosY");
        int k = pCompound.getInt("HomePosZ");
        this.setHomePos(new BlockPos(i, j, k));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putBoolean("Is_Sitting", this.isSitting());
        pCompound.putBoolean("Is_Napping", this.isNapping());
        pCompound.putInt("Tame_Command", this.getCommand());
        pCompound.putInt("HomePosX", this.getHomePos().getX());
        pCompound.putInt("HomePosY", this.getHomePos().getY());
        pCompound.putInt("HomePosZ", this.getHomePos().getZ());
    }

    @Override
    protected void updateWalkAnimation(float pPartialTick) {
        float f;
        if (this.getPose() == Pose.STANDING && !this.isDancing()) {
            f = Math.min(pPartialTick * 6f, 1f);
        } else {
            f = 0;
        }

        this.walkAnimation.update(f, 0.2f);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        if (source.is(DamageTypes.IN_WALL)) {
            return true;
        }
        return super.isInvulnerableTo(source);
    }



    @Override
    public boolean hurt(DamageSource source, float pAmount) {
        if (super.hurt(source, pAmount)) {
            if(!this.level().isClientSide()) {
                if (!this.isOrderedToSit() && this.isSitting()) {
                    this.setSitting(false);
                } else if (this.isNapping()) {
                    this.setNapping(false);
                }
            }
            return true;
        }
        return false;
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
        this.sitAnimationState.animateWhen(this.isSitting() && !this.isDancing(), this.tickCount);
        this.napAnimationState.animateWhen(this.isNapping(), this.tickCount);
        this.danceAnimationState.animateWhen(this.isDancing(), this.tickCount);
    }

    public void setCommand(int i) {
        this.entityData.set(TAME_COMMAND, i);
    }

    public int getCommand() {
        return this.entityData.get(TAME_COMMAND);
    }

    @Override
    public void tame(Player player) {
        super.tame(player);
        if (this.getTarget() == player) {
            this.setTarget(null);
        }
    }

    @Nullable
    public BlockPos getJukebox() {
        return this.jukebox;
    }

    public void setJukeboxPos(BlockPos pos) {
        this.jukebox = pos;
    }

    public void setRecordPlayingNearby(BlockPos pos, boolean playing) {
        this.onClientPlayMusicDisc(this.getId(), pos, playing);
    }

    public void onClientPlayMusicDisc(int entityId, BlockPos pos, boolean dancing) {
        HedgesBestiary.sendMSGToServer(new DanceJukeboxMessage(entityId, dancing, pos));
        if (dancing) {
            this.jukebox = pos;
        } else {
            this.jukebox = null;
        }
    }

    public void setSitting(boolean b) {
        this.entityData.set(IS_SITTING, b);
    }

    public boolean isSitting() {
        return this.entityData.get(IS_SITTING);
    }

    public void setDancing(boolean b) {
        this.entityData.set(IS_DANCING, b);
    }

    public boolean isDancing() {
        return this.entityData.get(IS_DANCING);
    }

    public boolean isNapping() {
        return this.entityData.get(IS_NAPPING);
    }

    public void setNapping(boolean b) {
        this.entityData.set(IS_NAPPING, b);
    }

    public void setHomePos(BlockPos pos) {
        this.entityData.set(HOME_POS, pos);
    }

    public BlockPos getHomePos() {
        return this.entityData.get(HOME_POS);
    }

    @Override
    public boolean canPlayIdle() {
        return this.getTarget() == null && this.getAnimState() == 0;
    }

}
