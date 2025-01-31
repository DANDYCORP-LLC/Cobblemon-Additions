package net.dandycorp.dccobblemon.util.vendor;

import io.wispforest.owo.ui.base.BaseComponent;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Color;
import io.wispforest.owo.ui.core.Insets;
import io.wispforest.owo.ui.core.Sizing;
import io.wispforest.owo.ui.core.VerticalAlignment;
import net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions;
import net.dandycorp.dccobblemon.block.DANDYCORPBlocks;
import net.dandycorp.dccobblemon.item.DANDYCORPItems;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class VendorBalanceManager {

    private final PlayerInventory playerInventory;
    private final LabelComponent balanceLabel;
    private int balance;

    public VendorBalanceManager(PlayerInventory playerInventory) {
        this.playerInventory = playerInventory;
        this.balance = getBalance();
        balanceLabel = Components.label(Text.literal("x" + balance))
                .color(Color.GREEN)
                .verticalTextAlignment(VerticalAlignment.CENTER)
                .shadow(true);
    }

    public int getBalance() {
        int ticketCount = 0;
        for (ItemStack stack : playerInventory.main) {
            if (stack.getItem() == DANDYCORPItems.TICKET) {
                ticketCount += stack.getCount();
            } else if (stack.getItem() == DANDYCORPBlocks.TICKET_BAG.asItem()) {
                ticketCount += (16 * stack.getCount());
            }
        }

        // count up offhand
        for (ItemStack stack : playerInventory.offHand) {
            if (stack.getItem() == DANDYCORPItems.TICKET) {
                ticketCount += stack.getCount();
            } else if (stack.getItem() == DANDYCORPBlocks.TICKET_BAG.asItem()) {
                ticketCount += (16 * stack.getCount());
            }
        }
        return ticketCount;
    }

    public void updateBalance() {
        this.balance = getBalance();
        this.balanceLabel.text(Text.literal("x" + balance));
    }

    public LabelComponent getBalanceLabel() {
        return balanceLabel;
    }

    public BaseComponent getBalanceDisplay(int indent) {
        FlowLayout balanceDisplay = Containers.horizontalFlow(Sizing.content(), Sizing.content())
                .child(Components.texture(new Identifier(DANDYCORPCobblemonAdditions.MOD_ID, "textures/gui/vendor/ticket.png"), 0, 0, 20, 20, 20, 20).margins(Insets.right(2)))
                .child(balanceLabel);
        balanceDisplay.margins(Insets.bottom(6)).padding(Insets.left(indent)).verticalAlignment(VerticalAlignment.CENTER);
        return balanceDisplay;
    }
}
