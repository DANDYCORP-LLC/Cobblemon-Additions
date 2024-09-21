package net.dandycorp.dccobblemon.util;

import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class TextWrapper {

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

}
