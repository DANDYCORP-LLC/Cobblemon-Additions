package net.dandycorp.dccobblemon.item.custom.badges;

import com.cobblemon.mod.common.api.types.ElementalType;
import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotReference;
import net.dandycorp.dccobblemon.item.DANDYCORPItems;
import net.dandycorp.dccobblemon.item.custom.BadgeItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import virtuoel.pehkui.api.ScaleTypes;

import java.util.List;
import java.util.UUID;

public class GhostBadgeItem extends BadgeItem {
    public GhostBadgeItem(Settings settings, List<ElementalType> elementalTypes) {
        super(settings,elementalTypes);
    }
    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        super.appendTooltip(itemStack, world, tooltip, tooltipContext);
        tooltip.add(Text.literal(""));
        tooltip.add(Text.literal("Massively reduced mob detection radius").formatted(Formatting.GRAY));
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {

        if(!entity.getEntityWorld().isClient) {
            if(entity.age % 20 == 0) {
                if (isEquipped(entity, DANDYCORPItems.GHOST_BADGE)) {
                    ScaleTypes.VISIBILITY.getScaleData(entity).setScale(0.2f);
                }
                else {
                    ScaleTypes.VISIBILITY.getScaleData(entity).setScale(1);
                }
            }
        }
    }

    @Override
    public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        ScaleTypes.VISIBILITY.getScaleData(entity).setScale(1);
    }

    @Override
    public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if(!entity.getEntityWorld().isClient) {
            ScaleTypes.VISIBILITY.getScaleData(entity).setScale(0.2f);
        }
    }
}
