package com.hedge.hedges_expansion.entity.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
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

    // positive = right, negative = left
    public static List<LivingEntity> aoeAttack(LivingEntity entity, Vec3 offset, double pX, double pY, double pZ, float damageMultiplier, float horizontalMultiplier) {

        Vec3 origin = entity.position().add(offset);

        AABB aoe = new AABB(origin.subtract(pX, pY, pZ), origin.add(pX, pY, pZ));

        List<LivingEntity> hit = entity.level().getEntitiesOfClass(LivingEntity.class, aoe, (target) ->
                target != entity && target.isAlive() && entity.hasLineOfSight(target) && !entity.isAlliedTo(target));


        for (LivingEntity target: hit) {
            if (target.isBlocking() && target instanceof Player player) {
                player.disableShield(true);
            } else {
                double hKB = (float) entity.getAttribute(Attributes.ATTACK_KNOCKBACK).getValue() * horizontalMultiplier;
                target.hurt(target.damageSources().mobAttack(entity), (float) entity.getAttribute(Attributes.ATTACK_DAMAGE).getValue() * damageMultiplier);
                target.knockback(0.8D + 0.5D * hKB, entity.getX() - target.getX(), entity.getZ() - target.getZ());
            }
        }
        return hit;

    }

    public static List<LivingEntity> aoeAttack(LivingEntity entity, Vec3 offset, double sideOffset, double pX, double pY, double pZ, float damageMultiplier, float horizontalMultiplier) {

        Vec3 sideVec = offset.cross(UP).normalize();
        Vec3 origin = entity.position().add(offset).add(sideVec.scale(sideOffset));

        AABB aoe = new AABB(origin.subtract(pX, pY, pZ), origin.add(pX, pY, pZ));

        List<LivingEntity> hit = entity.level().getEntitiesOfClass(LivingEntity.class, aoe, (target) ->
                target != entity && target.isAlive() && entity.hasLineOfSight(target) && !entity.isAlliedTo(target));


        for (LivingEntity target: hit) {
            if (target.isBlocking() && target instanceof Player player) {
                player.disableShield(true);
            } else {
                double hKB = (float) entity.getAttribute(Attributes.ATTACK_KNOCKBACK).getValue() * horizontalMultiplier;
                target.hurt(target.damageSources().mobAttack(entity), (float) entity.getAttribute(Attributes.ATTACK_DAMAGE).getValue() * damageMultiplier);
                target.knockback(0.8D + 0.5D * hKB, entity.getX() - target.getX(), entity.getZ() - target.getZ());
            }
        }
        return hit;

    }

    public static void knockUp(LivingEntity hit, double vKB) {
        vKB = Math.max(vKB - hit.getAttribute(Attributes.KNOCKBACK_RESISTANCE).getValue(), 0);
        hit.setDeltaMovement(hit.getDeltaMovement().add(0.0, vKB, 0.0));

    }

    public static Vec3 bodyAngle(LivingEntity entity) {
        return Vec3.directionFromRotation(0.0f, entity.yBodyRot);
    }



    @Nullable
    public static Vec3 getSmartSwimTarget(PathfinderMob mob, int radius, int verticalDistance, boolean preferSurface) {
        Level level = mob.level();
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        int maxAttempts = radius * radius * radius;

        for (int i = 0; i < maxAttempts; i++) {
            Vec3 candidate = DefaultRandomPos.getPos(mob, radius, verticalDistance);
            if (candidate == null) continue;

            boolean nearBoundary = preferSurface
                    ? EntityHelpers.closeToSurface(mob, verticalDistance)
                    : EntityHelpers.closeToBottom(mob, verticalDistance);

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



    public static boolean isWaterBlock(Level world, BlockPos pos) {
        return world.getFluidState(pos).is(FluidTags.WATER);
    }

    public static boolean closeToSurface(LivingEntity entity, int maxDist) {
        return isNearWaterBoundary(entity, maxDist, Direction.UP);
    }

    public static boolean closeToBottom(LivingEntity entity, int maxDist) {
        return isNearWaterBoundary(entity, maxDist, Direction.DOWN);
    }

    private static boolean isNearWaterBoundary(LivingEntity entity, int maxDistance, Direction direction) {
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
