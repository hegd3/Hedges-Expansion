package com.hedge.hedges_bestiary.client.renderer.projectile;

import com.hedge.hedges_bestiary.HedgesBestiary;
import com.hedge.hedges_bestiary.client.EntityLayers;
import com.hedge.hedges_bestiary.client.models.CrossedProjectileModel;
import com.hedge.hedges_bestiary.entity.projectile.EndgelBullet;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class EndgelBulletRenderer extends EntityRenderer<EndgelBullet> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(HedgesBestiary.MODID, "textures/entity/projectile/endgel_bullet.png");
    private final CrossedProjectileModel model;
    public EndgelBulletRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new CrossedProjectileModel(pContext.bakeLayer(EntityLayers.GENERIC_PROJECTILE_LAYER));
    }

    @Override
    public void render(EndgelBullet entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if(entity.isInvisible()){
            return;
        }
        poseStack.pushPose();
        poseStack.translate(0.0D, 0.375, 0.0D);
        poseStack.mulPose(Axis.YN.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) + 180.0F));
        poseStack.mulPose(Axis.ZN.rotationDegrees(180.0F));

        float ageInTicks = entity.tickCount + partialTicks;
        this.model.setupAnim(entity, 0.0F, 0.0F, ageInTicks, 0.0F, 0F);
        VertexConsumer vertexBuilder = buffer.getBuffer(RenderType.entityTranslucentEmissive(getTextureLocation(entity)));
        this.model.renderToBuffer(poseStack, vertexBuilder, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);

    }

    @Override
    protected int getBlockLightLevel(EndgelBullet pEntity, BlockPos pPos) {
        return 15;
    }

    @Override
    public ResourceLocation getTextureLocation(EndgelBullet pEntity) {
        return TEXTURE;
    }
}
