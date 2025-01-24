package net.dandycorp.dccobblemon.datagen;

import net.dandycorp.dccobblemon.block.DANDYCORPBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
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
                DANDYCORPBlocks.CHROMIUM_BLOCK,
                DANDYCORPBlocks.RAW_CHROMIUM_BLOCK,
                DANDYCORPBlocks.WALKER_MAGMA,
                DANDYCORPBlocks.VENDOR_BLOCK
        );

        getOrCreateTagBuilder(BlockTags.NEEDS_STONE_TOOL).add(
                DANDYCORPBlocks.CHROMIUM_BLOCK,
                DANDYCORPBlocks.RAW_CHROMIUM_BLOCK
        );
        getOrCreateTagBuilder(BlockTags.NEEDS_IRON_TOOL).add(
                DANDYCORPBlocks.VENDOR_BLOCK
        );
    }
}
