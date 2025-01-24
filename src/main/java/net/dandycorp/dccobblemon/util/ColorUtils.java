package net.dandycorp.dccobblemon.util;

public class ColorUtils {
    public static int interpolateColor(int startColor, int endColor, float t) {
        // clamp
        t = Math.max(0, Math.min(1, t));

        // unpack
        int sa = (startColor >> 24) & 0xFF;
        int sr = (startColor >> 16) & 0xFF;
        int sg = (startColor >> 8)  & 0xFF;
        int sb = startColor & 0xFF;

        int ea = (endColor >> 24) & 0xFF;
        int er = (endColor >> 16) & 0xFF;
        int eg = (endColor >> 8)  & 0xFF;
        int eb = endColor & 0xFF;

        // default alpha to 255 if none
        if (sa == 0 && startColor <= 0xFFFFFF) sa = 0xFF;
        if (ea == 0 && endColor <= 0xFFFFFF)   ea = 0xFF;

        // lerp each channel
        int a = (int)(sa + (ea - sa) * t);
        int r = (int)(sr + (er - sr) * t);
        int g = (int)(sg + (eg - sg) * t);
        int b = (int)(sb + (eb - sb) * t);

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static float[] colorToFloats(int color) {
        float a = ((color >> 24) & 0xFF) / 255.0f;
        float r = ((color >> 16) & 0xFF) / 255.0f;
        float g = ((color >> 8)  & 0xFF) / 255.0f;
        float b = ( color        & 0xFF) / 255.0f;
        if (a == 0 && color <= 0xFFFFFF) a = 1.0f;
        return new float[]{r, g, b, a};
    }
}
