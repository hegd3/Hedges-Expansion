package com.hedge.hedges_bestiary.client.models;// Made with Blockbench 5.1.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.hedge.hedges_bestiary.client.animations.ChubAnimation;
import com.hedge.hedges_bestiary.client.EntityLayers;
import com.hedge.hedges_bestiary.entity.living.ambientfish.ChubEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class ChubModel extends HBModel<ChubEntity> {
	public static final ModelLayerLocation LAYER_LOCATION = EntityLayers.CHUB_LAYER;
	private final ModelPart root;
	private final ModelPart swimcontrol;
	private final ModelPart leftfin;
	private final ModelPart rightfin;
	private final ModelPart tail;
	private final ModelPart leftfin2;
	private final ModelPart rightfin2;
	private final ModelPart tail2;

	public ChubModel(ModelPart root) {
		this.root = root.getChild("root");
		this.swimcontrol = this.root.getChild("swimcontrol");
		this.leftfin = this.swimcontrol.getChild("leftfin");
		this.rightfin = this.swimcontrol.getChild("rightfin");
		this.tail = this.swimcontrol.getChild("tail");
		this.leftfin2 = this.tail.getChild("leftfin2");
		this.rightfin2 = this.tail.getChild("rightfin2");
		this.tail2 = this.tail.getChild("tail2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 20.0F, 2.0F));

		PartDefinition swimcontrol = root.addOrReplaceChild("swimcontrol", CubeListBuilder.create().texOffs(0, 0).addBox(-4.5F, -5.0F, -8.0F, 9.0F, 9.0F, 8.0F, new CubeDeformation(0.01F))
		.texOffs(32, 29).addBox(-0.01F, -9.0F, -5.0F, 0.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition leftfin = swimcontrol.addOrReplaceChild("leftfin", CubeListBuilder.create().texOffs(10, 33).addBox(0.0F, 0.0F, -2.0F, 0.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(4.5F, 4.0F, -2.0F));

		PartDefinition rightfin = swimcontrol.addOrReplaceChild("rightfin", CubeListBuilder.create().texOffs(10, 33).mirror().addBox(0.0F, 0.0F, -2.0F, 0.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-4.5F, 4.0F, -2.0F));

		PartDefinition tail = swimcontrol.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 17).addBox(-4.5F, -5.0F, 0.0F, 9.0F, 9.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(0, 33).addBox(-0.01F, -8.0F, 0.0F, 0.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition leftfin2 = tail.addOrReplaceChild("leftfin2", CubeListBuilder.create().texOffs(18, 33).addBox(0.0F, 0.0F, -2.0F, 0.0F, 2.0F, 4.0F, new CubeDeformation(0.01F)), PartPose.offset(4.5F, 4.0F, 5.0F));

		PartDefinition rightfin2 = tail.addOrReplaceChild("rightfin2", CubeListBuilder.create().texOffs(18, 33).mirror().addBox(0.0F, 0.0F, -2.0F, 0.0F, 2.0F, 4.0F, new CubeDeformation(0.01F)).mirror(false), PartPose.offset(-4.5F, 4.0F, 5.0F));

		PartDefinition tail2 = tail.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(32, 17).addBox(0.0F, -4.0F, 0.0F, 0.0F, 7.0F, 5.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, 7.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}

	@Override
	public void setupAnim(ChubEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);

		if (entity.isInFluidType()) {
			this.swimcontrol.xRot = Mth.clamp(headPitch, -45.0F, 45.0F) * ((float) Math.PI / 180F);
			this.swimcontrol.zRot = entity.roll * 2;
			this.animate(entity.idleAnimationState, ChubAnimation.idle, ageInTicks, 0.2f + limbSwingAmount * 0.5f);
			this.animateWalk(ChubAnimation.swim, limbSwing, limbSwingAmount, 1.5f, 2.5f);
		} else {
			this.animate(entity.idleAnimationState, ChubAnimation.flop, ageInTicks, 1);
		}
	}
}