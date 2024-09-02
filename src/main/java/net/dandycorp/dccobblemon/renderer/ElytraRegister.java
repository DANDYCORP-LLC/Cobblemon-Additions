package net.dandycorp.dccobblemon.renderer;

import net.dandycorp.dccobblemon.item.custom.badges.DragonBadgeItem;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback.RegistrationHelper;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;

/** Client-side methods for Elytra Trinket. */
public final class ElytraRegister {
    /** Disable rendering capes when wearing an Elytra in a cape trinket slot. */
    public static void registerCapeRenderer() {
        LivingEntityFeatureRenderEvents.ALLOW_CAPE_RENDER.register(
                (AbstractClientPlayerEntity player) -> !DragonBadgeItem.dragonBadgeEquipped(player));
    }

    /** Enable rendering Elytra when wearing an Elytra in a cape trinket slot. */
    public static void registerRenderer() {
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register(
                (EntityType<? extends LivingEntity> entityType, LivingEntityRenderer<?, ?> entityRenderer,
                 RegistrationHelper registrationHelper, Context context) -> {
                    registrationHelper
                            .register(new ElytraRenderer<>(entityRenderer, context.getModelLoader()));
                });
    }
}
