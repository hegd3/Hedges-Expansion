package com.hedge.hedges_bestiary.client.renderer;

import com.hedge.hedges_bestiary.client.ClientHelpers;
import com.hedge.hedges_bestiary.client.EntityLayers;
import com.hedge.hedges_bestiary.client.models.WaveModel;
import com.hedge.hedges_bestiary.entity.projectile.WaveEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class WaveRenderer extends EntityRenderer<WaveEntity> {
    private static final ResourceLocation[] textures = ClientHelpers.generateVariants("projectile/wave/wave", 3);

    private final WaveModel model;


    public WaveRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new WaveModel(pContext.bakeLayer(EntityLayers.WAVE_LAYER));
    }

    public void render(WaveEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        if(entityIn.isInvisible()){
            return;
        }
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.0D, 1.5, 0.0D);
        matrixStackIn.mulPose(Axis.YN.rotationDegrees(Mth.lerp(partialTicks, entityIn.yRotO, entityIn.getYRot()) + 180.0F));
        matrixStackIn.mulPose(Axis.ZN.rotationDegrees(180.0F));

        float ageInTicks = entityIn.tickCount + partialTicks;
        this.model.setupAnim(entityIn, 0.0F, 0.0F, ageInTicks, 0.0F, 0F);
        VertexConsumer vertexBuilder = bufferIn.getBuffer(RenderType.entityTranslucent(getTextureLocation(entityIn)));
        this.model.renderToBuffer(matrixStackIn, vertexBuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.popPose();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    public ResourceLocation getTextureLocation(WaveEntity pEntity) {
        return getWaveTexture(pEntity.tickCount);
    }

    private ResourceLocation getWaveTexture(int tickCount) {
        int j = tickCount % 12 / 4;
        return switch (j) {
            case 0, 1 -> textures[j];
            default -> textures[2];
        };
    }
}
