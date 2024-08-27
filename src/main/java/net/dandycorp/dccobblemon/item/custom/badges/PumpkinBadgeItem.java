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
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

public class PumpkinBadgeItem extends BadgeItem {

    public PumpkinBadgeItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add(Text.literal("Sneaky sneaky!").formatted(Formatting.GOLD));
    }

    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid){
        var modifiers = super.getModifiers(stack, slot, entity, uuid);
        // +10% movement speed
        modifiers.put(EntityAttributes.GENERIC_MOVEMENT_SPEED, new EntityAttributeModifier(uuid, "dandycorp:pumpkin_speed", 0.2, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
        modifiers.put(AdditionalEntityAttributes.MOB_DETECTION_RANGE, new EntityAttributeModifier(uuid, "generic.mob_detection_range", -5, EntityAttributeModifier.Operation.ADDITION));
        return modifiers;
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {

        if(entity.getEntityWorld().isClient) {
            tickCounter++;

            if (stack.isOf(Items.PUMPKIN_BADGE) && tickCounter >= 20) {
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 600, 0, false, false));
                tickCounter = 0;
            }
        }
    }

    @Override
    public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if(entity.getEntityWorld().isClient) {
            if (stack.isOf(Items.PUMPKIN_BADGE)) {
                entity.removeStatusEffect(StatusEffects.NIGHT_VISION);
            }
        }
    }

    @Override
    public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if(entity.getEntityWorld().isClient) {
            if (stack.isOf(Items.PUMPKIN_BADGE)) {
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 600, 0, false, false));
            }
        }
    }

}
