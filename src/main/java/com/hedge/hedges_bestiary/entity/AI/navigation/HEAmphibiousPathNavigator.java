package com.hedge.hedges_bestiary.entity.AI.navigation;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.AmphibiousNodeEvaluator;
import net.minecraft.world.level.pathfinder.PathFinder;

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
