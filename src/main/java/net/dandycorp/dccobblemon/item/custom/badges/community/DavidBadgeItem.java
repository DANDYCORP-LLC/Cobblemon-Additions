package net.dandycorp.dccobblemon.item.custom.badges.community;

import com.cobblemon.mod.common.api.types.ElementalType;
import net.dandycorp.dccobblemon.item.custom.BadgeItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class DavidBadgeItem extends BadgeItem {

    public DavidBadgeItem(Settings settings, List<ElementalType> elementalTypes) {
        super(settings,elementalTypes);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        super.appendTooltip(itemStack, world, tooltip, tooltipContext);
        tooltip.add(Text.literal(""));
        tooltip.add(Text.literal("Divine protection").formatted(Formatting.YELLOW, Formatting.ITALIC));
    }

    @Override
    public int getGradientStartColor() {
        return 0xe19c16;
    }

    @Override
    public int getGradientEndColor() {
        return 0xfdf59f;
    }
}
