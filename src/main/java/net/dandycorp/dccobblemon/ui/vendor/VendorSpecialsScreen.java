package net.dandycorp.dccobblemon.ui.vendor;

//TODO: special screen with buttons for compliment, retire, and shareholder

import io.wispforest.owo.ui.container.FlowLayout;
import net.dandycorp.dccobblemon.util.VendorBalanceManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class VendorSpecialsScreen extends VendorPurchaseScreen {
    public VendorSpecialsScreen(VendorScreenHandler handler, PlayerInventory inventory, Text title, VendorBalanceManager balanceManager) {
        super(handler, inventory, title, balanceManager, "specials");
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        super.build(rootComponent);
    }
}
