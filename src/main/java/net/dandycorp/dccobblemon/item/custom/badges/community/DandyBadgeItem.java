package net.dandycorp.dccobblemon.item.custom.badges.community;

import com.cobblemon.mod.common.api.types.ElementalType;
import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotReference;
import net.dandycorp.dccobblemon.item.custom.BadgeItem;
import net.dandycorp.dccobblemon.util.GradientFormatting;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

public class DandyBadgeItem extends BadgeItem implements GradientFormatting {

    public DandyBadgeItem(Settings settings, List<ElementalType> elementalTypes) {
        super(settings,elementalTypes);
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid){
        var modifiers = super.getModifiers(stack, slot, entity, uuid);
        modifiers.put(EntityAttributes.GENERIC_LUCK, new EntityAttributeModifier(uuid, "dandycorp:luck", 2.0, EntityAttributeModifier.Operation.ADDITION));
        return modifiers;
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        super.appendTooltip(itemStack, world, tooltip, tooltipContext);
        tooltip.add(Text.literal(""));
        tooltip.add(Text.literal("Fortune at any cost").styled(style -> style.withColor(0x00ff00).withItalic(true)));
    }

    @Override
    public Text getName(ItemStack stack) {
        return gradientName(stack);
    }

    @Override
    public int getGradientStartColor() {
        return 0x00ff00;
    }

    @Override
    public int getGradientEndColor() {
        return 0x222430;
    }

    @Override
    public boolean isBold() {
        return true;
    }
}
