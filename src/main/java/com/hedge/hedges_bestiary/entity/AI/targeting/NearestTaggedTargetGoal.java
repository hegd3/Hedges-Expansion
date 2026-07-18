package com.hedge.hedges_bestiary.entity.AI.targeting;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

public class NearestTaggedTargetGoal<E extends LivingEntity> extends NearestAttackableTargetGoal<E> {

    public NearestTaggedTargetGoal(Mob pMob, Class<E> pTargetType, boolean pMustSee, TagKey<EntityType<?>> targetTag) {
        super(pMob, pTargetType, pMustSee, (target) -> target.getType().is(targetTag));
    }

}
