package net.dandycorp.dccobblemon.util;

import net.minecraft.client.sound.AbstractSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;

public class LoopingSoundInstance extends AbstractSoundInstance {

    private final Identifier soundId;
    private boolean playing;

    public LoopingSoundInstance(SoundEvent soundEvent, SoundCategory category, Vec3d position, float volume, float pitch) {
        super(soundEvent, category, Random.create());
        this.soundId = soundEvent.getId();
        this.volume = volume;
        this.pitch = pitch;
        this.x = position.x;
        this.y = position.y;
        this.z = position.z;
        this.attenuationType = AttenuationType.LINEAR;
        this.repeat = true;
        this.playing = true;
    }

    public void stopSound(SoundManager soundManager) {
        if (playing) {
            soundManager.stop(this);
            playing = false;
        }
    }

    public void resumeSound(SoundManager soundManager) {
        playing = true;
    }

    public void resetSound(SoundManager soundManager) {
        if (playing) {
            soundManager.stop(this);
            playing = false;
        }
        soundManager.play(this);
        playing = true;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public Boolean isPlaying(){
        return playing;
    }

}
