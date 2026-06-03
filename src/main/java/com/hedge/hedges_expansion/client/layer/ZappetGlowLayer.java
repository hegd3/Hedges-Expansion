package com.hedge.hedges_expansion.client.layer;

import com.hedge.hedges_expansion.HedgesExpansion;
import com.hedge.hedges_expansion.client.models.ZappetModel;
import com.hedge.hedges_expansion.entity.living.ZappetEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class ZappetGlowLayer extends RenderLayer<ZappetEntity, ZappetModel> {

    private static final RenderType texture = RenderType.entityTranslucentEmissive(new ResourceLocation(HedgesExpansion.MODID, "textures/entity/zappet/zappet_glow.png"));

    public ZappetGlowLayer(RenderLayerParent<ZappetEntity, ZappetModel> pRenderer) {
        super(pRenderer);
    }

    @Override
    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, ZappetEntity entity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {

        if (entity.isInvisible()) return;
        if (entity.getAnimState() == 1) {
            VertexConsumer vertexconsumer = pBuffer.getBuffer(texture);
            //this.getParentModel().renderToBuffer(pPoseStack, vertexconsumer, (int) (0.125 * 15728640 * this.getGlowProgress(entity.getAnimTicks())), OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0F);
            this.getParentModel().renderToBuffer(pPoseStack, vertexconsumer, 1, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 0.125F * getGlowProgress(entity.getAnimTicks()));

        }
    }

    private int getGlowProgress(int animTicks) {
        if (animTicks < 9) {
            return animTicks;
        }
        return (Math.max(18 - animTicks, 0));
    }



}
