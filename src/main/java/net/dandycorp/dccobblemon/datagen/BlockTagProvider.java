package net.dandycorp.dccobblemon.datagen;

import net.dandycorp.dccobblemon.block.Blocks;
import net.dandycorp.dccobblemon.item.Items;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBlockTags;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class BlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public BlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE).add(
                Blocks.CHROMIUM_BLOCK,
                Blocks.RAW_CHROMIUM_BLOCK,
                Blocks.WALKER_MAGMA
        );

        getOrCreateTagBuilder(BlockTags.NEEDS_STONE_TOOL).add(
                Blocks.CHROMIUM_BLOCK,
                Blocks.RAW_CHROMIUM_BLOCK,
                Blocks.WALKER_MAGMA
        );
        getOrCreateTagBuilder(BlockTags.NEEDS_IRON_TOOL).add(
                Blocks.CHROMIUM_BLOCK,
                Blocks.RAW_CHROMIUM_BLOCK,
                Blocks.WALKER_MAGMA
        );
        getOrCreateTagBuilder(BlockTags.NEEDS_DIAMOND_TOOL).add(
                Blocks.CHROMIUM_BLOCK,
                Blocks.RAW_CHROMIUM_BLOCK,
                Blocks.WALKER_MAGMA
        );
    }
}
