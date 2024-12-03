package net.dandycorp.dccobblemon.sound;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;

@Environment(EnvType.CLIENT)
public class GrinderContinuousSound extends MovingSoundInstance {

    private final GrinderSoundScape scape;
    private float relativeVolume;

    public GrinderContinuousSound(SoundEvent soundEvent, GrinderSoundScape scape, float pitch, float relativeVolume) {
        super(soundEvent, SoundCategory.BLOCKS, net.minecraft.util.math.random.Random.create());
        this.scape = scape;
        this.pitch = pitch;
        this.relativeVolume = relativeVolume;
        this.repeat = true;
        this.repeatDelay = 0;
        this.relative = false;
        updatePosition();
    }

    public void setPosition(BlockPos pos) {
        updatePosition();
    }

    private void updatePosition() {
        this.x = scape.getPos().getX() + 0.5;
        this.y = scape.getPos().getY() + 0.5;
        this.z = scape.getPos().getZ() + 0.5;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void stop() {
        this.setDone();
    }

    @Override
    public void tick() {
        if (this.isDone()) return;
        updatePosition();
    }

    @Override
    public float getVolume() {
        return relativeVolume;
    }
}
