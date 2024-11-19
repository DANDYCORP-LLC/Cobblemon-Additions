package net.dandycorp.dccobblemon.util;

import net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions;
import net.dandycorp.dccobblemon.mixin.accessor.SmithingTransformRecipeAccessor;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.*;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

public class GrinderPointGenerator {

    public static Map<Item, Float> basePointValues = new HashMap<>();
    public static Map<Item, Float> calculatedPointValues = new HashMap<>();
    private static Map<Item, Float> computedValues = new HashMap<>();
    public static Map<Item, List<Identifier>> recipes = new HashMap<>();
    private static RecipeManager recipeManager;
    private static DynamicRegistryManager.Immutable registryManager;

    public static void initializePointValues(MinecraftServer server) {
        DANDYCORPCobblemonAdditions.LOGGER.info("Initializing DANDYCORP Grinder Point Values");

        calculatedPointValues.clear();
        basePointValues.clear();
        computedValues.clear();
        recipes.clear();
        populateBaseValuesFromJson();

        recipeManager = server.getRecipeManager();
        registryManager = server.getRegistryManager();

        for (Recipe<?> recipe : recipeManager.values()) {
            Item output = recipe.getOutput(registryManager).getItem();
            recipes.computeIfAbsent(output, k -> new ArrayList<>()).add(recipe.getId());
        }

        calculatedPointValues.putAll(basePointValues);
        for (Item item : Registries.ITEM){
            calculateValue(item);
        }

        long uniqueValues = calculatedPointValues.values().stream().filter(points -> points > 0).count();
        DANDYCORPCobblemonAdditions.LOGGER.info("{} / {} unique grinder values initialized! ({}%)",
                uniqueValues,
                calculatedPointValues.size(),
                ((float) uniqueValues/(float) calculatedPointValues.size())*100f);
    }

    private static void populateBaseValuesFromJson() {
        Map<String, Float> basePointData = GrinderDataLoader.loadBasePointData();
        if (basePointData != null) {
            basePointData.forEach((itemId, pointValue) -> {
                try {
                    if (itemId.startsWith("#")) {
                        Identifier tagId = new Identifier(itemId.substring(1));

                        TagKey<Item> itemTagKey = TagKey.of(RegistryKeys.ITEM, tagId);
                        Optional<RegistryEntryList.Named<Item>> optionalItems = Registries.ITEM.getEntryList(itemTagKey);

                        if (optionalItems.isPresent()) {
                            for (RegistryEntry<Item> entry : optionalItems.get()) {
                                Item item = entry.comp_349();
                                if (item != null) {
                                    basePointValues.put(item, round(pointValue));
                                    //DANDYCORPCobblemonAdditions.LOGGER.debug("Loaded base point for item: " + Registries.ITEM.getId(item) + " from item tag: " + itemId + " -> " + pointValue);
                                } else {
                                    //DANDYCORPCobblemonAdditions.LOGGER.warn("Item was null for: " + entry);
                                }
                            }
                        } else {
                            TagKey<Block> blockTagKey = TagKey.of(RegistryKeys.BLOCK, tagId);
                            Optional<RegistryEntryList.Named<Block>> optionalBlocks = Registries.BLOCK.getEntryList(blockTagKey);

                            if (optionalBlocks.isPresent()) {
                                for (RegistryEntry<Block> blockEntry : optionalBlocks.get()) {
                                    Block block = blockEntry.comp_349();
                                    if (block != null) {
                                        Item item = block.asItem();
                                        if (item != Items.AIR) {
                                            basePointValues.put(item, round(pointValue));
                                            //DANDYCORPCobblemonAdditions.LOGGER.debug("Loaded base point for item: " + Registries.ITEM.getId(item) + " from block tag: " + itemId + " -> " + pointValue);
                                        } else {
                                            DANDYCORPCobblemonAdditions.LOGGER.warn("Block " + Registries.BLOCK.getId(block) + " has no corresponding item.");
                                        }
                                    } else {
                                        DANDYCORPCobblemonAdditions.LOGGER.warn("Block was null for: " + blockEntry);
                                    }
                                }
                            } else {
                                DANDYCORPCobblemonAdditions.LOGGER.warn("Tag not found or empty for ID: " + tagId);
                            }
                        }
                    } else {
                        Identifier identifier = new Identifier(itemId);
                        Item item = Registries.ITEM.get(identifier);
                        if (item != null) {
                            basePointValues.put(item, round(pointValue));
                            //DANDYCORPCobblemonAdditions.LOGGER.debug("Loaded base point for item: " + itemId + " -> " + pointValue);
                        } else {
                            DANDYCORPCobblemonAdditions.LOGGER.warn("Item not found for ID: " + itemId);
                        }
                    }
                } catch (Exception e) {
                    DANDYCORPCobblemonAdditions.LOGGER.error("Invalid item or tag identifier: " + itemId, e);
                }
            });
        } else {
            DANDYCORPCobblemonAdditions.LOGGER.warn("Base point data could not be loaded.");
        }
    }

