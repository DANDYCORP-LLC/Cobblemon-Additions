package net.dandycorp.dccobblemon.item.custom.badges;

import com.cobblemon.mod.common.api.types.ElementalType;
import net.dandycorp.dccobblemon.item.custom.BadgeItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class FairyBadgeItem extends BadgeItem {
    public FairyBadgeItem(Settings settings, List<ElementalType> elementalTypes) {
        super(settings,elementalTypes);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        super.appendTooltip(itemStack, world, tooltip, tooltipContext);
        tooltip.add(Text.literal(""));
        tooltip.add(Text.literal("Prevents mobs from spawning nearby").formatted(Formatting.GRAY));
        tooltip.add(Text.literal("Doubles Pokemon friendship gain").formatted(Formatting.GRAY));
    }
}
