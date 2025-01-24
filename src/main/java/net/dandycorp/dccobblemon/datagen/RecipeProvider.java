package net.dandycorp.dccobblemon.datagen;

import net.dandycorp.dccobblemon.block.DANDYCORPBlocks;
import net.dandycorp.dccobblemon.item.DANDYCORPItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Consumer;

public class RecipeProvider extends FabricRecipeProvider {

    private static final List<ItemConvertible> CHROMIUM_SMELTABLES = List.of(DANDYCORPItems.CHROMIUM_DUST, DANDYCORPItems.RAW_CHROMIUM);
    private static final List<ItemConvertible> CHROMIUM_EQUIPMENT = List.of(DANDYCORPItems.CHROMIUM_HELMET, DANDYCORPItems.CHROMIUM_CHESTPLATE,
            DANDYCORPItems.CHROMIUM_LEGGINGS, DANDYCORPItems.CHROMIUM_BOOTS, DANDYCORPItems.CHROMIUM_SWORD, DANDYCORPItems.CHROMIUM_PICKAXE, DANDYCORPItems.CHROMIUM_AXE,
            DANDYCORPItems.CHROMIUM_SHOVEL, DANDYCORPItems.CHROMIUM_HOE);

    public RecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        offerSmelting(exporter, CHROMIUM_SMELTABLES, RecipeCategory.MISC, DANDYCORPItems.CHROMIUM_INGOT,
                2f, 200, "chromium");
        offerBlasting(exporter, CHROMIUM_SMELTABLES, RecipeCategory.MISC, DANDYCORPItems.CHROMIUM_INGOT,
                2f, 100, "chromium");

