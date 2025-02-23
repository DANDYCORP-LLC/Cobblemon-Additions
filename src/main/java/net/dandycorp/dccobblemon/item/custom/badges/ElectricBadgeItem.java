package net.dandycorp.dccobblemon.item.custom.badges;

import com.cobblemon.mod.common.api.types.ElementalType;
import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotReference;
import net.dandycorp.dccobblemon.item.custom.BadgeItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.UUID;

public class ElectricBadgeItem extends BadgeItem {
    public ElectricBadgeItem(Settings settings, List<ElementalType> elementalTypes) {
        super(settings,elementalTypes);
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid){
        var modifiers = super.getModifiers(stack, slot, entity, uuid);
        modifiers.put(EntityAttributes.GENERIC_MOVEMENT_SPEED, new EntityAttributeModifier(uuid, "dandycorp:electric_speed", 0.20, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
        return modifiers;
    }
}
