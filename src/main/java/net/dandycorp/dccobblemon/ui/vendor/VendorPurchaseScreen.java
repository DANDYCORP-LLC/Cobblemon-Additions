package net.dandycorp.dccobblemon.ui.vendor;

import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.ItemComponent;
import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.container.ScrollContainer;
import io.wispforest.owo.ui.core.*;
import net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions;
import net.dandycorp.dccobblemon.ui.PokemonComponent;
import net.dandycorp.dccobblemon.util.*;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class VendorPurchaseScreen extends VendorScreen {

    protected final PlayerInventory playerInventory;
    protected final VendorBalanceManager balanceManager;
    protected List<VendorEntry> categoryEntries;
    protected final List<EntryDisplayState> entryDisplayStates = new ArrayList<>();
    private final ParentComponent backButton;
    int scrollHeight = 70;

    protected static class EntryDisplayState {
        VendorEntry vendorEntry;
        int currentItemIndex;
        Component itemComponent;
        LabelComponent quantityLabel;
        FlowLayout entryItemDisplay;

        EntryDisplayState(VendorEntry vendorEntry) {
            this.vendorEntry = vendorEntry;
            this.currentItemIndex = 0;
        }
    }

    public VendorPurchaseScreen(VendorScreenHandler handler, PlayerInventory inventory, Text title, VendorBalanceManager balanceManager, String category) {
        super(handler, inventory, title);
        this.playerInventory = inventory;
        this.balanceManager = balanceManager;
        VendorData vendorData = handler.getVendorData();
        for (VendorCategory vc : vendorData.getCategories()) {
            if (category.equalsIgnoreCase(vc.getName())) {
                categoryEntries = new ArrayList<>(vc.getEntries()); // Copy the list to prevent modifications
                break;
            }
        }
        if (categoryEntries == null){
            playerInventory.player.closeHandledScreen();
            playerInventory.player.sendMessage(Text.of("no category found \"" + category + "\""));
        }

        backButton = Containers.stack(Sizing.content(1),Sizing.content(1)).child(
                        Components.button(Text.empty(), button -> {
                                    close();
                                })
                                .renderer(ButtonComponent.Renderer.flat(0xFF000000, 0xFF002200, 0xFF008800))
                                .sizing(Sizing.fixed(30), Sizing.fixed(18))
                                .margins(Insets.of(0, 0, 0, 0)))
                .child(Components.label(Text.of("§l⬅"))
                        .color(Color.GREEN)
                        .verticalTextAlignment(VerticalAlignment.CENTER)
                        .horizontalTextAlignment(HorizontalAlignment.CENTER)
                        .shadow(true))
                .surface(Surface.outline(0xFF00FF00))
                .alignment(HorizontalAlignment
                        .CENTER, VerticalAlignment.CENTER);
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        rootComponent.clearChildren();
        rootComponent.surface(Surface.VANILLA_TRANSLUCENT)
                .alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER);

        rootComponent.child(Containers.horizontalFlow(Sizing.content(),Sizing.content())
                .child(backButton)
                .child(balanceManager.getBalanceDisplay(220)));

        FlowLayout buttonContainer = Containers.verticalFlow(Sizing.content(), Sizing.content());
        buttonContainer.padding(Insets.of(5, 5, 5, 5));

        entryDisplayStates.clear();

        for (VendorEntry vendorEntry : categoryEntries) {
            //add first item to entry display state
            EntryDisplayState displayState = new EntryDisplayState(vendorEntry);
            entryDisplayStates.add(displayState);

            //create item display (either item or pokemon)
            Component itemComponent = createItemComponent(vendorEntry, displayState);

            //create hover text with a list of all the entry items
            List<Text> tooltipText = createTooltipText(vendorEntry);

            //create quantity for each item in entry
            LabelComponent quantityLabel = createQuantityLabel(vendorEntry, displayState);
            displayState.quantityLabel = quantityLabel;

            // Combine itemComponent and quantityLabel into entryItemDisplay
            FlowLayout entryItemDisplay = Containers.horizontalFlow(Sizing.content(), Sizing.content());
            entryItemDisplay.child(itemComponent);
            entryItemDisplay.tooltip(tooltipText);
            entryItemDisplay.child(quantityLabel);

            // Set entryItemDisplay in displayState
            displayState.entryItemDisplay = entryItemDisplay;


            //create title for the entry
            LabelComponent titleLabel = createTitleLabel(vendorEntry);

            //create description for the entry
            Component descriptionLabel = createDescriptionLabel(vendorEntry);

            //combine
            FlowLayout entryDescriptionDisplay = Containers.horizontalFlow(Sizing.content(), Sizing.content());
            entryDescriptionDisplay.child(entryItemDisplay);
            entryDescriptionDisplay.child(descriptionLabel);

            //combine
            FlowLayout entryFullDisplay = Containers.verticalFlow(Sizing.content(), Sizing.content());
            entryFullDisplay.child(titleLabel);
            entryFullDisplay.child(entryDescriptionDisplay);

            //completed container
            ParentComponent purchaseButton = createPurchaseButton(vendorEntry);
            FlowLayout purchaseDisplay = createEntryDisplay(entryFullDisplay, purchaseButton);
            buttonContainer.child(purchaseDisplay);
        }

        ScrollContainer<Component> scrollContainer = Containers.verticalScroll(Sizing.fixed(330), Sizing.fill(scrollHeight), buttonContainer);

        rootComponent.child(scrollContainer);
        //this.uiAdapter.toggleInspector();
    }

    /**
     returns the item display for the entry. if the input is a pokemon, the display will be a
     PokemonComponent, otherwise is an ItemComponent. determines which to display based on
     displayState's index in the vendorEntry.
     */
    private Component createItemComponent(VendorEntry vendorEntry, EntryDisplayState displayState) {
        VendorItem currentItem = vendorEntry.getItems().get(displayState.currentItemIndex);
        String itemId = currentItem.getId();
        Component itemComponent;

        if (itemId.startsWith("pokemon:")) {
            String pokemonName = itemId.substring("pokemon:".length());
            PokemonComponent pokemonComponent = new PokemonComponent(pokemonName, false, 25, 25);
            displayState.itemComponent = pokemonComponent;
            itemComponent = pokemonComponent;
        }

        else { // return an item component
            ItemStack stack = handler.getItemStackFromID(itemId);
            if (stack != null) {
                stack.setCount(currentItem.getQuantity());
            }
            ItemComponent itemComp = (ItemComponent) Components.item(stack)
                    .sizing(Sizing.fixed(25), Sizing.fixed(25));
            displayState.itemComponent = itemComp;
            itemComponent = itemComp;
        }

        return itemComponent;
    }

    /**
     creates a display list of each purchasable in a vendor entry
     */
    private List<Text> createTooltipText(VendorEntry vendorEntry) {
        List<Text> tooltipText = new ArrayList<>();
        for (VendorItem item : vendorEntry.getItems()) {
            String id = item.getId();
            int quantity = item.getQuantity();

            if (id.startsWith("pokemon:")) {
                String text = id.substring("pokemon:".length());
                String[] textList = text.split("_");
                StringBuilder tooltip = new StringBuilder();
                for (String token : textList){
                    if(token.endsWith("nature")){
                        tooltip.append(StringUtils.capitalize(token.substring(0, token.length() - "nature".length()))).append(" ");
                    }
                    else {
                        tooltip.append(StringUtils.capitalize(token+" "));
                    }
                }
                tooltipText.add(Text.literal(tooltip + "x" + quantity));
            } else {
                ItemStack itemStack = handler.getItemStackFromID(id);
                if (itemStack != null) {
                    String itemName = itemStack.getName().getString();
                    tooltipText.add(Text.literal(itemName + " x" + quantity));
                }
            }
        }
        return tooltipText;
    }

    /**
     gives a LabelComponent for the quantity of a provided purchasable. finds the purchasable
     with the display state's index and the full entry list.
     */
    private LabelComponent createQuantityLabel(VendorEntry vendorEntry, EntryDisplayState displayState) {
        VendorItem currentItem = vendorEntry.getItems().get(displayState.currentItemIndex);
        String quantityText = "x" + currentItem.getQuantity();

        return (LabelComponent) Components.label(Text.of(quantityText))
                .sizing(Sizing.fixed(25), Sizing.content())
                .margins(Insets.of(0, 5, 0, 0));
    }

    /**
     gives a fancy LabelComponent from the "title" field in vendor entry
     */
    private LabelComponent createTitleLabel(VendorEntry vendorEntry) {
        return (LabelComponent) Components.label(Text.of(vendorEntry.getTitle()))
                .color(Color.GREEN)
                .shadow(true)
                .sizing(Sizing.content())
                .margins(Insets.of(0, 5, 3, 0));
    }

    /**
     gives a fancy LabelComponent from the "description" field in vendor entry
     */
    private Component createDescriptionLabel(VendorEntry vendorEntry) {
        return Components.label(Text.of(vendorEntry.getDescription()))
                .color(Color.ofArgb(0xFF00AA00))
                .shadow(true)
                .margins(Insets.of(0, 0, 0, 8))
                .sizing(Sizing.fixed(190), Sizing.content());
    }

    /**
     creates the button shell that displays the cost and calls handlePurchaseButtonClick
     */
    private ParentComponent createPurchaseButton(VendorEntry vendorEntry) {
        String plural = vendorEntry.getCost() > 1 ? " tickets" : " ticket";
        String buttonText = vendorEntry.getCost() + plural;

        return Containers.stack(Sizing.content(1), Sizing.content(1))
                .child(
                        Components.button(Text.empty(), b -> {
                                    handlePurchaseButtonClick(vendorEntry);
                                })
                                .renderer(ButtonComponent.Renderer.flat(0xFF000000, 0xFF002200, 0xFF008800))
                                .sizing(Sizing.fixed(62), Sizing.fixed(20))
                                .margins(Insets.of(0, 0, 0, 0)))
                .child(
                        Components.label(Text.literal(buttonText))
                                .color(Color.GREEN)
                                .verticalTextAlignment(VerticalAlignment.CENTER)
                                .horizontalTextAlignment(HorizontalAlignment.CENTER)
                                .shadow(true))
                .surface(Surface.outline(0xFF00FF00))
                .alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
    }


    /**
     creates the full outlined display for the entry. takes the full description container and the
     button container.
     */
    private FlowLayout createEntryDisplay(FlowLayout mainContainer, ParentComponent purchaseButton){
        FlowLayout entryDisplay = Containers.horizontalFlow(Sizing.fill(100), Sizing.content())
                .child(mainContainer)
                .child(purchaseButton);

        entryDisplay.margins(Insets.of(0, 10, 0, 0))
                .surface(Surface.flat(0xFF000000).and(Surface.outline(0xFF00FF00)))
                .padding(Insets.of(4, 4, 4, 4));

        return entryDisplay;
    }

    /**
     sends "button ID" field from the vendorEntry back to the screen handler which then actually does
     the transaction.
     */
    private void handlePurchaseButtonClick(VendorEntry vendorEntry) {
        client.interactionManager.clickButton(this.getScreenHandler().syncId, vendorEntry.getButtonID());
        balanceManager.updateBalance();
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
        if (playerInventory.player.age % 40 == 0) {
            for (EntryDisplayState displayState : entryDisplayStates) {
                List<VendorItem> items = displayState.vendorEntry.getItems();
                if (items.size() > 1) {
                    // Move to the next item
                    displayState.currentItemIndex = (displayState.currentItemIndex + 1) % items.size();
                    VendorItem currentItem = items.get(displayState.currentItemIndex);
                    String itemId = currentItem.getId();

                    if (itemId.startsWith("pokemon:")) {
                        String pokemonName = itemId.substring("pokemon:".length());

                        if (displayState.itemComponent instanceof PokemonComponent) {
                            // Update existing PokemonComponent
                            ((PokemonComponent) displayState.itemComponent).setPokemonName(pokemonName);
                        } else {
                            // Replace with a new PokemonComponent
                            PokemonComponent pokemonComponent = new PokemonComponent(pokemonName, false, 25, 25);
                            replaceItemComponent(displayState, pokemonComponent);
                        }
                    } else {
                        ItemStack stack = handler.getItemStackFromID(itemId);
                        if (stack != null) {
                            stack.setCount(currentItem.getQuantity());

                            if (displayState.itemComponent instanceof ItemComponent) {
                                // Update existing ItemComponent
                                ((ItemComponent) displayState.itemComponent).stack(stack);
                            } else {
                                // Replace with a new ItemComponent
                                ItemComponent itemComp = (ItemComponent) Components.item(stack)
                                        .sizing(Sizing.fixed(25), Sizing.fixed(25));
                                replaceItemComponent(displayState, itemComp);
                            }
                        }
                    }

                    // Update the quantity label
                    String quantityText = "x" + currentItem.getQuantity();
                    displayState.quantityLabel.text(Text.of(quantityText));
                }
            }
        }
    }

    private void replaceItemComponent(EntryDisplayState displayState, Component newItemComponent) {
        FlowLayout entryItemDisplay = displayState.entryItemDisplay;

        // Remove the old itemComponent
        entryItemDisplay.removeChild(displayState.itemComponent);

        // Add the new itemComponent at the same position (index 0)
        entryItemDisplay.child(0,newItemComponent);

        // Update the displayState
        displayState.itemComponent = newItemComponent;
    }


}
