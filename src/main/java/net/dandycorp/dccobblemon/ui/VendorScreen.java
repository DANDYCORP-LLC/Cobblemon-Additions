package net.dandycorp.dccobblemon.ui;

import com.sun.jna.platform.win32.VerRsrc;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.TickEvent;
import io.wispforest.owo.ui.base.BaseOwoHandledScreen;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.component.TextureComponent;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.container.GridLayout;
import io.wispforest.owo.ui.core.*;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.client.registry.display.visibility.DisplayVisibilityPredicate;
import me.shedaniel.rei.api.common.display.Display;
import net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import static java.lang.Math.min;

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

        rootComponent.child(balanceManager.getBalanceDisplay());


        // ---------


        //engineering
        GridLayout grid = Containers.grid(Sizing.content(), Sizing.content(), 4, 2);
        grid.child(Components.button(Text.empty(), button -> {
            client.setScreen(new VendorEngineeringScreen(handler,playerInventory,title,balanceManager));
        })
                .renderer(ButtonComponent.Renderer.texture(new Identifier(DANDYCORPCobblemonAdditions.MOD_ID, "textures/gui/vendor/engineering.png"),0,0,100,186))
                .margins(Insets.of(1,1,1,1))
                .sizing(Sizing.fixed(100),Sizing.fixed(62)),
                0, 0);


        //agriculture
        grid.child(Components.button(Text.empty(), button -> {
            client.interactionManager.clickButton(this.getScreenHandler().syncId, 20);
        })
                .renderer(ButtonComponent.Renderer.texture(new Identifier(DANDYCORPCobblemonAdditions.MOD_ID, "textures/gui/vendor/engineering.png"),0,0,100,186))
                .margins(Insets.of(1,1,1,1))
                .sizing(Sizing.fixed(100),Sizing.fixed(62)),
                1, 0);


        //pokemon
        grid.child(Components.button(Text.empty(), button -> {
            client.interactionManager.clickButton(this.getScreenHandler().syncId, 30);
        })
                .renderer(ButtonComponent.Renderer.texture(new Identifier(DANDYCORPCobblemonAdditions.MOD_ID, "textures/gui/vendor/engineering.png"),0,0,100,186))
                .margins(Insets.of(1,1,1,1))
                .sizing(Sizing.fixed(100),Sizing.fixed(62)),
                0, 1);


        //resources
        grid.child(Components.button(Text.empty(), button -> {
            client.interactionManager.clickButton(this.getScreenHandler().syncId, 40);
        })
                .renderer(ButtonComponent.Renderer.texture(new Identifier(DANDYCORPCobblemonAdditions.MOD_ID, "textures/gui/vendor/engineering.png"),0,0,100,186))
                .margins(Insets.of(1,1,1,1))
                .sizing(Sizing.fixed(100),Sizing.fixed(62)),
                1, 1);


        rootComponent.child(grid);

        // ---------


        rootComponent.child(
                Components.button(
                                Text.empty(),
                                button -> {
                                    assert client != null;
                                    client.interactionManager.clickButton(this.getScreenHandler().syncId, 1);
                                    balanceManager.updateBalance();
                                }
                        )
                        .renderer(ButtonComponent.Renderer.texture(new Identifier(DANDYCORPCobblemonAdditions.MOD_ID, "textures/gui/vendor/dc_specials.png"),0,0,220,120))
                        .margins(Insets.of(1,1,1,1))
                        .sizing(Sizing.fixed(220),Sizing.fixed(40))
        )
                .surface(Surface.VANILLA_TRANSLUCENT)
                .horizontalAlignment(HorizontalAlignment.CENTER)
                .verticalAlignment(VerticalAlignment.CENTER);
        //this.uiAdapter.toggleInspector();
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

}
