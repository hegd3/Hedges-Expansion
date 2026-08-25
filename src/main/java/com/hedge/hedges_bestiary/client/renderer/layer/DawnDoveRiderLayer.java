package com.hedge.hedges_bestiary.client.renderer.layer;

import com.hedge.hedges_bestiary.HedgesBestiary;
import com.hedge.hedges_bestiary.client.models.DawnDoveModel;
import com.hedge.hedges_bestiary.client.renderer.DawnDoveRenderer;
import com.hedge.hedges_bestiary.entity.living.DawnDoveEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.Entity;

public class DawnDoveRiderLayer extends RiderLayer<DawnDoveEntity, DawnDoveModel> {
    public DawnDoveRiderLayer(DawnDoveRenderer render) {
        super(render);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, DawnDoveEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity.isVehicle()) {
            float bodyYaw = entity.yBodyRotO + (entity.yBodyRot - entity.yBodyRotO) * partialTicks;
            for (Entity passenger : entity.getPassengers()) {
                if (passenger == Minecraft.getInstance().player && Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
                    continue;
                }
                HedgesBestiary.PROXY.releaseRenderingEntity(passenger.getUUID());
                poseStack.pushPose();
                this.getParentModel().root().translateAndRotate(poseStack);
                this.getParentModel().flycontrol.translateAndRotate(poseStack);
                this.getParentModel().body.translateAndRotate(poseStack);
                poseStack.translate(0, passenger.getBbHeight() / -1.66f, entity.getPassengers().indexOf(passenger) == 0 ? -1 : 0.25f);
                poseStack.mulPose(Axis.XN.rotationDegrees(180F));
                poseStack.mulPose(Axis.YN.rotationDegrees(360F - bodyYaw));

                renderPassenger(passenger, 0, 0, 0, 0, partialTicks, poseStack, bufferIn, packedLightIn);
                poseStack.popPose();
                HedgesBestiary.PROXY.blockRenderingEntity(passenger.getUUID());
            }

        }
        if (entity.isGrabbing()) {
            Entity grabbed = entity.getGrabbedEntity();
            if (grabbed == Minecraft.getInstance().player && Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
                return;
            }
            HedgesBestiary.PROXY.releaseRenderingEntity(grabbed.getUUID());
            poseStack.pushPose();
            this.getParentModel().root().translateAndRotate(poseStack);
            this.getParentModel().flycontrol.translateAndRotate(poseStack);
            this.getParentModel().leftleg.translateAndRotate(poseStack);
            poseStack.translate(-0.3f, grabbed.getBbHeight() * 1.41f, entity.getBbWidth() / -6);
            poseStack.mulPose(Axis.XN.rotationDegrees(180F));
            //poseStack.mulPose(Axis.YN.rotationDegrees(360F - bodyYaw));
            renderPassenger(grabbed, 0, 0, 0, 0, partialTicks, poseStack, bufferIn, packedLightIn);
            poseStack.popPose();
            HedgesBestiary.PROXY.blockRenderingEntity(grabbed.getUUID());

        }
    }

}
