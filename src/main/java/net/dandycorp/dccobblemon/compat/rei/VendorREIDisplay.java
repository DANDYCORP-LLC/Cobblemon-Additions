package net.dandycorp.dccobblemon.compat.rei;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.dandycorp.dccobblemon.util.VendorCategory;
import net.dandycorp.dccobblemon.util.VendorEntry;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class VendorREIDisplay extends BasicDisplay {

    int cost;
    VendorCategory category;
    VendorEntry entry;


    public VendorREIDisplay(ItemStack input, List<EntryIngredient> outputs, VendorCategory category, VendorEntry entry) {
        super((Collections.singletonList(EntryIngredients.of(input))), outputs);
        this.category = category;
        this.entry = entry;
        this.cost = entry.getCost();
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return VendorREICategory.VENDOR;
    }

    public VendorCategory getCategory() {
        return category;
    }

    public VendorEntry getEntry() {
        return entry;
    }
}