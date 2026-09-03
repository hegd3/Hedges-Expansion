package com.hedge.hedges_bestiary.entity.types;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Stream;

public class HBSchoolingMob extends HBAquaticMob implements HBGroupMob<HBSchoolingMob> {
    @Nullable
    protected HBSchoolingMob leader;
    private int schoolSize = 1;

    public HBSchoolingMob(EntityType<? extends HBSchoolingMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }


    public int getMaxSpawnClusterSize() {
        return 1;
    }

    @Override
    public boolean isFollower() {
        return this.leader != null && this.leader.isAlive();
    }

    @Override
    public HBSchoolingMob getLeader() {
        return this.leader;
    }

    @Override
    public void setLeader(HBSchoolingMob leader) {
        this.leader = leader;
    }

    @Override
    public HBSchoolingMob startFollowing(HBSchoolingMob pLeader) {
        this.leader = pLeader;
        pLeader.addFollower();
        return pLeader;
    }

    @Override
    public void stopFollowing() {
        this.leader.removeFollower();
        this.leader = null;
    }

    @Override
    public void addFollower() {
        ++this.schoolSize;
    }

    @Override
    public void removeFollower() {
        --this.schoolSize;
    }

    @Override
    public boolean canBeFollowed() {
        return this.hasFollowers() && this.schoolSize < this.getMaxGroupSize();
    }


    public void tick() {
        super.tick();
        if (this.hasFollowers() && this.level().random.nextInt(200) == 1) {
            List<? extends HBSchoolingMob> list = this.level().getEntitiesOfClass(this.getClass(), this.getBoundingBox().inflate(10.0D, 10.0D, 10.0D));
            if (list.size() <= 1) {
                this.schoolSize = 1;
            }
        }

    }

    public boolean hasFollowers() {
        return this.schoolSize > 1;
    }

    @Override
    public int getGroupSize() {
        return this.schoolSize;
    }

    @Override
    public int getMaxGroupSize() {
        return super.getMaxSpawnClusterSize();
    }

    @Override
    public boolean inRangeOfLeader() {
        return this.distanceToSqr(this.leader) <= 400.0D;
    }

    @Override
    public void pathToLeader() {
        if (this.isFollower()) {
            Vec3 pos = this.leader.position().add(0, 3 * this.getRandom().nextDouble() - 3 * this.getRandom().nextDouble(), 0);
            this.getNavigation().moveTo(pos.x, pos.y, pos.z, 1.2f);
        }
    }

    public void addFollowers(Stream<HBSchoolingMob> pFollowers) {
        pFollowers.limit((long)(this.getMaxGroupSize() - this.schoolSize)).filter((p_27538_) -> {
            return p_27538_ != this;
        }).forEach((p_27536_) -> {
            p_27536_.startFollowing(this);
        });
    }

    @Override
    protected @org.jetbrains.annotations.Nullable SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.COD_HURT;
    }

    @Override
    protected @org.jetbrains.annotations.Nullable SoundEvent getDeathSound() {
        return SoundEvents.COD_DEATH;
    }
}
