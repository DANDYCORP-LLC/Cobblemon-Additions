package net.dandycorp.dccobblemon.compat.rei;

import me.shedaniel.rei.api.client.registry.screen.OverlayDecider;
import net.dandycorp.dccobblemon.ui.vendor.VendorScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ActionResult;

public class VendorScreenDecider implements OverlayDecider {

    @Override
    public double getPriority() {
        return 20.0;
    }

    @Override
    public <R extends Screen> boolean isHandingScreen(Class<R> screen) {
        return VendorScreen.class.isAssignableFrom(screen);
    }

    @Override
    public <R extends Screen> ActionResult shouldScreenBeOverlaid(R screen) {
        return ActionResult.FAIL;
    }
}
