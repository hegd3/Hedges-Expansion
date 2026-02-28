package com.hedge.hedges_expansion.client.models;


import com.hedge.hedges_expansion.client.animations.SmarmAnimation;
import com.hedge.hedges_expansion.client.layer.EntityLayers;
import com.hedge.hedges_expansion.entity.living.ambientfish.SmarmEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;


public class SmarmModel extends HierarchicalModel<SmarmEntity> {
	public static final ModelLayerLocation LAYER_LOCATION = EntityLayers.SMARM_LAYER;
	private final ModelPart root;
	private final ModelPart swimcontrol;
	private final ModelPart tail;
	private final ModelPart leftfin;
	private final ModelPart rightfin;

	public SmarmModel(ModelPart root) {
		this.root = root.getChild("root");
		this.swimcontrol = this.root.getChild("swimcontrol");
		this.tail = this.swimcontrol.getChild("tail");
		this.leftfin = this.swimcontrol.getChild("leftfin");
		this.rightfin = this.swimcontrol.getChild("rightfin");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 22.5F, 0.0F));

		PartDefinition swimcontrol = root.addOrReplaceChild("swimcontrol", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -4.5F, -7.0F, 3.0F, 6.0F, 11.0F, new CubeDeformation(0.0F))
		.texOffs(0, 17).addBox(0.0F, -8.5F, -4.0F, 0.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(26, 27).addBox(0.0F, 1.5F, 1.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition tail = swimcontrol.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(16, 17).addBox(0.0F, -4.5F, 0.0F, 0.0F, 8.0F, 5.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, -1.0F, 4.0F));

		PartDefinition leftfin = swimcontrol.addOrReplaceChild("leftfin", CubeListBuilder.create().texOffs(26, 17).addBox(0.0F, 0.0F, -1.5F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.01F)), PartPose.offset(1.5F, 1.5F, -1.5F));

		PartDefinition rightfin = swimcontrol.addOrReplaceChild("rightfin", CubeListBuilder.create().texOffs(26, 17).addBox(0.0F, 0.0F, -1.5F, 0.0F, 2.0F, 3.0F, new CubeDeformation(0.01F)), PartPose.offset(-1.5F, 1.5F, -1.5F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}

	@Override
	public void setupAnim(SmarmEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		headPitch = Mth.clamp(headPitch, -45.0F, 45.0F) * ((float) Math.PI / 180F);
		if (entity.isInFluidType()) {
			this.swimcontrol.xRot = headPitch;
			this.animate(entity.idleAnimationState, SmarmAnimation.swim, ageInTicks, 0.5f + limbSwingAmount);
		} else {
			this.animate(entity.idleAnimationState, SmarmAnimation.flop, ageInTicks, 1);
		}
	}
}