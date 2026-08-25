package com.hedge.hedges_bestiary.client.renderer.layer;

import com.hedge.hedges_bestiary.HedgesBestiary;
import com.hedge.hedges_bestiary.client.models.MurkModel;
import com.hedge.hedges_bestiary.client.models.PlomboModel;
import com.hedge.hedges_bestiary.client.renderer.MurkRenderer;
import com.hedge.hedges_bestiary.client.renderer.PlomboRenderer;
import com.hedge.hedges_bestiary.entity.living.MurkEntity;
import com.hedge.hedges_bestiary.entity.living.PlomboEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;

public class PlomboRiderLayer extends RiderLayer<PlomboEntity, PlomboModel> {
    public PlomboRiderLayer(PlomboRenderer render) {
        super(render);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, PlomboEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity.isVehicle()) {
            float bodyYaw = entity.yBodyRotO + (entity.yBodyRot - entity.yBodyRotO) * partialTicks;
            for (Entity passenger : entity.getPassengers()) {
                if (passenger == Minecraft.getInstance().player && Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
                    continue;
                }
                HedgesBestiary.PROXY.releaseRenderingEntity(passenger.getUUID());
                poseStack.pushPose();
                this.getParentModel().root().translateAndRotate(poseStack);
                this.getParentModel().bodyfrontlegs.translateAndRotate(poseStack);
                this.getParentModel().body.translateAndRotate(poseStack);
                this.getParentModel().body2.translateAndRotate(poseStack);

                poseStack.translate(0, passenger.getBbHeight() / -4f, 0.35f);
                poseStack.mulPose(Axis.XN.rotationDegrees(180F));
                poseStack.mulPose(Axis.YN.rotationDegrees(360F - bodyYaw));

                renderPassenger(passenger, 0, 0, 0, 0, partialTicks, poseStack, bufferIn, packedLightIn);
                poseStack.popPose();
                HedgesBestiary.PROXY.blockRenderingEntity(passenger.getUUID());
            }

        }
    }


}
