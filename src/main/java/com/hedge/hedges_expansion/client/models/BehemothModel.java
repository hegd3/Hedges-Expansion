package com.hedge.hedges_expansion.client.models;


import com.hedge.hedges_expansion.client.animations.BehemothAnimation;
import com.hedge.hedges_expansion.client.animations.BehemothAnimation2;
import com.hedge.hedges_expansion.client.layer.EntityLayers;
import com.hedge.hedges_expansion.entity.living.BehemothEntity;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class BehemothModel extends HierarchicalModel<BehemothEntity> {
	public static final ModelLayerLocation LAYER_LOCATION = EntityLayers.BEHEMOTH_LAYER;
	private final ModelPart root;
	private final ModelPart bodyandfrontlegs;
	private final ModelPart bodyandtail;
	private final ModelPart body;
	private final ModelPart tail;
	private final ModelPart neck;
	private final ModelPart headrot;
	private final ModelPart head;
	private final ModelPart jaw;
	private final ModelPart leftfrontleg;
	private final ModelPart leftfrontleg2;
	private final ModelPart leftfoot;
	private final ModelPart rightfrontleg;
	private final ModelPart rightfrontleg2;
	private final ModelPart rightfoot;
	private final ModelPart leftbackleg;
	private final ModelPart leftbackleg2;
	private final ModelPart rightbackleg;
	private final ModelPart rightbackleg2;

	public BehemothModel(ModelPart root) {
		this.root = root.getChild("root");
		this.bodyandfrontlegs = this.root.getChild("bodyandfrontlegs");
		this.bodyandtail = this.bodyandfrontlegs.getChild("bodyandtail");
		this.body = this.bodyandtail.getChild("body");
		this.tail = this.bodyandtail.getChild("tail");
		this.neck = this.bodyandfrontlegs.getChild("neck");
		this.headrot = this.neck.getChild("headrot");
		this.head = this.headrot.getChild("head");
		this.jaw = this.head.getChild("jaw");
		this.leftfrontleg = this.bodyandfrontlegs.getChild("leftfrontleg");
		this.leftfrontleg2 = this.leftfrontleg.getChild("leftfrontleg2");
		this.leftfoot = this.leftfrontleg2.getChild("leftfoot");
		this.rightfrontleg = this.bodyandfrontlegs.getChild("rightfrontleg");
		this.rightfrontleg2 = this.rightfrontleg.getChild("rightfrontleg2");
		this.rightfoot = this.rightfrontleg2.getChild("rightfoot");
		this.leftbackleg = this.root.getChild("leftbackleg");
		this.leftbackleg2 = this.leftbackleg.getChild("leftbackleg2");
		this.rightbackleg = this.root.getChild("rightbackleg");
		this.rightbackleg2 = this.rightbackleg.getChild("rightbackleg2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition bodyandfrontlegs = root.addOrReplaceChild("bodyandfrontlegs", CubeListBuilder.create(), PartPose.offset(0.0F, -28.0F, 16.0F));

		PartDefinition bodyandtail = bodyandfrontlegs.addOrReplaceChild("bodyandtail", CubeListBuilder.create(), PartPose.offset(0.0F, -9.0F, -16.0F));

		PartDefinition body = bodyandtail.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 233).addBox(0.0F, -30.0F, 1.0F, 0.0F, 9.0F, 26.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-18.5F, -21.0F, -29.0F, 37.0F, 43.0F, 57.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition tail = bodyandtail.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(188, 70).addBox(-6.5F, -0.5F, 0.25F, 13.0F, 12.0F, 17.0F, new CubeDeformation(0.0F))
				.texOffs(236, 123).addBox(0.0F, -10.5F, 1.25F, 0.0F, 10.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.5F, 27.75F));

		PartDefinition neck = bodyandfrontlegs.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(0, 100).addBox(-11.5F, -20.0F, -24.5F, 23.0F, 36.0F, 35.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -15.0F, -25.5F));

		PartDefinition plane_r1 = neck.addOrReplaceChild("plane_r1", CubeListBuilder.create().texOffs(162, 225).addBox(0.0F, -12.0F, -17.0F, 0.0F, 12.0F, 34.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, -20.0F, -7.5F, 0.0F, 0.0F, -0.2618F));

		PartDefinition plane_r2 = neck.addOrReplaceChild("plane_r2", CubeListBuilder.create().texOffs(162, 202).addBox(0.0F, -12.0F, -17.0F, 0.0F, 12.0F, 34.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.0F, -20.0F, -6.5F, 0.0F, 0.0F, -0.6545F));

		PartDefinition plane_r3 = neck.addOrReplaceChild("plane_r3", CubeListBuilder.create().texOffs(162, 225).addBox(0.0F, -12.0F, -17.0F, 0.0F, 12.0F, 34.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, -20.0F, -7.5F, 0.0F, 0.0F, 0.2618F));

		PartDefinition plane_r4 = neck.addOrReplaceChild("plane_r4", CubeListBuilder.create().texOffs(162, 202).addBox(0.0F, -12.0F, -17.0F, 0.0F, 12.0F, 34.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.0F, -20.0F, -6.5F, 0.0F, 0.0F, 0.6545F));

		PartDefinition headrot = neck.addOrReplaceChild("headrot", CubeListBuilder.create(), PartPose.offset(0.0F, 4.0F, -24.5F));

		PartDefinition head = headrot.addOrReplaceChild("head", CubeListBuilder.create().texOffs(116, 100).addBox(-12.5F, -15.0F, -35.0F, 25.0F, 21.0F, 35.0F, new CubeDeformation(0.02F))
				.texOffs(144, 216).addBox(-3.5F, -12.0F, -38.0F, 7.0F, 6.0F, 3.0F, new CubeDeformation(0.02F))
				.texOffs(191, 4).addBox(-12.5F, -3.0F, -35.0F, 25.0F, 0.0F, 31.0F, new CubeDeformation(0.0F))
				.texOffs(144, 29).addBox(-12.5F, -4.0F, -4.1F, 25.0F, 10.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(236, 99).addBox(3.5F, -20.0F, -14.0F, 11.0F, 10.0F, 14.0F, new CubeDeformation(0.03F))
				.texOffs(236, 99).mirror().addBox(-14.5F, -20.0F, -14.0F, 11.0F, 10.0F, 14.0F, new CubeDeformation(0.03F)).mirror(false)
				.texOffs(72, 171).addBox(3.5F, -27.0F, -24.0F, 11.0F, 17.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(72, 171).mirror().addBox(-14.5F, -27.0F, -24.0F, 11.0F, 17.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition horn_r1 = head.addOrReplaceChild("horn_r1", CubeListBuilder.create().texOffs(236, 149).mirror().addBox(0.0F, -8.5F, -7.0F, 0.0F, 9.0F, 14.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-14.5F, -20.5F, -7.0F, 0.0F, 0.0F, -0.5672F));

		PartDefinition horn_r2 = head.addOrReplaceChild("horn_r2", CubeListBuilder.create().texOffs(236, 172).mirror().addBox(0.0F, -10.0F, -4.5F, 0.0F, 10.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-14.5F, -27.0F, -18.5F, 0.0F, 0.0F, -0.5672F));

		PartDefinition horn_r3 = head.addOrReplaceChild("horn_r3", CubeListBuilder.create().texOffs(236, 172).addBox(0.0F, -10.0F, -4.5F, 0.0F, 10.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(14.5F, -27.0F, -18.5F, 0.0F, 0.0F, 0.5672F));

		PartDefinition horn_r4 = head.addOrReplaceChild("horn_r4", CubeListBuilder.create().texOffs(236, 149).addBox(0.0F, -8.5F, -7.0F, 0.0F, 9.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(14.5F, -20.5F, -7.0F, 0.0F, 0.0F, 0.5672F));

		PartDefinition jaw = head.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(116, 156).addBox(-16.8333F, -0.5F, -34.0F, 25.0F, 11.0F, 35.0F, new CubeDeformation(0.01F))
				.texOffs(193, 41).addBox(-16.8333F, 5.5F, -34.0F, 25.0F, 0.0F, 29.0F, new CubeDeformation(0.0F))
				.texOffs(52, 236).addBox(-4.3333F, 10.5F, -23.0F, 0.0F, 8.0F, 26.0F, new CubeDeformation(0.0F))
				.texOffs(6, 235).addBox(-16.8333F, 0.5F, -5.1F, 25.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(4.3333F, 2.5F, -1.0F));

		PartDefinition beard_r1 = jaw.addOrReplaceChild("beard_r1", CubeListBuilder.create().texOffs(52, 236).addBox(0.0F, 0.0F, -13.0F, 0.0F, 8.0F, 26.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-16.8333F, 10.5F, -12.0F, 0.0F, 0.0F, 0.6545F));

		PartDefinition beard_r2 = jaw.addOrReplaceChild("beard_r2", CubeListBuilder.create().texOffs(52, 236).addBox(0.0F, 0.0F, -13.0F, 0.0F, 8.0F, 26.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.1667F, 10.5F, -12.0F, 0.0F, 0.0F, -0.6545F));

		PartDefinition leftfrontleg = bodyandfrontlegs.addOrReplaceChild("leftfrontleg", CubeListBuilder.create(), PartPose.offset(16.0F, -12.5F, -31.5F));

		PartDefinition leftfrontleg2 = leftfrontleg.addOrReplaceChild("leftfrontleg2", CubeListBuilder.create().texOffs(0, 171).addBox(-7.5F, -0.5F, -10.5F, 15.0F, 41.0F, 21.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition leftfoot = leftfrontleg2.addOrReplaceChild("leftfoot", CubeListBuilder.create().texOffs(78, 208).addBox(-9.5F, -1.0F, -20.0F, 19.0F, 8.0F, 20.0F, new CubeDeformation(0.01F))
				.texOffs(104, 236).addBox(-8.5F, -1.0F, -26.0F, 17.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 33.5F, 4.5F));

		PartDefinition rightfrontleg = bodyandfrontlegs.addOrReplaceChild("rightfrontleg", CubeListBuilder.create(), PartPose.offset(-16.0F, -12.5F, -31.5F));

		PartDefinition rightfrontleg2 = rightfrontleg.addOrReplaceChild("rightfrontleg2", CubeListBuilder.create().texOffs(0, 171).mirror().addBox(-7.5F, -0.5F, -10.5F, 15.0F, 41.0F, 21.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition rightfoot = rightfrontleg2.addOrReplaceChild("rightfoot", CubeListBuilder.create().texOffs(78, 208).mirror().addBox(-9.5F, -1.0F, -20.0F, 19.0F, 8.0F, 20.0F, new CubeDeformation(0.01F)).mirror(false)
				.texOffs(104, 236).mirror().addBox(-8.5F, -1.0F, -26.0F, 17.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 33.5F, 4.5F));

		PartDefinition leftbackleg = root.addOrReplaceChild("leftbackleg", CubeListBuilder.create(), PartPose.offset(15.0F, -27.5F, 16.0F));

		PartDefinition leftbackleg2 = leftbackleg.addOrReplaceChild("leftbackleg2", CubeListBuilder.create().texOffs(230, 202).addBox(-6.5F, -1.5F, -9.0F, 13.0F, 29.0F, 18.0F, new CubeDeformation(0.0F))
				.texOffs(68, 237).addBox(-5.5F, 20.5F, -15.0F, 11.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition rightbackleg = root.addOrReplaceChild("rightbackleg", CubeListBuilder.create(), PartPose.offset(-15.0F, -27.5F, 16.0F));

		PartDefinition rightbackleg2 = rightbackleg.addOrReplaceChild("rightbackleg2", CubeListBuilder.create().texOffs(230, 202).mirror().addBox(-6.5F, -1.5F, -9.0F, 13.0F, 29.0F, 18.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(68, 237).addBox(-5.5F, 20.5F, -15.0F, 11.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 512, 512);
	}


	@Override
	public ModelPart root() {
		return this.root;
	}

	@Override
	public void setupAnim(BehemothEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

		int animState = entity.getAnimState();

		this.root().getAllParts().forEach(ModelPart::resetPose);
		netHeadYaw = Mth.clamp(netHeadYaw, -25.0F, 25.0F) * ((float) Math.PI / 180F);
		headPitch = Mth.clamp(headPitch, -15.0F, 25.0F) * ((float) Math.PI / 180F);

		this.headrot.yRot = netHeadYaw;
		this.headrot.xRot = headPitch;

		if (animState != BehemothEntity.CHARGE_ANIM) {
			this.animateWalk(BehemothAnimation.walk_body, limbSwing, limbSwingAmount, 2f, 2.5f);
			this.animateWalk(BehemothAnimation.walk_legs, limbSwing, limbSwingAmount, 2.3f, 2.5f);
		}
		this.animate(entity.idleAnimationState, BehemothAnimation.idle, ageInTicks, 1);
		this.animate(entity.headTwitchAnimationState, BehemothAnimation.head_twitch, ageInTicks, 1);
		if (animState > 0) {
			boolean left = entity.swingingLeft();
			this.animate(entity.attackAnimationState, BehemothAnimation.bite, ageInTicks, 1);
			this.animate(entity.hornAttackAnimationState, BehemothAnimation.horn_attack, ageInTicks, 1);
			this.animate(entity.armAtackAnimationState, left ? BehemothAnimation.arm_slam_left : BehemothAnimation.arm_slam_right, ageInTicks, 1);
			this.animate(entity.bodySlamAnimationState, BehemothAnimation.body_slam, ageInTicks, 1);
			this.animate(entity.roarAnimationState, BehemothAnimation.roar, ageInTicks, 1);
			this.animate(entity.chargeStartUpAnimationState, left ? BehemothAnimation2.charge_startup_left : BehemothAnimation2.charge_startup_right, ageInTicks, Math.min(1, entity.getAnimTicks() * 0.05f));
			this.animate(entity.chargeAnimationState, BehemothAnimation2.charge, ageInTicks, 1);
			this.animate(entity.jumpAnimationState, BehemothAnimation2.jump, ageInTicks, 1);
			this.animate(entity.landAnimationState, BehemothAnimation2.land, ageInTicks, 1);
		}
		this.animate(entity.airAnimationState, BehemothAnimation2.air, ageInTicks);
	}
}