package jp.nora.minecraftsurvivors_mod.regi;

import jp.nora.minecraftsurvivors_mod.entity.render.RedZombieEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MobEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, "minecraftsurvivors_mod");

    public static final RegistryObject<EntityType<RedZombieEntity>> RED_ZOMBIE = register("red_zombie", RedZombieEntity::new, EntityClassification.MONSTER, 0.6F, 1.95F);


    private static <T extends Entity> RegistryObject<EntityType<T>> register(String id, EntityType.IFactory<T> factory, EntityClassification classification, float width, float height) {
        EntityType<T> type = EntityType.Builder.create(factory, classification)
                .size(width, height)
                .build("minecraftsurvivors_mod:" + id);
        return ENTITY_TYPES.register(id, () -> type);
    }


}
