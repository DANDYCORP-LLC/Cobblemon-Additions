package net.dandycorp.dccobblemon.sound;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.dandycorp.dccobblemon.DANDYCORPSounds;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;

@Environment(EnvType.CLIENT)
public class GrinderSoundScape {
    private final GrinderSoundScapes.AmbienceGroup group;
    private final BlockPos pos;
    private GrinderContinuousSound soundInstance;
    private float pitch;

    public GrinderSoundScape(GrinderSoundScapes.AmbienceGroup group, BlockPos pos, float pitch) {
        this.group = group;
        this.pos = pos;
        this.pitch = pitch;
    }

    public static GrinderSoundScape grinderPassive(float pitch, BlockPos pos) {
        return new GrinderSoundScape(GrinderSoundScapes.AmbienceGroup.GRINDER_PASSIVE, pos, pitch)
                .continuous(DANDYCORPSounds.GRINDER_POWERED_EVENT, 0.2f);
    }

    public static GrinderSoundScape grinderActive(float pitch, BlockPos pos) {
        return new GrinderSoundScape(GrinderSoundScapes.AmbienceGroup.GRINDER_ACTIVE, pos, pitch)
                .continuous(DANDYCORPSounds.GRINDER_ACTIVE_EVENT, 0.3f);
    }

    private GrinderSoundScape continuous(SoundEvent soundEvent, float relativeVolume) {
        this.soundInstance = new GrinderContinuousSound(soundEvent, this, pitch, relativeVolume);
        return this;
    }

    public void play() {
        if (soundInstance == null) return;
        SoundManager soundManager = MinecraftClient.getInstance().getSoundManager();
        soundManager.play(soundInstance);
    }

    public void update(float pitch, BlockPos pos) {
        this.pitch = pitch;
        if (soundInstance != null) {
            soundInstance.setPitch(pitch);
            soundInstance.setPosition(pos);
        }
    }

    public void stop() {
        if (soundInstance != null) {
            soundInstance.stop();
            soundInstance = null;
        }
    }

    public void tick() {
        if (soundInstance != null) {
            soundInstance.tick();
        }
    }

    public boolean isPlaying() {
        return soundInstance != null && !soundInstance.isDone();
    }

    public BlockPos getPos() {
        return pos;
    }
}
