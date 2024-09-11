package net.dandycorp.dccobblemon.ui;

import dev.architectury.event.EventResult;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.client.registry.display.visibility.DisplayVisibilityPredicate;
import me.shedaniel.rei.api.client.registry.screen.OverlayDecider;
import me.shedaniel.rei.api.common.display.Display;
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
