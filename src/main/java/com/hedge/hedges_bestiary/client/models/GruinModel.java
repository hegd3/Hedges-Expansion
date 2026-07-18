package com.hedge.hedges_bestiary.client.models;// Made with Blockbench 5.0.7
import com.hedge.hedges_bestiary.client.animations.GenericPosesAnimation;
import com.hedge.hedges_bestiary.client.animations.GruinAnimation;
import com.hedge.hedges_bestiary.client.layer.EntityLayers;
import com.hedge.hedges_bestiary.entity.living.GruinEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class GruinModel extends HBModel<GruinEntity> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = EntityLayers.GRUIN_LAYER;
	private final ModelPart root;
	private final ModelPart wholebody;
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart headrot;
	private final ModelPart nose;
	private final ModelPart jaw;
	private final ModelPart body2;
	private final ModelPart leftarm;
	private final ModelPart leftarmpos;
	private final ModelPart rightarm;
	private final ModelPart rightarmpos;
	private final ModelPart leftleg;
	private final ModelPart leftlegpos;
	private final ModelPart rightleg;
	private final ModelPart rightlegpos;

	public GruinModel(ModelPart root) {
		super(0.5f, 24);
		this.root = root.getChild("root");
		this.wholebody = this.root.getChild("wholebody");
		this.body = this.wholebody.getChild("body");
		this.head = this.body.getChild("head");
		this.headrot = this.head.getChild("headrot");
		this.nose = this.headrot.getChild("nose");
		this.jaw = this.headrot.getChild("jaw");
		this.body2 = this.wholebody.getChild("body2");
		this.leftarm = this.root.getChild("leftarm");
		this.leftarmpos = this.leftarm.getChild("leftarmpos");
		this.rightarm = this.root.getChild("rightarm");
		this.rightarmpos = this.rightarm.getChild("rightarmpos");
		this.leftleg = this.root.getChild("leftleg");
		this.leftlegpos = this.leftleg.getChild("leftlegpos");
		this.rightleg = this.root.getChild("rightleg");
		this.rightlegpos = this.rightleg.getChild("rightlegpos");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 14.0F, 0.0F));

		PartDefinition wholebody = root.addOrReplaceChild("wholebody", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, 15.0F));

		PartDefinition body = wholebody.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-12.5F, -15.0F, -22.0F, 25.0F, 30.0F, 22.0F, new CubeDeformation(0.0F))
		.texOffs(0, 52).addBox(-12.5F, 15.0F, -22.0F, 25.0F, 2.0F, 22.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -13.0F, -5.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 2.0F, -22.0F));

		PartDefinition headrot = head.addOrReplaceChild("headrot", CubeListBuilder.create().texOffs(22, 114).addBox(-3.5F, 0.0F, -13.0F, 7.0F, 1.0F, 4.0F, new CubeDeformation(0.02F))
		.texOffs(94, 16).addBox(-7.5F, -8.0F, -9.0F, 15.0F, 12.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(36, 121).addBox(-2.5F, 1.0F, -12.8F, 5.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(94, 58).addBox(-6.5F, 4.0F, -9.0F, 13.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(44, 114).addBox(3.5F, -11.0F, -5.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(44, 114).mirror().addBox(-7.5F, -11.0F, -5.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition nose = headrot.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(94, 68).addBox(-3.5F, -1.0F, -2.0F, 7.0F, 3.0F, 4.0F, new CubeDeformation(0.025F)), PartPose.offset(0.0F, -2.0F, -11.0F));

		PartDefinition jaw = headrot.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(0, 114).addBox(-3.5F, 0.0F, -4.0F, 7.0F, 2.0F, 4.0F, new CubeDeformation(0.01F))
		.texOffs(22, 119).addBox(-1.5F, -2.0F, -4.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, -9.0F));

		PartDefinition body2 = wholebody.addOrReplaceChild("body2", CubeListBuilder.create().texOffs(0, 76).addBox(-11.5F, -12.0F, 0.0F, 23.0F, 26.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(94, 0).addBox(-11.5F, 14.0F, 0.0F, 23.0F, 4.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(116, 68).addBox(0.0F, -14.0F, 3.0F, 0.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -13.0F, -5.0F));

		PartDefinition leftarm = root.addOrReplaceChild("leftarm", CubeListBuilder.create(), PartPose.offset(8.0F, -20.0F, -2.0F));

		PartDefinition leftarmpos = leftarm.addOrReplaceChild("leftarmpos", CubeListBuilder.create().texOffs(70, 76).addBox(-6.5F, 0.0F, -8.0F, 13.0F, 30.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(0, 120).addBox(2.5F, 27.0F, -11.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 120).addBox(-5.5F, 27.0F, -11.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 120).addBox(-1.5F, 27.0F, -11.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition rightarm = root.addOrReplaceChild("rightarm", CubeListBuilder.create(), PartPose.offset(-8.0F, -20.0F, -2.0F));

		PartDefinition rightarmpos = rightarm.addOrReplaceChild("rightarmpos", CubeListBuilder.create().texOffs(70, 76).mirror().addBox(-6.5F, 0.0F, -8.0F, 13.0F, 30.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 120).mirror().addBox(-5.5F, 27.0F, -11.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 120).mirror().addBox(2.5F, 27.0F, -11.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(0, 120).mirror().addBox(-1.5F, 27.0F, -11.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition leftleg = root.addOrReplaceChild("leftleg", CubeListBuilder.create(), PartPose.offset(5.0F, 0.0F, 14.5F));

		PartDefinition leftlegpos = leftleg.addOrReplaceChild("leftlegpos", CubeListBuilder.create().texOffs(94, 37).addBox(-4.5F, 0.0F, -5.5F, 9.0F, 10.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition rightleg = root.addOrReplaceChild("rightleg", CubeListBuilder.create(), PartPose.offset(-5.0F, 0.0F, 14.5F));

		PartDefinition rightlegpos = rightleg.addOrReplaceChild("rightlegpos", CubeListBuilder.create().texOffs(94, 37).mirror().addBox(-4.5F, 0.0F, -5.5F, 9.0F, 10.0F, 11.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}

	@Override
	public void setupAnim(GruinEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		netHeadYaw = Mth.clamp(netHeadYaw, -15.0F, 15.0F) * ((float)Math.PI / 180F);
		headPitch = Mth.clamp(headPitch, -5.0F, 25.0F) * ((float)Math.PI / 180F);

		this.headrot.yRot = netHeadYaw;
		this.headrot.xRot = headPitch;
		if (this.young) {
			this.applyStatic(GenericPosesAnimation.BABY_TRANSFORM);
		}
		this.animate(entity.idleAnimationState, GruinAnimation.idle, ageInTicks, 0.5f);
		this.animateWalk(GruinAnimation.walk, limbSwing, limbSwingAmount, 2.0f, 2.2f);
		this.animate(entity.biteAnimationState, GruinAnimation.bite, ageInTicks);
		this.animate(entity.swipeAnimationState, entity.swingingLeft() ? GruinAnimation.swipe_left : GruinAnimation.swipe_right, ageInTicks);
		this.animate(entity.multiAttackAnimationState, entity.swingingLeft() ? GruinAnimation.multiattack_left : GruinAnimation.multiattack_right, ageInTicks);

		this.animateSmooth(entity.sitAnimationState, GruinAnimation.sit, ageInTicks, 1f);
		this.animateSmooth(entity.sniffAnimationState, GruinAnimation.sniff, ageInTicks, 1f);
		this.animateSmooth(entity.yawnAnimationState, GruinAnimation.yawn, ageInTicks, 1f);
	}
}