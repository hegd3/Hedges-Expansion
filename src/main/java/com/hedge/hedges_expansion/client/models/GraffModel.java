package com.hedge.hedges_expansion.client.models;


import com.hedge.hedges_expansion.client.animations.GraffAnimation;
import com.hedge.hedges_expansion.client.layer.EntityLayers;
import com.hedge.hedges_expansion.entity.living.GraffEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class GraffModel extends HierarchicalModel<GraffEntity> {
	public static final ModelLayerLocation LAYER_LOCATION = EntityLayers.GRAFF_LAYER;
	private final ModelPart root;
	private final ModelPart upperbody;
	private final ModelPart body;
	private final ModelPart tail;
	private final ModelPart tail2;
	private final ModelPart neck;
	private final ModelPart neckrot;
	private final ModelPart headrot;
	private final ModelPart head;
	private final ModelPart jaw;
	private final ModelPart leftear;
	private final ModelPart rightear;
	private final ModelPart leftleg;
	private final ModelPart leftlegpos;
	private final ModelPart leftleg2;
	private final ModelPart leftlegpos2;
	private final ModelPart rightleg;
	private final ModelPart rightlegpos;
	private final ModelPart rightleg2;
	private final ModelPart rightlegpos2;

	public GraffModel(ModelPart root) {
		this.root = root.getChild("root");
		this.upperbody = this.root.getChild("upperbody");
		this.body = this.upperbody.getChild("body");
		this.tail = this.upperbody.getChild("tail");
		this.tail2 = this.tail.getChild("tail2");
		this.neck = this.upperbody.getChild("neck");
		this.neckrot = this.neck.getChild("neckrot");
		this.headrot = this.neckrot.getChild("headrot");
		this.head = this.headrot.getChild("head");
		this.jaw = this.head.getChild("jaw");
		this.leftear = this.jaw.getChild("leftear");
		this.rightear = this.jaw.getChild("rightear");
		this.leftleg = this.root.getChild("leftleg");
		this.leftlegpos = this.leftleg.getChild("leftlegpos");
		this.leftleg2 = this.root.getChild("leftleg2");
		this.leftlegpos2 = this.leftleg2.getChild("leftlegpos2");
		this.rightleg = this.root.getChild("rightleg");
		this.rightlegpos = this.rightleg.getChild("rightlegpos");
		this.rightleg2 = this.root.getChild("rightleg2");
		this.rightlegpos2 = this.rightleg2.getChild("rightlegpos2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 15.0F, 0.0F));

		PartDefinition upperbody = root.addOrReplaceChild("upperbody", CubeListBuilder.create(), PartPose.offset(0.0F, -19.0F, -0.5F));

		PartDefinition body = upperbody.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-8.5F, -7.0F, -10.5F, 17.0F, 13.0F, 21.0F, new CubeDeformation(0.0F))
		.texOffs(76, 33).addBox(0.0F, -13.0F, 1.5F, 0.0F, 6.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(0, 34).addBox(-8.5F, 6.0F, -10.5F, 17.0F, 2.0F, 21.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition tail = upperbody.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(76, 46).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.5F, 10.5F));

		PartDefinition tail2 = tail.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(76, 20).addBox(-2.5F, -3.5F, 0.0F, 5.0F, 7.0F, 6.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, 5.0F));

		PartDefinition neck = upperbody.addOrReplaceChild("neck", CubeListBuilder.create(), PartPose.offset(0.0F, -2.0F, -10.5F));

		PartDefinition neckrot = neck.addOrReplaceChild("neckrot", CubeListBuilder.create().texOffs(0, 70).addBox(-4.5F, -19.0F, -2.0F, 9.0F, 23.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(30, 70).addBox(-6.5F, -19.0F, -3.0F, 13.0F, 8.0F, 8.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition headrot = neckrot.addOrReplaceChild("headrot", CubeListBuilder.create(), PartPose.offset(0.0F, -19.0F, 1.0F));

		PartDefinition head = headrot.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 57).addBox(-7.5F, -2.0F, -9.0F, 15.0F, 2.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition jaw = head.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(52, 57).addBox(-7.5F, -2.0F, -11.0F, 15.0F, 2.0F, 11.0F, new CubeDeformation(0.01F))
		.texOffs(76, 13).addBox(-5.5F, -5.0F, -4.0F, 11.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(40, 103).addBox(0.5F, -7.0F, -6.0F, 3.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(38, 113).addBox(0.5F, -9.0F, -6.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(38, 113).mirror().addBox(-3.5F, -9.0F, -6.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(40, 103).mirror().addBox(-3.5F, -7.0F, -6.0F, 3.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -2.0F, 2.0F));

		PartDefinition leftear = jaw.addOrReplaceChild("leftear", CubeListBuilder.create().texOffs(76, 54).addBox(0.0F, -2.5F, 0.0F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(4.5F, -3.5F, -1.0F));

		PartDefinition rightear = jaw.addOrReplaceChild("rightear", CubeListBuilder.create().texOffs(76, 54).mirror().addBox(-4.0F, -2.5F, 0.0F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-4.5F, -3.5F, -1.0F));

		PartDefinition leftleg = root.addOrReplaceChild("leftleg", CubeListBuilder.create(), PartPose.offset(5.0F, -13.0F, -7.5F));

		PartDefinition leftlegpos = leftleg.addOrReplaceChild("leftlegpos", CubeListBuilder.create().texOffs(72, 70).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 22.0F, 5.0F, new CubeDeformation(0.01F))
		.texOffs(96, 35).addBox(-3.5F, 11.0F, -3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition leftleg2 = root.addOrReplaceChild("leftleg2", CubeListBuilder.create(), PartPose.offset(5.0F, -13.0F, 6.5F));

		PartDefinition leftlegpos2 = leftleg2.addOrReplaceChild("leftlegpos2", CubeListBuilder.create().texOffs(72, 70).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 22.0F, 5.0F, new CubeDeformation(0.01F))
		.texOffs(96, 35).addBox(-3.5F, 11.0F, -3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition rightleg = root.addOrReplaceChild("rightleg", CubeListBuilder.create(), PartPose.offset(-5.0F, -13.0F, -7.5F));

		PartDefinition rightlegpos = rightleg.addOrReplaceChild("rightlegpos", CubeListBuilder.create().texOffs(72, 70).mirror().addBox(-2.5F, 0.0F, -2.5F, 5.0F, 22.0F, 5.0F, new CubeDeformation(0.01F)).mirror(false)
		.texOffs(96, 35).mirror().addBox(-3.5F, 11.0F, -3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition rightleg2 = root.addOrReplaceChild("rightleg2", CubeListBuilder.create(), PartPose.offset(-5.0F, -13.0F, 6.5F));

		PartDefinition rightlegpos2 = rightleg2.addOrReplaceChild("rightlegpos2", CubeListBuilder.create().texOffs(72, 70).mirror().addBox(-2.5F, 0.0F, -2.5F, 5.0F, 22.0F, 5.0F, new CubeDeformation(0.01F)).mirror(false)
		.texOffs(96, 35).mirror().addBox(-3.5F, 11.0F, -3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}


	@Override
	public ModelPart root() {
		return this.root;
	}

	@Override
	public void setupAnim(GraffEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		netHeadYaw = Mth.clamp(netHeadYaw, -15.0F, 15.0F) * ((float) Math.PI / 180F);
		headPitch = Mth.clamp(headPitch, -5.0F, 25.0F) * ((float) Math.PI / 180F);
		this.headrot.yRot = netHeadYaw;
		this.headrot.xRot = headPitch;
		this.neckrot.yRot = netHeadYaw / 2;

		this.animate(entity.idleAnimationState, GraffAnimation.idle, ageInTicks, 0.6f);
		if (entity.getDeltaMovement().horizontalDistanceSqr() > 0.01) {
			this.animateWalk(GraffAnimation.run, limbSwing, limbSwingAmount / 2, 1.2f, 2.5f);
		} else {
			this.animateWalk(GraffAnimation.walk, limbSwing, limbSwingAmount, 1.5f, 2.5f);
		}
	}
}