package net.dandycorp.dccobblemon.util;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions.SHAKE_PACKET_ID;

public class ScreenShakeController {

    public enum FadeType {
        LINEAR,
        EXPONENTIAL,
        REVERSE_EXPONENTIAL
    }

    public enum DistanceFalloff {
        LINEAR,
        EXPONENTIAL,
        REVERSE_EXPONENTIAL
    }

    // List for delayed shake tasks
    private static final List<DelayedShakeTask> delayedTasks = new ArrayList<>();
    // List for delayed sound tasks
    private static final List<DelayedSoundTask> delayedSoundTasks = new ArrayList<>();

    private static boolean isShaking = false;
    private static float baseIntensity = 0.0f;
    private static float currentIntensity = 0.0f;
    private static int fullIntensityTicksLeft = 0;
    private static int fadeOutTicksLeft = 0;
    private static int fadeOutDuration = 0;
    private static FadeType fadeType = FadeType.LINEAR;

    public static void startShake(float intensity, int maxDuration, int fadeDuration, FadeType fadeType) {
        isShaking = true;
        baseIntensity = intensity;
        currentIntensity = intensity;
        fullIntensityTicksLeft = maxDuration;

        fadeOutTicksLeft = 0;
        fadeOutDuration = fadeDuration;
        ScreenShakeController.fadeType = fadeType;
    }

    public static void startShake(ScreenShake shake) {
        startShake(shake.intensity, shake.maxDuration, shake.fadeDuration, shake.fadeType);
    }

