package net.dandycorp.dccobblemon.datagen;

import net.dandycorp.dccobblemon.block.Blocks;
import net.dandycorp.dccobblemon.item.Items;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.item.ArmorItem;
import net.minecraft.util.Identifier;

public class ModelProvider extends FabricModelProvider {
    public ModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.registerArmor((ArmorItem) Items.PARAGONIUM_HELMET);
        itemModelGenerator.registerArmor((ArmorItem) Items.PARAGONIUM_CHESTPLATE);
        itemModelGenerator.registerArmor((ArmorItem) Items.PARAGONIUM_LEGGINGS);
        itemModelGenerator.registerArmor((ArmorItem) Items.PARAGONIUM_BOOTS);
    }
}
