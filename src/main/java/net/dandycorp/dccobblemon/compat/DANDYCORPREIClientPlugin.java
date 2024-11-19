package net.dandycorp.dccobblemon.compat;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ExclusionZones;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.dandycorp.dccobblemon.block.Blocks;
import net.dandycorp.dccobblemon.item.Items;
import net.dandycorp.dccobblemon.ui.vendor.VendorScreen;
import net.dandycorp.dccobblemon.ui.vendor.VendorScreenDecider;
import net.dandycorp.dccobblemon.util.GrinderPointGenerator;
import net.dandycorp.dccobblemon.util.VendorCategory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

public class DANDYCORPREIClientPlugin implements REIClientPlugin {

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
        registry.add(new GrinderCategory());
        registry.addWorkstations(GrinderCategory.GRINDING, EntryStacks.of(Blocks.GRINDER_BLOCK));
        REIClientPlugin.super.registerCategories(registry);
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        // For each item with points, create and register a GrinderDisplay
        for (Item item : GrinderPointGenerator.getItemsWithPoints()) {
            float pointValue = GrinderPointGenerator.getPointValue(item);
            if (pointValue > 0f) {
                ItemStack inputStack = new ItemStack(item);
                ItemStack outputStack = new ItemStack(Items.TICKET);
                GrinderDisplay display = new GrinderDisplay(inputStack, outputStack, pointValue);
                registry.add(display);
            }
        }
    }
}
