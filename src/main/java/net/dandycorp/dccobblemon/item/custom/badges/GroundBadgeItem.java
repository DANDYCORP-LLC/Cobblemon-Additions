package net.dandycorp.dccobblemon.item.custom.badges;

import net.dandycorp.dccobblemon.item.custom.BadgeItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class GroundBadgeItem extends BadgeItem {

    public GroundBadgeItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        super.appendTooltip(itemStack, world, tooltip, tooltipContext);
        tooltip.add(Text.literal("Shovels and pickaxes now mine in 3x3").formatted(Formatting.GRAY));
        tooltip.add(Text.literal("+ Efficiency III").formatted(Formatting.GRAY));
    }

}
