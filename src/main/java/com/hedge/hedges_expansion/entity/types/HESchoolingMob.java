package com.hedge.hedges_expansion.entity.types;

import com.hedge.hedges_expansion.entity.util.EntityHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Stream;

public class HESchoolingMob extends HEAquaticMob implements HEGroupMob<HESchoolingMob> {
    @Nullable
    protected HESchoolingMob leader;
    private int schoolSize = 1;

    public HESchoolingMob(EntityType<? extends HESchoolingMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }


    public int getMaxSpawnClusterSize() {
        return 1;
    }

    public boolean canRandomSwim() {
        return !this.isFollower();
    }

    @Override
    public boolean isFollower() {
        return this.leader != null && this.leader.isAlive();
    }

    @Override
    public HESchoolingMob getLeader() {
        return this.leader;
    }

    @Override
    public void setLeader(HESchoolingMob leader) {
        this.leader = leader;
    }

    @Override
    public HESchoolingMob startFollowing(HESchoolingMob pLeader) {
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
            List<? extends HESchoolingMob> list = this.level().getEntitiesOfClass(this.getClass(), this.getBoundingBox().inflate(10.0D, 10.0D, 10.0D));
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

    public void addFollowers(Stream<HESchoolingMob> pFollowers) {
        pFollowers.limit((long)(this.getMaxGroupSize() - this.schoolSize)).filter((p_27538_) -> {
            return p_27538_ != this;
        }).forEach((p_27536_) -> {
            p_27536_.startFollowing(this);
        });
    }

}
