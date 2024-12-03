package net.dandycorp.dccobblemon.item.custom.badges;

import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotReference;
import net.dandycorp.dccobblemon.item.Items;
import net.dandycorp.dccobblemon.item.custom.BadgeItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

public class IceBadgeItem extends BadgeItem {

    public IceBadgeItem(Settings settings) {
        super(settings);
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid){
        var modifiers = super.getModifiers(stack, slot, entity, uuid);
        modifiers.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(uuid, "dandycorp:ice_damage", 1.0, EntityAttributeModifier.Operation.ADDITION));
        return modifiers;
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        super.appendTooltip(itemStack, world, tooltip, tooltipContext);
        tooltip.add(Text.literal("Inflicts frost on-hit").formatted(Formatting.GRAY));
        tooltip.add(Text.literal("Inflicts slowness on-hit").formatted(Formatting.GRAY));
        tooltip.add(Text.literal("Grants immunity to frost").formatted(Formatting.GRAY));
        tooltip.add(Text.literal("Grants immunity to slowness").formatted(Formatting.GRAY));
        tooltip.add(Text.literal("Allows walking on powdered snow").formatted(Formatting.GRAY));
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if (!entity.getEntityWorld().isClient) {

            if (entity.age % 20 == 0) {
                if(this.isEquipped(entity,Items.ICE_BADGE)) {
                    entity.setFrozenTicks(0);
                    entity.removeStatusEffect(StatusEffects.SLOWNESS);
                }
            }
        }
    }

    @Override
    public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if (!entity.getEntityWorld().isClient) {
            if (this.isEquipped(entity,Items.ICE_BADGE)) {
                entity.setFrozenTicks(0);
            }
        }
    }

    @Override
    public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if (!entity.getEntityWorld().isClient) {
            entity.setFrozenTicks(0);
            entity.removeStatusEffect(StatusEffects.SLOWNESS);
        }
    }
}
