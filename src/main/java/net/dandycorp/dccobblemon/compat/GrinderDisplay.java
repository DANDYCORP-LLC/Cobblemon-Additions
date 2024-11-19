package net.dandycorp.dccobblemon.compat;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.dandycorp.dccobblemon.item.Items;
import net.dandycorp.dccobblemon.util.GrinderPointGenerator;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class GrinderDisplay extends BasicDisplay {

    private final float pointValue;

    public GrinderDisplay(ItemStack input, ItemStack output, float pointValue) {
        super(Collections.singletonList(EntryIngredients.of(input)), Collections.singletonList(EntryIngredients.of(output)), Optional.empty());
        this.pointValue = pointValue;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return GrinderCategory.GRINDING;
    }

    public float getPointValue() {
        return pointValue;
    }
}
