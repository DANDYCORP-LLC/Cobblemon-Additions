package net.dandycorp.dccobblemon.ui;

import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.container.ScrollContainer;
import io.wispforest.owo.ui.core.*;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

import java.util.List;

public class VendorEngineeringScreen extends VendorScreen {

    private final PlayerInventory playerInventory;
    private final VendorBalanceManager balanceManager;

    public VendorEngineeringScreen(VendorScreenHandler handler, PlayerInventory inventory, Text title, VendorBalanceManager balanceManager) {
        super(handler, inventory, title);
        this.playerInventory = inventory;
        this.balanceManager = balanceManager;
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        rootComponent.clearChildren();
        rootComponent.surface(Surface.VANILLA_TRANSLUCENT)
                .alignment(HorizontalAlignment.CENTER,VerticalAlignment.CENTER);

        rootComponent.child(balanceManager.getBalanceDisplay());

        List<Component> buttons = new java.util.ArrayList<>(List.of());
        for (int i = 0; i < 20; i++){
            buttons.add(Components.button(Text.literal("buy now 20 tickets"),buttonComponent -> {
                        client.interactionManager.clickButton(this.getScreenHandler().syncId, 101);
                        balanceManager.updateBalance();
                    }).sizing(Sizing.fill(100),Sizing.content()));
        }

        ScrollContainer<Component> scrollContainer = Containers.verticalScroll(Sizing.fixed(200),Sizing.fill(70),buttons.get(0));
        for (int i = 1; i < 20; i++) {
            scrollContainer.child(buttons.get(i));
        }

        rootComponent.child(scrollContainer);
        uiAdapter.toggleInspector();
    }

    @Override
    public void close() {
        this.client.setScreen(new VendorScreen(this.handler, this.playerInventory, this.title));
    }

    @Override
    protected void handledScreenTick() {
        if (playerInventory.player.age % 20 == 0) {
            balanceManager.updateBalance();
        }
    }
}
