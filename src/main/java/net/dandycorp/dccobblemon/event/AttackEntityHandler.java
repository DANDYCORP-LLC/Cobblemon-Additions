package net.dandycorp.dccobblemon.event;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketsApi;
import net.dandycorp.dccobblemon.item.DANDYCORPItems;
import net.dandycorp.dccobblemon.item.custom.BadgeItem;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;


import java.util.List;

public class AttackEntityHandler implements AttackEntityCallback {


    @Override
    public ActionResult interact(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hitResult) {


        if (!world.isClient() && entity instanceof LivingEntity targetEntity) {

            TrinketsApi.getTrinketComponent(player).ifPresent(trinketComponent -> {
                // Get all non-empty equipped trinkets
                List<Pair<SlotReference, ItemStack>> equippedTrinkets = trinketComponent.getAllEquipped();

                // Process each equipped trinket to check for badges
                equippedTrinkets.stream()
                        .map(Pair::getRight) // Get the ItemStack from the pair
                        .map(ItemStack::getItem) // Get the Item from the ItemStack
                        .filter(item -> item instanceof BadgeItem) // Filter for BadgeItems
                        .forEach(item -> {
                            // Apply effects based on the type of badge
                            if (item == DANDYCORPItems.POISON_BADGE) {
                                targetEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 100, 2));
                            } else if (item == DANDYCORPItems.ICE_BADGE) {
                                targetEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 0));
                                targetEntity.setFrozenTicks(220);
                            } else if (item == DANDYCORPItems.FIRE_BADGE) {
                                targetEntity.setOnFireFor((targetEntity.getFireTicks()/20)+4);
                            } else if (item == DANDYCORPItems.DARK_BADGE) {
                                targetEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 100, 2));
                                targetEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 100, 2));
                            }
                        });
            });

        }


        return ActionResult.PASS;
    }
}
