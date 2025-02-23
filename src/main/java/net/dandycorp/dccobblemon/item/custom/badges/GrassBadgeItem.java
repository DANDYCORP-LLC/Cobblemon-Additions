package net.dandycorp.dccobblemon.item.custom.badges;

import com.cobblemon.mod.common.api.types.ElementalType;
import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotReference;
import net.dandycorp.dccobblemon.item.DANDYCORPItems;
import net.dandycorp.dccobblemon.item.custom.BadgeItem;
import net.dandycorp.dccobblemon.util.TextUtils;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

public class GrassBadgeItem extends BadgeItem {
    public GrassBadgeItem(Settings settings, List<ElementalType> elementalTypes) {
        super(settings,elementalTypes);
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
        super.tick(stack, slot, entity);
        World world = entity.getWorld();
        if(world.isSkyVisible(entity.getBlockPos()) && world.isDay()){
            if(entity.age % 120 == 0 && entity instanceof PlayerEntity player) {
                player.heal(1);
                player.getHungerManager().add(1,0);
            }
        }
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        super.appendTooltip(itemStack, world, tooltip, tooltipContext);
        tooltip.add(Text.literal(""));
        tooltip.add(Text.literal("Restores hunger and health while in sunlight").formatted(Formatting.GRAY));
    }
}
