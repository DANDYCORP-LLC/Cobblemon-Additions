package net.dandycorp.dccobblemon.ui.vendor;

import io.wispforest.owo.ui.base.BaseOwoHandledScreen;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.container.GridLayout;
import io.wispforest.owo.ui.container.StackLayout;
import io.wispforest.owo.ui.core.*;
import net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions;
import net.dandycorp.dccobblemon.util.VendorBalanceManager;
import net.dandycorp.dccobblemon.util.VendorCategory;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class VendorScreen extends BaseOwoHandledScreen<FlowLayout,VendorScreenHandler> {

    private final PlayerInventory playerInventory;
    public VendorBalanceManager balanceManager;


    // new constructor
    public VendorScreen(VendorScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.playerInventory = inventory;
        this.balanceManager = new VendorBalanceManager(inventory);
    }

    // returning constructor
    public VendorScreen(VendorScreenHandler handler, PlayerInventory inventory, Text title, VendorBalanceManager balanceManager) {
        super(handler, inventory, title);
        this.playerInventory = inventory;
        this.balanceManager = balanceManager;
    }

    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this,Containers::verticalFlow);
    }


    @Override
    protected void init() {
        super.init();
        titleY=1000;
        playerInventoryTitleY=1000;
        for (Slot slot : this.handler.slots) {
            disableSlot(slot);
        }
    }

    @Override
    protected void build(FlowLayout rootComponent) {

        rootComponent.child(balanceManager.getBalanceDisplay(140));


        // ---------

        List<VendorCategory> categories = handler.getVendorData().getCategories();
        GridLayout grid = Containers.grid(Sizing.content(), Sizing.content(), categories.size()/2, 2);
        int column = 0;
        int row = 0;

        for(VendorCategory category : categories) {
            if(!Objects.equals(category.getName(), "specials")) {
                StackLayout buttonContainer = (StackLayout) Containers.stack(Sizing.fixed(100), Sizing.fixed(62)).surface(Surface.outline(0xFF00FF00));

                buttonContainer.child(Components.button(Text.empty(), button -> {
                            playerInventory.player.playSound(DANDYCORPCobblemonAdditions.VENDOR_CLICK_EVENT, 1.0f, (float) (0.8 + (0.4 * Math.random())));
                            client.setScreen(new VendorPurchaseScreen(handler, playerInventory, title, balanceManager, category.getName()));
                        })
                        .renderer(ButtonComponent.Renderer.flat(0xFF000000, 0xFF002200, 0xFF000000))
                        .margins(Insets.of(1, 1, 1, 1))
                        .sizing(Sizing.fixed(98), Sizing.fixed(60)));


                buttonContainer.child(Components.label(Text.of("§l"+category.getName().toUpperCase()))
                        .color(Color.GREEN)
                        .shadow(true))
                .alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER);

                buttonContainer.margins(Insets.of(3, 3, 3, 3));
                grid.child(buttonContainer, row, column);

                column++;
                if (column > 1) {
                    column = 0;
                    row++;
                }
            }
        }

        grid.alignment(HorizontalAlignment.CENTER,VerticalAlignment.CENTER);
        rootComponent.child(grid);

        // ---------


        rootComponent.child(
                Components.button(
                                Text.empty(),
                                button -> {
                                    assert client != null;
                                    playerInventory.player.playSound(DANDYCORPCobblemonAdditions.VENDOR_CLICK_EVENT, 1.0f, (float) (0.8+(0.4*Math.random())));
                                    client.setScreen(new VendorSpecialsScreen(handler, playerInventory, title, balanceManager));
                                }
                        )
                        .renderer(ButtonComponent.Renderer.texture(new Identifier(DANDYCORPCobblemonAdditions.MOD_ID, "textures/gui/vendor/dc_specials.png"),0,0,220,120))
                        .margins(Insets.of(1,1,1,1))
                        .sizing(Sizing.fixed(220),Sizing.fixed(40))
        )
                .surface(Surface.VANILLA_TRANSLUCENT)
                .horizontalAlignment(HorizontalAlignment.CENTER)
                .verticalAlignment(VerticalAlignment.CENTER);

    }

    @Override
    public void render(DrawContext vanillaContext, int mouseX, int mouseY, float delta) {
        super.render(vanillaContext, mouseX, mouseY, delta);
    }

    @Override
    protected void handledScreenTick() {
        if (playerInventory.player.age % 20 == 0) {
            balanceManager.updateBalance();
        }
    }

    @Override
    public void close() {
        playerInventory.player.playSound(DANDYCORPCobblemonAdditions.VENDOR_CLICK_EVENT, 1.0f, (float) (0.2*Math.random()));
        super.close();
    }
}
