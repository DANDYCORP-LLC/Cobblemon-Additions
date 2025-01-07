package net.dandycorp.dccobblemon.util;

import com.google.common.base.Strings;
import net.minecraft.MinecraftVersion;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.Font;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

import java.awt.font.FontRenderContext;
import java.util.ArrayList;
import java.util.List;

public class TextUtils {

    public static String wrapString(String text, int maxLineLength) {
        StringBuilder wrappedText = new StringBuilder();
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();

        for (String word : words) {
            if (line.length() + word.length() > maxLineLength) {
                wrappedText.append(line.toString().trim()).append("\n"); // Add the current line with a newline
                line = new StringBuilder(); // Start a new line
            }
            line.append(word).append(" "); // Add the word to the line
        }

        if (line.length() > 0) {
            wrappedText.append(line.toString().trim()); // Add the last line
        }

        return wrappedText.toString();
    }

    public static Text progressBar(float percent){
        percent = Math.max(percent,0);
        int complete = (int) (40 * percent);
        int incomplete = Math.max(40 - complete, 0);
        return Text.of((Formatting.DARK_GRAY + "" + Formatting.BOLD + "[") + Formatting.RESET +
                (Formatting.GOLD + Strings.repeat("|",complete)) +
                (Formatting.GRAY + Strings.repeat("|",incomplete)) +
                (Formatting.DARK_GRAY + "" + Formatting.BOLD + "]"));
    }

    public static List<Text> wrapText(Text text, int maxLineLength) {
        List<Text> wrappedTexts = new ArrayList<>();

        String plainString = text.getString();
        String wrappedString = wrapString(plainString, maxLineLength);
        String[] lines = wrappedString.split("\n");
        Style originalStyle = text.getStyle();

        for (String line : lines) {
            Text lineText = Text.literal(line).styled(style -> style.withColor(originalStyle.getColor()).withBold(originalStyle.isBold()).withItalic(originalStyle.isItalic()).withUnderline(originalStyle.isUnderlined()).withStrikethrough(originalStyle.isStrikethrough()).withObfuscated(originalStyle.isObfuscated()));
            wrappedTexts.add(lineText);
        }
        return wrappedTexts;
    }

    public static MutableText createGradientText(Text originalText, int startColorHex, int endColorHex, int tick) {
        String content = originalText.getString();
        int length = content.length();
        if (length == 0) {
            return originalText.copy();
        }
        int cycleTick = tick % 60;
        MutableText result = Text.empty();
        for (int i = 0; i < length; i++) {
            char c = content.charAt(i);
            float charFraction = (float) i / (float) (length - 1);
            float timeOffset = (float) cycleTick / 60.0f;
            float adjustedFraction = (charFraction - timeOffset + 1.0f) % 1.0f;
            int color;
            if (adjustedFraction <= 0.5f) {
                float t = adjustedFraction / 0.5f;
                color = interpolateColor(endColorHex, startColorHex, t);
            } else {
                float t = (adjustedFraction - 0.5f) / 0.5f;
                color = interpolateColor(startColorHex, endColorHex, t);
            }
            MutableText charText = Text.literal(String.valueOf(c))
                    .setStyle(Style.EMPTY.withColor(TextColor.fromRgb(color)));
            result.append(charText);
        }
        return result;
    }

    private static int interpolateColor(int startColor, int endColor, float t) {
        int startR = (startColor >> 16) & 0xFF;
        int startG = (startColor >> 8) & 0xFF;
        int startB = startColor & 0xFF;

        int endR = (endColor >> 16) & 0xFF;
        int endG = (endColor >> 8) & 0xFF;
        int endB = endColor & 0xFF;

        int r = (int)(startR + (endR - startR) * t);
        int g = (int)(startG + (endG - startG) * t);
        int b = (int)(startB + (endB - startB) * t);

        return (r << 16) | (g << 8) | b;
    }

}
