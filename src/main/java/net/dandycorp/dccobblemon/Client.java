package net.dandycorp.dccobblemon;

import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import net.dandycorp.dccobblemon.item.Items;
import net.dandycorp.dccobblemon.renderer.BadgeRenderer;
import net.dandycorp.dccobblemon.renderer.ElytraRegister;
import net.dandycorp.dccobblemon.renderer.ElytraRenderer;
import net.dandycorp.dccobblemon.ui.ScreenHandlers;
import net.dandycorp.dccobblemon.ui.VendorScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

public class Client implements ClientModInitializer {

    //private static final KeyBinding BEGIN = new KeyBinding("ui.dccobblemon.vendor", GLFW.GLFW_KEY_G, "key.categories.misc");

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
