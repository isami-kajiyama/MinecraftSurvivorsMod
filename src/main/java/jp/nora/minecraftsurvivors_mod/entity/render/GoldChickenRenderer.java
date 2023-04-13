//package jp.nora.minecraftsurvivors_mod.entity.render;
//
//import net.minecraft.client.model.geom.ModelLayers;
//import net.minecraft.client.renderer.entity.EntityRendererProvider;
//import net.minecraft.client.renderer.entity.MobRenderer;
//import net.minecraft.resources.ResourceLocation;
//
//public class GoldChickenRenderer extends MobRenderer<GoldChicken, GoldChickenModel<GoldChicken>> {
//    private static final ResourceLocation GOLD_CHICKEN_TEXTURE = new ResourceLocation("modid", "textures/entity/gold_chicken.png");
//
//    public GoldChickenRenderer(EntityRendererProvider.Context context) {
//        super(context, new GoldChickenModel<>(context.bakeLayer(GoldChickenModel.LAYER_LOCATION)), 0.3F);
//    }
//
//    @Override
//    public ResourceLocation getTextureLocation(GoldChicken entity) {
//        return GOLD_CHICKEN_TEXTURE;
//    }
//}
