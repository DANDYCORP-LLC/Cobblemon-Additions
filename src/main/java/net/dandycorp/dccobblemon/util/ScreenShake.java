package net.dandycorp.dccobblemon.util;


public class ScreenShake {
    public float intensity;
    public int maxDuration;
    public int fadeDuration;
    public ScreenShakeController.FadeType fadeType;

    public ScreenShake(float intensity, int maxDuration, int fadeDuration, ScreenShakeController.FadeType fadeType) {
        this.intensity = intensity;
        this.maxDuration = maxDuration;
        this.fadeDuration = fadeDuration;
        this.fadeType = fadeType;
    }
}
