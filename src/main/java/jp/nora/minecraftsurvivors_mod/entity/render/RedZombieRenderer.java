package jp.nora.minecraftsurvivors_mod.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import jp.nora.minecraftsurvivors_mod.entity.model.RedZombieModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class RedZombieRenderer extends MobRenderer<RedZombieEntity, RedZombieModel<RedZombieEntity>>{

    private static final ResourceLocation RED_ZOMBIE = new ResourceLocation("minecraftsurvivors_mod","textures/entity/mob/scaredzombie.png");

    public RedZombieRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new RedZombieModel<>(), 0.5f);
    }

    @Override
    public ResourceLocation getEntityTexture(RedZombieEntity entity) {
        return RED_ZOMBIE;
    }

    @Override
    protected void preRenderCallback(RedZombieEntity entityLivingBaseIn, MatrixStack matrixStackIn, float partialTickTime) {

        float scale_size = 1.5F;
        matrixStackIn.scale(scale_size,scale_size,scale_size);
    }

}

