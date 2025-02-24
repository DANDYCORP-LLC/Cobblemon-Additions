package net.dandycorp.dccobblemon.util;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import static net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions.SHAKE_PACKET_ID;

public class ScreenShakeController {

    public enum FadeType {
        LINEAR,
        EXPONENTIAL,
        REVERSE_EXPONENTIAL
    }

    private static boolean isShaking = false;
    private static float baseIntensity = 0.0f;
    private static float currentIntensity = 0.0f;
    private static int fullIntensityTicksLeft = 0;
    private static int fadeOutTicksLeft = 0;
    private static int fadeOutDuration = 0;
    private static FadeType fadeType = FadeType.LINEAR;

    /**
     * Start the camera shaking at the given intensity for 'maxDuration' ticks,
     * then fade to zero intensity over 'fadeDuration' ticks according to 'fadeType'.
     *
     * @param intensity    the maximum shake intensity
     * @param maxDuration  how many ticks to stay at max intensity
     * @param fadeDuration how many ticks to fade out
     * @param fadeType     the fade function to use
     */
    public static void startShake(float intensity, int maxDuration, int fadeDuration, FadeType fadeType) {
        isShaking = true;
        baseIntensity = intensity;
        currentIntensity = intensity;
        fullIntensityTicksLeft = maxDuration;

        fadeOutTicksLeft = 0;
        fadeOutDuration = fadeDuration;
        ScreenShakeController.fadeType = fadeType;
    }

    public static void sendShakeToClient(ServerPlayerEntity player,float intensity,int maxDuration,int fadeDuration,ScreenShakeController.FadeType fadeType) {
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeFloat(intensity);
            buf.writeInt(maxDuration);
            buf.writeInt(fadeDuration);
            buf.writeInt(fadeType.ordinal());
            ServerPlayNetworking.send(player, SHAKE_PACKET_ID, buf);
    }

    /**
     * Whether we should apply camera shake this tick.
     */
    public static boolean isShaking() {
        return isShaking;
    }

    /**
     * Get the intensity for this tick, update our timers, and
     * return the intensity value to be used for random offsets.
     */
    public static float getAndUpdateIntensity() {
        if (!isShaking) {
            return 0.0f;
        }

        if (fullIntensityTicksLeft > 0) {
            fullIntensityTicksLeft--;
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
        float fadeFactor;
        switch (fadeType) {
            case EXPONENTIAL -> {
                fadeFactor = 1.0f - (fadeProgress * fadeProgress);
            }
            case REVERSE_EXPONENTIAL -> {
                fadeFactor = 1.0f - (float) Math.sqrt(fadeProgress);
            }
            default -> {
                fadeFactor = 1.0f - fadeProgress;
            }
        }
        return fadeFactor;
    }
}
