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

public class HESchoolingMob extends HEAquaticMob {
    @Nullable
    private HESchoolingMob leader;
    private int schoolSize = 1;

    public HESchoolingMob(EntityType<? extends WaterAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }


    public int getMaxSpawnClusterSize() {
        return this.getMaxSchoolSize();
    }

    public int getMaxSchoolSize() {
        return super.getMaxSpawnClusterSize();
    }

    public boolean canRandomSwim() {
        return !this.isFollower();
    }

    public boolean isFollower() {
        return this.leader != null && this.leader.isAlive();
    }

    public HESchoolingMob getLeader() {
        return this.leader;
    }

    public HESchoolingMob startFollowing(HESchoolingMob pLeader) {
        this.leader = pLeader;
        pLeader.addFollower();
        return pLeader;
    }

    public void stopFollowing() {
        this.leader.removeFollower();
        this.leader = null;
    }

    private void addFollower() {
        ++this.schoolSize;
    }

    private void removeFollower() {
        --this.schoolSize;
    }

    public boolean canBeFollowed() {
        return this.hasFollowers() && this.schoolSize < this.getMaxSchoolSize();
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

    public boolean inRangeOfLeader() {
        return this.distanceToSqr(this.leader) <= 200.0D;
    }

    public void pathToLeader() {
        if (this.isFollower()) {
            Vec3 pos = this.leader.position().add(EntityHelpers.bodyAngle(this.leader).cross(EntityHelpers.UP).normalize().scale(4 * (this.getRandom().nextDouble()- this.getRandom().nextDouble())));
            this.getNavigation().moveTo(pos.x, pos.y + 0.5, pos.z, 1.0f);
        }

    }

    public void addFollowers(Stream<? extends HESchoolingMob> pFollowers) {
        pFollowers.limit((long)(this.getMaxSchoolSize() - this.schoolSize)).filter((p_27538_) -> {
            return p_27538_ != this;
        }).forEach((p_27536_) -> {
            p_27536_.startFollowing(this);
        });
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
        if (pSpawnData == null) {
            pSpawnData = new HESchoolingMob.SchoolSpawnGroupData(this);
        } else {
            this.startFollowing(((HESchoolingMob.SchoolSpawnGroupData)pSpawnData).leader);
        }

        return pSpawnData;
    }

    public static class SchoolSpawnGroupData implements SpawnGroupData {
        public final HESchoolingMob leader;

        public SchoolSpawnGroupData(HESchoolingMob pLeader) {
            this.leader = pLeader;
        }
    }
}
