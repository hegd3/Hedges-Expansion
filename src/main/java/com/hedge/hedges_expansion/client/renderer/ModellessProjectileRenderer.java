package com.hedge.hedges_expansion.client.renderer;

import com.hedge.hedges_expansion.entity.projectile.GenericProjectile;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class ModellessProjectileRenderer extends EntityRenderer<GenericProjectile> {
    public ModellessProjectileRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public ResourceLocation getTextureLocation(GenericProjectile pEntity) {
        return null;
    }

    @Override
    public boolean shouldRender(GenericProjectile pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        return false;
    }
}
