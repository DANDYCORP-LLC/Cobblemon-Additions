package net.dandycorp.dccobblemon.datagen;

import net.dandycorp.dccobblemon.item.Items;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ItemTagProvider extends FabricTagProvider.ItemTagProvider {


    public ItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(ItemTags.TRIMMABLE_ARMOR)
                .add(
                        Items.CHROMIUM_CHESTPLATE,
                        Items.CHROMIUM_HELMET,
                        Items.CHROMIUM_BOOTS,
                        Items.CHROMIUM_LEGGINGS
                );
        getOrCreateTagBuilder(ItemTags.SHOVELS).add(Items.CHROMIUM_SHOVEL);
        getOrCreateTagBuilder(ItemTags.PICKAXES).add(Items.CHROMIUM_PICKAXE);
        getOrCreateTagBuilder(ItemTags.SWORDS).add(Items.CHROMIUM_SWORD);
        getOrCreateTagBuilder(ItemTags.AXES).add(Items.CHROMIUM_AXE);
        getOrCreateTagBuilder(ItemTags.HOES).add(Items.CHROMIUM_HOE);
    }
}
