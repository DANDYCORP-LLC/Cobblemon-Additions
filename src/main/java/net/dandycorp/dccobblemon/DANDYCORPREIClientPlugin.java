package net.dandycorp.dccobblemon;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.screen.ExclusionZones;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import net.dandycorp.dccobblemon.ui.VendorScreen;
import net.dandycorp.dccobblemon.ui.VendorScreenDecider;

import java.util.List;

public class DANDYCORPREIClientPlugin implements REIClientPlugin {

    @Override
    public void registerExclusionZones(ExclusionZones zones) {
        zones.register(VendorScreen.class, screen -> {
            return List.of(new Rectangle(0,0,1000,1000));
        });
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        ScreenRegistry.getInstance().registerDecider(new VendorScreenDecider());
        REIClientPlugin.super.registerScreens(registry);
    }
}
