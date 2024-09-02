package net.dandycorp.dccobblemon;

import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import net.dandycorp.dccobblemon.item.Items;
import net.dandycorp.dccobblemon.renderer.BadgeRenderer;
import net.dandycorp.dccobblemon.renderer.ElytraRegister;
import net.dandycorp.dccobblemon.renderer.ElytraRenderer;
import net.fabricmc.api.ClientModInitializer;

public class Client implements ClientModInitializer {


    @Override
    public void onInitializeClient() {

        TrinketRendererRegistry.registerRenderer(Items.BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(Items.BUG_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(Items.DANDY_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(Items.DARK_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(Items.DRAGON_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(Items.FIRE_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(Items.FLYING_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(Items.GROUND_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(Items.ICE_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(Items.JEFF_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(Items.LINA_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(Items.NATE_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(Items.POISON_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(Items.PUMPKIN_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(Items.ROCK_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(Items.SHELLY_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(Items.WATER_BADGE, new BadgeRenderer());
        ElytraRegister.registerCapeRenderer();
        ElytraRegister.registerRenderer();

    }
}
