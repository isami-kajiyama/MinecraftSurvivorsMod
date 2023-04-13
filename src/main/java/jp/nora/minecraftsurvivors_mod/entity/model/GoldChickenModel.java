//package jp.nora.minecraftsurvivors_mod.entity.model;
//
//import net.minecraft.client.model.EntityModel;
//import net.minecraft.client.model.geom.ModelPart;
//import net.minecraft.client.renderer.model.ModelRenderer;
//import net.minecraft.world.entity.Entity;
//
//public class GoldChickenModel<T extends Entity> extends EntityModel<T> {
//	private final ModelPart body;
//	private final ModelPart head;
//	private final ModelPart leg0;
//	private final ModelPart leg1;
//	private final ModelPart wing0;
//	private final ModelPart wing1;
//
//	public GoldChickenModel(ModelPart root) {
//		this.body = root.getChild("body");
//		this.head = root.getChild("head");
//		this.leg0 = root.getChild("leg0");
//		this.leg1 = root.getChild("leg1");
//		this.wing0 = root.getChild("wing0");
//		this.wing1 = root.getChild("wing1");
//	}
//
//	public static LayerDefinition createBodyLayer() {
//		MeshDefinition meshdefinition = new MeshDefinition();
//		PartDefinition partdefinition = meshdefinition.getRoot();
//
//		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 9).addBox(-3.0F, -4.0F, -3.0F, 6.0F, 8.0
//
