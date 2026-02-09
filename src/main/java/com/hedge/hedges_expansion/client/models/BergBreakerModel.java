package com.hedge.hedges_expansion.client.models;// Made with Blockbench 5.0.7
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.hedge.hedges_expansion.client.animations.BergBreakerAnimation;
import com.hedge.hedges_expansion.client.layer.EntityLayers;
import com.hedge.hedges_expansion.entity.living.BergBreakerEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class BergBreakerModel extends HierarchicalModel<BergBreakerEntity> {
	public static final ModelLayerLocation LAYER_LOCATION = EntityLayers.BERG_BREAKER_LAYER;
	private final ModelPart root;
	private final ModelPart swimcontrol;
	private final ModelPart headrot;
	private final ModelPart head;
	private final ModelPart jaw;
	private final ModelPart leftleg;
	private final ModelPart rightleg;
	private final ModelPart leftleg2;
	private final ModelPart rightleg2;
	private final ModelPart body;
	private final ModelPart tail;

	public BergBreakerModel(ModelPart root) {
		this.root = root.getChild("root");
		this.swimcontrol = this.root.getChild("swimcontrol");
		this.headrot = this.swimcontrol.getChild("headrot");
		this.head = this.headrot.getChild("head");
		this.jaw = this.head.getChild("jaw");
		this.leftleg = this.swimcontrol.getChild("leftleg");
		this.rightleg = this.swimcontrol.getChild("rightleg");
		this.leftleg2 = this.swimcontrol.getChild("leftleg2");
		this.rightleg2 = this.swimcontrol.getChild("rightleg2");
		this.body = this.swimcontrol.getChild("body");
		this.tail = this.swimcontrol.getChild("tail");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

		PartDefinition swimcontrol = root.addOrReplaceChild("swimcontrol", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition headrot = swimcontrol.addOrReplaceChild("headrot", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, -16.0F));

		PartDefinition head = headrot.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 81).addBox(-8.5F, -3.0F, -17.0F, 17.0F, 4.0F, 17.0F, new CubeDeformation(0.0F))
		.texOffs(108, 0).addBox(-7.5F, 1.0F, -16.0F, 15.0F, 2.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(0, 54).addBox(-9.5F, -12.0F, -18.0F, 19.0F, 9.0F, 18.0F, new CubeDeformation(0.0F))
		.texOffs(0, 102).addBox(-9.5F, -15.0F, -18.0F, 19.0F, 3.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition jaw = head.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(68, 83).addBox(-8.5F, 0.0F, -17.0F, 17.0F, 3.0F, 17.0F, new CubeDeformation(0.0F))
		.texOffs(108, 40).addBox(-6.5F, -2.0F, -15.0F, 13.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 0.0F));

		PartDefinition leftleg = swimcontrol.addOrReplaceChild("leftleg", CubeListBuilder.create().texOffs(66, 103).addBox(0.0F, -1.5F, -7.0F, 17.0F, 3.0F, 14.0F, new CubeDeformation(0.01F)), PartPose.offset(11.5F, 10.5F, -11.0F));

		PartDefinition rightleg = swimcontrol.addOrReplaceChild("rightleg", CubeListBuilder.create().texOffs(66, 103).mirror().addBox(-17.0F, -1.5F, -7.0F, 17.0F, 3.0F, 14.0F, new CubeDeformation(0.01F)).mirror(false), PartPose.offset(-11.5F, 10.5F, -11.0F));

		PartDefinition leftleg2 = swimcontrol.addOrReplaceChild("leftleg2", CubeListBuilder.create().texOffs(0, 119).addBox(0.0F, -0.5F, -5.0F, 9.0F, 2.0F, 9.0F, new CubeDeformation(0.01F)), PartPose.offset(11.5F, 10.5F, 10.0F));

		PartDefinition rightleg2 = swimcontrol.addOrReplaceChild("rightleg2", CubeListBuilder.create().texOffs(0, 119).mirror().addBox(-9.0F, -0.5F, -5.0F, 9.0F, 2.0F, 9.0F, new CubeDeformation(0.01F)).mirror(false), PartPose.offset(-11.5F, 10.5F, 10.0F));

		PartDefinition body = swimcontrol.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-11.5F, -11.5F, -15.5F, 23.0F, 23.0F, 31.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.5F, -0.5F));

		PartDefinition tail = swimcontrol.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(74, 54).addBox(-7.5F, -8.0F, 0.0F, 15.0F, 13.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(108, 12).addBox(0.0F, -11.0F, 2.0F, 0.0F, 11.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, 15.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}

	@Override
	public void setupAnim(BergBreakerEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);

		netHeadYaw = Mth.clamp(netHeadYaw, -25.0F, 25.0F) * ((float)Math.PI / 180F);
		headPitch = Mth.clamp(headPitch, -25.0F, 25.0F) * ((float)Math.PI / 180F);

		this.swimcontrol.xRot = entity.isInFluidType() ? headPitch * 0.9f : 0;

		this.headrot.yRot = netHeadYaw;
		this.headrot.xRot = headPitch;
		if (entity.isInFluidType()) {
			this.animateWalk(BergBreakerAnimation.swim, limbSwing, limbSwingAmount, 1.2f, 2.5f);
			this.animate(entity.idleAnimationState, BergBreakerAnimation.swim_idle, ageInTicks, 0.5f);
		} else {
			this.animateWalk(BergBreakerAnimation.walk, limbSwing, limbSwingAmount, 2.5f, 5f);
			this.animate(entity.idleAnimationState, BergBreakerAnimation.idle, ageInTicks, 0.5f);
		}
	}
}