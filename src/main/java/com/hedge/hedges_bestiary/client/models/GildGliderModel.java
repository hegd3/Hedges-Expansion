package com.hedge.hedges_bestiary.client.models;// Made with Blockbench 5.0.7
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.hedge.hedges_bestiary.client.animations.GildGliderAnimation;
import com.hedge.hedges_bestiary.client.EntityLayers;
import com.hedge.hedges_bestiary.entity.living.ambientfish.GildGliderEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class GildGliderModel extends HBModel<GildGliderEntity> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = EntityLayers.GILD_GLIDER_LAYER;
	private final ModelPart root;
	private final ModelPart swimcontrol;
	private final ModelPart mouth;
	private final ModelPart leftfin;
	private final ModelPart leftfin2;
	private final ModelPart rightfin;
	private final ModelPart rightfin2;
	private final ModelPart leftbackfin;
	private final ModelPart rightbackfin;
	private final ModelPart tail;

	public GildGliderModel(ModelPart root) {
		this.root = root.getChild("root");
		this.swimcontrol = this.root.getChild("swimcontrol");
		this.mouth = this.swimcontrol.getChild("mouth");
		this.leftfin = this.swimcontrol.getChild("leftfin");
		this.leftfin2 = this.leftfin.getChild("leftfin2");
		this.rightfin = this.swimcontrol.getChild("rightfin");
		this.rightfin2 = this.rightfin.getChild("rightfin2");
		this.leftbackfin = this.swimcontrol.getChild("leftbackfin");
		this.rightbackfin = this.swimcontrol.getChild("rightbackfin");
		this.tail = this.swimcontrol.getChild("tail");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 22.0F, 0.0F));

		PartDefinition swimcontrol = root.addOrReplaceChild("swimcontrol", CubeListBuilder.create().texOffs(0, 0).addBox(-5.5F, -4.0F, -8.0F, 11.0F, 4.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.0F));

		PartDefinition mouth = swimcontrol.addOrReplaceChild("mouth", CubeListBuilder.create().texOffs(32, 37).addBox(-2.5F, -4.0F, -2.0F, 5.0F, 4.0F, 2.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, -8.0F));

		PartDefinition leftfin = swimcontrol.addOrReplaceChild("leftfin", CubeListBuilder.create().texOffs(0, 34).addBox(0.0F, 0.0F, -3.5F, 9.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(5.5F, -1.0F, -4.5F));

		PartDefinition leftfin2 = leftfin.addOrReplaceChild("leftfin2", CubeListBuilder.create().texOffs(34, 18).addBox(0.0F, 0.0F, -3.5F, 5.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(9.0F, 0.0F, 0.0F));

		PartDefinition rightfin = swimcontrol.addOrReplaceChild("rightfin", CubeListBuilder.create().texOffs(0, 34).mirror().addBox(-9.0F, 0.0F, -3.5F, 9.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-5.5F, -1.0F, -4.5F));

		PartDefinition rightfin2 = rightfin.addOrReplaceChild("rightfin2", CubeListBuilder.create().texOffs(34, 18).mirror().addBox(-5.0F, 0.0F, -3.5F, 5.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-9.0F, 0.0F, 0.0F));

		PartDefinition leftbackfin = swimcontrol.addOrReplaceChild("leftbackfin", CubeListBuilder.create().texOffs(34, 24).addBox(0.0F, 0.0F, -1.5F, 8.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(5.5F, -1.0F, 4.5F));

		PartDefinition rightbackfin = swimcontrol.addOrReplaceChild("rightbackfin", CubeListBuilder.create().texOffs(34, 24).mirror().addBox(-8.0F, 0.0F, -1.5F, 8.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-5.5F, -1.0F, 4.5F));

		PartDefinition tail = swimcontrol.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 18).addBox(-1.5F, -1.0F, 0.0F, 3.0F, 2.0F, 14.0F, new CubeDeformation(0.01F))
		.texOffs(34, 27).addBox(0.0F, -5.0F, 1.0F, 0.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, 6.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}

	@Override
	public void setupAnim(GildGliderEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		netHeadYaw = Mth.clamp(netHeadYaw, -55.0F, 55.0F) * ((float) Math.PI / 180F);
		headPitch = Mth.clamp(headPitch, -45.0F, 45.0F) * ((float) Math.PI / 180F);
		if (entity.isInFluidType()) {
			this.swimcontrol.xRot = headPitch;
			this.swimcontrol.zRot = entity.roll * 2;
			this.animate(entity.idleAnimationState, GildGliderAnimation.idle, ageInTicks, 0.4f);
			this.animateWalk(GildGliderAnimation.swim, limbSwing, limbSwingAmount, 1f, 1.5f);
		} else if (entity.groundTimer == 0) {
			this.swimcontrol.xRot = headPitch;
			this.animate(entity.idleAnimationState, GildGliderAnimation.air, ageInTicks, 1 + limbSwingAmount);
		} else {
			this.animate(entity.idleAnimationState, GildGliderAnimation.flop, ageInTicks, 1f);
		}
	}
}