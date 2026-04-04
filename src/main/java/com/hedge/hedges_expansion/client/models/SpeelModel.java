package com.hedge.hedges_expansion.client.models;


import com.hedge.hedges_expansion.client.animations.SpeelAnimation;
import com.hedge.hedges_expansion.client.layer.EntityLayers;
import com.hedge.hedges_expansion.entity.living.ambientfish.SpeelEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class SpeelModel extends HierarchicalModel<SpeelEntity> {
	public static final ModelLayerLocation LAYER_LOCATION = EntityLayers.SPEEL_LAYER;
	private final ModelPart root;
	private final ModelPart swimcontrol;
	private final ModelPart tail;
	private final ModelPart tailrot;
	private final ModelPart tail2;
	private final ModelPart tail2rot;
	private final ModelPart tail3;
	private final ModelPart tail3rot;
	private final ModelPart tail4;
	private final ModelPart tail4rot;
	private final ModelPart head;

	public SpeelModel(ModelPart root) {
		this.root = root.getChild("root");
		this.swimcontrol = this.root.getChild("swimcontrol");
		this.tail = this.swimcontrol.getChild("tail");
		this.tailrot = this.tail.getChild("tailrot");
		this.tail2 = this.tailrot.getChild("tail2");
		this.tail2rot = this.tail2.getChild("tail2rot");
		this.tail3 = this.tail2rot.getChild("tail3");
		this.tail3rot = this.tail3.getChild("tail3rot");
		this.tail4 = this.tail3rot.getChild("tail4");
		this.tail4rot = this.tail4.getChild("tail4rot");
		this.head = this.swimcontrol.getChild("head");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, -6.0F));

		PartDefinition swimcontrol = root.addOrReplaceChild("swimcontrol", CubeListBuilder.create().texOffs(28, 33).addBox(0.0F, -9.0F, -11.0F, 0.0F, 6.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(14, 28).addBox(0.0F, 3.0F, -9.0F, 0.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-2.5F, -3.0F, -12.0F, 5.0F, 6.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, 5.0F));

		PartDefinition tail = swimcontrol.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -4.0F));

		PartDefinition tailrot = tail.addOrReplaceChild("tailrot", CubeListBuilder.create().texOffs(0, 36).addBox(0.0F, 3.0F, 1.0F, 0.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(0, 14).addBox(-2.5F, -3.0F, 0.0F, 5.0F, 6.0F, 8.0F, new CubeDeformation(0.01F))
				.texOffs(26, 27).addBox(0.0F, -8.0F, 0.0F, 0.0F, 5.0F, 8.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition tail2 = tailrot.addOrReplaceChild("tail2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 8.0F));

		PartDefinition tail2rot = tail2.addOrReplaceChild("tail2rot", CubeListBuilder.create().texOffs(0, 36).addBox(0.0F, 3.0F, 1.0F, 0.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(0, 14).addBox(-2.5F, -3.0F, 0.0F, 5.0F, 6.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(26, 27).addBox(0.0F, -8.0F, 0.0F, 0.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition tail3 = tail2rot.addOrReplaceChild("tail3", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 8.0F));

		PartDefinition tail3rot = tail3.addOrReplaceChild("tail3rot", CubeListBuilder.create().texOffs(20, 40).addBox(0.0F, -5.0F, 0.0F, 0.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(26, 0).addBox(-1.5F, -2.0F, 0.0F, 3.0F, 4.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition tail4 = tail3rot.addOrReplaceChild("tail4", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, 9.0F));

		PartDefinition tail4rot = tail4.addOrReplaceChild("tail4rot", CubeListBuilder.create().texOffs(12, 36).addBox(0.0F, -1.0F, 0.0F, 0.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition head = swimcontrol.addOrReplaceChild("head", CubeListBuilder.create().texOffs(39, 49).addBox(-1.5F, -2.0F, -7.0F, 3.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(11, 51).addBox(-2.5F, 2.0F, -8.0F, 5.0F, 1.0F, 8.0F, new CubeDeformation(0.01F))
				.texOffs(35, 24).addBox(-2.5F, 3.0F, -8.0F, 5.0F, 1.0F, 7.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, -12.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}

	@Override
	public void setupAnim(SpeelEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		headPitch = Mth.clamp(headPitch, -25.0F, 25.0F) * ((float) Math.PI / 180F);
		float tailYaw = entity.getTrailYaw(ageInTicks - entity.tickCount);
		this.tailrot.yRot = Mth.lerp(0.3F, this.tailrot.yRot, tailYaw * 0.2F);
		this.tail2rot.yRot = Mth.lerp(0.25F, this.tail2rot.yRot, tailYaw * 0.3F);
		this.tail3rot.yRot = Mth.lerp(0.2F, this.tail3rot.yRot, tailYaw * 0.4F);
		this.tail4rot.yRot = Mth.lerp(0.15F, this.tail4rot.yRot, tailYaw * 0.5F);

		this.animate(entity.idleAnimationState, SpeelAnimation.swim, ageInTicks, 0.1f + limbSwingAmount * 1.5f);

		if (entity.isInFluidType()) {
			this.swimcontrol.xRot = headPitch;

		}

	}
}