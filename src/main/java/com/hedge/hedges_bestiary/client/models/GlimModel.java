package com.hedge.hedges_bestiary.client.models;// Made with Blockbench 5.0.7
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.hedge.hedges_bestiary.client.animations.GlimAnimation;
import com.hedge.hedges_bestiary.entity.living.ambientfish.GlimEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class GlimModel extends HBModel<GlimEntity> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "glim"), "main");
	private final ModelPart root;
	private final ModelPart swimcontrol;
	private final ModelPart lefttentacle1;
	private final ModelPart righttentacle1;
	private final ModelPart lefttentacle2;
	private final ModelPart righttentacle2;
	private final ModelPart lefttentacle3;
	private final ModelPart righttentacle3;
	private final ModelPart lefttentacle4;
	private final ModelPart righttentacle4;
	private final ModelPart leftmantle;
	private final ModelPart rightmantle;

	public GlimModel(ModelPart root) {
		this.root = root.getChild("root");
		this.swimcontrol = this.root.getChild("swimcontrol");
		this.lefttentacle1 = this.swimcontrol.getChild("lefttentacle1");
		this.righttentacle1 = this.swimcontrol.getChild("righttentacle1");
		this.lefttentacle2 = this.swimcontrol.getChild("lefttentacle2");
		this.righttentacle2 = this.swimcontrol.getChild("righttentacle2");
		this.lefttentacle3 = this.swimcontrol.getChild("lefttentacle3");
		this.righttentacle3 = this.swimcontrol.getChild("righttentacle3");
		this.lefttentacle4 = this.swimcontrol.getChild("lefttentacle4");
		this.righttentacle4 = this.swimcontrol.getChild("righttentacle4");
		this.leftmantle = this.swimcontrol.getChild("leftmantle");
		this.rightmantle = this.swimcontrol.getChild("rightmantle");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition swimcontrol = root.addOrReplaceChild("swimcontrol", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -6.0F, -7.0F, 7.0F, 6.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(0, 16).addBox(-3.5F, -4.0F, -15.0F, 7.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition lefttentacle1 = swimcontrol.addOrReplaceChild("lefttentacle1", CubeListBuilder.create().texOffs(26, 26).addBox(-0.5F, -5.0F, 0.0F, 3.0F, 5.0F, 0.0F, new CubeDeformation(0.01F)), PartPose.offset(3.0F, -6.0F, 3.0F));

		PartDefinition righttentacle1 = swimcontrol.addOrReplaceChild("righttentacle1", CubeListBuilder.create().texOffs(26, 26).mirror().addBox(-2.5F, -5.0F, 0.0F, 3.0F, 5.0F, 0.0F, new CubeDeformation(0.01F)).mirror(false), PartPose.offset(-3.0F, -6.0F, 3.0F));

		PartDefinition lefttentacle2 = swimcontrol.addOrReplaceChild("lefttentacle2", CubeListBuilder.create().texOffs(26, 24).addBox(0.0F, -1.0F, 0.0F, 5.0F, 2.0F, 0.0F, new CubeDeformation(0.01F)), PartPose.offset(3.5F, -3.0F, 3.0F));

		PartDefinition righttentacle2 = swimcontrol.addOrReplaceChild("righttentacle2", CubeListBuilder.create().texOffs(26, 24).mirror().addBox(-5.0F, -1.0F, 0.0F, 5.0F, 2.0F, 0.0F, new CubeDeformation(0.01F)).mirror(false), PartPose.offset(-3.5F, -3.0F, 3.0F));

		PartDefinition lefttentacle3 = swimcontrol.addOrReplaceChild("lefttentacle3", CubeListBuilder.create().texOffs(30, 16).addBox(-0.5F, 0.0F, 0.0F, 3.0F, 5.0F, 0.0F, new CubeDeformation(0.01F)), PartPose.offset(3.0F, 0.0F, 3.0F));

		PartDefinition righttentacle3 = swimcontrol.addOrReplaceChild("righttentacle3", CubeListBuilder.create().texOffs(30, 16).mirror().addBox(-2.5F, 0.0F, 0.0F, 3.0F, 5.0F, 0.0F, new CubeDeformation(0.01F)).mirror(false), PartPose.offset(-3.0F, 0.0F, 3.0F));

		PartDefinition lefttentacle4 = swimcontrol.addOrReplaceChild("lefttentacle4", CubeListBuilder.create().texOffs(26, 31).addBox(-0.5F, 0.0F, 0.0F, 2.0F, 6.0F, 0.0F, new CubeDeformation(0.01F)), PartPose.offset(1.0F, 0.0F, 3.0F));

		PartDefinition righttentacle4 = swimcontrol.addOrReplaceChild("righttentacle4", CubeListBuilder.create().texOffs(26, 31).mirror().addBox(-1.5F, 0.0F, 0.0F, 2.0F, 6.0F, 0.0F, new CubeDeformation(0.01F)).mirror(false), PartPose.offset(-1.0F, 0.0F, 3.0F));

		PartDefinition leftmantle = swimcontrol.addOrReplaceChild("leftmantle", CubeListBuilder.create().texOffs(0, 24).addBox(0.0F, 0.0F, -6.0F, 4.0F, 0.0F, 9.0F, new CubeDeformation(0.01F)), PartPose.offset(3.5F, -4.0F, -4.0F));

		PartDefinition rightmantle = swimcontrol.addOrReplaceChild("rightmantle", CubeListBuilder.create().texOffs(0, 24).mirror().addBox(-4.0F, 0.0F, -6.0F, 4.0F, 0.0F, 9.0F, new CubeDeformation(0.01F)).mirror(false), PartPose.offset(-3.5F, -4.0F, -4.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}

	@Override
	public void setupAnim(GlimEntity entity, float pLimbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);

		if (entity.isInFluidType()) {
			this.swimcontrol.xRot = Mth.clamp(headPitch, -45.0F, 45.0F) * ((float) Math.PI / 180F);
			this.swimcontrol.zRot = entity.roll * 2;
			this.animate(entity.idleAnimationState, GlimAnimation.swim, ageInTicks, 0.2f + limbSwingAmount);
		} else {
			this.animate(entity.idleAnimationState, GlimAnimation.flop, ageInTicks, 1);
		}
	}
}