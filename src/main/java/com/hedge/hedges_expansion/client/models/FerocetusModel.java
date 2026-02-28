package com.hedge.hedges_expansion.client.models;// Made with Blockbench 5.0.7
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.hedge.hedges_expansion.client.animations.FerocetusAnimation;
import com.hedge.hedges_expansion.client.layer.EntityLayers;
import com.hedge.hedges_expansion.entity.living.FerocetusEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class FerocetusModel extends HierarchicalModel<FerocetusEntity> {
	public static final ModelLayerLocation LAYER_LOCATION = EntityLayers.FEROCETUS_LAYER;
	private final ModelPart root;
	private final ModelPart swimcontrol;
	private final ModelPart jaw;
	private final ModelPart fin;
	private final ModelPart leftfin;
	private final ModelPart rightfin;
	private final ModelPart tailrot;
	private final ModelPart tail;
	private final ModelPart tail2rot;
	private final ModelPart tail2;

	public FerocetusModel(ModelPart root) {
		this.root = root.getChild("root");
		this.swimcontrol = this.root.getChild("swimcontrol");
		this.jaw = this.swimcontrol.getChild("jaw");
		this.fin = this.swimcontrol.getChild("fin");
		this.leftfin = this.swimcontrol.getChild("leftfin");
		this.rightfin = this.swimcontrol.getChild("rightfin");
		this.tailrot = this.swimcontrol.getChild("tailrot");
		this.tail = this.tailrot.getChild("tail");
		this.tail2rot = this.tail.getChild("tail2rot");
		this.tail2 = this.tail2rot.getChild("tail2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 12.0F, 0.0F));

		PartDefinition swimcontrol = root.addOrReplaceChild("swimcontrol", CubeListBuilder.create().texOffs(0, 0).addBox(-12.5F, -13.0F, -16.0F, 25.0F, 27.0F, 35.0F, new CubeDeformation(0.0F))
		.texOffs(120, 37).addBox(4.5F, -16.0F, -15.0F, 8.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(120, 37).mirror().addBox(-12.5F, -16.0F, -15.0F, 8.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 88).addBox(-9.5F, -13.0F, -34.0F, 19.0F, 17.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 0.0F));

		PartDefinition jaw = swimcontrol.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(98, 62).addBox(-9.5F, 0.0F, -18.0F, 19.0F, 7.0F, 18.0F, new CubeDeformation(0.01F))
		.texOffs(120, 21).addBox(-9.5F, -3.0F, -18.0F, 19.0F, 3.0F, 13.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 4.0F, -16.0F));

		PartDefinition fin = swimcontrol.addOrReplaceChild("fin", CubeListBuilder.create().texOffs(0, 123).addBox(-3.5F, -14.0F, 0.0F, 7.0F, 14.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -13.0F, -2.0F));

		PartDefinition leftfin = swimcontrol.addOrReplaceChild("leftfin", CubeListBuilder.create().texOffs(122, 2).addBox(0.0F, -1.5F, -9.0F, 18.0F, 3.0F, 16.0F, new CubeDeformation(0.01F)), PartPose.offset(12.5F, 12.5F, -4.0F));

		PartDefinition rightfin = swimcontrol.addOrReplaceChild("rightfin", CubeListBuilder.create().texOffs(122, 2).mirror().addBox(-18.0F, -1.5F, -9.0F, 18.0F, 3.0F, 16.0F, new CubeDeformation(0.01F)).mirror(false), PartPose.offset(-12.5F, 12.5F, -4.0F));

		PartDefinition tailrot = swimcontrol.addOrReplaceChild("tailrot", CubeListBuilder.create(), PartPose.offset(0.0F, -5.0F, 19.0F));

		PartDefinition tail = tailrot.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(77, 91).addBox(-7.5F, -8.0F, 0.0F, 15.0F, 19.0F, 16.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition tail2rot = tail.addOrReplaceChild("tail2rot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 14.0F));

		PartDefinition tail2 = tail2rot.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(0, 62).addBox(-14.5F, -3.0F, 0.0F, 29.0F, 6.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}



	@Override
	public ModelPart root() {
		return this.root;
	}

	@Override
	public void setupAnim(FerocetusEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		headPitch = Mth.clamp(headPitch, -45.0F, 45.0F) * ((float) Math.PI / 180F);
		this.tailrot.yRot = -(entity.tilt * (Mth.DEG_TO_RAD) / 1.5f);
		this.tail2rot.yRot = -(entity.tilt * (Mth.DEG_TO_RAD) / 1.5f);

		if (entity.isInFluidType()) {
			this.swimcontrol.xRot = headPitch;
			this.animate(entity.idleAnimationState, FerocetusAnimation.idle, ageInTicks, 0.5f);
			this.animateWalk(FerocetusAnimation.swim, limbSwing, limbSwingAmount, 1.3f, 1f);
		} else {
			if (entity.groundTimer == 0 ) {
				this.swimcontrol.xRot = headPitch;
				this.tailrot.xRot = -headPitch * 0.5f;
				this.tail2rot.xRot = -headPitch * 0.7f;
				this.animate(entity.idleAnimationState, FerocetusAnimation.air, ageInTicks, 0.5f);
			} else {
				this.animate(entity.idleAnimationState, FerocetusAnimation.beached, ageInTicks, 0.5f);
			}
		}
	}
}