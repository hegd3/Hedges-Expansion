package com.hedge.hedges_expansion.client.models;// Made with Blockbench 5.0.7
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.hedge.hedges_expansion.client.animations.TransfiguredAnimation;
import com.hedge.hedges_expansion.client.layer.EntityLayers;
import com.hedge.hedges_expansion.entity.living.TransfiguredEntity;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class TransfiguredModel extends HierarchicalModel<TransfiguredEntity> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = EntityLayers.TRANSFIGURED_LAYER;
	private final ModelPart root;
	private final ModelPart body;
	private final ModelPart headrot;
	private final ModelPart head;
	private final ModelPart rightarm;
	private final ModelPart leftarm;
	private final ModelPart rightleg;
	private final ModelPart rightleg2;
	private final ModelPart leftleg;
	private final ModelPart leftleg2;

	public TransfiguredModel(ModelPart root) {
		this.root = root.getChild("root");
		this.body = this.root.getChild("body");
		this.headrot = this.body.getChild("headrot");
		this.head = this.headrot.getChild("head");
		this.rightarm = this.body.getChild("rightarm");
		this.leftarm = this.body.getChild("leftarm");
		this.rightleg = this.root.getChild("rightleg");
		this.rightleg2 = this.rightleg.getChild("rightleg2");
		this.leftleg = this.root.getChild("leftleg");
		this.leftleg2 = this.leftleg.getChild("leftleg2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 36).addBox(-10.0F, -14.0F, -4.0F, 20.0F, 9.0F, 11.0F, new CubeDeformation(0.0F))
		.texOffs(50, 0).addBox(-9.0F, -5.0F, -4.0F, 18.0F, 13.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -20.0F, 0.0F));

		PartDefinition headrot = body.addOrReplaceChild("headrot", CubeListBuilder.create(), PartPose.offset(0.0F, -14.0F, 1.0F));

		PartDefinition head = headrot.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -27.0F, -5.0F, 16.0F, 27.0F, 9.0F, new CubeDeformation(0.01F))
		.texOffs(62, 43).mirror().addBox(-13.0F, -21.0F, -4.0F, 5.0F, 5.0F, 3.0F, new CubeDeformation(0.01F)).mirror(false)
		.texOffs(62, 43).addBox(8.0F, -21.0F, -4.0F, 5.0F, 5.0F, 3.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition rightarm = body.addOrReplaceChild("rightarm", CubeListBuilder.create().texOffs(30, 56).mirror().addBox(-7.0F, -5.0F, -4.0F, 7.0F, 29.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-10.0F, -9.0F, 1.0F));

		PartDefinition leftarm = body.addOrReplaceChild("leftarm", CubeListBuilder.create().texOffs(30, 56).addBox(0.0F, -5.0F, -4.0F, 7.0F, 29.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(10.0F, -9.0F, 1.0F));

		PartDefinition rightleg = root.addOrReplaceChild("rightleg", CubeListBuilder.create(), PartPose.offset(-4.9F, -12.0F, 1.0F));

		PartDefinition rightleg2 = rightleg.addOrReplaceChild("rightleg2", CubeListBuilder.create().texOffs(62, 24).mirror().addBox(-3.1F, 0.0F, -3.0F, 7.0F, 12.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition leftleg = root.addOrReplaceChild("leftleg", CubeListBuilder.create(), PartPose.offset(4.9F, -12.0F, 1.0F));

		PartDefinition leftleg2 = leftleg.addOrReplaceChild("leftleg2", CubeListBuilder.create().texOffs(62, 24).addBox(-3.9F, 0.0F, -3.0F, 7.0F, 12.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}

	@Override
	public void setupAnim(TransfiguredEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		netHeadYaw = Mth.clamp(netHeadYaw, -25.0F, 25.0F) * ((float) Math.PI / 180F);
		headPitch = Mth.clamp(headPitch, -15.0F, 25.0F) * ((float) Math.PI / 180F);

		this.headrot.yRot = netHeadYaw;
		this.headrot.xRot = headPitch;


		this.animateWalk(TransfiguredAnimation.walk, limbSwing, limbSwingAmount, 2.0f, 2.5f);
		this.animate(entity.idleAnimationState, TransfiguredAnimation.idle, ageInTicks, 1);
		this.animate(entity.attackAnimationState, this.getStateAnim(entity.getAnimState(), entity.swingingLeft()), ageInTicks, 1);
	}

	private AnimationDefinition getStateAnim(int i, boolean left) {
		return switch(i) {
			case 2 -> TransfiguredAnimation.slam;
			case 3 -> TransfiguredAnimation.rush;
			default -> left ? TransfiguredAnimation.punch_left : TransfiguredAnimation.punch_right;
		};
	}
}