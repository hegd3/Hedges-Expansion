package com.hedge.hedges_expansion.client.models;

import com.hedge.hedges_expansion.util.SmoothAnimationState;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public abstract class HEModel<E extends Entity> extends HierarchicalModel<E> {

    protected void animateSmooth(SmoothAnimationState animationState, @NotNull AnimationDefinition definition, float ageInTicks, float speed) {
        animationState.animate(this, definition, ageInTicks, speed);
    }
}
