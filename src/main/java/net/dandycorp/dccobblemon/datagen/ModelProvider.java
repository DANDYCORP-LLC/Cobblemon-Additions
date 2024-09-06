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
        blockStateModelGenerator.registerSimpleCubeAll(Blocks.CHROMIUM_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(Blocks.RAW_CHROMIUM_BLOCK);
        blockStateModelGenerator.registerAxisRotated(Blocks.VENDOR_BLOCK,new Identifier("vendor"));
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(Items.BUG_BADGE, Models.GENERATED);
        itemModelGenerator.register(Items.DANDY_BADGE, Models.GENERATED);
        itemModelGenerator.register(Items.DARK_BADGE, Models.GENERATED);
        itemModelGenerator.register(Items.DRAGON_BADGE, Models.GENERATED);
        itemModelGenerator.register(Items.FIRE_BADGE, Models.GENERATED);
        itemModelGenerator.register(Items.FLYING_BADGE, Models.GENERATED);
        itemModelGenerator.register(Items.GROUND_BADGE, Models.GENERATED);
        itemModelGenerator.register(Items.ICE_BADGE, Models.GENERATED);
        itemModelGenerator.register(Items.JEFF_BADGE, Models.GENERATED);
        itemModelGenerator.register(Items.LINA_BADGE, Models.GENERATED);
        itemModelGenerator.register(Items.NATE_BADGE, Models.GENERATED);
        itemModelGenerator.register(Items.POISON_BADGE, Models.GENERATED);
        itemModelGenerator.register(Items.PUMPKIN_BADGE, Models.GENERATED);
        itemModelGenerator.register(Items.ROCK_BADGE, Models.GENERATED);
        itemModelGenerator.register(Items.SHELLY_BADGE, Models.GENERATED);
        itemModelGenerator.register(Items.WATER_BADGE, Models.GENERATED);

        itemModelGenerator.register(Items.TICKET, Models.GENERATED);
        itemModelGenerator.register(Items.CHROMIUM_INGOT, Models.GENERATED);
        itemModelGenerator.register(Items.CHROMIUM_NUGGET, Models.GENERATED);
        itemModelGenerator.register(Items.CHROMIUM_SWORD, Models.HANDHELD);
        itemModelGenerator.register(Items.CHROMIUM_PICKAXE, Models.HANDHELD);
        itemModelGenerator.register(Items.CHROMIUM_AXE, Models.HANDHELD);
        itemModelGenerator.register(Items.CHROMIUM_SHOVEL, Models.HANDHELD);
        itemModelGenerator.register(Items.CHROMIUM_HOE, Models.HANDHELD);
        itemModelGenerator.register(Items.CHROMIUM_DUST, Models.GENERATED);
        itemModelGenerator.register(Items.RAW_CHROMIUM, Models.GENERATED);

        itemModelGenerator.registerArmor((ArmorItem) Items.CHROMIUM_HELMET);
        itemModelGenerator.registerArmor((ArmorItem) Items.CHROMIUM_CHESTPLATE);
        itemModelGenerator.registerArmor((ArmorItem) Items.CHROMIUM_LEGGINGS);
        itemModelGenerator.registerArmor((ArmorItem) Items.CHROMIUM_BOOTS);

    }
}
