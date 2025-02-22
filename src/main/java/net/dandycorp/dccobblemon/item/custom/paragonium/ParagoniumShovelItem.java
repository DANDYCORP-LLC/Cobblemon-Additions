package net.dandycorp.dccobblemon.item.custom.paragonium;

import net.dandycorp.dccobblemon.util.GradientFormatting;
import net.dandycorp.dccobblemon.util.ParagoniumFormatting;
import net.dandycorp.dccobblemon.util.TextUtils;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ParagoniumShovelItem extends ShovelItem implements ParagoniumFormatting {
    public ParagoniumShovelItem(ToolMaterial toolMaterial, float f, float g, Settings settings) {
        super(toolMaterial, f, g, settings);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, @Nullable World world, List<Text> list, TooltipContext tooltipContext) {
        super.appendTooltip(itemStack, world, list, tooltipContext);
        list.addAll(TextUtils.wrapText(Text.translatable("item.dccobblemon.paragonium_tool.description").formatted(Formatting.GRAY),30));
    }

    @Override
    public Text getName(ItemStack stack) {
        return gradientName(stack);
    }

    @Override
    public boolean isDamageable() {
        return false;
    }


}
