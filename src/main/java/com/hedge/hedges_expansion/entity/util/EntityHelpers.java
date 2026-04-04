package com.hedge.hedges_expansion.entity.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public class EntityHelpers {

    public static final Vec3 UP = new Vec3(0, 1, 0);


    public static double angleFromEntity(Entity entity, Entity other) {
        Vec3 forward = Vec3.directionFromRotation(0, entity.getYRot()).normalize();
        Vec3 toOther = other.position().subtract(entity.position()).normalize();
        double dot = forward.dot(toOther);

        return dot;
    }

    public static boolean rightOfEntity(Entity entity, Entity other) {
        Vec3 forward = Vec3.directionFromRotation(0, entity.getYRot()).normalize();
        Vec3 toOther = other.position().subtract(entity.position()).normalize();

        double crossZ = forward.x * toOther.z - forward.z * toOther.x;

        return crossZ < 0;
    }

    public static void spawnParticles(Level level, ParticleOptions particle, double x, double y, double z, int count, double deltaX, double deltaY, double deltaZ, double speed, boolean force) {
        level.getServer().getPlayerList().getPlayers().forEach(player -> ((ServerLevel) level).sendParticles(player, particle, force, x, y, z, count, deltaX, deltaY, deltaZ, speed));
    }

    public static double getRandomScaled(double sc) {
        return (2.0D * Math.random() - 1.0D) * sc;
    }

    public static Vec3 getRandomVec3(double sc) {
        return new Vec3(getRandomScaled(sc), getRandomScaled(sc), getRandomScaled(sc));
    }

    public static void particleOnhitEffect(SimpleParticleType particle, Entity target, Level level, int count) {
        ((ServerLevel)level).sendParticles(
                particle,
                target.getX(), target.getY(), target.getZ(),
                count,
                0.0, 0.0, 0.0, 0.0);
    }


    public static void knockUp(LivingEntity hit, double vKB) {
        vKB = Math.max(vKB - hit.getAttribute(Attributes.KNOCKBACK_RESISTANCE).getValue(), 0);
        hit.setDeltaMovement(hit.getDeltaMovement().add(0.0, vKB, 0.0));

    }

    public static Vec3 bodyAngle(LivingEntity entity) {
        return Vec3.directionFromRotation(0.0f, entity.yBodyRot);
    }

    public static Vec3 bodyAngle(LivingEntity entity, float xRot) {
        return Vec3.directionFromRotation(xRot, entity.yBodyRot);
    }

    @Nullable
    public static Vec3 getSmartFlyingTarget(PathfinderMob mob, int radius, int verticalDistance, int targetDist, int blocksFromGround) {
        Level level = mob.level();
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        int maxAttempts = radius * radius * radius;
        boolean nearBoundary = blocksFromGround > targetDist;
        for (int i = 0; i < maxAttempts; i++) {
            Vec3 vec3 = mob.getViewVector(0.0f);
            Vec3 candidate = HoverRandomPos.getPos(mob, radius * radius, verticalDistance, vec3.x, vec3.z, ((float)Math.PI / 2F), verticalDistance, 0);
            if (candidate == null) continue;


            Vec3 adjusted = candidate.add(0, nearBoundary ? -1 : 1, 0);
            mutablePos.set(adjusted.x, adjusted.y, adjusted.z);
            if (level.getBlockState(mutablePos).isAir()) {
                return adjusted;
            }


            if (i == maxAttempts - 1) {
                return candidate;
            }
        }

        return null;
    }

    @Nullable
    public static Vec3 getSmartSwimTarget(PathfinderMob mob, int radius, int verticalDistance, boolean preferSurface) {
        Level level = mob.level();
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        int maxAttempts = radius * radius * radius;
        boolean nearBoundary = preferSurface
                ? EntityHelpers.closeToSurface(mob, verticalDistance)
                : EntityHelpers.closeToBottom(mob, verticalDistance);

        for (int i = 0; i < maxAttempts; i++) {
            Vec3 candidate = DefaultRandomPos.getPos(mob, radius * radius, verticalDistance);
            if (candidate == null) continue;


            Vec3 adjusted = candidate.add(0, nearBoundary ? -1 : 1, 0);
            mutablePos.set(adjusted.x, adjusted.y, adjusted.z);

            if (level.getBlockState(mutablePos).isPathfindable(level, mutablePos, PathComputationType.WATER)) {
                return adjusted;
            }

            if (i == maxAttempts - 1) {
                return candidate;
            }
        }

        return null;
    }

    @Nullable
    public static Vec3 getSmartSwimAwayTarget(PathfinderMob mob, int radius, int verticalDistance, boolean preferSurface) {
        Level level = mob.level();
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        int maxAttempts = radius * radius * radius;
        boolean nearBoundary = preferSurface
                ? EntityHelpers.closeToSurface(mob, verticalDistance)
                : EntityHelpers.closeToBottom(mob, verticalDistance);

        for (int i = 0; i < maxAttempts; i++) {
            Vec3 candidate = DefaultRandomPos.getPosAway(mob, radius * radius, verticalDistance, mob.position());
            if (candidate == null) continue;


            Vec3 adjusted = candidate.add(0, nearBoundary ? -1 : 1, 0);
            mutablePos.set(adjusted.x, adjusted.y, adjusted.z);

            if (level.getBlockState(mutablePos).isPathfindable(level, mutablePos, PathComputationType.WATER)) {
                return adjusted;
            }

            if (i == maxAttempts - 1) {
                return candidate;
            }
        }

        return null;
    }

    public static void snapTowardsEntity(LivingEntity entity, Entity target) {
        Vec3 towardTarget = target.position().subtract(entity.position()).normalize();
        float yaw = (float)(Mth.atan2(towardTarget.z, towardTarget.x) * (180F / Math.PI)) - 90.0F;

        entity.setYHeadRot(yaw);
        entity.setYBodyRot(yaw);
    }

    public static boolean isAir(Level world, BlockPos pos) {
        return world.getBlockState(pos).isAir();
    }

    public static boolean isWaterBlock(Level world, BlockPos pos) {
        return world.getFluidState(pos).is(FluidTags.WATER);
    }

    public static boolean closeToSurface(LivingEntity entity, int maxDist) {
        return isNearWaterBoundary(entity, maxDist, Direction.UP);
    }

    public static boolean closeToBottom(LivingEntity entity, int maxDist) {
        return isNearWaterBoundary(entity, maxDist, Direction.DOWN);
    }

    public static int blocksFromGround(LivingEntity entity, int maxDistance) {
        BlockPos basePos = entity.blockPosition();
        Level level = entity.level();
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        int i = 1;
        while (i <= maxDistance) {
            mutablePos.set(basePos).move(Direction.DOWN, i);
            if (!isAir(level, mutablePos)) {
                return i;
            }
            i++;
        }

        return i;
    }

    public static int blocksFromGround(Level level, BlockPos basePos, int maxDistance) {

        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        int i = 1;
        while (i <= maxDistance) {
            mutablePos.set(basePos).move(Direction.DOWN, i);
            if (!isAir(level, mutablePos)) {
                return i;
            }
            i++;
        }

        return i;
    }

    public static int blocksFromWaterBoundary(Level level, BlockPos basePos, int maxDistance, Direction direction) {


        if (!isWaterBlock(level, basePos) && !isWaterBlock(level, basePos.above())) {
            return 0;
        }

        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        int i = 1;
        while (i <= maxDistance) {
            mutablePos.set(basePos).move(direction, i);
            if (!isWaterBlock(level, mutablePos)) {
                return i;
            }
            i++;
        }

        return i;
    }

    public static int blocksFromWaterBoundary(LivingEntity entity, int maxDistance, Direction direction) {
        BlockPos basePos = entity.blockPosition();
        Level level = entity.level();

        if (!isWaterBlock(level, basePos) && !isWaterBlock(level, basePos.above())) {
            return 0;
        }

        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        int i = 1;
        while (i <= maxDistance) {
            mutablePos.set(basePos).move(direction, i);
            if (!isWaterBlock(level, mutablePos)) {
                return i;
            }
            i++;
        }

        return i;
    }

    public static boolean isNearWaterBoundary(LivingEntity entity, int maxDistance, Direction direction) {
        BlockPos basePos = entity.blockPosition();
        Level level = entity.level();

        if (!isWaterBlock(level, basePos) && !isWaterBlock(level, basePos.above())) {
            return false;
        }

        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        for (int i = 1; i <= maxDistance; i++) {
            mutablePos.set(basePos).move(direction, i);
            if (!isWaterBlock(level, mutablePos)) {
                return true;
            }
        }

        return false;
    }




}
