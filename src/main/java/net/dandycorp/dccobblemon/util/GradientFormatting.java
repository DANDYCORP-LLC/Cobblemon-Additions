package net.dandycorp.dccobblemon.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public interface GradientFormatting {

    int getGradientStartColor();
    int getGradientEndColor();
    boolean isBold();

    default Text gradientName(ItemStack stack){
        Text baseName = stack.hasCustomName() ? stack.toHoverableText() : stack.getItem().getName();
        return gradientText(baseName);
    }

    default Text gradientText(Text text){
        if (FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT) {
            return text;
        }
        long worldTime = (MinecraftClient.getInstance().world != null)
                ? MinecraftClient.getInstance().world.getTime() : 0;
        int tick = (int)(worldTime % 60);
        MutableText gradientText = TextUtils.createGradientText(text, getGradientStartColor(), getGradientEndColor(), tick);
        return isBold() ? gradientText.styled(style -> style.withBold(true)) : gradientText;
    }
}
