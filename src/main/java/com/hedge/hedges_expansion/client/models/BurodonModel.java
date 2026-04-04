
package com.hedge.hedges_expansion.client.models;

import com.hedge.hedges_expansion.client.animations.BurodonAnimation;
import com.hedge.hedges_expansion.client.layer.EntityLayers;
import com.hedge.hedges_expansion.entity.living.BurodonEntity;
import com.hedge.hedges_expansion.util.SmoothAnimationState;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class BurodonModel extends HEModel<BurodonEntity> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = EntityLayers.BURODON_LAYER;
	private final ModelPart root;
	private final ModelPart bodyandfrontlegs;
	private final ModelPart leftleg;
	private final ModelPart rightleg;
	private final ModelPart bodyheadtail;
	private final ModelPart body;
	private final ModelPart neckrot;
	private final ModelPart neck;
	private final ModelPart headrot;
	private final ModelPart head;
	private final ModelPart ear;
	private final ModelPart ear2;
	private final ModelPart jaw;
	private final ModelPart tail;
	private final ModelPart tail2;
	private final ModelPart rightleg2;
	private final ModelPart leftleg2;

	public BurodonModel(ModelPart root) {
		this.root = root.getChild("root");
		this.bodyandfrontlegs = this.root.getChild("bodyandfrontlegs");
		this.leftleg = this.bodyandfrontlegs.getChild("leftleg");
		this.rightleg = this.bodyandfrontlegs.getChild("rightleg");
		this.bodyheadtail = this.bodyandfrontlegs.getChild("bodyheadtail");
		this.body = this.bodyheadtail.getChild("body");
		this.neckrot = this.bodyheadtail.getChild("neckrot");
		this.neck = this.neckrot.getChild("neck");
		this.headrot = this.neck.getChild("headrot");
		this.head = this.headrot.getChild("head");
		this.ear = this.head.getChild("ear");
		this.ear2 = this.head.getChild("ear2");
		this.jaw = this.head.getChild("jaw");
		this.tail = this.bodyheadtail.getChild("tail");
		this.tail2 = this.tail.getChild("tail2");
		this.rightleg2 = this.root.getChild("rightleg2");
		this.leftleg2 = this.root.getChild("leftleg2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition bodyandfrontlegs = root.addOrReplaceChild("bodyandfrontlegs", CubeListBuilder.create(), PartPose.offset(0.0F, -11.0F, 4.0F));

		PartDefinition leftleg = bodyandfrontlegs.addOrReplaceChild("leftleg", CubeListBuilder.create().texOffs(86, 56).addBox(-3.0F, 0.0F, -3.5F, 6.0F, 10.0F, 7.0F, new CubeDeformation(0.01F)), PartPose.offset(3.5F, 1.0F, -10.5F));

		PartDefinition rightleg = bodyandfrontlegs.addOrReplaceChild("rightleg", CubeListBuilder.create().texOffs(86, 56).mirror().addBox(-3.0F, 0.0F, -3.5F, 6.0F, 10.0F, 7.0F, new CubeDeformation(0.01F)).mirror(false), PartPose.offset(-3.5F, 1.0F, -10.5F));

		PartDefinition bodyheadtail = bodyandfrontlegs.addOrReplaceChild("bodyheadtail", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, -4.0F));

		PartDefinition body = bodyheadtail.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-7.5F, -9.0F, -13.0F, 15.0F, 19.0F, 23.0F, new CubeDeformation(0.0F))
		.texOffs(48, 88).addBox(0.0F, -15.0F, -1.0F, 0.0F, 6.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(0, 42).addBox(-7.5F, 10.0F, -13.0F, 15.0F, 3.0F, 23.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.0F, 1.0F));

		PartDefinition neckrot = bodyheadtail.addOrReplaceChild("neckrot", CubeListBuilder.create(), PartPose.offset(0.0F, -9.5F, -12.0F));

		PartDefinition neck = neckrot.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(0, 68).addBox(-4.5F, -11.5F, -4.0F, 9.0F, 19.0F, 15.0F, new CubeDeformation(0.0F))
		.texOffs(88, 90).addBox(-4.5F, 7.5F, -4.0F, 9.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(86, 73).addBox(0.0F, -17.5F, -1.0F, 0.0F, 6.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition headrot = neck.addOrReplaceChild("headrot", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, -2.0F));

		PartDefinition head = headrot.addOrReplaceChild("head", CubeListBuilder.create().texOffs(88, 96).addBox(0.0F, 6.5F, -9.0F, 0.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(0, 102).addBox(0.0F, -7.5F, -14.0F, 0.0F, 4.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(48, 68).addBox(-5.5F, -5.5F, -8.0F, 11.0F, 12.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(66, 101).addBox(5.5F, 1.5F, -6.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(66, 101).mirror().addBox(-9.5F, 1.5F, -6.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(76, 0).addBox(-3.5F, -3.5F, -18.0F, 7.0F, 5.0F, 10.0F, new CubeDeformation(0.01F))
		.texOffs(34, 110).addBox(-3.5F, 1.5F, -18.0F, 7.0F, 2.0F, 10.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition ear = head.addOrReplaceChild("ear", CubeListBuilder.create().texOffs(75, 55).addBox(1.06F, -4.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(12, 102).addBox(-0.94F, -3.0F, -1.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(2.5F, -5.5F, -6.0F));

		PartDefinition ear2 = head.addOrReplaceChild("ear2", CubeListBuilder.create().texOffs(75, 55).mirror().addBox(-1.06F, -4.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(12, 102).mirror().addBox(-1.06F, -3.0F, -1.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.5F, -5.5F, -6.0F));

		PartDefinition jaw = head.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(76, 32).addBox(-3.5F, 0.0F, -10.0F, 7.0F, 4.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.5F, -8.0F));

		PartDefinition teeth_r1 = jaw.addOrReplaceChild("teeth_r1", CubeListBuilder.create().texOffs(34, 71).mirror().addBox(0.0F, -2.0F, 2.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.02F)).mirror(false), PartPose.offsetAndRotation(-3.5F, 0.0F, -6.0F, 0.0F, 0.0F, -0.3491F));

		PartDefinition teeth_r2 = jaw.addOrReplaceChild("teeth_r2", CubeListBuilder.create().texOffs(34, 71).addBox(0.0F, -2.0F, 2.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.02F)), PartPose.offsetAndRotation(3.5F, 0.0F, -6.0F, 0.0F, 0.0F, 0.3491F));

		PartDefinition tail = bodyheadtail.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(66, 90).addBox(-2.5F, -2.5F, 0.0F, 5.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.5F, 11.0F));

		PartDefinition tail2 = tail.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(76, 16).addBox(-3.5F, -3.5F, 0.0F, 7.0F, 7.0F, 9.0F, new CubeDeformation(0.01F))
		.texOffs(112, 25).addBox(-3.5F, -3.5F, 4.0F, 7.0F, 7.0F, 0.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, 6.0F));

		PartDefinition rightleg2 = root.addOrReplaceChild("rightleg2", CubeListBuilder.create().texOffs(86, 56).mirror().addBox(-3.0F, 0.0F, -3.5F, 6.0F, 10.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.5F, -10.0F, 5.5F));

		PartDefinition leftleg2 = root.addOrReplaceChild("leftleg2", CubeListBuilder.create().texOffs(86, 56).addBox(-3.0F, 0.0F, -3.5F, 6.0F, 10.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(3.5F, -10.0F, 5.5F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}


	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}

	@Override
	public void setupAnim(BurodonEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		netHeadYaw = Mth.clamp(netHeadYaw, -15.0F, 15.0F) * ((float)Math.PI / 180F);
		headPitch = Mth.clamp(headPitch, -5.0F, 25.0F) * ((float)Math.PI / 180F);

		this.headrot.yRot = netHeadYaw;
		this.headrot.xRot = headPitch;
		this.neckrot.yRot = netHeadYaw / 2;
		this.neckrot.xRot = headPitch / 2;

		if (entity.inAirTimer < 5) {
			if (entity.getDeltaMovement().horizontalDistanceSqr() > 0.01) {
				this.animateWalk(BurodonAnimation.run, limbSwing, limbSwingAmount, 1.2f, 2.2f);
			} else {
				this.animateWalk(BurodonAnimation.walk, limbSwing, limbSwingAmount, 1.5f, 2.5f);
			}
		}
		this.animateSmooth(entity.idleAnimationState, BurodonAnimation.idle, ageInTicks, 1f);
		this.animateSmooth(entity.sitAnimationState, BurodonAnimation.sit, ageInTicks, 1f);
		this.animateSmooth(entity.airAnimationState, BurodonAnimation.air, ageInTicks, 1f);
		if (entity.getAnimState() > 0) {
			this.animateSmooth(entity.biteAnimationState, BurodonAnimation.bite, ageInTicks, 1f);
			this.animateSmooth(entity.jumpAnimationState, BurodonAnimation.jump, ageInTicks, 1f);
			this.animateSmooth(entity.roarAnimationState, BurodonAnimation.roar, ageInTicks, 1f);
			this.animateSmooth(entity.yawnAnimationState, BurodonAnimation.yawn, ageInTicks, 1f);
		}
	}


}