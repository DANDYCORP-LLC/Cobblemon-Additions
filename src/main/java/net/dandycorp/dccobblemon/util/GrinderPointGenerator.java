package net.dandycorp.dccobblemon.util;

import com.cobblemon.mod.common.Environment;
import net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.*;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;

import javax.annotation.Nonnull;
import java.util.*;

public class GrinderPointGenerator {

    public static Map<Item, Float> basePointValues = new HashMap<>();
    public static Map<Item, Float> calculatedPointValues = new HashMap<>();
    private static Map<Item, Float> computedValues = new HashMap<>();
    public static Map<Item, List<Identifier>> recipes = new HashMap<>();
    private static RecipeManager recipeManager;
    private static DynamicRegistryManager.Immutable registryManager;

    public static void initializePointValues(MinecraftServer server){
        DANDYCORPCobblemonAdditions.LOGGER.info("Initializing DANDYCORP Grinder Point Values");

        calculatedPointValues.clear();
        basePointValues.clear();
        computedValues.clear();
        recipes.clear();
        populateBaseValuesFromJson();

        recipeManager = server.getRecipeManager();
        registryManager = server.getRegistryManager();

        for (Recipe<?> recipe : recipeManager.values()){
            Item output = recipe.getOutput(server.getRegistryManager()).getItem();
            recipe.getId();
            recipes.computeIfAbsent(output, k -> new ArrayList<>()).add(recipe.getId());
        }

        calculatedPointValues.putAll(basePointValues);
//        for (Item item : Registries.ITEM){
//            calculateValue(item);
//            System.out.println(item + " calculated to be: " + calculatedPointValues.get(item) + " points.");
//        }

        calculateValue(Items.IRON_PICKAXE);
        System.out.println("\n\n\n");
        calculateValue(Items.IRON_SWORD);
        System.out.println(calculatedPointValues);
    }

    private static void populateBaseValuesFromJson(){
        Map<String, Float> basePointData = GrinderDataLoader.loadBasePointData();
        if (basePointData != null) {
            basePointData.forEach((itemId, pointValue) -> {
                try {
                    Identifier identifier = new Identifier(itemId);
                    Item item = Registries.ITEM.get(identifier);
                    if (item != null) {
                        basePointValues.put(item, pointValue);
                        DANDYCORPCobblemonAdditions.LOGGER.debug("Loaded base point for item: " + itemId + " -> " + pointValue);
                    } else {
                        DANDYCORPCobblemonAdditions.LOGGER.warn("Item not found for ID: " + itemId);
                    }
                } catch (Exception e) {
                    DANDYCORPCobblemonAdditions.LOGGER.error("Invalid item identifier: " + itemId, e);
                }
            });
        } else {
            DANDYCORPCobblemonAdditions.LOGGER.warn("Base point data could not be loaded.");
        }
    }

    /**
     * takes the recipe tree of a given item and finds the minimum point sum of
     * all of its parts.
     *
     * @param item the item's tree to start parsing
     * @return the minimum sum of all recipe trees
     */
    private static void calculateValue(Item item){
        float value = dfs(item, new HashSet<>());
        if (value == Float.MAX_VALUE) {
            calculatedPointValues.put(item, 0f);
        } else {
            calculatedPointValues.put(item, value);
        }
    }

