package net.dandycorp.dccobblemon.item.custom.badges;

import com.cobblemon.mod.common.api.types.ElementalType;
import com.cobblemon.mod.common.api.types.ElementalTypes;
import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotReference;
import net.dandycorp.dccobblemon.item.DANDYCORPItems;
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

import java.lang.annotation.ElementType;
import java.util.List;
import java.util.UUID;

public class BugBadgeItem extends BadgeItem {

    public static final ElementalType ELEMENT_TYPE = ElementalTypes.INSTANCE.getBUG();

    public BugBadgeItem(Settings settings) {
        super(settings);
    }

    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        super.appendTooltip(itemStack, world, tooltip, tooltipContext);
        tooltip.add(Text.literal("Size reduced by 33%").formatted(Formatting.GRAY));
        tooltip.add(Text.literal("Thorns effect when hit").formatted(Formatting.GRAY));
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid){
        var modifiers = super.getModifiers(stack, slot, entity, uuid);
        modifiers.put(EntityAttributes.GENERIC_MOVEMENT_SPEED, new EntityAttributeModifier(uuid, "dandycorp:bug_speed", 0.8, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
        return modifiers;
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {

        if(!entity.getEntityWorld().isClient) {
            if(entity.age % 20 == 0) {
                if (this.isEquipped(entity, DANDYCORPItems.BUG_BADGE)) {
                    ScaleTypes.HEIGHT.getScaleData(entity).setScale(0.66f);
                    ScaleTypes.WIDTH.getScaleData(entity).setScale(0.66f);
                }
                else {
                    ScaleTypes.HEIGHT.getScaleData(entity).setScale(1f);
                    ScaleTypes.WIDTH.getScaleData(entity).setScale(1f);
                }
            }
        }
    }

    @Override
    public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
            ScaleTypes.HEIGHT.getScaleData(entity).setScale(1f);
            ScaleTypes.WIDTH.getScaleData(entity).setScale(1f);
    }

    @Override
    public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if(!entity.getEntityWorld().isClient) {
            ScaleTypes.HEIGHT.getScaleData(entity).setScale(0.66f);
            ScaleTypes.WIDTH.getScaleData(entity).setScale(0.66f);
        }
    }

}
