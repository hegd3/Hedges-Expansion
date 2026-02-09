package com.hedge.hedges_expansion.client.layer;

import com.hedge.hedges_expansion.HedgesExpansion;
import com.hedge.hedges_expansion.client.models.TransfiguredModel;
import com.hedge.hedges_expansion.entity.living.TransfiguredEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;

public class TransfiguredEyeLayer extends EyesLayer<TransfiguredEntity, TransfiguredModel> {
    private static final RenderType texture = RenderType.eyes(new ResourceLocation(HedgesExpansion.MODID, "textures/entity/transfigured/transfigured_eyes.png"));
    public TransfiguredEyeLayer(RenderLayerParent<TransfiguredEntity, TransfiguredModel> pRenderer) {
        super(pRenderer);
    }

    @Override
    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, TransfiguredEntity pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        if (pLivingEntity.isAngry())
            super.render(pPoseStack, pBuffer, pPackedLight, pLivingEntity, pLimbSwing, pLimbSwingAmount, pPartialTicks, pAgeInTicks, pNetHeadYaw, pHeadPitch);
    }

    @Override
    public RenderType renderType() {
        return texture;
    }
}
