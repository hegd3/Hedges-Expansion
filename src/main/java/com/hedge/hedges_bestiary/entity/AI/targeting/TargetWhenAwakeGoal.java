package com.hedge.hedges_bestiary.entity.AI.targeting;

import com.hedge.hedges_bestiary.entity.types.HBTamableAnimal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class TargetWhenAwakeGoal<T extends LivingEntity> extends NonTameRandomTargetGoal<T> {
    private final HBTamableAnimal animal;

    public TargetWhenAwakeGoal(HBTamableAnimal tamableMob, Class<T> toTarget, @Nullable Predicate<LivingEntity> predicate) {
        super(tamableMob, toTarget, true, predicate);
        this.animal = tamableMob;
    }

    @Override
    public boolean canUse() {
        return !this.animal.isNapping() && super.canUse();
    }
}
