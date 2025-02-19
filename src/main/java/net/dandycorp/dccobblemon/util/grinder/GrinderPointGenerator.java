package net.dandycorp.dccobblemon.util.grinder;

import net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions;
import net.dandycorp.dccobblemon.compat.ChippedCompat;
import net.dandycorp.dccobblemon.item.DANDYCORPItems;
import net.dandycorp.dccobblemon.mixin.accessor.SmithingTransformRecipeAccessor;
import net.fabricmc.loader.api.FabricLoader;
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

import java.util.*;
import java.util.stream.Collectors;

public class GrinderPointGenerator {

    public static Map<Item, Float> basePointValues = new HashMap<>();
    public static Map<Item, Float> calculatedPointValues = new HashMap<>();
    private static Map<Item, Float> computedValues = new HashMap<>();
    public static Map<Item, List<Identifier>> recipes = new HashMap<>();
    private static List<Item> items = new ArrayList<>();
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
        if (FabricLoader.getInstance().isModLoaded("chipped")) {
            ChippedCompat.initialize();
        }

        calculatedPointValues.putAll(basePointValues);
        items.addAll(Registries.ITEM.stream().toList());


        for (Item item : items){
            calculateValue(item);
        }

        long uniqueValues = calculatedPointValues.values().stream().filter(points -> points > 0).count();
        DANDYCORPCobblemonAdditions.LOGGER.info("{} / {} unique grinder values initialized! ({}%)",
                uniqueValues,
                calculatedPointValues.size(),
                ((float) uniqueValues/(float) calculatedPointValues.size())*100f);

        DANDYCORPCobblemonAdditions.LOGGER.warn("Pointless items: {}", calculatedPointValues.entrySet()
                .stream()
                .filter(a->a.getValue() <= 0)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
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
        return dfs(item, new ArrayList<>());
    }

    private static float dfs(Item item, List<Item> path) {
        if (computedValues.containsKey(item)) {
            return computedValues.get(item);
        }

        // Cycle detection
        if (path.contains(item)) {
            int index = path.indexOf(item);
            float cycleSum = 0f;
            for (int i = index + 1; i < path.size(); i++) {
                Item cycleItem = path.get(i);
                cycleSum += computedValues.getOrDefault(cycleItem, basePointValues.getOrDefault(cycleItem, 0f));
            }
            return cycleSum;
        }

        path.add(item);

        if (basePointValues.containsKey(item)) {
            float base = basePointValues.get(item);
            setComputedValue(item, base);
            path.remove(path.size() - 1);
            return base;
        }

        // chipped compat
        if (FabricLoader.getInstance().isModLoaded("chipped")
                && Registries.ITEM.getId(item).getNamespace().startsWith("chipped")) {
            Item baseItem = ChippedCompat.getChippedBaseItem(item);
            if (baseItem != null) {
                float baseValue = dfs(baseItem, path);
                float finalValue = baseValue * 1.1f;
                setComputedValue(item, finalValue);
                path.remove(path.size() - 1);
                return finalValue;
            }
        }

        // Get all recipes that output this item.
        List<Identifier> itemRecipes = recipes.getOrDefault(item, Collections.emptyList());
        if (itemRecipes.isEmpty()) {
            float fallback = basePointValues.getOrDefault(item, 0f);
            setComputedValue(item, fallback);
            path.remove(path.size() - 1);
            return fallback;
        }

        List<Float> branchValues = new ArrayList<>();
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
                    float ingredientValue = dfs(ingredientItem, path);
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
                float ingValue = computedValues.getOrDefault(entry.getKey(), 0f);
                currentRecipeValue += ingValue * entry.getValue();
            }

            ItemStack outputStack = recipe.getOutput(registryManager);
            int outputQuantity = outputStack.getCount();
            currentRecipeValue /= outputQuantity;
            currentRecipeValue *= 1.10f; //10% increase per crafting step

            branchValues.add(currentRecipeValue);
        }

        path.remove(path.size() - 1);

        float minPositive = Float.MAX_VALUE;
        for (float branchValue : branchValues) {
            if (branchValue > 0 && branchValue < minPositive) {
                minPositive = branchValue;
            }
        }
        float result = (minPositive != Float.MAX_VALUE)
                ? minPositive
                : basePointValues.getOrDefault(item, 0f);

        setComputedValue(item, result);
        return result;
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
        return (float) ((int) (1000 * value)) / 1000f;
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

    public static boolean isInitialized() {
        return !calculatedPointValues.isEmpty();
    }
}
