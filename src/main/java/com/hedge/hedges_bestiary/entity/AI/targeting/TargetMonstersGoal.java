package com.hedge.hedges_bestiary.entity.AI.targeting;

import com.hedge.hedges_bestiary.entity.types.HBTamableAnimal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;

public class TargetMonstersGoal extends NearestAttackableTargetGoal<Monster> {

    private final HBTamableAnimal animal;
    public TargetMonstersGoal(HBTamableAnimal mob) {
        super(mob, Monster.class, true);
        this.animal = mob;
    }

    @Override
    public boolean canUse() {
        if (this.animal.getAutoTargetType() == 1 || this.animal.getAutoTargetType() == 3) {
            return super.canUse();
        }
        return false;
    }
}