    /**
     * Calculates the point value for the specified item by recursively traversing its recipe tree.
     * Stores the computed value in the calculatedPointValues map.
     *
     * @param item The item to calculate the point value for.
     */
    private static float calculateValue(Item item) {
        return dfs(item, new HashSet<>());
    }

    /**
     * recursive memoizing algorithm that finds the smallest non-zero value path down an item's recipe tree.
     * if a path yields >0 points it will prefer that, and it will prefer the path with the least
     * value. if no valued paths are found, it will return 0.
     *
     * @param item The item to calculate the point value for.
     * @param inProgress pass in an empty list that DFS will populate to handle cycles
     */
    private static float dfs(Item item, Set<Item> inProgress) {
        if (computedValues.containsKey(item)) {
            return computedValues.get(item);
        }

        if (inProgress.contains(item)) {
            setComputedValue(item, calculatedPointValues.getOrDefault(item, 0f));
            return calculatedPointValues.getOrDefault(item, 0f);
        }

        inProgress.add(item);

        if (basePointValues.containsKey(item)) {
            float baseValue = basePointValues.get(item);
            setComputedValue(item, baseValue);
            inProgress.remove(item);
            return baseValue;
        }

        List<Identifier> itemRecipes = recipes.getOrDefault(item, Collections.emptyList());

        if (itemRecipes.isEmpty()) {
            setComputedValue(item, basePointValues.getOrDefault(item, 0f));
            inProgress.remove(item);
            return basePointValues.getOrDefault(item, 0f);
        }

        float minValue = Float.MAX_VALUE;
        boolean foundNonZeroValue = false; // Flag to track non-zero minimum value

        for (Identifier recipeId : itemRecipes) {
            Recipe<?> recipe = recipeManager.get(recipeId).orElse(null);
            if (recipe == null) {
                continue;
            }

            float currentRecipeValue = 0.0f;
            boolean validRecipe = true;

            Map<Item, Integer> ingredientCounts = new HashMap<>();
            List<Ingredient> ingredients = new ArrayList<>(recipe.getIngredients());

            if (recipe instanceof SmithingTransformRecipe str) {
                SmithingTransformRecipeAccessor accessor = (SmithingTransformRecipeAccessor) str;
                ingredients.add(accessor.getBase());
                ingredients.add(accessor.getTemplate());
                ingredients.add(accessor.getAddition());
            }

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
                        continue;
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

            if (!validRecipe) {
                continue;
            }

            for (Map.Entry<Item, Integer> entry : ingredientCounts.entrySet()) {
                Item ingredientItem = entry.getKey();
                int quantity = entry.getValue();
                float ingredientValue = computedValues.get(ingredientItem);
                currentRecipeValue += ingredientValue * quantity;
            }

            ItemStack outputStack = recipe.getOutput(registryManager);
            int outputQuantity = outputStack.getCount();
            currentRecipeValue /= outputQuantity;

            currentRecipeValue *= 1.10f; // Add 10% crafting cost

            if (currentRecipeValue > 0) {
                if (!foundNonZeroValue || currentRecipeValue < minValue) {
                    minValue = currentRecipeValue;
                    foundNonZeroValue = true;
                }
            } else if (!foundNonZeroValue && currentRecipeValue == 0) {
                if (minValue == Float.MAX_VALUE || currentRecipeValue < minValue) {
                    minValue = currentRecipeValue;
                }
            }
        }

        inProgress.remove(item);

        if (minValue == Float.MAX_VALUE) {
            // No valid recipes found, set to base value or zero
            setComputedValue(item, basePointValues.getOrDefault(item, 0f));
            return basePointValues.getOrDefault(item, 0f);
        } else {
            setComputedValue(item, minValue);
            return minValue;
        }
    }


    private static void setComputedValue(Item item, float value) {
        if (value == Float.MAX_VALUE) {
            computedValues.put(item, Float.MAX_VALUE);
            calculatedPointValues.put(item, 0f);
        } else {
            float roundedValue = round(value);
            computedValues.put(item, roundedValue);
            calculatedPointValues.put(item, roundedValue);
        }
    }

    private static float round(float value) {
        BigDecimal bd = new BigDecimal(Float.toString(value));
        bd = bd.setScale(3, RoundingMode.CEILING);
        return bd.floatValue();
    }

    public static List<Item> getItemsWithPoints() {
        return calculatedPointValues.entrySet().stream()
                .filter(entry -> entry.getValue() > 0)
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public static Map<Item,Float> getCalculatedValues() {
        return calculatedPointValues;
    }

    public static float getPointValue(Item item) {
        return calculatedPointValues.getOrDefault(item, 0f);
    }

    public static long getItemCount(){
        return calculatedPointValues.size();
    }
}
