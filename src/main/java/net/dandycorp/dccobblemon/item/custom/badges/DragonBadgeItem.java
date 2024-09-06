package net.dandycorp.dccobblemon.item.custom.badges;

import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketsApi;
import net.dandycorp.dccobblemon.item.Items;
import net.dandycorp.dccobblemon.item.custom.BadgeItem;
import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents;
import net.fabricmc.fabric.api.entity.event.v1.FabricElytraItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

public class DragonBadgeItem extends BadgeItem implements FabricElytraItem {


    public DragonBadgeItem(Settings settings) {
        super(settings);
    }

    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid){
        var modifiers = super.getModifiers(stack, slot, entity, uuid);
        modifiers.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(uuid, "dandycorp:dragon_speed", 0.8, EntityAttributeModifier.Operation.ADDITION));
        modifiers.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(uuid, "dandycorp:dragon_speed", 0.8, EntityAttributeModifier.Operation.ADDITION));
        return modifiers;
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add(Text.literal("Functions as an elytra").formatted(Formatting.GRAY));
        tooltip.add(Text.literal("Applies soul fire flames on-hit").formatted(Formatting.GRAY));
    }

    public static void registerFlight(){
        EntityElytraEvents.CUSTOM.register((LivingEntity e, boolean doTicks) -> {
            if (e instanceof PlayerEntity p){
                return dragonBadgeEquipped(p);
            } else return false;
        });
    }


    public static boolean dragonBadgeEquipped(PlayerEntity player) {
        return TrinketsApi.getTrinketComponent(player)
                .map(trinketComponent ->
                        trinketComponent.getAllEquipped().stream()
                                .map(Pair::getRight) // Get the ItemStack from the pair
                                .map(ItemStack::getItem) // Get the Item from the ItemStack
                                .anyMatch(item -> item == Items.DRAGON_BADGE)
                )
                .orElse(false);
    }

}
