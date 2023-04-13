package jp.nora.minecraftsurvivors_mod.custom_sounds;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MSModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, "minecraftsurvivors_mod");

    public static final RegistryObject<SoundEvent> POINT = SOUNDS.register("point", () -> new SoundEvent(new ResourceLocation("minecraftsurvivors_mod", "point")));
    public static final RegistryObject<SoundEvent> BIG_POINT = SOUNDS.register("bigpoint", () -> new SoundEvent(new ResourceLocation("minecraftsurvivors_mod", "bigpoint")));
    public static final RegistryObject<SoundEvent> RECOVERY = SOUNDS.register("recovery", () -> new SoundEvent(new ResourceLocation("minecraftsurvivors_mod", "recovery")));
}