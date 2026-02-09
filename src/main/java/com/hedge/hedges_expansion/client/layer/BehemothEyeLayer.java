package com.hedge.hedges_expansion.client.layer;

import com.hedge.hedges_expansion.client.ClientHelpers;
import com.hedge.hedges_expansion.client.models.BehemothModel;
import com.hedge.hedges_expansion.entity.living.BehemothEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class BehemothEyeLayer extends EyesLayer<BehemothEntity, BehemothModel> {
    private static final RenderType[] texture = ClientHelpers.generateEyeVariants("behemoth/behemoth_eyes", 4);

    public BehemothEyeLayer(RenderLayerParent<BehemothEntity, BehemothModel> pRenderer) {
        super(pRenderer);
    }

    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, BehemothEntity entity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        VertexConsumer vertexconsumer = pBuffer.getBuffer(texture[entity.getVariant()]);
        this.getParentModel().renderToBuffer(pPoseStack, vertexconsumer, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public RenderType renderType() {
        return texture[0];
    }
}
