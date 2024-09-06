package net.dandycorp.dccobblemon.datagen;

import net.dandycorp.dccobblemon.block.Blocks;
import net.dandycorp.dccobblemon.item.Items;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Consumer;

public class RecipeProvider extends FabricRecipeProvider {

    private static final List<ItemConvertible> CHROMIUM_SMELTABLES = List.of(Items.CHROMIUM_DUST,Items.RAW_CHROMIUM);
    private static final List<ItemConvertible> CHROMIUM_EQUIPMENT = List.of(Items.CHROMIUM_HELMET,Items.CHROMIUM_CHESTPLATE,
            Items.CHROMIUM_LEGGINGS,Items.CHROMIUM_BOOTS,Items.CHROMIUM_SWORD,Items.CHROMIUM_PICKAXE,Items.CHROMIUM_AXE,
            Items.CHROMIUM_SHOVEL,Items.CHROMIUM_HOE);

    public RecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        offerSmelting(exporter, CHROMIUM_SMELTABLES, RecipeCategory.MISC, Items.CHROMIUM_INGOT,
                2f, 200, "chromium");
        offerBlasting(exporter, CHROMIUM_SMELTABLES, RecipeCategory.MISC, Items.CHROMIUM_INGOT,
                2f, 100, "chromium");

        offerReversibleCompactingRecipesWithCompactingRecipeGroup(exporter, RecipeCategory.BUILDING_BLOCKS, Items.CHROMIUM_INGOT, RecipeCategory.MISC,
                Blocks.CHROMIUM_BLOCK,"chromium_block_from_ingots","chromium");
        offerReversibleCompactingRecipesWithCompactingRecipeGroup(exporter, RecipeCategory.MISC, Items.CHROMIUM_NUGGET, RecipeCategory.MISC,
                Items.CHROMIUM_INGOT,"chromium_ingot_from_nuggets","chromium");
        offerReversibleCompactingRecipesWithCompactingRecipeGroup(exporter, RecipeCategory.MISC, Items.RAW_CHROMIUM, RecipeCategory.MISC,
                Blocks.RAW_CHROMIUM_BLOCK,"raw_chromium_block_from_raw_chromium","chromium");

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, Items.CHROMIUM_CHESTPLATE, 1)
                .pattern("# #")
                .pattern("###")
                .pattern("###")
                .input('#', Items.CHROMIUM_INGOT)
                .criterion(hasItem(Items.CHROMIUM_INGOT), conditionsFromItem(Items.CHROMIUM_INGOT))
                .offerTo(exporter, new Identifier(getRecipeName(Items.CHROMIUM_CHESTPLATE)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, Items.CHROMIUM_LEGGINGS, 1)
                .pattern("###")
                .pattern("# #")
                .pattern("# #")
                .input('#', Items.CHROMIUM_INGOT)
                .criterion(hasItem(Items.CHROMIUM_INGOT), conditionsFromItem(Items.CHROMIUM_INGOT))
                .offerTo(exporter, new Identifier(getRecipeName(Items.CHROMIUM_LEGGINGS)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, Items.CHROMIUM_HELMET, 1)
                .pattern("###")
                .pattern("# #")
                .input('#', Items.CHROMIUM_INGOT)
                .criterion(hasItem(Items.CHROMIUM_INGOT), conditionsFromItem(Items.CHROMIUM_INGOT))
                .offerTo(exporter, new Identifier(getRecipeName(Items.CHROMIUM_HELMET)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, Items.CHROMIUM_BOOTS, 1)
                .pattern("# #")
                .pattern("# #")
                .input('#', Items.CHROMIUM_INGOT)
                .criterion(hasItem(Items.CHROMIUM_INGOT), conditionsFromItem(Items.CHROMIUM_INGOT))
                .offerTo(exporter, new Identifier(getRecipeName(Items.CHROMIUM_BOOTS)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, Items.CHROMIUM_SWORD, 1)
                .pattern("#")
                .pattern("#")
                .pattern("|")
                .input('#', Items.CHROMIUM_INGOT)
                .input('|', net.minecraft.item.Items.STICK)
                .criterion(hasItem(Items.CHROMIUM_INGOT), conditionsFromItem(Items.CHROMIUM_INGOT))
                .offerTo(exporter, new Identifier(getRecipeName(Items.CHROMIUM_SWORD)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, Items.CHROMIUM_PICKAXE, 1)
                .pattern("###")
                .pattern(" | ")
                .pattern(" | ")
                .input('#', Items.CHROMIUM_INGOT)
                .input('|', net.minecraft.item.Items.STICK)
                .criterion(hasItem(Items.CHROMIUM_INGOT), conditionsFromItem(Items.CHROMIUM_INGOT))
                .offerTo(exporter, new Identifier(getRecipeName(Items.CHROMIUM_PICKAXE)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, Items.CHROMIUM_AXE, 1)
                .pattern("##")
                .pattern("#|")
                .pattern(" |")
                .input('#', Items.CHROMIUM_INGOT)
                .input('|', net.minecraft.item.Items.STICK)
                .criterion(hasItem(Items.CHROMIUM_INGOT), conditionsFromItem(Items.CHROMIUM_INGOT))
                .offerTo(exporter, new Identifier(getRecipeName(Items.CHROMIUM_AXE)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, Items.CHROMIUM_SHOVEL, 1)
                .pattern("#")
                .pattern("|")
                .pattern("|")
                .input('#', Items.CHROMIUM_INGOT)
                .input('|', net.minecraft.item.Items.STICK)
                .criterion(hasItem(Items.CHROMIUM_INGOT), conditionsFromItem(Items.CHROMIUM_INGOT))
                .offerTo(exporter, new Identifier(getRecipeName(Items.CHROMIUM_SHOVEL)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, Items.CHROMIUM_HOE, 1)
                .pattern("##")
                .pattern(" |")
                .pattern(" |")
                .input('#', Items.CHROMIUM_INGOT)
                .input('|', net.minecraft.item.Items.STICK)
                .criterion(hasItem(Items.CHROMIUM_INGOT), conditionsFromItem(Items.CHROMIUM_INGOT))
                .offerTo(exporter, new Identifier(getRecipeName(Items.CHROMIUM_HOE)));


        offerSmelting(exporter,CHROMIUM_EQUIPMENT,RecipeCategory.MISC,Items.CHROMIUM_NUGGET,0.2f,200,"chromium");
        offerBlasting(exporter,CHROMIUM_EQUIPMENT,RecipeCategory.MISC,Items.CHROMIUM_NUGGET,0.1f,100,"chromium");
    }
}
