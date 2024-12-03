package net.dandycorp.dccobblemon.compat.rei;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.Optional;

public class GrinderREIDisplay extends BasicDisplay {

    private final float pointValue;

    public GrinderREIDisplay(ItemStack input, ItemStack output, float pointValue) {
        super(Collections.singletonList(EntryIngredients.of(input)), Collections.singletonList(EntryIngredients.of(output)), Optional.empty());
        this.pointValue = pointValue;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return GrinderREICategory.GRINDING;
    }

    public float getPointValue() {
        return pointValue;
    }
}
