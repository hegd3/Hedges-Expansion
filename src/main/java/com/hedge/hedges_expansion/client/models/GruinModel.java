package com.hedge.hedges_expansion.client.models;// Made with Blockbench 5.0.7
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.hedge.hedges_expansion.client.animations.GruinAnimation;
import com.hedge.hedges_expansion.client.layer.EntityLayers;
import com.hedge.hedges_expansion.entity.living.GruinEntity;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class GruinModel extends HierarchicalModel<GruinEntity> {
	public static final ModelLayerLocation LAYER_LOCATION = EntityLayers.GRUIN_LAYER;
	private final ModelPart root;
	private final ModelPart frontbodylegs;
	private final ModelPart frontbody;
	private final ModelPart headrot;
	private final ModelPart head;
	private final ModelPart nose;
	private final ModelPart jaw;
	private final ModelPart body;
	private final ModelPart leftleg;
	private final ModelPart leftlegpos;
	private final ModelPart leftfoot;
	private final ModelPart rightleg;
	private final ModelPart rightlegpos;
	private final ModelPart rightfoot;
	private final ModelPart body2;
	private final ModelPart leftleg2;
	private final ModelPart leftlegpos2;
	private final ModelPart rightleg2;
	private final ModelPart rightlegpos2;

	public GruinModel(ModelPart root) {
		this.root = root.getChild("root");
		this.frontbodylegs = this.root.getChild("frontbodylegs");
		this.frontbody = this.frontbodylegs.getChild("frontbody");
		this.headrot = this.frontbody.getChild("headrot");
		this.head = this.headrot.getChild("head");
		this.nose = this.head.getChild("nose");
		this.jaw = this.head.getChild("jaw");
		this.body = this.frontbody.getChild("body");
		this.leftleg = this.frontbodylegs.getChild("leftleg");
		this.leftlegpos = this.leftleg.getChild("leftlegpos");
		this.leftfoot = this.leftlegpos.getChild("leftfoot");
		this.rightleg = this.frontbodylegs.getChild("rightleg");
		this.rightlegpos = this.rightleg.getChild("rightlegpos");
		this.rightfoot = this.rightlegpos.getChild("rightfoot");
		this.body2 = this.frontbodylegs.getChild("body2");
		this.leftleg2 = this.root.getChild("leftleg2");
		this.leftlegpos2 = this.leftleg2.getChild("leftlegpos2");
		this.rightleg2 = this.root.getChild("rightleg2");
		this.rightlegpos2 = this.rightleg2.getChild("rightlegpos2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition frontbodylegs = root.addOrReplaceChild("frontbodylegs", CubeListBuilder.create(), PartPose.offset(0.0F, -13.0F, 20.0F));

		PartDefinition frontbody = frontbodylegs.addOrReplaceChild("frontbody", CubeListBuilder.create(), PartPose.offset(0.0F, -10.0F, -10.0F));

		PartDefinition headrot = frontbody.addOrReplaceChild("headrot", CubeListBuilder.create(), PartPose.offset(0.0F, -8.0F, -24.0F));

		PartDefinition head = headrot.addOrReplaceChild("head", CubeListBuilder.create().texOffs(106, 0).addBox(-9.5F, -6.0F, -12.0F, 19.0F, 13.0F, 12.0F, new CubeDeformation(0.0F))
				.texOffs(106, 25).addBox(-9.5F, 7.0F, -12.0F, 19.0F, 4.0F, 12.0F, new CubeDeformation(0.0F))
				.texOffs(0, 148).addBox(5.5F, -9.0F, -6.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 148).mirror().addBox(-9.5F, -9.0F, -6.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition nose = head.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(80, 133).addBox(-4.5F, -3.0F, -5.0F, 9.0F, 6.0F, 8.0F, new CubeDeformation(0.02F))
				.texOffs(48, 145).addBox(-4.5F, 3.0F, -4.0F, 9.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, -15.0F));

		PartDefinition jaw = head.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(114, 133).addBox(-4.5F, 0.0F, -8.0F, 9.0F, 3.0F, 8.0F, new CubeDeformation(0.01F))
				.texOffs(144, 144).addBox(-4.5F, -2.0F, -7.0F, 9.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, -12.0F));

		PartDefinition body = frontbody.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-14.5F, -16.0F, -24.0F, 29.0F, 31.0F, 24.0F, new CubeDeformation(0.0F))
				.texOffs(92, 55).addBox(-14.5F, 15.0F, -24.0F, 29.0F, 2.0F, 24.0F, new CubeDeformation(0.0F))
				.texOffs(78, 147).addBox(0.0F, -19.0F, -23.0F, 0.0F, 3.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition leftleg = frontbodylegs.addOrReplaceChild("leftleg", CubeListBuilder.create(), PartPose.offset(11.5F, -20.5F, -23.5F));

		PartDefinition leftlegpos = leftleg.addOrReplaceChild("leftlegpos", CubeListBuilder.create().texOffs(92, 81).addBox(-7.0F, -1.5F, -8.5F, 14.0F, 35.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition leftfoot = leftlegpos.addOrReplaceChild("leftfoot", CubeListBuilder.create().texOffs(106, 41).addBox(-7.5F, 1.0F, -9.0F, 16.0F, 3.0F, 9.0F, new CubeDeformation(0.01F))
				.texOffs(114, 144).addBox(-4.5F, 0.0F, -12.0F, 10.0F, 4.0F, 5.0F, new CubeDeformation(0.02F))
				.texOffs(100, 147).addBox(5.5F, 1.0F, -11.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(100, 147).addBox(-7.5F, 1.0F, -11.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(80, 125).addBox(0.5F, 0.0F, -14.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(80, 125).addBox(-3.5F, 0.0F, -14.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 29.5F, -1.5F));

		PartDefinition rightleg = frontbodylegs.addOrReplaceChild("rightleg", CubeListBuilder.create(), PartPose.offset(-11.5F, -20.5F, -23.5F));

		PartDefinition rightlegpos = rightleg.addOrReplaceChild("rightlegpos", CubeListBuilder.create().texOffs(92, 81).mirror().addBox(-7.0F, -1.5F, -8.5F, 14.0F, 35.0F, 17.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition rightfoot = rightlegpos.addOrReplaceChild("rightfoot", CubeListBuilder.create().texOffs(106, 41).mirror().addBox(-8.5F, 1.0F, -9.0F, 16.0F, 3.0F, 9.0F, new CubeDeformation(0.01F)).mirror(false)
				.texOffs(114, 144).mirror().addBox(-5.5F, 0.0F, -12.0F, 10.0F, 4.0F, 5.0F, new CubeDeformation(0.02F)).mirror(false)
				.texOffs(100, 147).mirror().addBox(4.5F, 1.0F, -11.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(100, 147).mirror().addBox(-8.5F, 1.0F, -11.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(80, 125).mirror().addBox(0.5F, 0.0F, -14.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(80, 125).mirror().addBox(-3.5F, 0.0F, -14.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 29.5F, -1.5F));

		PartDefinition body2 = frontbodylegs.addOrReplaceChild("body2", CubeListBuilder.create().texOffs(0, 55).addBox(-12.5F, -14.0F, 0.0F, 25.0F, 27.0F, 21.0F, new CubeDeformation(0.0F))
				.texOffs(48, 125).addBox(0.0F, -18.0F, 3.0F, 0.0F, 4.0F, 16.0F, new CubeDeformation(0.0F))
				.texOffs(0, 103).addBox(-12.5F, 13.0F, 1.0F, 25.0F, 2.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -9.0F, -10.0F));

		PartDefinition leftleg2 = root.addOrReplaceChild("leftleg2", CubeListBuilder.create(), PartPose.offset(7.5F, -33.5F, 20.5F));

		PartDefinition leftlegpos2 = leftleg2.addOrReplaceChild("leftlegpos2", CubeListBuilder.create().texOffs(0, 164).addBox(-6.0F, 19.5F, -7.5F, 10.0F, 14.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition rightleg2 = root.addOrReplaceChild("rightleg2", CubeListBuilder.create(), PartPose.offset(-5.5F, -33.5F, 20.5F));

		PartDefinition rightlegpos2 = rightleg2.addOrReplaceChild("rightlegpos2", CubeListBuilder.create().texOffs(0, 164).mirror().addBox(-6.0F, 19.5F, -7.5F, 10.0F, 14.0F, 14.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}

	@Override
	public void setupAnim(GruinEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		netHeadYaw = Mth.clamp(netHeadYaw, -15.0F, 15.0F) * ((float) Math.PI / 180F);
		headPitch = Mth.clamp(headPitch, -5.0F, 25.0F) * ((float) Math.PI / 180F);

		this.headrot.yRot = netHeadYaw;
		this.headrot.xRot = headPitch;
		if (entity.getDeltaMovement().horizontalDistanceSqr() > 0.01) {
			this.animateWalk(GruinAnimation.run, limbSwing, limbSwingAmount, 1.2f, 2.5f);

		} else {
			this.animateWalk(GruinAnimation.walk, limbSwing, limbSwingAmount, 2f, 2.5f);
		}
		this.animate(entity.idleAnimationState, GruinAnimation.idle, ageInTicks, 0.25f);
		if (entity.getAnimState() > 0) {
			this.animate(entity.biteAnimationState, GruinAnimation.bite, ageInTicks, 1);
			this.animate(entity.swipeAnimationState, entity.swingingLeft() ? GruinAnimation.swipe_left : GruinAnimation.swipe_right, ageInTicks, 1);
			this.animate(entity.roarAnimationState, GruinAnimation.roar, ageInTicks, 1);
			this.animate(entity.sniffAnimationState, GruinAnimation.sniff, ageInTicks, 1);

		}
	}


}