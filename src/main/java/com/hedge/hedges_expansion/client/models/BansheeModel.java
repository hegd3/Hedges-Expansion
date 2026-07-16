package com.hedge.hedges_expansion.client.models;// Made with Blockbench 5.1.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.hedge.hedges_expansion.client.animations.BansheeAnimation;
import com.hedge.hedges_expansion.client.layer.EntityLayers;
import com.hedge.hedges_expansion.entity.living.BansheeEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class BansheeModel extends HEModel<BansheeEntity> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = EntityLayers.BANSHEE_LAYER;
	private final ModelPart root;
	private final ModelPart flycontrol;
	private final ModelPart jaw;
	private final ModelPart leftwing;
	private final ModelPart leftwing2;
	private final ModelPart rightwing;
	private final ModelPart rightwing2;
	private final ModelPart tail;
	private final ModelPart leftwing3;
	private final ModelPart rightwing3;
	private final ModelPart tail2;
	private final ModelPart tail3;

	public BansheeModel(ModelPart root) {
		this.root = root.getChild("root");
		this.flycontrol = this.root.getChild("flycontrol");
		this.jaw = this.flycontrol.getChild("jaw");
		this.leftwing = this.flycontrol.getChild("leftwing");
		this.leftwing2 = this.leftwing.getChild("leftwing2");
		this.rightwing = this.flycontrol.getChild("rightwing");
		this.rightwing2 = this.rightwing.getChild("rightwing2");
		this.tail = this.flycontrol.getChild("tail");
		this.leftwing3 = this.tail.getChild("leftwing3");
		this.rightwing3 = this.tail.getChild("rightwing3");
		this.tail2 = this.tail.getChild("tail2");
		this.tail3 = this.tail2.getChild("tail3");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 15.5F, -9.0F));

		PartDefinition flycontrol = root.addOrReplaceChild("flycontrol", CubeListBuilder.create().texOffs(0, 0).addBox(-14.5F, -8.5F, -11.0F, 29.0F, 17.0F, 22.0F, new CubeDeformation(0.0F))
				.texOffs(82, 87).addBox(-13.5F, -8.5F, -14.0F, 27.0F, 17.0F, 3.0F, new CubeDeformation(0.01F))
				.texOffs(88, 107).addBox(13.5F, -8.5F, -15.0F, 0.0F, 17.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(88, 107).mirror().addBox(-13.5F, -8.5F, -15.0F, 0.0F, 17.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(90, 71).addBox(-13.5F, -8.5F, -16.0F, 27.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition jaw = flycontrol.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(90, 39).addBox(-16.5F, -16.0F, -3.0F, 25.0F, 16.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(112, 106).addBox(8.5F, -16.0F, 0.0F, 0.0F, 16.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(112, 106).mirror().addBox(-16.5F, -16.0F, 0.0F, 0.0F, 16.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(102, 26).addBox(-16.5F, -16.0F, 0.0F, 25.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 8.5F, -14.0F));

		PartDefinition leftwing = flycontrol.addOrReplaceChild("leftwing", CubeListBuilder.create().texOffs(0, 73).addBox(0.0F, -1.5F, -8.5F, 24.0F, 3.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offset(14.5F, 5.0F, 4.5F));

		PartDefinition leftwing2 = leftwing.addOrReplaceChild("leftwing2", CubeListBuilder.create().texOffs(90, 58).addBox(0.0F, 0.0F, -5.5F, 20.0F, 2.0F, 11.0F, new CubeDeformation(0.01F))
				.texOffs(82, 73).addBox(2.0F, 1.0F, 0.5F, 20.0F, 0.0F, 14.0F, new CubeDeformation(0.01F)), PartPose.offset(24.0F, -1.5F, -3.0F));

		PartDefinition rightwing = flycontrol.addOrReplaceChild("rightwing", CubeListBuilder.create().texOffs(0, 73).mirror().addBox(-24.0F, -1.5F, -8.5F, 24.0F, 3.0F, 17.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-14.5F, 5.0F, 4.5F));

		PartDefinition rightwing2 = rightwing.addOrReplaceChild("rightwing2", CubeListBuilder.create().texOffs(90, 58).mirror().addBox(-20.0F, 0.0F, -5.5F, 20.0F, 2.0F, 11.0F, new CubeDeformation(0.01F)).mirror(false)
				.texOffs(82, 73).mirror().addBox(-22.0F, 1.0F, 0.5F, 20.0F, 0.0F, 14.0F, new CubeDeformation(0.01F)).mirror(false), PartPose.offset(-24.0F, -1.5F, -3.0F));

		PartDefinition tail = flycontrol.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 39).addBox(-11.5F, -6.0F, 0.0F, 23.0F, 12.0F, 22.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 11.0F));

		PartDefinition leftwing3 = tail.addOrReplaceChild("leftwing3", CubeListBuilder.create().texOffs(50, 107).addBox(0.0F, -1.5F, -4.5F, 11.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(50, 99).addBox(11.0F, -1.5F, -4.5F, 3.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(50, 93).addBox(3.0F, -0.5F, 3.5F, 7.0F, 0.0F, 6.0F, new CubeDeformation(0.01F)), PartPose.offset(11.5F, 4.5F, 19.5F));

		PartDefinition rightwing3 = tail.addOrReplaceChild("rightwing3", CubeListBuilder.create().texOffs(50, 107).mirror().addBox(-11.0F, -1.5F, -4.5F, 11.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(50, 99).mirror().addBox(-14.0F, -1.5F, -4.5F, 3.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(50, 93).mirror().addBox(-10.0F, -0.5F, 3.5F, 7.0F, 0.0F, 6.0F, new CubeDeformation(0.01F)).mirror(false), PartPose.offset(-11.5F, 4.5F, 19.5F));

		PartDefinition tail2 = tail.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(0, 93).addBox(-5.5F, -5.0F, 0.0F, 11.0F, 10.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 22.0F));

		PartDefinition tail3 = tail2.addOrReplaceChild("tail3", CubeListBuilder.create().texOffs(102, 0).addBox(-2.5F, -4.0F, 0.0F, 5.0F, 7.0F, 19.0F, new CubeDeformation(0.0F))
				.texOffs(102, 28).addBox(-4.5F, 0.0F, 14.0F, 9.0F, 0.0F, 11.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, 14.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}


	@Override
	public ModelPart root() {
		return this.root;
	}

	@Override
	public void setupAnim(BansheeEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);

		this.animateSmooth(entity.idleAnimationState, BansheeAnimation.fly, ageInTicks, 0.7f + limbSwingAmount * 0.5f);
		this.animate(entity.spinAnimationState, entity.getLeft() ? BansheeAnimation.roll_left : BansheeAnimation.roll_right, ageInTicks);
		this.animateSmooth(entity.windupAnimationState, BansheeAnimation.scream_windup, ageInTicks, 1);
		this.animateSmooth(entity.screamAnimationState, BansheeAnimation.scream, ageInTicks, 1);
		this.animateSmooth(entity.diveAnimationState, BansheeAnimation.dive, ageInTicks, 1 + limbSwingAmount * 0.5f);


		float tailYaw = entity.getTrailYaw(ageInTicks - entity.tickCount);
		this.tail.yRot = Mth.lerp(0.3F, this.tail.yRot, tailYaw * 0.3F);
		this.tail2.yRot = Mth.lerp(0.3F, this.tail2.yRot, tailYaw * 0.25F);
		this.tail3.yRot = Mth.lerp(0.3F, this.tail3.yRot, tailYaw * 0.15F);
		this.flycontrol.xRot = Mth.clamp(headPitch, -35.0F, 35.0F) * ((float) Math.PI / 180F);
		this.flycontrol.zRot = entity.roll * 2f;
	}
}