    public static void sendShakeToClient(ServerPlayerEntity player,
                                         float intensity,
                                         int maxDuration,
                                         int fadeDuration,
                                         FadeType fadeType) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeFloat(intensity);
        buf.writeInt(maxDuration);
        buf.writeInt(fadeDuration);
        buf.writeInt(fadeType.ordinal());
        ServerPlayNetworking.send(player, SHAKE_PACKET_ID, buf);
    }

    public static void sendShakeToClient(ServerPlayerEntity player, ScreenShake shake) {
        sendShakeToClient(player, shake.intensity, shake.maxDuration, shake.fadeDuration, shake.fadeType);
    }

    public static boolean isShaking() {
        return isShaking;
    }

    public static float getAndUpdateIntensity() {
        if (!isShaking) {
            return 0.0f;
        }

        if (fullIntensityTicksLeft > 0) {
            fullIntensityTicksLeft--;
            // Enter fade-out
            if (fullIntensityTicksLeft == 0 && fadeOutDuration > 0) {
                fadeOutTicksLeft = fadeOutDuration;
            }
            return baseIntensity;
        }

        if (fadeOutTicksLeft > 0) {
            float fadeFactor = getFadeFactor();
            currentIntensity = baseIntensity * fadeFactor;
            fadeOutTicksLeft--;
            if (fadeOutTicksLeft <= 0) {
                isShaking = false;
                currentIntensity = 0.0f;
            }
            return currentIntensity;
        }

        isShaking = false;
        currentIntensity = 0.0f;
        return 0.0f;
    }

    private static float getFadeFactor() {
        float fadeProgress = 1.0f - (fadeOutTicksLeft / (float) fadeOutDuration);
        return switch (fadeType) {
            case EXPONENTIAL -> 1.0f - (fadeProgress * fadeProgress);
            case REVERSE_EXPONENTIAL -> 1.0f - (float) Math.sqrt(fadeProgress);
            default -> 1.0f - fadeProgress; // LINEAR
        };
    }

    public static <T extends World> void causeTremor(T world, BlockPos pos, float radius, ScreenShake baseShake, DistanceFalloff distanceFalloff) {
        causeTremor(world, pos.getX(), pos.getY(), pos.getZ(), radius, baseShake, distanceFalloff);
    }

    public static <T extends World> void causeTremor(T world, double centerX, double centerY, double centerZ, float maxRadius, ScreenShake baseShake, DistanceFalloff distanceFalloff) {
        if (world.isClient) {
            return;
        }

        double maxDistSq = maxRadius * maxRadius;

        for (PlayerEntity player : world.getPlayers()) {
            double sqDist = player.squaredDistanceTo(centerX, centerY, centerZ);
            if (sqDist <= maxDistSq) {
                float distanceRatio = (float) (sqDist / maxDistSq);
                float scaledIntensity = applyDistanceFalloff(baseShake.intensity, distanceRatio, distanceFalloff);
                ScreenShake scaledShake = new ScreenShake(
                        scaledIntensity,
                        baseShake.maxDuration,
                        baseShake.fadeDuration,
                        baseShake.fadeType
                );
                sendShakeToClient((ServerPlayerEntity) player, scaledShake);
            }
        }
    }

    public static <T extends World> void causeTremorWithDelay(T world, double centerX, double centerY, double centerZ, float maxRadius, ScreenShake baseShake, DistanceFalloff distanceFalloff, int delayTicks) {
        if (world.isClient) {
            return;
        }
        double maxDistSq = maxRadius * maxRadius;
        for (PlayerEntity player : world.getPlayers()) {
            double sqDist = player.squaredDistanceTo(centerX, centerY, centerZ);
            if (sqDist <= maxDistSq) {
                float distanceRatio = (float) (sqDist / maxDistSq);
                float scaledIntensity = applyDistanceFalloff(baseShake.intensity, distanceRatio, distanceFalloff);
                ScreenShake scaledShake = new ScreenShake(
                        scaledIntensity,
                        baseShake.maxDuration,
                        baseShake.fadeDuration,
                        baseShake.fadeType
                );
                shakeWithDelay((ServerPlayerEntity) player, scaledShake, delayTicks);
            }
        }
    }

    public static <T extends World> void causeTremorWithDelay(T world, BlockPos pos, float maxRadius, ScreenShake baseShake, DistanceFalloff distanceFalloff, int delayTicks) {
        causeTremorWithDelay(world, pos.getX(), pos.getY(), pos.getZ(), maxRadius, baseShake, distanceFalloff, delayTicks);
    }

    private static float applyDistanceFalloff(float baseIntensity, float distanceRatio, DistanceFalloff mode) {
        return switch (mode) {
            case LINEAR -> baseIntensity * (1.0f - distanceRatio);
            case EXPONENTIAL -> baseIntensity * (1.0f - (distanceRatio * distanceRatio));
            case REVERSE_EXPONENTIAL -> baseIntensity * (1.0f - (float) Math.sqrt(distanceRatio));
        };
    }

    private static class DelayedShakeTask {
        final ServerPlayerEntity player;
        final ScreenShake shake;
        int remainingTicks;

        DelayedShakeTask(ServerPlayerEntity player, ScreenShake shake, int delayTicks) {
            this.player = player;
            this.shake = shake;
            this.remainingTicks = delayTicks;
        }
    }

    public static void shakeWithDelay(ServerPlayerEntity player, ScreenShake shake, int delayTicks) {
        delayedTasks.add(new DelayedShakeTask(player, shake, delayTicks));
    }

    public static void tickDelayedShakes() {
        if (delayedTasks.isEmpty()) {
            return;
        }
        Iterator<DelayedShakeTask> iter = delayedTasks.iterator();
        while (iter.hasNext()) {
            DelayedShakeTask task = iter.next();
            task.remainingTicks--;
            if (task.remainingTicks <= 0) {
                sendShakeToClient(task.player, task.shake);
                iter.remove();
            }
        }
    }


    private static class DelayedSoundTask {
        final World world;
        final double centerX;
        final double centerY;
        final double centerZ;
        final SoundEvent sound;
        final SoundCategory category;
        final float volume;
        final float pitch;
        int remainingTicks;

        DelayedSoundTask(World world, double centerX, double centerY, double centerZ, int delayTicks, SoundEvent sound, SoundCategory category, float volume, float pitch) {
            this.world = world;
            this.centerX = centerX;
            this.centerY = centerY;
            this.centerZ = centerZ;
            this.remainingTicks = delayTicks;
            this.sound = sound;
            this.category = category;
            this.volume = volume;
            this.pitch = pitch;
        }
    }

    public static void playSoundWithDelay(World world, double centerX, double centerY, double centerZ, int delayTicks, SoundEvent sound, SoundCategory category, float volume, float pitch) {
        delayedSoundTasks.add(new DelayedSoundTask(world, centerX, centerY, centerZ, delayTicks, sound, category, volume, pitch));
    }

    public static void tickDelayedSounds() {
        if (delayedSoundTasks.isEmpty()) {
            return;
        }
        Iterator<DelayedSoundTask> iter = delayedSoundTasks.iterator();
        while (iter.hasNext()) {
            DelayedSoundTask task = iter.next();
            task.remainingTicks--;
            if (task.remainingTicks <= 0) {
                task.world.playSound(null, BlockPos.ofFloored(task.centerX, task.centerY, task.centerZ), task.sound, task.category, task.volume, task.pitch);
                iter.remove();
            }
        }
    }


    /**
     * Causes a tremor immediately and plays a sound at the given position.
     *
     * @param world       The world.
     * @param centerX     X coordinate of the tremor center.
     * @param centerY     Y coordinate of the tremor center.
     * @param centerZ     Z coordinate of the tremor center.
     * @param maxRadius   Maximum radius of effect.
     * @param baseShake   Base shake parameters.
     * @param distanceFalloff How intensity decreases with distance.
     * @param sound       The sound event to play.
     * @param category    Sound category.
     * @param volume      Sound volume.
     * @param pitch       Sound pitch.
     */
    public static <T extends World> void causeTremorWithSound(T world, double centerX, double centerY, double centerZ, float maxRadius, ScreenShake baseShake, DistanceFalloff distanceFalloff, SoundEvent sound, SoundCategory category, float volume, float pitch) {
        if (world.isClient) {
            return;
        }
        causeTremor(world, centerX, centerY, centerZ, maxRadius, baseShake, distanceFalloff);
        world.playSound(null, BlockPos.ofFloored(centerX, centerY, centerZ), sound, category, volume, pitch);
    }

    public static <T extends World> void causeTremorWithSound(T world, BlockPos pos, float maxRadius, ScreenShake baseShake, DistanceFalloff distanceFalloff, SoundEvent sound, SoundCategory category, float volume, float pitch) {
        causeTremorWithSound(world, pos.getX(), pos.getY(), pos.getZ(), maxRadius, baseShake, distanceFalloff, sound, category, volume, pitch);
    }

    /**
     * Causes a tremor with delay and plays a sound at the given position after the delay.
     *
     * @param world       The world.
     * @param centerX     X coordinate of the tremor center.
     * @param centerY     Y coordinate of the tremor center.
     * @param centerZ     Z coordinate of the tremor center.
     * @param maxRadius   Maximum radius of effect.
     * @param baseShake   Base shake parameters.
     * @param distanceFalloff How intensity decreases with distance.
     * @param delayTicks  Delay in ticks before the shake and sound occur.
     * @param sound       The sound event to play.
     * @param category    Sound category.
     * @param volume      Sound volume.
     * @param pitch       Sound pitch.
     */
    public static <T extends World> void causeTremorWithDelayAndSound(T world, double centerX, double centerY, double centerZ, float maxRadius, ScreenShake baseShake, DistanceFalloff distanceFalloff, int delayTicks, SoundEvent sound, SoundCategory category, float volume, float pitch) {
        if (world.isClient) {
            return;
        }
        causeTremorWithDelay(world, centerX, centerY, centerZ, maxRadius, baseShake, distanceFalloff, delayTicks);
        playSoundWithDelay(world, centerX, centerY, centerZ, delayTicks, sound, category, volume, pitch);
    }

    public static <T extends World> void causeTremorWithDelayAndSound(T world, BlockPos pos, float maxRadius, ScreenShake baseShake, DistanceFalloff distanceFalloff, int delayTicks, SoundEvent sound, SoundCategory category, float volume, float pitch) {
        causeTremorWithDelayAndSound(world, pos.getX(), pos.getY(), pos.getZ(), maxRadius, baseShake, distanceFalloff, delayTicks, sound, category, volume, pitch);
    }
}
