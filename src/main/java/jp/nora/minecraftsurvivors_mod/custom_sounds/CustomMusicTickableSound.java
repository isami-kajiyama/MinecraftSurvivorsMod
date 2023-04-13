package jp.nora.minecraftsurvivors_mod.custom_sounds;

import net.minecraft.client.audio.TickableSound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CustomMusicTickableSound extends TickableSound {
    private boolean isStopped = false;

    public CustomMusicTickableSound(ResourceLocation soundLocation, SoundCategory soundCategory, float volume, float pitch) {
        super(new SoundEvent(soundLocation), soundCategory);
        this.volume = volume;
        this.pitch = pitch;
        this.repeat = true;
        this.repeatDelay = 0;
    }

    public void stop() {
        this.isStopped = true;
    }

    @Override
    public void tick() {
        if (isStopped) {
            this.finishPlaying();
        }
    }
}
