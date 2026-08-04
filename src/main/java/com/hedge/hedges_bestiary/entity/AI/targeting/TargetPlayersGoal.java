package com.hedge.hedges_bestiary.entity.AI.targeting;

import com.hedge.hedges_bestiary.entity.types.HBTamableAnimal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;

public class TargetPlayersGoal extends NearestAttackableTargetGoal<Player> {

    private final HBTamableAnimal animal;
    public TargetPlayersGoal(HBTamableAnimal mob) {
        super(mob, Player.class, true);
        this.animal = mob;
    }

    @Override
    public boolean canUse() {
        if (this.animal.getAutoTargetType() > 1) {
            return super.canUse();
        }
        return false;
    }
}
