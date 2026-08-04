package com.hedge.hedges_bestiary.entity.living;

import com.hedge.hedges_bestiary.blocks.HBBlocks;
import com.hedge.hedges_bestiary.entity.AI.control.SemiaquaticLookControl;
import com.hedge.hedges_bestiary.entity.AI.control.SemiaquaticMoveControl;
import com.hedge.hedges_bestiary.entity.AI.goal.*;
import com.hedge.hedges_bestiary.entity.AI.navigation.HBAmphibiousPathNavigator;
import com.hedge.hedges_bestiary.entity.types.EggLayer;
import com.hedge.hedges_bestiary.entity.types.HBTamableAnimal;
import com.hedge.hedges_bestiary.entity.types.VariantMob;
import com.hedge.hedges_bestiary.items.TreatItem;
import com.hedge.hedges_bestiary.registry.HBEntities;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;

public class GurkEntity extends HBTamableAnimal implements VariantMob, EggLayer {

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(GurkEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> LEFT = SynchedEntityData.defineId(GurkEntity.class, EntityDataSerializers.BOOLEAN);

    public GurkEntity(EntityType<? extends GurkEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);


        this.moveControl = new SemiaquaticMoveControl(this, 999, 10, 0.25f);
        this.lookControl = new SemiaquaticLookControl(this, 30);


        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0f);
        this.setPathfindingMalus(BlockPathTypes.WATER_BORDER, 0.0f);

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
        this.entityData.define(VARIANT, 0);
        this.entityData.define(LEFT, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("Variant", this.getVariant());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setVariant(pCompound.getInt("Variant"));
    }

    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        return new HBAmphibiousPathNavigator(this, pLevel);
    }

    public static AttributeSupplier.Builder bakeAttributes(){
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.FOLLOW_RANGE, 8F)
                .add(Attributes.MOVEMENT_SPEED, 0.25F);
    }



    @Override
    protected void registerGoals() {
        int i = 0;
        this.goalSelector.addGoal(i++, new HBSitWhenOrderedGoal(this));
        this.goalSelector.addGoal(i++, new PanicGoal(this, 1.2));
        this.goalSelector.addGoal(i++, new HBFollowOwnerGoal(this, 1.2, 1.3, 4.0f, 2.0f));
        this.goalSelector.addGoal(i++, new MoveToHomePosGoal(this));
        this.goalSelector.addGoal(i++, new RandomlySitGoal(this));
        this.goalSelector.addGoal(i++, new LookAtPlayerGoal(this, LivingEntity.class, 5));
        this.goalSelector.addGoal(i++, new CustomSwimGoal(this, 1.0, 10, 4, 4, true));
        this.goalSelector.addGoal(i++, new RandomStrollGoal(this, 1.0) {
            @Override
            public boolean canUse() {
                return !this.mob.isInWaterOrBubble() && super.canUse();
            }

            @Override
            public boolean canContinueToUse() {
                return !this.mob.isInWaterOrBubble() && super.canContinueToUse();
            }
        });
        this.goalSelector.addGoal(i++, new DancingGoal(this));
        this.goalSelector.addGoal(i, new RandomLookAroundGoal(this));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.setUpAnimStates();
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        InteractionResult type = super.mobInteract(player, hand);
        if (!this.isTame() && itemStack.getItem() instanceof TreatItem treat && treat.getTier() >= 0) {
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
        return type;
    }

    @Override
    public boolean canDrownInFluidType(FluidType type) {
        return false;
    }

    @Override
    public void travel(Vec3 pTravelVector) {
        if (this.isEffectiveAi() && this.isInWaterOrBubble()) {
            this.moveRelative(this.getSpeed(), pTravelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
        } else {
            super.travel(pTravelVector);
        }

    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {

        if (this.getRandom().nextInt(20) == 0) {
            this.setVariant(3);
        } else {
            Holder<Biome> biome = pLevel.getBiome(this.blockPosition());
            if (biome.is(BiomeTags.IS_RIVER)) {
                this.setVariant(1);
            } else if (biome.is(BiomeTags.IS_OCEAN) || biome.is(BiomeTags.IS_BEACH)) {
                this.setVariant(2);
            }
        }
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    @Override
    public void setVariant(int i) {
        this.entityData.set(VARIANT, i);
    }

    @Override
    public int getVariant() {
        return this.entityData.get(VARIANT);
    }

    @Override
    public void playIdle() {
    }

    @Override
    public void setSitting(boolean b) {
        this.entityData.set(LEFT, this.getRandom().nextBoolean());
        super.setSitting(b);
    }

    public boolean left() {
        return this.entityData.get(LEFT);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return HBEntities.GURK.get().create(level);
    }


    @Override
    public BlockState getEgg() {
        return HBBlocks.GURK_EGG.get().defaultBlockState();
    }
}
