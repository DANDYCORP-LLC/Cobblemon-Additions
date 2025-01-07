package net.dandycorp.dccobblemon.ui.vendor;

import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.*;
import net.dandycorp.dccobblemon.util.vendor.VendorBalanceManager;
import net.dandycorp.dccobblemon.util.vendor.VendorEntry;
import net.dandycorp.dccobblemon.util.vendor.VendorItem;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

import java.util.List;

public class VendorSpecialsScreen extends VendorPurchaseScreen {

    public VendorSpecialsScreen(VendorScreenHandler handler, PlayerInventory inventory, Text title, VendorBalanceManager balanceManager) {
        super(handler, inventory, title, balanceManager, "specials");
        scrollHeight = 60;
        addDonorEntry();
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        super.build(rootComponent);
        FlowLayout specialButtons = Containers.horizontalFlow(Sizing.fixed(310), Sizing.content());
        specialButtons.padding(Insets.of(5)).alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER).margins(Insets.top(5));

        // Left Button
        ParentComponent leftButton = Containers.stack(Sizing.content(1),Sizing.content(1)).child(
                Components.button(Text.empty(), button -> {
                            client.interactionManager.clickButton(handler.syncId,-120);
                })
                .renderer(ButtonComponent.Renderer.flat(0xFF000000, 0xFF002200, 0xFF008800))
                    .sizing(Sizing.fixed(140), Sizing.fixed(40))
                    .margins(Insets.of(0, 0, 0, 0)))
                .child(Components.label(Text.of("§lCOMPLIMENT"))
                        .color(Color.GREEN)
                        .verticalTextAlignment(VerticalAlignment.CENTER)
                        .horizontalTextAlignment(HorizontalAlignment.CENTER)
                        .shadow(true))
                .surface(Surface.outline(0xFF00FF00))
                .alignment(HorizontalAlignment
                        .CENTER, VerticalAlignment.CENTER);

        // Spacer between buttons
        FlowLayout spacer = Containers.horizontalFlow(Sizing.fixed(16), Sizing.fixed(0));

        // Right Button
        ParentComponent rightButton = Containers.stack(Sizing.content(1),Sizing.content(1)).child(
                Components.button(Text.empty(), button -> {
                    client.interactionManager.clickButton(handler.syncId,-121);
                })
                    .renderer(ButtonComponent.Renderer.flat(0xFF000000, 0xFF002200, 0xFF008800))
                    .sizing(Sizing.fixed(140), Sizing.fixed(40))
                    .margins(Insets.of(0, 0, 0, 0)))
                .child(Components.label(Text.of("§lRETIREMENT"))
                        .color(Color.GREEN)
                        .verticalTextAlignment(VerticalAlignment.CENTER)
                        .horizontalTextAlignment(HorizontalAlignment.CENTER)
                        .shadow(true))
                .surface(Surface.outline(0xFF00FF00))
                .alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER);

        specialButtons.child(leftButton).child(spacer).child(rightButton);

        rootComponent.child(specialButtons);

        //this.uiAdapter.toggleInspector();
    }

    private void addDonorEntry() {

        VendorEntry donorHeadEntry = new VendorEntry();
        donorHeadEntry.setCost(1);
        donorHeadEntry.setTitle("Board Member Bust");
        donorHeadEntry.setDescription("Who needs action figures or trading cards when you can collect figurines of corporate shareholders? Display DANDYCORP's finest with pride, and try not to question why you need so many or what they’ve done to deserve this honor.");
        VendorItem item = new VendorItem();
        item.setId("minecraft:player_head#aef167f4-1786-4ff5-ab7f-6baf16dbb56b");
        item.setQuantity(1);

        donorHeadEntry.setItems(List.of(item));
        donorHeadEntry.setButtonID(-122);

        boolean donorEntryExists = categoryEntries.stream()
                .anyMatch(entry -> entry.getButtonID() == donorHeadEntry.getButtonID());

        if (!donorEntryExists) {
            categoryEntries.add(donorHeadEntry);
        }
    }
}
