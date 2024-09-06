package net.dandycorp.dccobblemon.ui;

import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.container.GridLayout;
import io.wispforest.owo.ui.core.HorizontalAlignment;
import io.wispforest.owo.ui.core.OwoUIAdapter;
import io.wispforest.owo.ui.core.Surface;
import io.wispforest.owo.ui.core.VerticalAlignment;
import net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

public class VendorScreen extends BaseOwoScreen {

    private final VendorScreenHandler handler;
    private final PlayerInventory playerInventory;

    // Updated constructor to accept VendorScreenHandler and PlayerInventory
    public VendorScreen(VendorScreenHandler handler, PlayerInventory playerInventory, Text title) {
        super(title);
        this.handler = handler;
        this.playerInventory = playerInventory;
    }


    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this,Containers::verticalFlow);
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        rootComponent.surface(Surface.VANILLA_TRANSLUCENT)
                .horizontalAlignment(HorizontalAlignment.CENTER)
                .verticalAlignment(VerticalAlignment.CENTER);

        rootComponent.child(
                Components.button(
                        Text.literal("compliment"),
                        button -> {
                            Entity entity = this.client.getCameraEntity();
                            System.out.println(entity.getName());
                            entity.playSound(DANDYCORPCobblemonAdditions.COMPLIMENT_EVENT, 0.8f, 1.1f);

                        }
                )
        );

    }
}
