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
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

public class PoisonBadgeItem extends BadgeItem {

    public PoisonBadgeItem(Settings settings, List<ElementalType> elementalTypes) {
        super(settings,elementalTypes);
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid){
        var modifiers = super.getModifiers(stack, slot, entity, uuid);
        modifiers.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(uuid, "dandycorp:poison_damage", 1.0, EntityAttributeModifier.Operation.ADDITION));
        return modifiers;
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        super.appendTooltip(itemStack, world, tooltip, tooltipContext);
        tooltip.add(Text.literal(""));
        tooltip.add(Text.literal("Inflicts poison on-hit").formatted(Formatting.GRAY));
        tooltip.add(Text.literal("Grants poison immunity").formatted(Formatting.GRAY));
        tooltip.add(Text.literal("Grants wither immunity").formatted(Formatting.GRAY));
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if (!entity.getEntityWorld().isClient) {
            if(entity.age % 20 == 0) {
                if (this.isEquipped(entity, DANDYCORPItems.POISON_BADGE)) {
                    entity.removeStatusEffect(StatusEffects.POISON);
                    entity.removeStatusEffect(StatusEffects.WITHER);
                }
            }
        }
    }

    @Override
    public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if (!entity.getEntityWorld().isClient) {
                entity.removeStatusEffect(StatusEffects.POISON);
                entity.removeStatusEffect(StatusEffects.WITHER);
        }
    }
}
