package com.hedge.hedges_expansion.client.models;// Made with Blockbench 5.0.7
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.hedge.hedges_expansion.client.animations.SkartleAnimation;
import com.hedge.hedges_expansion.client.layer.EntityLayers;
import com.hedge.hedges_expansion.entity.living.SkartleEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class SkartleModel extends HierarchicalModel<SkartleEntity> {
	public static final ModelLayerLocation LAYER_LOCATION = EntityLayers.SKARTLE_LAYER;
	private final ModelPart root;
	private final ModelPart body;
	private final ModelPart leftarm;
	private final ModelPart rightarm;
	private final ModelPart neckrot;
	private final ModelPart neck;
	private final ModelPart neck2rot;
	private final ModelPart neck2;
	private final ModelPart leftfrill;
	private final ModelPart rightfrill;
	private final ModelPart jaw;
	private final ModelPart tongue;
	private final ModelPart tail;
	private final ModelPart tail2;
	private final ModelPart leftleg;
	private final ModelPart leftlegpos;
	private final ModelPart leftleg2;
	private final ModelPart leftfoot;
	private final ModelPart rightleg;
	private final ModelPart rightlegpos;
	private final ModelPart rightleg2;
	private final ModelPart rightfoot;

	public SkartleModel(ModelPart root) {
		this.root = root.getChild("root");
		this.body = this.root.getChild("body");
		this.leftarm = this.body.getChild("leftarm");
		this.rightarm = this.body.getChild("rightarm");
		this.neckrot = this.body.getChild("neckrot");
		this.neck = this.neckrot.getChild("neck");
		this.neck2rot = this.neck.getChild("neck2rot");
		this.neck2 = this.neck2rot.getChild("neck2");
		this.leftfrill = this.neck2.getChild("leftfrill");
		this.rightfrill = this.neck2.getChild("rightfrill");
		this.jaw = this.neck2.getChild("jaw");
		this.tongue = this.jaw.getChild("tongue");
		this.tail = this.body.getChild("tail");
		this.tail2 = this.tail.getChild("tail2");
		this.leftleg = this.root.getChild("leftleg");
		this.leftlegpos = this.leftleg.getChild("leftlegpos");
		this.leftleg2 = this.leftlegpos.getChild("leftleg2");
		this.leftfoot = this.leftleg2.getChild("leftfoot");
		this.rightleg = this.root.getChild("rightleg");
		this.rightlegpos = this.rightleg.getChild("rightlegpos");
		this.rightleg2 = this.rightlegpos.getChild("rightleg2");
		this.rightfoot = this.rightleg2.getChild("rightfoot");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-6.5F, -9.0F, -18.0F, 13.0F, 15.0F, 23.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -16.0F, 5.0F));

		PartDefinition leftarm = body.addOrReplaceChild("leftarm", CubeListBuilder.create().texOffs(32, 76).addBox(0.0F, -1.0F, -2.0F, 3.0F, 10.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(16, 90).addBox(0.0F, 5.0F, -5.0F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(60, 90).addBox(0.0F, 5.0F, -8.0F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(6.5F, 2.0F, -15.0F));

		PartDefinition rightarm = body.addOrReplaceChild("rightarm", CubeListBuilder.create().texOffs(32, 76).mirror().addBox(-3.0F, -1.0F, -2.0F, 3.0F, 10.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(16, 90).mirror().addBox(-3.0F, 5.0F, -5.0F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(60, 90).mirror().addBox(-3.0F, 5.0F, -8.0F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-6.5F, 2.0F, -15.0F));

		PartDefinition neckrot = body.addOrReplaceChild("neckrot", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, -15.5F));

		PartDefinition neck = neckrot.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(72, 0).addBox(-4.5F, -14.0F, -6.5F, 9.0F, 14.0F, 7.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition neck2rot = neck.addOrReplaceChild("neck2rot", CubeListBuilder.create(), PartPose.offset(0.0F, -11.0F, -1.5F));

		PartDefinition neck2 = neck2rot.addOrReplaceChild("neck2", CubeListBuilder.create().texOffs(72, 21).addBox(-4.5F, -9.0F, 0.0F, 9.0F, 11.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(48, 76).addBox(0.0F, -13.0F, -3.0F, 0.0F, 4.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(74, 49).addBox(-3.5F, -9.0F, -9.0F, 7.0F, 2.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(66, 83).addBox(-3.5F, -7.0F, -9.0F, 7.0F, 2.0F, 5.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition leftfrill = neck2.addOrReplaceChild("leftfrill", CubeListBuilder.create().texOffs(48, 89).addBox(0.0F, -6.0F, 0.0F, 6.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(4.5F, -6.0F, 5.0F));

		PartDefinition rightfrill = neck2.addOrReplaceChild("rightfrill", CubeListBuilder.create().texOffs(48, 89).mirror().addBox(-6.0F, -6.0F, 0.0F, 6.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-4.5F, -6.0F, 5.0F));

		PartDefinition jaw = neck2.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(74, 38).addBox(-3.5F, 0.0F, -9.0F, 7.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.0F, 0.0F));

		PartDefinition tongue = jaw.addOrReplaceChild("tongue", CubeListBuilder.create().texOffs(100, 37).addBox(-1.5F, 0.0F, -7.0F, 3.0F, 0.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.01F, -2.0F));

		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(34, 38).addBox(-5.5F, -5.0F, 1.0F, 11.0F, 10.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, 4.0F));

		PartDefinition tail2 = tail.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(34, 57).addBox(-4.5F, -3.0F, 1.0F, 9.0F, 7.0F, 11.0F, new CubeDeformation(0.0F))
		.texOffs(0, 38).addBox(0.0F, -11.0F, 2.0F, 0.0F, 14.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 9.0F));

		PartDefinition leftleg = root.addOrReplaceChild("leftleg", CubeListBuilder.create(), PartPose.offset(4.5F, -18.0F, 3.0F));

		PartDefinition leftlegpos = leftleg.addOrReplaceChild("leftlegpos", CubeListBuilder.create().texOffs(0, 69).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 13.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition leftleg2 = leftlegpos.addOrReplaceChild("leftleg2", CubeListBuilder.create().texOffs(74, 60).addBox(-3.0F, 0.0F, 0.0F, 6.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 9.0F, -1.0F));

		PartDefinition leftfoot = leftleg2.addOrReplaceChild("leftfoot", CubeListBuilder.create().texOffs(74, 75).addBox(-3.0F, 0.0F, -6.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.01F))
		.texOffs(4, 90).addBox(0.0F, 0.0F, -8.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.01F))
		.texOffs(4, 90).addBox(-3.0F, 0.0F, -8.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 7.0F, 2.0F));

		PartDefinition rightleg = root.addOrReplaceChild("rightleg", CubeListBuilder.create(), PartPose.offset(-4.5F, -18.0F, 3.0F));

		PartDefinition rightlegpos = rightleg.addOrReplaceChild("rightlegpos", CubeListBuilder.create().texOffs(0, 69).mirror().addBox(-4.0F, 0.0F, -4.0F, 8.0F, 13.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition rightleg2 = rightlegpos.addOrReplaceChild("rightleg2", CubeListBuilder.create().texOffs(74, 60).mirror().addBox(-3.0F, 0.0F, 0.0F, 6.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 9.0F, -1.0F));

		PartDefinition rightfoot = rightleg2.addOrReplaceChild("rightfoot", CubeListBuilder.create().texOffs(74, 75).mirror().addBox(-3.0F, 0.0F, -6.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.01F)).mirror(false)
		.texOffs(4, 90).addBox(1.0F, 0.0F, -8.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.01F))
		.texOffs(4, 90).addBox(-2.0F, 0.0F, -8.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 7.0F, 2.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}

	@Override
	public void setupAnim(SkartleEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

		this.root().getAllParts().forEach(ModelPart::resetPose);
		netHeadYaw = Mth.clamp(netHeadYaw, -35.0F, 35.0F) * ((float)Math.PI / 180F);
		headPitch = Mth.clamp(headPitch, -5.0F, 15.0F) * ((float)Math.PI / 180F);

		this.neckrot.yRot = netHeadYaw;
		this.neckrot.xRot = headPitch;
		if (entity.getDeltaMovement().horizontalDistanceSqr() > 0.01) {
			this.animateWalk(SkartleAnimation.run, limbSwing, limbSwingAmount, 2f, 2.5f);
		} else {
			this.animateWalk(SkartleAnimation.walk, limbSwing, limbSwingAmount, 2f, 2.5f);
		}
		this.animate(entity.idleAnimationState, SkartleAnimation.idle, ageInTicks, 0.4f);
		this.animate(entity.biteAnimationState, SkartleAnimation.bite, ageInTicks, 1f);
		this.animate(entity.clawAnimationState, entity.swingingLeft() ? SkartleAnimation.claw_left : SkartleAnimation.claw_right, ageInTicks, 1f);
		this.animate(entity.spitAnimationState, SkartleAnimation.spit, ageInTicks, 1f);

	}
}