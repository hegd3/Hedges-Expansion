package com.hedge.hedges_bestiary.client.models;// Made with Blockbench 5.0.7
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.hedge.hedges_bestiary.client.animations.GenericPosesAnimation;
import com.hedge.hedges_bestiary.client.animations.MurkAnimation;
import com.hedge.hedges_bestiary.client.animations.MurkAnimation2;
import com.hedge.hedges_bestiary.client.layer.EntityLayers;
import com.hedge.hedges_bestiary.entity.living.MurkEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class MurkModel extends HBModel<MurkEntity> {
	public static final ModelLayerLocation LAYER_LOCATION = EntityLayers.MURK_LAYER;
	private final ModelPart root;
	private final ModelPart swimcontrol;
	private final ModelPart wholebody;
	private final ModelPart body;
	private final ModelPart medium_spike;
	private final ModelPart medium_spike4;
	private final ModelPart medium_spike6;
	private final ModelPart medium_spike2;
	private final ModelPart medium_spike3;
	private final ModelPart medium_spike5;
	private final ModelPart big_spike;
	private final ModelPart big_spike2;
	private final ModelPart neckrot;
	private final ModelPart neck;
	private final ModelPart small_spike;
	private final ModelPart small_spike2;
	private final ModelPart headrot;
	private final ModelPart head;
	private final ModelPart jaw;
	private final ModelPart tailrot;
	private final ModelPart tail;
	private final ModelPart small_spike3;
	private final ModelPart medium_spike7;
	private final ModelPart tailrot2;
	private final ModelPart tail2;
	private final ModelPart leftleg;
	private final ModelPart leftlegpos;
	private final ModelPart rightleg;
	private final ModelPart rightlegpos;
	private final ModelPart leftleg2;
	private final ModelPart leftlegpos2;
	private final ModelPart rightleg2;
	private final ModelPart rightlegpos2;

	public MurkModel(ModelPart root) {
		super(0.5f, 24);

		this.root = root.getChild("root");
		this.swimcontrol = this.root.getChild("swimcontrol");
		this.wholebody = this.swimcontrol.getChild("wholebody");
		this.body = this.wholebody.getChild("body");
		this.medium_spike = this.body.getChild("medium_spike");
		this.medium_spike4 = this.body.getChild("medium_spike4");
		this.medium_spike6 = this.body.getChild("medium_spike6");
		this.medium_spike2 = this.body.getChild("medium_spike2");
		this.medium_spike3 = this.body.getChild("medium_spike3");
		this.medium_spike5 = this.body.getChild("medium_spike5");
		this.big_spike = this.body.getChild("big_spike");
		this.big_spike2 = this.body.getChild("big_spike2");
		this.neckrot = this.wholebody.getChild("neckrot");
		this.neck = this.neckrot.getChild("neck");
		this.small_spike = this.neck.getChild("small_spike");
		this.small_spike2 = this.neck.getChild("small_spike2");
		this.headrot = this.neck.getChild("headrot");
		this.head = this.headrot.getChild("head");
		this.jaw = this.head.getChild("jaw");
		this.tailrot = this.wholebody.getChild("tailrot");
		this.tail = this.tailrot.getChild("tail");
		this.small_spike3 = this.tail.getChild("small_spike3");
		this.medium_spike7 = this.tail.getChild("medium_spike7");
		this.tailrot2 = this.tail.getChild("tailrot2");
		this.tail2 = this.tailrot2.getChild("tail2");
		this.leftleg = this.swimcontrol.getChild("leftleg");
		this.leftlegpos = this.leftleg.getChild("leftlegpos");
		this.rightleg = this.swimcontrol.getChild("rightleg");
		this.rightlegpos = this.rightleg.getChild("rightlegpos");
		this.leftleg2 = this.swimcontrol.getChild("leftleg2");
		this.leftlegpos2 = this.leftleg2.getChild("leftlegpos2");
		this.rightleg2 = this.swimcontrol.getChild("rightleg2");
		this.rightlegpos2 = this.rightleg2.getChild("rightlegpos2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 10.0F, 0.0F));

		PartDefinition swimcontrol = root.addOrReplaceChild("swimcontrol", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition wholebody = swimcontrol.addOrReplaceChild("wholebody", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition body = wholebody.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-14.5F, -12.5F, -18.0F, 29.0F, 25.0F, 36.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.5F, 1.0F));

		PartDefinition medium_spike = body.addOrReplaceChild("medium_spike", CubeListBuilder.create().texOffs(54, 131).addBox(-2.5F, -9.0F, -0.5F, 5.0F, 9.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(152, 54).addBox(-2.5F, -9.0F, 5.5F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(7.0F, -12.5F, -16.5F));

		PartDefinition medium_spike4 = body.addOrReplaceChild("medium_spike4", CubeListBuilder.create().texOffs(54, 131).addBox(-2.5F, -9.0F, -0.5F, 5.0F, 9.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(152, 54).addBox(-2.5F, -9.0F, 5.5F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(9.0F, -12.5F, 2.5F));

		PartDefinition medium_spike6 = body.addOrReplaceChild("medium_spike6", CubeListBuilder.create().texOffs(54, 131).addBox(-2.5F, -9.0F, -0.5F, 5.0F, 9.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(152, 54).addBox(-2.5F, -9.0F, 5.5F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(6.0F, -12.5F, 11.5F));

		PartDefinition medium_spike2 = body.addOrReplaceChild("medium_spike2", CubeListBuilder.create().texOffs(54, 131).mirror().addBox(-2.5F, -9.0F, -0.5F, 5.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(152, 54).mirror().addBox(-2.5F, -9.0F, 5.5F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-7.0F, -12.5F, -16.5F));

		PartDefinition medium_spike3 = body.addOrReplaceChild("medium_spike3", CubeListBuilder.create().texOffs(54, 131).mirror().addBox(-2.5F, -9.0F, -0.5F, 5.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(152, 54).mirror().addBox(-2.5F, -9.0F, 5.5F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-9.0F, -12.5F, 2.5F));

		PartDefinition medium_spike5 = body.addOrReplaceChild("medium_spike5", CubeListBuilder.create().texOffs(54, 131).mirror().addBox(-2.5F, -9.0F, -0.5F, 5.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(152, 54).mirror().addBox(-2.5F, -9.0F, 5.5F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-6.0F, -12.5F, 11.5F));

		PartDefinition big_spike = body.addOrReplaceChild("big_spike", CubeListBuilder.create().texOffs(142, 128).addBox(-4.5F, -16.0F, -0.5F, 9.0F, 16.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(130, 54).addBox(-4.5F, -16.0F, 8.5F, 9.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, -12.5F, -7.5F));

		PartDefinition big_spike2 = body.addOrReplaceChild("big_spike2", CubeListBuilder.create().texOffs(142, 128).mirror().addBox(-4.5F, -16.0F, -0.5F, 9.0F, 16.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(130, 54).mirror().addBox(-4.5F, -16.0F, 8.5F, 9.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-8.0F, -12.5F, -7.5F));

		PartDefinition neckrot = wholebody.addOrReplaceChild("neckrot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.5F, -17.0F));

		PartDefinition neck = neckrot.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(94, 99).addBox(-11.5F, -7.5F, -14.0F, 23.0F, 15.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition small_spike = neck.addOrReplaceChild("small_spike", CubeListBuilder.create().texOffs(30, 148).addBox(-1.5F, -4.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(130, 59).addBox(-1.5F, -4.0F, 1.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.5F, -4.5F));

		PartDefinition small_spike2 = neck.addOrReplaceChild("small_spike2", CubeListBuilder.create().texOffs(30, 148).addBox(-1.5F, -4.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(130, 59).addBox(-1.5F, -4.0F, 1.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.5F, -9.5F));

		PartDefinition headrot = neck.addOrReplaceChild("headrot", CubeListBuilder.create(), PartPose.offset(0.0F, 4.0F, -14.0F));

		PartDefinition head = headrot.addOrReplaceChild("head", CubeListBuilder.create().texOffs(130, 0).addBox(-10.5F, -9.5F, -14.0F, 21.0F, 8.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(0, 61).addBox(-12.5F, -1.5F, -22.0F, 25.0F, 3.0F, 22.0F, new CubeDeformation(0.0F))
		.texOffs(130, 22).addBox(-10.5F, 1.5F, -21.0F, 21.0F, 2.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition jaw = head.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(0, 86).addBox(-12.5F, 0.0F, -22.0F, 25.0F, 3.0F, 22.0F, new CubeDeformation(0.0F))
		.texOffs(130, 39).addBox(-9.5F, -2.0F, -20.0F, 19.0F, 2.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.5F, 0.0F));

		PartDefinition tailrot = wholebody.addOrReplaceChild("tailrot", CubeListBuilder.create(), PartPose.offset(0.0F, 5.5F, 18.5F));

		PartDefinition tail = tailrot.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(78, 128).addBox(-8.5F, -8.5F, 0.5F, 17.0F, 17.0F, 15.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition small_spike3 = tail.addOrReplaceChild("small_spike3", CubeListBuilder.create().texOffs(30, 148).addBox(-1.5F, -4.0F, -0.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(130, 59).addBox(-1.5F, -4.0F, 2.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.5F, 9.0F));

		PartDefinition medium_spike7 = tail.addOrReplaceChild("medium_spike7", CubeListBuilder.create().texOffs(54, 131).addBox(-2.5F, -9.0F, -0.5F, 5.0F, 9.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(152, 54).addBox(-2.5F, -9.0F, 5.5F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.5F, 2.0F));

		PartDefinition tailrot2 = tail.addOrReplaceChild("tailrot2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 15.0F));

		PartDefinition tail2 = tailrot2.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(94, 61).addBox(-5.5F, -2.5F, 0.5F, 11.0F, 11.0F, 27.0F, new CubeDeformation(0.02F))
		.texOffs(0, 148).addBox(0.0F, -8.5F, 12.5F, 0.0F, 6.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition leftleg = swimcontrol.addOrReplaceChild("leftleg", CubeListBuilder.create(), PartPose.offset(15.0F, 12.0F, -13.0F));

		PartDefinition leftlegpos = leftleg.addOrReplaceChild("leftlegpos", CubeListBuilder.create().texOffs(0, 111).addBox(-0.5F, -2.0F, -8.0F, 23.0F, 4.0F, 16.0F, new CubeDeformation(0.01F))
		.texOffs(78, 111).addBox(22.5F, -2.0F, -7.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(78, 111).addBox(22.5F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(78, 119).addBox(13.5F, -2.0F, -12.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition rightleg = swimcontrol.addOrReplaceChild("rightleg", CubeListBuilder.create(), PartPose.offset(-15.0F, 12.0F, -13.0F));

		PartDefinition rightlegpos = rightleg.addOrReplaceChild("rightlegpos", CubeListBuilder.create().texOffs(0, 111).mirror().addBox(-22.5F, -2.0F, -8.0F, 23.0F, 4.0F, 16.0F, new CubeDeformation(0.01F)).mirror(false)
		.texOffs(78, 111).mirror().addBox(-26.5F, -2.0F, -7.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(78, 111).mirror().addBox(-26.5F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(78, 119).mirror().addBox(-17.5F, -2.0F, -12.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition leftleg2 = swimcontrol.addOrReplaceChild("leftleg2", CubeListBuilder.create(), PartPose.offset(15.0F, 12.0F, 14.0F));

		PartDefinition leftlegpos2 = leftleg2.addOrReplaceChild("leftlegpos2", CubeListBuilder.create().texOffs(0, 131).addBox(-0.5F, -2.0F, -7.0F, 14.0F, 4.0F, 13.0F, new CubeDeformation(0.01F))
		.texOffs(54, 146).addBox(7.5F, -2.0F, 6.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(78, 111).addBox(13.5F, -2.0F, -1.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition rightleg2 = swimcontrol.addOrReplaceChild("rightleg2", CubeListBuilder.create(), PartPose.offset(-15.0F, 12.0F, 14.0F));

		PartDefinition rightlegpos2 = rightleg2.addOrReplaceChild("rightlegpos2", CubeListBuilder.create().texOffs(0, 131).mirror().addBox(-13.5F, -2.0F, -7.0F, 14.0F, 4.0F, 13.0F, new CubeDeformation(0.01F)).mirror(false)
		.texOffs(54, 146).mirror().addBox(-11.5F, -2.0F, 6.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(78, 111).mirror().addBox(-17.5F, -2.0F, -1.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}

	@Override
	public void setupAnim(MurkEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);

		netHeadYaw = Mth.clamp(netHeadYaw, -25.0F, 25.0F) * ((float)Math.PI / 180F);
		headPitch = Mth.clamp(headPitch, -45.0F, 45.0F) * ((float)Math.PI / 180F);
		float tailYaw = entity.getTrailYaw(ageInTicks - entity.tickCount);



		this.tailrot.yRot = Mth.lerp(0.3F, this.tailrot.yRot, tailYaw * 0.25F);
		this.tailrot2.yRot = Mth.lerp(0.3F, this.tailrot2.yRot, tailYaw * 0.2F);

		this.neckrot.yRot = netHeadYaw / 2;
		this.headrot.yRot = netHeadYaw;
		this.headrot.xRot = headPitch;
		if (this.young) {
			this.applyStatic(GenericPosesAnimation.BABY_TRANSFORM_WITH_NECK);
		}
		if (entity.isInFluidType()) {
			this.swimcontrol.xRot = headPitch * 0.6f;
			this.animateWalk(MurkAnimation.swim, limbSwing, limbSwingAmount, 1.2f, 2.5f);
			this.animate(entity.idleAnimationState, MurkAnimation.swim_idle, ageInTicks, 0.5f);
			this.animate(entity.powerBiteAnimationState, entity.swingingLeft() ? MurkAnimation.power_bite_left : MurkAnimation.power_bite_right, ageInTicks, 1);
			this.animate(entity.multiBiteAnimationState, entity.swingingLeft() ? MurkAnimation2.multi_bite_left : MurkAnimation2.multi_bite_right, ageInTicks, 1);

			this.animate(entity.roarAnimationState, MurkAnimation.roar, ageInTicks, 1f);


		} else {
			this.animateWalk(MurkAnimation.walk, limbSwing, limbSwingAmount, 1.7f, 2.5f);
			this.animate(entity.idleAnimationState, MurkAnimation.idle, ageInTicks, 0.5f);
			this.animate(entity.powerBiteAnimationState, entity.swingingLeft() ? MurkAnimation.power_bite_left_land: MurkAnimation.power_bite_right_land, ageInTicks, 1);
			this.animate(entity.roarAnimationState, MurkAnimation2.roar_land, ageInTicks, 1f);

			this.animate(entity.multiBiteAnimationState, entity.swingingLeft() ? MurkAnimation2.multi_bite_left_land : MurkAnimation2.multi_bite_right_land, ageInTicks, 1);


		}
		this.animate(entity.biteAnimationState, MurkAnimation.bite, ageInTicks, 1f);
		this.animate(entity.shootAnimationState, MurkAnimation.shoot, ageInTicks, 1f);
		this.animate(entity.breathAnimationState, entity.swingingLeft() ? MurkAnimation2.breath_left : MurkAnimation2.breath_right, ageInTicks, 1f);
		this.animate(entity.sideSlamAnimationState, entity.swingingLeft() ? MurkAnimation2.side_slam_left : MurkAnimation2.side_slam_right, ageInTicks, 1f);
		this.animateSmooth(entity.clicksAnimationState, MurkAnimation2.clicks, ageInTicks, 1f);
		this.animateSmooth(entity.sitAnimationState, MurkAnimation2.sit, ageInTicks, 1f);
		this.animateSmooth(entity.danceAnimationState, MurkAnimation2.DANCE, ageInTicks, 1f);


	}
}