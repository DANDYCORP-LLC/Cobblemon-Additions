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

public class PoisonBadgeItem extends BadgeItem {

    public PoisonBadgeItem(Settings settings) {
        super(settings);
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid){
        var modifiers = super.getModifiers(stack, slot, entity, uuid);
        modifiers.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(uuid, "dandycorp:poison_damage", 1.0, EntityAttributeModifier.Operation.ADDITION));
        return modifiers;
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add(Text.literal("Inflicts poison on-hit").formatted(Formatting.GRAY));
        tooltip.add(Text.literal("Grants poison immunity").formatted(Formatting.GRAY));
        tooltip.add(Text.literal("Grants wither immunity").formatted(Formatting.GRAY));
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {

        if (!entity.getEntityWorld().isClient) {
            tickCounter++;

            if (stack.isOf(Items.POISON_BADGE) && tickCounter >= 20) {
                entity.removeStatusEffect(StatusEffects.POISON);
                entity.removeStatusEffect(StatusEffects.WITHER);
                tickCounter = 0;
            }
        }
    }

    @Override
    public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if (!entity.getEntityWorld().isClient) {
            if (stack.isOf(Items.POISON_BADGE)) {
                entity.removeStatusEffect(StatusEffects.POISON);
                entity.removeStatusEffect(StatusEffects.WITHER);
            }
        }
    }
}
