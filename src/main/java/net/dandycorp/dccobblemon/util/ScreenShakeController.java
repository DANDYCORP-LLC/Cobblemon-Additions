package net.dandycorp.dccobblemon.util;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
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

    private static final List<DelayedShakeTask> delayedTasks = new ArrayList<>();

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

        // Completed fade, or no fade at all
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
        causeTremor(world,pos.getX(),pos.getY(),pos.getZ(),radius, baseShake, distanceFalloff);
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
                ScreenShakeController.sendShakeToClient((ServerPlayerEntity) player, scaledShake);
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
                // Instead of sending the shake immediately, schedule it with the given delay
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
}
