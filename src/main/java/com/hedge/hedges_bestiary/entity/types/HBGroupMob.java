package com.hedge.hedges_bestiary.entity.types;

import net.minecraft.world.entity.LivingEntity;

import java.util.stream.Stream;


public interface HBGroupMob<E extends LivingEntity & HBGroupMob<E>> {


    public default boolean isFollower() {
        return this.getLeader() != null && this.getLeader().isAlive();
    }

    public E getLeader();

    public void setLeader(E leader);

    public default E startFollowing(E leader) {
        this.setLeader(leader);
        this.getLeader().addFollower();
        return leader;
    }

    public default void stopFollowing() {
        this.getLeader().removeFollower();
        this.setLeader(null);
    }

    public default boolean canBeFollowed() {
        return this.hasFollowers() && this.getGroupSize() < this.getMaxGroupSize();
    }
    public void pathToLeader();

    public boolean inRangeOfLeader();

    public default boolean hasFollowers() {
        return this.getGroupSize() > 1;
    }

    public int getGroupSize();

    public int getMaxGroupSize();

    public void addFollower();

    public void removeFollower();

    public void addFollowers(Stream<E> pFollowers);


}
