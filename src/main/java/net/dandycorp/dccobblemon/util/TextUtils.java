package net.dandycorp.dccobblemon.util;

import com.google.common.base.Strings;
import net.minecraft.MinecraftVersion;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.Font;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.awt.font.FontRenderContext;

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

}