        offerReversibleCompactingRecipesWithCompactingRecipeGroup(exporter, RecipeCategory.BUILDING_BLOCKS, DANDYCORPItems.CHROMIUM_INGOT, RecipeCategory.MISC,
                DANDYCORPBlocks.CHROMIUM_BLOCK,"chromium_block_from_ingots","chromium");
        offerReversibleCompactingRecipesWithCompactingRecipeGroup(exporter, RecipeCategory.MISC, DANDYCORPItems.CHROMIUM_NUGGET, RecipeCategory.MISC,
                DANDYCORPItems.CHROMIUM_INGOT,"chromium_ingot_from_nuggets","chromium");
        offerReversibleCompactingRecipesWithCompactingRecipeGroup(exporter, RecipeCategory.MISC, DANDYCORPItems.RAW_CHROMIUM, RecipeCategory.MISC,
                DANDYCORPBlocks.RAW_CHROMIUM_BLOCK,"raw_chromium_block_from_raw_chromium","chromium");

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, DANDYCORPItems.CHROMIUM_CHESTPLATE, 1)
                .pattern("# #")
                .pattern("###")
                .pattern("###")
                .input('#', DANDYCORPItems.CHROMIUM_INGOT)
                .criterion(hasItem(DANDYCORPItems.CHROMIUM_INGOT), conditionsFromItem(DANDYCORPItems.CHROMIUM_INGOT))
                .offerTo(exporter, new Identifier(getRecipeName(DANDYCORPItems.CHROMIUM_CHESTPLATE)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, DANDYCORPItems.CHROMIUM_LEGGINGS, 1)
                .pattern("###")
                .pattern("# #")
                .pattern("# #")
                .input('#', DANDYCORPItems.CHROMIUM_INGOT)
                .criterion(hasItem(DANDYCORPItems.CHROMIUM_INGOT), conditionsFromItem(DANDYCORPItems.CHROMIUM_INGOT))
                .offerTo(exporter, new Identifier(getRecipeName(DANDYCORPItems.CHROMIUM_LEGGINGS)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, DANDYCORPItems.CHROMIUM_HELMET, 1)
                .pattern("###")
                .pattern("# #")
                .input('#', DANDYCORPItems.CHROMIUM_INGOT)
                .criterion(hasItem(DANDYCORPItems.CHROMIUM_INGOT), conditionsFromItem(DANDYCORPItems.CHROMIUM_INGOT))
                .offerTo(exporter, new Identifier(getRecipeName(DANDYCORPItems.CHROMIUM_HELMET)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, DANDYCORPItems.CHROMIUM_BOOTS, 1)
                .pattern("# #")
                .pattern("# #")
                .input('#', DANDYCORPItems.CHROMIUM_INGOT)
                .criterion(hasItem(DANDYCORPItems.CHROMIUM_INGOT), conditionsFromItem(DANDYCORPItems.CHROMIUM_INGOT))
                .offerTo(exporter, new Identifier(getRecipeName(DANDYCORPItems.CHROMIUM_BOOTS)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, DANDYCORPItems.CHROMIUM_SWORD, 1)
                .pattern("#")
                .pattern("#")
                .pattern("|")
                .input('#', DANDYCORPItems.CHROMIUM_INGOT)
                .input('|', net.minecraft.item.Items.STICK)
                .criterion(hasItem(DANDYCORPItems.CHROMIUM_INGOT), conditionsFromItem(DANDYCORPItems.CHROMIUM_INGOT))
                .offerTo(exporter, new Identifier(getRecipeName(DANDYCORPItems.CHROMIUM_SWORD)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, DANDYCORPItems.CHROMIUM_PICKAXE, 1)
                .pattern("###")
                .pattern(" | ")
                .pattern(" | ")
                .input('#', DANDYCORPItems.CHROMIUM_INGOT)
                .input('|', net.minecraft.item.Items.STICK)
                .criterion(hasItem(DANDYCORPItems.CHROMIUM_INGOT), conditionsFromItem(DANDYCORPItems.CHROMIUM_INGOT))
                .offerTo(exporter, new Identifier(getRecipeName(DANDYCORPItems.CHROMIUM_PICKAXE)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, DANDYCORPItems.CHROMIUM_AXE, 1)
                .pattern("##")
                .pattern("#|")
                .pattern(" |")
                .input('#', DANDYCORPItems.CHROMIUM_INGOT)
                .input('|', net.minecraft.item.Items.STICK)
                .criterion(hasItem(DANDYCORPItems.CHROMIUM_INGOT), conditionsFromItem(DANDYCORPItems.CHROMIUM_INGOT))
                .offerTo(exporter, new Identifier(getRecipeName(DANDYCORPItems.CHROMIUM_AXE)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, DANDYCORPItems.CHROMIUM_SHOVEL, 1)
                .pattern("#")
                .pattern("|")
                .pattern("|")
                .input('#', DANDYCORPItems.CHROMIUM_INGOT)
                .input('|', net.minecraft.item.Items.STICK)
                .criterion(hasItem(DANDYCORPItems.CHROMIUM_INGOT), conditionsFromItem(DANDYCORPItems.CHROMIUM_INGOT))
                .offerTo(exporter, new Identifier(getRecipeName(DANDYCORPItems.CHROMIUM_SHOVEL)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, DANDYCORPItems.CHROMIUM_HOE, 1)
                .pattern("##")
                .pattern(" |")
                .pattern(" |")
                .input('#', DANDYCORPItems.CHROMIUM_INGOT)
                .input('|', net.minecraft.item.Items.STICK)
                .criterion(hasItem(DANDYCORPItems.CHROMIUM_INGOT), conditionsFromItem(DANDYCORPItems.CHROMIUM_INGOT))
                .offerTo(exporter, new Identifier(getRecipeName(DANDYCORPItems.CHROMIUM_HOE)));


        offerSmelting(exporter,CHROMIUM_EQUIPMENT,RecipeCategory.MISC, DANDYCORPItems.CHROMIUM_NUGGET,0.2f,200,"chromium");
        offerBlasting(exporter,CHROMIUM_EQUIPMENT,RecipeCategory.MISC, DANDYCORPItems.CHROMIUM_NUGGET,0.1f,100,"chromium");
    }
}
