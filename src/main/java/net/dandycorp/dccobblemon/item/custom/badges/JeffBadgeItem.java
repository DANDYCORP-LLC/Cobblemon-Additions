package net.dandycorp.dccobblemon.item.custom.badges;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketsApi;
import net.dandycorp.dccobblemon.item.DANDYCORPItems;
import net.dandycorp.dccobblemon.item.custom.BadgeItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

import java.util.List;

public class JeffBadgeItem extends BadgeItem {

    public JeffBadgeItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        super.appendTooltip(itemStack, world, tooltip, tooltipContext);
        tooltip.add(Text.literal("Strength in numbers").formatted(Formatting.YELLOW));
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if (entity.getEntityWorld() instanceof ServerWorld world && entity instanceof PlayerEntity player) {
            if (entity.age % 20 == 0) {
                if(this.isEquipped(entity, DANDYCORPItems.JEFF_BADGE)) {
                    applyEffects(player, world);
                }
            }
        }
    }

    @Override
    public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if (entity.getEntityWorld() instanceof ServerWorld world && entity instanceof PlayerEntity player) {
            applyEffects(player, world);
        }
    }

    @Override
    public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        entity.removeStatusEffect(StatusEffects.STRENGTH);
        entity.removeStatusEffect(StatusEffects.REGENERATION);
        entity.removeStatusEffect(StatusEffects.RESISTANCE);
    }

    private int countNearbyPlayers(PlayerEntity player, ServerWorld world) {
        int count = 1; // the wearing player is always 1
        List<ServerPlayerEntity> nearby = world.getPlayers(p -> hasJeffBadge(p) && p.squaredDistanceTo(player) <= 400 && p != player);
        return count + nearby.size();
    }

    private void applyEffects(PlayerEntity player, ServerWorld world) {
        int count = countNearbyPlayers(player, world);

        // Clear all the effects before applying new ones
        player.clearStatusEffects();

        // Apply effects based on the number of nearby badge wearers
        List<StatusEffectInstance> effects = getEffectsForCount(count);
        for (StatusEffectInstance effect : effects) {
            player.addStatusEffect(effect);
        }
    }

    private List<StatusEffectInstance> getEffectsForCount(int count) {
        if (count == 1) {
            return List.of(new StatusEffectInstance(StatusEffects.STRENGTH, 600, 0, false, false));
        } else if (count == 2) {
            return List.of(
                    new StatusEffectInstance(StatusEffects.STRENGTH, 600, 0, false, false),
                    new StatusEffectInstance(StatusEffects.REGENERATION, 600, 1, false, false)
            );
        } else if (count == 3) {
            return List.of(
                    new StatusEffectInstance(StatusEffects.STRENGTH, 600, 0, false, false),
                    new StatusEffectInstance(StatusEffects.REGENERATION, 600, 1, false, false),
                    new StatusEffectInstance(StatusEffects.RESISTANCE, 600, 0, false, false)
            );
        } else if (count == 4) {
            return List.of(
                    new StatusEffectInstance(StatusEffects.STRENGTH, 600, 1, false, false),
                    new StatusEffectInstance(StatusEffects.REGENERATION, 600, 1, false, false),
                    new StatusEffectInstance(StatusEffects.RESISTANCE, 600, 0, false, false)
            );
        } else {
            return List.of(
                    new StatusEffectInstance(StatusEffects.STRENGTH, 600, 1, false, false),
                    new StatusEffectInstance(StatusEffects.REGENERATION, 600, 1, false, false),
                    new StatusEffectInstance(StatusEffects.SPEED, 600, 0, false, false),
                    new StatusEffectInstance(StatusEffects.RESISTANCE, 600, 0, false, false)
            );
        }
    }

    private boolean hasJeffBadge(PlayerEntity player) {
        return TrinketsApi.getTrinketComponent(player)
                .map(trinketComponent ->
                        trinketComponent.getAllEquipped().stream()
                                .map(Pair::getRight) // Get the ItemStack from the pair
                                .map(ItemStack::getItem) // Get the Item from the ItemStack
                                .anyMatch(item -> item == DANDYCORPItems.JEFF_BADGE)
                )
                .orElse(false);
    }
}
