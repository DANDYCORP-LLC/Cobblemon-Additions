package net.dandycorp.dccobblemon.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.List;

public interface ParagoniumFormatting {

    default Text gradientName(ItemStack stack){
        Text baseName;
        if (stack.hasCustomName()){
            baseName = stack.toHoverableText();
        }
        else baseName = stack.getItem().getName();

        int startColor = 0x9600ff;
        int endColor = 0x3a00ce;

        long worldTime = 0;
        if (MinecraftClient.getInstance().world != null) {
            worldTime = MinecraftClient.getInstance().world.getTime();
        }
        int tick = (int)(worldTime % 60);

        MutableText gradientText = TextUtils.createGradientText(baseName, startColor, endColor, tick);
        return gradientText.styled(style -> style.withBold(true));
    }

    default Text gradientText(Text text){
        int startColor = 0x9600ff;
        int endColor = 0x3a00ce;

        long worldTime = 0;
        if (MinecraftClient.getInstance().world != null) {
            worldTime = MinecraftClient.getInstance().world.getTime();
        }
        int tick = (int)(worldTime % 60);

        MutableText gradientText = TextUtils.createGradientText(text, startColor, endColor, tick);
        return gradientText.styled(style -> style.withBold(true));
    }
}
