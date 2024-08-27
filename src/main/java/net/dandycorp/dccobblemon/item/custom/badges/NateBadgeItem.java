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
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import virtuoel.pehkui.api.ScaleTypes;

import java.util.List;
import java.util.UUID;

public class NateBadgeItem extends BadgeItem {

    public NateBadgeItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add(Text.literal("Size does matter...").formatted(Formatting.GREEN));
    }

    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid){
        var modifiers = super.getModifiers(stack, slot, entity, uuid);
        modifiers.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(uuid, "dandycorp:colossal_damage", 0.15, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
        modifiers.put(EntityAttributes.GENERIC_MAX_HEALTH, new EntityAttributeModifier(uuid, "dandycorp:colossal_health", 0.2, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
        return modifiers;
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {

        if(!entity.getEntityWorld().isClient) {
            tickCounter++;

            if (!stack.isOf(Items.NATE_BADGE) && tickCounter >= 20) {
                ScaleTypes.HEIGHT.getScaleData(entity).setScale(1);
                ScaleTypes.WIDTH.getScaleData(entity).setScale(1);
                ScaleTypes.STEP_HEIGHT.getScaleData(entity).setScale(1);
            }
        }
    }

    @Override
    public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if(!entity.getEntityWorld().isClient) {
            ScaleTypes.HEIGHT.getScaleData(entity).setScale(1f);
            ScaleTypes.WIDTH.getScaleData(entity).setScale(1f);
            ScaleTypes.STEP_HEIGHT.getScaleData(entity).setScale(1);
            if(entity.getHealth()/entity.getMaxHealth()>=0.8f){
                entity.setHealth(entity.getHealth()*0.8f);
            }
        }
    }

    @Override
    public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if(!entity.getEntityWorld().isClient) {
            if (stack.isOf(Items.NATE_BADGE)) {
                ScaleTypes.HEIGHT.getScaleData(entity).setScale(1.33f);
                ScaleTypes.WIDTH.getScaleData(entity).setScale(1.33f);
                ScaleTypes.STEP_HEIGHT.getScaleData(entity).setScale(2);
                //ScaleTypes.STEP_HEIGHT.getScaleData(entity).setScale(1.5f);
            }
        }
    }
}
