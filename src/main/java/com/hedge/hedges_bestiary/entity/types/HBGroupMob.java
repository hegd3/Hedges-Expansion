package com.hedge.hedges_bestiary.entity.types;

import net.minecraft.world.entity.LivingEntity;

import java.util.stream.Stream;


public interface HBGroupMob<E extends LivingEntity & HBGroupMob<E>> {


    default boolean isFollower() {
        return this.getLeader() != null && this.getLeader().isAlive();
    }

    E getLeader();

    void setLeader(E leader);

    default E startFollowing(E leader) {
        this.setLeader(leader);
        this.getLeader().addFollower();
        return leader;
    }

    default void stopFollowing() {
        this.getLeader().removeFollower();
        this.setLeader(null);
    }

    default boolean canNeverFollow() {
        return false;
    }

    default boolean canBeFollowed() {
        return this.hasFollowers() && this.getGroupSize() < this.getMaxGroupSize();
    }
    void pathToLeader();

    boolean inRangeOfLeader();

    default boolean hasFollowers() {
        return this.getGroupSize() > 1;
    }

    int getGroupSize();

    int getMaxGroupSize();

    void addFollower();

    void removeFollower();

    default void addFollowers(Stream<E> pFollowers) {
        pFollowers.limit((long)(this.getMaxGroupSize() - this.getGroupSize())).filter((mob) -> {
            return mob != this;
        }).forEach((mob) -> {
            mob.startFollowing((E) this);
        });
    };

}
