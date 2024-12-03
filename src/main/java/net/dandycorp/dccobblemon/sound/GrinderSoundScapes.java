package net.dandycorp.dccobblemon.sound;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class GrinderSoundScapes {

    public enum AmbienceGroup {
        GRINDER_PASSIVE(GrinderSoundScape::grinderPassive),
        GRINDER_ACTIVE(GrinderSoundScape::grinderActive);

        private final SoundScapeFactory factory;

        AmbienceGroup(SoundScapeFactory factory) {
            this.factory = factory;
        }

        public GrinderSoundScape create(float pitch, BlockPos pos) {
            return factory.create(pitch, pos);
        }

        @FunctionalInterface
        private interface SoundScapeFactory {
            GrinderSoundScape create(float pitch, BlockPos pos);
        }
    }

    private static final Map<AmbienceGroup, GrinderSoundScape> activeSounds = new HashMap<>();

    public static void play(AmbienceGroup group, BlockPos pos, float pitch) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null || client.world == null) {
            return;
        }

        // Check if the player is within sound range
        if (client.player.squaredDistanceTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) > 256) {
            return;
        }

        GrinderSoundScape soundScape = activeSounds.get(group);

        if (soundScape == null || !soundScape.isPlaying()) {
            soundScape = group.create(pitch, pos);
            soundScape.play();
            activeSounds.put(group, soundScape);
        } else {
            soundScape.update(pitch, pos);
        }
    }

    public static void stop(AmbienceGroup group) {
        GrinderSoundScape soundScape = activeSounds.remove(group);
        if (soundScape != null) {
            soundScape.stop();
        }
    }

    public static void tick() {
        activeSounds.values().forEach(GrinderSoundScape::tick);
    }
}
