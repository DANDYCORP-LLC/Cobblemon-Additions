package net.dandycorp.dccobblemon.item.custom.badges;

import com.google.common.collect.Multimap;
import de.dafuqs.additionalentityattributes.AdditionalEntityAttributes;
import dev.emi.trinkets.api.SlotReference;
import net.dandycorp.dccobblemon.item.Items;
import net.dandycorp.dccobblemon.item.custom.BadgeItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

public class WaterBadgeItem extends BadgeItem {

    public WaterBadgeItem(Settings settings) {
        super(settings);
    }

    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid){
        var modifiers = super.getModifiers(stack, slot, entity, uuid);
        modifiers.put(AdditionalEntityAttributes.WATER_SPEED, new EntityAttributeModifier(uuid, "dandycorp:water_speed", 0.05, EntityAttributeModifier.Operation.ADDITION));
        modifiers.put(AdditionalEntityAttributes.WATER_VISIBILITY, new EntityAttributeModifier(uuid, "dandycorp:water_visibility", 1, EntityAttributeModifier.Operation.ADDITION));
        return modifiers;
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add(Text.literal("Grants conduit power").formatted(Formatting.GRAY));
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {

        if(entity.getEntityWorld().isClient) {
            tickCounter++;

            if (stack.isOf(Items.WATER_BADGE) && tickCounter >= 20) {
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.CONDUIT_POWER, 60, 0, false, false));
                tickCounter = 0;
            }
        }
    }

    @Override
    public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if(entity.getEntityWorld().isClient) {
            if (stack.isOf(Items.WATER_BADGE)) {
                entity.removeStatusEffect(StatusEffects.CONDUIT_POWER);
            }
        }
    }

    @Override
    public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if(entity.getEntityWorld().isClient) {
            if (stack.isOf(Items.WATER_BADGE)) {
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.CONDUIT_POWER, 60, 0, false, false));
            }
        }
    }

}
