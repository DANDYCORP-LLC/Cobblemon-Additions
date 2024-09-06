package net.dandycorp.dccobblemon.ui;

import net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ScreenHandlers {
    public static final ScreenHandlerType<VendorScreenHandler> VENDOR_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(
            new Identifier(DANDYCORPCobblemonAdditions.MOD_ID, "vendor_screen"), VendorScreenHandler::new);

    public static void initialize() {
        // Initialization code here
    }
}
