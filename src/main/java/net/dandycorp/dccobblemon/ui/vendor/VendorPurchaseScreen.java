package net.dandycorp.dccobblemon.ui.vendor;

import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.container.ScrollContainer;
import io.wispforest.owo.ui.core.*;
import net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions;
import net.dandycorp.dccobblemon.util.VendorBalanceManager;
import net.dandycorp.dccobblemon.util.VendorCategory;
import net.dandycorp.dccobblemon.util.VendorData;
import net.dandycorp.dccobblemon.util.VendorItem;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.List;

//TODO: make json take a list of item ids rather than just one

public class VendorPurchaseScreen extends VendorScreen {

    private final PlayerInventory playerInventory;
    private final VendorBalanceManager balanceManager;
    private List<VendorItem> categoryItems;

    public VendorPurchaseScreen(VendorScreenHandler handler, PlayerInventory inventory, Text title, VendorBalanceManager balanceManager, String category) {
        super(handler, inventory, title);
        this.playerInventory = inventory;
        this.balanceManager = balanceManager;
        VendorData vendorData = handler.getVendorData();
        for (VendorCategory vc : vendorData.getCategories()) {
            if (category.equalsIgnoreCase(vc.getName())) {
                categoryItems = vc.getItems();
                break;
            }
        }
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        rootComponent.clearChildren();
        rootComponent.surface(Surface.VANILLA_TRANSLUCENT)
                .alignment(HorizontalAlignment.CENTER,VerticalAlignment.CENTER);

        rootComponent.child(balanceManager.getBalanceDisplay(250));

        FlowLayout buttonContainer = Containers.verticalFlow(Sizing.content(), Sizing.content());
        buttonContainer.padding(Insets.of(5,5,5,5));

        for (VendorItem vendorItem : categoryItems) {

            ItemStack stack = handler.getItemStackFromID(vendorItem.getItemID(), vendorItem.getQuantity());
            if(stack != null && client != null) {

                String plural = " ticket";
                if (vendorItem.getCost()>1) {
                    plural = " tickets";
                }

                FlowLayout buttonDisplay = Containers.horizontalFlow(Sizing.fill(100), Sizing.content())
                        .child(Components.item(stack).sizing(Sizing.fixed(25),Sizing.fixed(25))) // item
                        .child(Components.label(Text.of("x" + vendorItem.getQuantity())).sizing(Sizing.fixed(25),Sizing.content())) // quantity
                        .child(Components.label(Text.of(vendorItem.getDescription())).color(Color.ofArgb(0xFF00BB00)).shadow(true).sizing(Sizing.fixed(185),Sizing.content()).margins(Insets.of(0,0,4,4))) // desc
                        .child(Components.button(Text.literal(vendorItem.getCost()+plural), buttonComponent -> { // button
                                    client.interactionManager.clickButton(this.getScreenHandler().syncId, vendorItem.getButtonID());
                                    balanceManager.updateBalance();
                                })
                                .sizing(Sizing.fixed(50), Sizing.content()));



                buttonDisplay.margins(Insets.of(5,5,0,0))
                        .surface(Surface.flat(0xFF000000).and(Surface.outline(0xFF00FF00)))
                        .padding(Insets.of(4,4,4,4));
                buttonContainer.child(buttonDisplay);
            }
        }

        // Wrap the button container in a scroll container
        ScrollContainer<Component> scrollContainer = Containers.verticalScroll(Sizing.fixed(310), Sizing.fill(70), buttonContainer);


        rootComponent.child(scrollContainer);
        uiAdapter.toggleInspector();
    }

    @Override
    public void close() {
        playerInventory.player.playSound(DANDYCORPCobblemonAdditions.VENDOR_CLICK_EVENT, 1.0f, (float) (0.8+(0.4*Math.random())));
        this.client.setScreen(new VendorScreen(this.handler, this.playerInventory, this.title));
    }

    @Override
    protected void handledScreenTick() {
        if (playerInventory.player.age % 20 == 0) {
            balanceManager.updateBalance();
        }
    }
}
