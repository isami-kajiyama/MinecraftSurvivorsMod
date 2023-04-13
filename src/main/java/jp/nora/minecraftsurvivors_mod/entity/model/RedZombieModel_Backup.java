//package jp.nora.minecraftsurvivors_mod.entity.model;
//
//import com.google.common.collect.ImmutableList;
//import net.minecraft.client.renderer.entity.model.*;
//import net.minecraft.client.renderer.model.ModelRenderer;
//import net.minecraft.entity.Entity;
//
//public class RedZombieModel_Backup<T extends Entity> extends SegmentedModel<T> implements IHasHead, IHeadToggle {
//	private final ModelRenderer Head;
//	private final ModelRenderer Body;
//	private final ModelRenderer RightArm;
//	private final ModelRenderer LeftArm;
//	private final ModelRenderer RightLeg;
//	private final ModelRenderer LeftLeg;
//	private final ModelRenderer Hone;
//	private final ModelRenderer hone_r1;
//
//	public RedZombieModel_Backup() {
//		this(64,64);
//	}
//
//	public RedZombieModel_Backup(int w, int h) {
//
//		Head = new ModelRenderer(this).setTextureSize(w, h);
//		Head.setRotationPoint(0.0F, 0.0F, 0.0F);
//		Head.setTextureOffset(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
//
//		Body = new ModelRenderer(this).setTextureSize(w, h);
//		Body.setRotationPoint(0.0F, 0.0F, 0.0F);
//		Body.setTextureOffset(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.0F, false);
//
//		RightArm = new ModelRenderer(this).setTextureSize(w, h);
//		RightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
//		RightArm.setTextureOffset(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
//
//		LeftArm = new ModelRenderer(this).setTextureSize(w, h);
//		LeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
//		LeftArm.setTextureOffset(40, 16).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
//
//		RightLeg = new ModelRenderer(this).setTextureSize(w, h);
//		RightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
//		RightLeg.setTextureOffset(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
//
//		LeftLeg = new ModelRenderer(this).setTextureSize(w, h);
//		LeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
//		LeftLeg.setTextureOffset(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
//
//		Hone = new ModelRenderer(this).setTextureSize(w, h);
//		Hone.setRotationPoint(0.0F, 0.0F, 0.0F);
//		Hone.setTextureOffset(0, 0).addBox(2.5F, -10.0F, -4.3F, 1.0F, 3.0F, 1.0F, 0.0F, false);
//		Hone.setTextureOffset(0, 0).addBox(-3.5F, -10.0F, -4.3F, 1.0F, 3.0F, 1.0F, 0.0F, false);
//		Head.addChild(Hone);
//
//		hone_r1 = new ModelRenderer(this).setTextureSize(w, h);
//		hone_r1.setRotationPoint(-3.0F, -10.5F, -4.0F);
//		Hone.addChild(hone_r1);
//		hone_r1.setTextureOffset(0, 0).addBox(-0.5F, -1.5F, -0.125F, 1.0F, 3.0F, 1.0F, -0.8F, false);
//		hone_r1.setTextureOffset(0, 0).addBox(5.5F, -1.5F, -0.125F, 1.0F, 3.0F, 1.0F, -0.8F, false);
//	}
//
//	@Override
//	public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
//		this.hone_r1.rotateAngleX = -0.3927F;
//	}
//
//	@Override
//	public Iterable<ModelRenderer> getParts() {
//		return ImmutableList.of(this.Head, this.Body, this.RightArm, this.LeftArm, this.RightLeg, this.LeftLeg);
//	}
//
//	@Override
//	public ModelRenderer getModelHead(){
//		return this.Head;
//	}
//
//	@Override
//	public void func_217146_a(boolean p_217146_1_) {
//		this.Head.showModel = p_217146_1_;
//
//	}
//
////	@Override
////	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
////		//previously the render function, render code was moved to a method below
////	}
////
////	@Override
////	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
////		Head.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
////		Body.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
////		RightArm.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
////		LeftArm.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
////		RightLeg.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
////		LeftLeg.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
////		Hone.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
////	}
////
////	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
////		modelRenderer.rotateAngleX = x;
////		modelRenderer.rotateAngleY = y;
////		modelRenderer.rotateAngleZ = z;
////	}
//}