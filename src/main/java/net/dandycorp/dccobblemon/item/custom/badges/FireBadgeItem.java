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

public class FireBadgeItem extends BadgeItem {

    public FireBadgeItem(Settings settings) {
        super(settings);
    }

    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid){
        var modifiers = super.getModifiers(stack, slot, entity, uuid);
        modifiers.put(AdditionalEntityAttributes.LAVA_SPEED, new EntityAttributeModifier(uuid, "dandycorp:lava_speed", 2, EntityAttributeModifier.Operation.ADDITION));
        modifiers.put(AdditionalEntityAttributes.LAVA_VISIBILITY, new EntityAttributeModifier(uuid, "dandycorp:lava_visibility", 1, EntityAttributeModifier.Operation.ADDITION));
        return modifiers;
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add(Text.literal("Inflicts fire on-hit").formatted(Formatting.GRAY));
        tooltip.add(Text.literal("Grants fire resistance").formatted(Formatting.GRAY));
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {

        if(!entity.getEntityWorld().isClient) {
            tickCounter++;

            if (stack.isOf(Items.FIRE_BADGE) && tickCounter >= 20) {
                entity.setFireTicks(0);
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 60, 0, false, false));
                tickCounter = 0;
            }
        }
    }

    @Override
    public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if(!entity.getEntityWorld().isClient) {
            if (stack.isOf(Items.FIRE_BADGE)) {
                entity.removeStatusEffect(StatusEffects.FIRE_RESISTANCE);
            }
        }
    }

    @Override
    public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if(!entity.getEntityWorld().isClient) {
            if (stack.isOf(Items.FIRE_BADGE) && tickCounter >= 20) {
                entity.setFireTicks(0);
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 60, 0, false, false));
            }
        }
    }

}
