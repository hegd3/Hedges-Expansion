package com.hedge.hedges_expansion.entity.AI.navigation;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.AmphibiousNodeEvaluator;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

public class HEAmphibiousPathNavigator extends AmphibiousPathNavigation {
    public HEAmphibiousPathNavigator(Mob mob, Level level) {
        super(mob, level);
    }

    protected PathFinder createPathFinder(int maxVisitedNodes) {
        this.nodeEvaluator = new AmphibiousNodeEvaluator(false);
        this.nodeEvaluator.setCanPassDoors(true);
        return new MMPathFinder(this.nodeEvaluator, maxVisitedNodes);
    }

}