    /**
     * depth first search algorithm for calculateValue() that probes branches
     * of the recipe tree. returns the sum of its path.
     *
     * @param item root node
     * @param inProgress list of visited nodes
     * @return
     */
    private static float dfs(Item item, Set<Item> inProgress){
        if (computedValues.containsKey(item)) {
            return computedValues.get(item);
        }

        if (inProgress.contains(item)) {
            DANDYCORPCobblemonAdditions.LOGGER.warn("Cycle detected for item: " + Registries.ITEM.getId(item));
            return Float.MAX_VALUE;
        }

        inProgress.add(item);

        // Base case: if the item has a base point value
        if (basePointValues.containsKey(item)) {
            float baseValue = basePointValues.get(item);
            computedValues.put(item, baseValue);
            inProgress.remove(item);
            return baseValue;
        }

        List<Identifier> itemRecipes = recipes.getOrDefault(item, Collections.emptyList());

        if (itemRecipes.isEmpty()) {
            DANDYCORPCobblemonAdditions.LOGGER.warn("No recipe for item: " + Registries.ITEM.getId(item));
            computedValues.put(item, Float.MAX_VALUE);
            inProgress.remove(item);
            return Float.MAX_VALUE;
        }

        float minValue = Float.MAX_VALUE;

        for (Identifier recipeId : itemRecipes){
            Recipe<?> recipe = recipeManager.get(recipeId).orElse(null);
            if (recipe == null) {
                DANDYCORPCobblemonAdditions.LOGGER.warn("Recipe not found for ID: " + recipeId);
                continue;
            }

            float currentRecipeValue = 0.0f;
            boolean validRecipe = true;

            Map<Item, Integer> ingredientCounts = new HashMap<>();

            // Handle Shaped and Shapeless recipes separately
            if (recipe instanceof ShapedRecipe) {
                ShapedRecipe shapedRecipe = (ShapedRecipe) recipe;
                @Nonnull List<Ingredient> ingredients = shapedRecipe.getIngredients();

                for (Ingredient ingredient : ingredients) {
                    if (ingredient.isEmpty()) {
                        continue;
                    }
                    float ingredientMinValue = Float.MAX_VALUE;
                    Item selectedItem = null;
                    for (ItemStack stack : ingredient.getMatchingStacks()) {
                        Item ingredientItem = stack.getItem();
                        float ingredientValue = dfs(ingredientItem, inProgress);
                        if (ingredientValue == Float.MAX_VALUE) {
                            continue; // Skip invalid ingredients
                        }
                        if (ingredientValue < ingredientMinValue) {
                            ingredientMinValue = ingredientValue;
                            selectedItem = ingredientItem;
                        }
                    }
                    if (ingredientMinValue == Float.MAX_VALUE || selectedItem == null) {
                        validRecipe = false;
                        break;
                    }
                    ingredientCounts.put(selectedItem, ingredientCounts.getOrDefault(selectedItem, 0) + 1);
                }
            } else if (recipe instanceof ShapelessRecipe) {
                ShapelessRecipe shapelessRecipe = (ShapelessRecipe) recipe;
                @Nonnull List<Ingredient> ingredients = shapelessRecipe.getIngredients();

                for (Ingredient ingredient : ingredients) {
                    if (ingredient.isEmpty()) {
                        continue;
                    }
                    float ingredientMinValue = Float.MAX_VALUE;
                    Item selectedItem = null;
                    for (ItemStack stack : ingredient.getMatchingStacks()) {
                        Item ingredientItem = stack.getItem();
                        float ingredientValue = dfs(ingredientItem, inProgress);
                        if (ingredientValue == Float.MAX_VALUE) {
                            continue; // Skip invalid ingredients
                        }
                        if (ingredientValue < ingredientMinValue) {
                            ingredientMinValue = ingredientValue;
                            selectedItem = ingredientItem;
                        }
                    }
                    if (ingredientMinValue == Float.MAX_VALUE || selectedItem == null) {
                        validRecipe = false;
                        break;
                    }
                    ingredientCounts.put(selectedItem, ingredientCounts.getOrDefault(selectedItem, 0) + 1);
                }
            } else {
                // Handle other recipe types or skip
                continue;
            }

            if (!validRecipe) {
                continue;
            }

            // Calculate the total cost of the ingredients
            for (Map.Entry<Item, Integer> entry : ingredientCounts.entrySet()) {
                Item ingredientItem = entry.getKey();
                int quantity = entry.getValue();
                float ingredientValue = computedValues.get(ingredientItem);
                currentRecipeValue += ingredientValue * quantity;
            }

            // Adjust for output quantity
            ItemStack outputStack = recipe.getOutput(registryManager);
            int outputQuantity = outputStack.getCount();
            currentRecipeValue /= outputQuantity;

            // Apply a 10% increase to account for the crafting step
            currentRecipeValue *= 1.10f;

            if (currentRecipeValue < minValue) {
                minValue = currentRecipeValue;
            }
        }

        inProgress.remove(item);

        if (minValue == Float.MAX_VALUE) {
            // No valid recipe found
            computedValues.put(item, Float.MAX_VALUE);
            return Float.MAX_VALUE;
        } else {
            computedValues.put(item, minValue);
            return minValue;
        }
    }

}
