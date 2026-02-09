package com.hedge.hedges_expansion.client.layer;

import com.hedge.hedges_expansion.HedgesExpansion;
import com.hedge.hedges_expansion.client.models.MurkModel;
import com.hedge.hedges_expansion.entity.living.MurkEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class MurkEyeLayer extends EyesLayer<MurkEntity, MurkModel> {

    private static final RenderType eyes = RenderType.eyes(new ResourceLocation(HedgesExpansion.MODID, "textures/entity/murk/murk_eyes.png"));
    private static final RenderType charge = RenderType.eyes(new ResourceLocation(HedgesExpansion.MODID, "textures/entity/murk/murk_charge.png"));

    @Override
    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, MurkEntity pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        VertexConsumer vertexconsumer = pBuffer.getBuffer(pLivingEntity.isCharged() ? charge : renderType());
        this.getParentModel().renderToBuffer(pPoseStack, vertexconsumer, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    public MurkEyeLayer(RenderLayerParent<MurkEntity, MurkModel> pRenderer) {
        super(pRenderer);
    }

    @Override
    public RenderType renderType() {
        return eyes;
    }
}
