package net.dandycorp.dccobblemon.mixin.accessor;

import net.minecraft.recipe.SmithingTransformRecipe;
import net.minecraft.recipe.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SmithingTransformRecipe.class)
public interface SmithingTransformRecipeAccessor {
    @Accessor("template")
    Ingredient getTemplate();

    @Accessor("base")
    Ingredient getBase();

    @Accessor("addition")
    Ingredient getAddition();
}
