package net.dandycorp.dccobblemon.compat.rei;

import com.cobblemon.mod.common.CobblemonItems;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ExclusionZones;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.dandycorp.dccobblemon.block.DANDYCORPBlocks;
import net.dandycorp.dccobblemon.util.grinder.GrinderDataCache;
import net.dandycorp.dccobblemon.util.vendor.*;
import net.dandycorp.dccobblemon.item.DANDYCORPItems;
import net.dandycorp.dccobblemon.ui.vendor.VendorScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DANDYCORPREIClientPlugin implements REIClientPlugin {

    private static final List<GrinderREIDisplay> registeredGrinderDisplays = new ArrayList<>();

    @Override
    public void registerExclusionZones(ExclusionZones zones) {
        zones.register(VendorScreen.class, screen -> {
            return List.of(new Rectangle(0,0,1000,1000));
        });
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        ScreenRegistry.getInstance().registerDecider(new VendorScreenDecider());
        REIClientPlugin.super.registerScreens(registry);
    }

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new GrinderREICategory());
        registry.addWorkstations(GrinderREICategory.GRINDING, EntryStacks.of(DANDYCORPBlocks.GRINDER_BLOCK));
        registry.add(new VendorREICategory());
        registry.addWorkstations(VendorREICategory.VENDOR, EntryStacks.of(DANDYCORPBlocks.VENDOR_BLOCK));
        REIClientPlugin.super.registerCategories(registry);
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registerGrinderDisplays();
        registerVendorDisplays();
    }

    public static void registerGrinderDisplays() {
        DisplayRegistry registry = DisplayRegistry.getInstance();

        Map<Item, Float> calculatedPointValues = GrinderDataCache.getCalculatedPointValues();
        if (calculatedPointValues == null || calculatedPointValues.isEmpty()) {
            System.err.println("GrinderData is not loaded or empty.");
            return;
        }

        for (Map.Entry<Item, Float> entry : calculatedPointValues.entrySet()) {
            Item item = entry.getKey();
            float pointValue = entry.getValue();
            if (pointValue > 0f) {
                ItemStack inputStack = new ItemStack(item);
                ItemStack outputStack = new ItemStack(DANDYCORPItems.TICKET);
                GrinderREIDisplay display = new GrinderREIDisplay(inputStack, outputStack, pointValue);
                registry.add(display);
                registeredGrinderDisplays.add(display);
            }
        }
    }

    public static void registerVendorDisplays() {
        MinecraftClient client = MinecraftClient.getInstance();
        DisplayRegistry registry = DisplayRegistry.getInstance();
        VendorData vendorData = VendorDataCache.getVendorData();
        if (vendorData == null || vendorData.getCategories() == null) {
            System.err.println("VendorData is not loaded or has no categories.");
            return;
        }

        for (VendorCategory category : vendorData.getCategories()) {
            if (category.getEntries() == null) continue;
            for (VendorEntry entry : category.getEntries()) {
                List<VendorItem> entryItems = entry.getItems();

                ItemStack ticketStack = DANDYCORPItems.TICKET.getDefaultStack();
                ticketStack.setCount(entry.getCost());

                ItemStack bagStack = DANDYCORPItems.TICKET_BAG_ITEM.getDefaultStack();
                bagStack.setCount(1);

                List<EntryIngredient> inputs = new ArrayList<>();
                inputs.add(EntryIngredients.of(ticketStack));
                inputs.add(EntryIngredients.of(bagStack));

                List<EntryIngredient> outputs = new ArrayList<>(entryItems.size());
                for (VendorItem item : entryItems) {
                    System.out.println("adding item " + item.getId() + " to outputs of " + entry.getTitle());
                    if (item.getId().startsWith("pokemon")) {
                        outputs.add(EntryIngredients.of(CobblemonItems.CHERISH_BALL.getDefaultStack()));
                        continue;
                    }
                    Item itemObj = Registries.ITEM.get(Identifier.tryParse(item.getId()));
                    if (itemObj != null) {
                        ItemStack stack = new ItemStack(itemObj, item.getQuantity());
                        outputs.add(EntryIngredients.of(stack));
                    }
                }
                VendorREIDisplay display = new VendorREIDisplay(inputs, outputs, category, entry);
                registry.add(display);
            }
        }
    }
}
