package net.dandycorp.dccobblemon.datagen;

import net.dandycorp.dccobblemon.item.DANDYCORPItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.CompletableFuture;

public class ItemTagProvider extends FabricTagProvider.ItemTagProvider {


    public ItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(ItemTags.TRIMMABLE_ARMOR)
                .add(
                        DANDYCORPItems.CHROMIUM_CHESTPLATE,
                        DANDYCORPItems.CHROMIUM_HELMET,
                        DANDYCORPItems.CHROMIUM_BOOTS,
                        DANDYCORPItems.CHROMIUM_LEGGINGS
                );
        getOrCreateTagBuilder(ItemTags.SHOVELS).add(DANDYCORPItems.CHROMIUM_SHOVEL);
        getOrCreateTagBuilder(ItemTags.PICKAXES).add(DANDYCORPItems.CHROMIUM_PICKAXE);
        getOrCreateTagBuilder(ItemTags.SWORDS).add(DANDYCORPItems.CHROMIUM_SWORD);
        getOrCreateTagBuilder(ItemTags.AXES).add(DANDYCORPItems.CHROMIUM_AXE);
        getOrCreateTagBuilder(ItemTags.HOES).add(DANDYCORPItems.CHROMIUM_HOE);
    }
}
