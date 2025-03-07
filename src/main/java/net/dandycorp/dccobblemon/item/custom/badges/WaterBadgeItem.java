package net.dandycorp.dccobblemon.item.custom.badges;

import com.cobblemon.mod.common.api.types.ElementalType;
import dev.emi.trinkets.api.SlotReference;
import net.dandycorp.dccobblemon.item.DANDYCORPItems;
import net.dandycorp.dccobblemon.item.custom.BadgeItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class WaterBadgeItem extends BadgeItem {

    public WaterBadgeItem(Settings settings, List<ElementalType> elementalTypes) {
        super(settings,elementalTypes);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        super.appendTooltip(itemStack, world, tooltip, tooltipContext);
        tooltip.add(Text.literal(""));
        tooltip.add(Text.literal("Grants conduit power").formatted(Formatting.GRAY));
        tooltip.add(Text.literal("+ Depth strider III").formatted(Formatting.GRAY));
        tooltip.add(Text.literal("+ Aqua Affinity").formatted(Formatting.GRAY));
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if (!entity.getEntityWorld().isClient) {
            if (entity.age % 20 == 0) {
                if(this.isEquipped(entity, DANDYCORPItems.WATER_BADGE)){
                    entity.addStatusEffect(new StatusEffectInstance(StatusEffects.CONDUIT_POWER, 600, 0, false, false));
                }
            }
        }
    }

    @Override
    public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        entity.removeStatusEffect(StatusEffects.CONDUIT_POWER);
    }

    @Override
    public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if (!entity.getEntityWorld().isClient) {
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.CONDUIT_POWER, 60, 0, false, false));
        }
    }
}
