package com.hedge.hedges_expansion.client.models;


import com.hedge.hedges_expansion.client.animations.SpottedStrikerAnimation;
import com.hedge.hedges_expansion.client.layer.EntityLayers;
import com.hedge.hedges_expansion.entity.living.SpottedStrikerEntity;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class SpottedStrikerModel extends HierarchicalModel<SpottedStrikerEntity> {
	public static final ModelLayerLocation LAYER_LOCATION = EntityLayers.SPOTTED_STRIKER_LAYER;
	private final ModelPart root;
	private final ModelPart swimcontrol;
	private final ModelPart gills;
	private final ModelPart gillsrot;
	private final ModelPart gills2;
	private final ModelPart headrot;
	private final ModelPart head;
	private final ModelPart jaw;
	private final ModelPart body;
	private final ModelPart fin;
	private final ModelPart leftfin;
	private final ModelPart rightfin;
	private final ModelPart leftfin2;
	private final ModelPart rightfin2;
	private final ModelPart tailrot;
	private final ModelPart tail;
	private final ModelPart tail2rot;
	private final ModelPart tail2;
	private final ModelPart leftfin3;
	private final ModelPart rightfin3;

	public SpottedStrikerModel(ModelPart root) {
		this.root = root.getChild("root");
		this.swimcontrol = this.root.getChild("swimcontrol");
		this.gills = this.swimcontrol.getChild("gills");
		this.gillsrot = this.gills.getChild("gillsrot");
		this.gills2 = this.gillsrot.getChild("gills2");
		this.headrot = this.gills2.getChild("headrot");
		this.head = this.headrot.getChild("head");
		this.jaw = this.head.getChild("jaw");
		this.body = this.gills.getChild("body");
		this.fin = this.body.getChild("fin");
		this.leftfin = this.body.getChild("leftfin");
		this.rightfin = this.body.getChild("rightfin");
		this.leftfin2 = this.body.getChild("leftfin2");
		this.rightfin2 = this.body.getChild("rightfin2");
		this.tailrot = this.body.getChild("tailrot");
		this.tail = this.tailrot.getChild("tail");
		this.tail2rot = this.tail.getChild("tail2rot");
		this.tail2 = this.tail2rot.getChild("tail2");
		this.leftfin3 = this.tail.getChild("leftfin3");
		this.rightfin3 = this.tail.getChild("rightfin3");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 16.0F, 0.0F));

		PartDefinition swimcontrol = root.addOrReplaceChild("swimcontrol", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition gills = swimcontrol.addOrReplaceChild("gills", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, -9.0F));

		PartDefinition gillsrot = gills.addOrReplaceChild("gillsrot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition gills2 = gillsrot.addOrReplaceChild("gills2", CubeListBuilder.create().texOffs(102, 115).addBox(0.0F, -10.0F, -9.0F, 0.0F, 4.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(102, 115).addBox(0.0F, -10.0F, -9.0F, 0.0F, 4.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(68, 42).addBox(-8.5F, -6.0F, -9.0F, 17.0F, 12.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition headrot = gills2.addOrReplaceChild("headrot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -9.0F));

		PartDefinition head = headrot.addOrReplaceChild("head", CubeListBuilder.create().texOffs(52, 72).addBox(-5.5F, -4.0F, -13.0F, 11.0F, 5.0F, 13.0F, new CubeDeformation(0.0F))
				.texOffs(0, 96).addBox(-5.5F, 1.0F, -13.0F, 11.0F, 1.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition jaw = head.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(100, 72).addBox(-4.5F, 0.0F, -12.0F, 9.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
				.texOffs(48, 103).addBox(-4.5F, -1.0F, -12.0F, 9.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 0.0F));

		PartDefinition fin_r1 = jaw.addOrReplaceChild("fin_r1", CubeListBuilder.create().texOffs(24, 110).addBox(-0.5F, 0.0F, -5.0F, 0.0F, 5.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 2.0F, -4.0F, 0.0F, 0.0F, 0.5672F));

		PartDefinition fin_r2 = jaw.addOrReplaceChild("fin_r2", CubeListBuilder.create().texOffs(24, 110).addBox(0.5F, 0.0F, -5.0F, 0.0F, 5.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 2.0F, -4.0F, 0.0F, 0.0F, -0.5672F));

		PartDefinition body = gills.addOrReplaceChild("body", CubeListBuilder.create().texOffs(68, 0).addBox(-6.5F, -8.5F, -0.5F, 13.0F, 17.0F, 25.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.5F, -2.5F));

		PartDefinition fin_r3 = body.addOrReplaceChild("fin_r3", CubeListBuilder.create().texOffs(104, 86).addBox(0.0F, -6.0F, -5.5F, 0.0F, 6.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -8.5F, 5.0F, 0.0F, 0.0F, -0.3054F));

		PartDefinition fin_r4 = body.addOrReplaceChild("fin_r4", CubeListBuilder.create().texOffs(104, 86).addBox(0.0F, -6.0F, -5.5F, 0.0F, 6.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -8.5F, 5.0F, 0.0F, 0.0F, 0.3054F));

		PartDefinition fin = body.addOrReplaceChild("fin", CubeListBuilder.create().texOffs(0, 110).addBox(-1.5F, -11.0F, -0.5F, 3.0F, 11.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.5F, 12.0F));

		PartDefinition leftfin = body.addOrReplaceChild("leftfin", CubeListBuilder.create().texOffs(52, 90).addBox(-0.5F, -1.0F, -5.0F, 15.0F, 2.0F, 11.0F, new CubeDeformation(0.01F)), PartPose.offset(7.0F, 7.5F, 2.5F));

		PartDefinition rightfin = body.addOrReplaceChild("rightfin", CubeListBuilder.create().texOffs(52, 90).mirror().addBox(-14.5F, -1.0F, -5.0F, 15.0F, 2.0F, 11.0F, new CubeDeformation(0.01F)).mirror(false), PartPose.offset(-7.0F, 7.5F, 2.5F));

		PartDefinition leftfin2 = body.addOrReplaceChild("leftfin2", CubeListBuilder.create().texOffs(84, 91).addBox(-0.5F, 0.0F, -5.0F, 0.0F, 5.0F, 12.0F, new CubeDeformation(0.01F)), PartPose.offset(7.0F, 8.5F, 18.5F));

		PartDefinition rightfin2 = body.addOrReplaceChild("rightfin2", CubeListBuilder.create().texOffs(84, 91).mirror().addBox(-0.5F, 0.0F, -5.0F, 0.0F, 5.0F, 12.0F, new CubeDeformation(0.01F)).mirror(false), PartPose.offset(-6.0F, 8.5F, 18.5F));

		PartDefinition tailrot = body.addOrReplaceChild("tailrot", CubeListBuilder.create(), PartPose.offset(0.0F, -0.5F, 24.5F));

		PartDefinition tail = tailrot.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 69).addBox(-4.5F, -5.0F, 0.0F, 9.0F, 10.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition tail2rot = tail.addOrReplaceChild("tail2rot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 9.0F));

		PartDefinition tail2 = tail2rot.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -22.0F, -3.0F, 0.0F, 35.0F, 34.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition leftfin3 = tail.addOrReplaceChild("leftfin3", CubeListBuilder.create().texOffs(90, 115).addBox(-0.5F, 1.0F, -5.0F, 0.0F, 5.0F, 6.0F, new CubeDeformation(0.01F)), PartPose.offset(2.0F, 4.0F, 5.0F));

		PartDefinition rightfin3 = tail.addOrReplaceChild("rightfin3", CubeListBuilder.create().texOffs(90, 115).addBox(-0.5F, 1.0F, -5.0F, 0.0F, 5.0F, 6.0F, new CubeDeformation(0.01F)), PartPose.offset(-1.0F, 4.0F, 5.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}


	@Override
	public ModelPart root() {
		return this.root;
	}

	@Override
	public void setupAnim(SpottedStrikerEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

		this.root().getAllParts().forEach(ModelPart::resetPose);
		netHeadYaw = Mth.clamp(netHeadYaw, -25.0F, 25.0F) * ((float) Math.PI / 180F);
		headPitch = Mth.clamp(headPitch, -25.0F, 25.0F) * ((float) Math.PI / 180F);

		this.swimcontrol.xRot = entity.isInFluidType()?  headPitch : 0;
		this.gillsrot.yRot = netHeadYaw * 0.5f;
		this.headrot.yRot = netHeadYaw;
		this.tailrot.yRot = -(entity.tilt * (Mth.DEG_TO_RAD) / 1.5f);
		this.tail2rot.yRot = -(entity.tilt * (Mth.DEG_TO_RAD) / 1.5f);

		if (entity.isInFluidType()) {
			this.animate(entity.idleAnimationState, SpottedStrikerAnimation.swim, ageInTicks, limbSwingAmount * 1.5f);
		} else {
			this.animate(entity.idleAnimationState, SpottedStrikerAnimation.flop, ageInTicks, 1f);
		}
		this.animate(entity.abilityAnimationState, this.getStateAnim(entity.getAnimState()), ageInTicks, 1);
	}

	private AnimationDefinition getStateAnim(int i) {
		return switch(i) {
			case 2 -> SpottedStrikerAnimation.bite_strong;
			case 3 -> SpottedStrikerAnimation.tail_swipe_left;
			default -> SpottedStrikerAnimation.bite;
		};
	}
}