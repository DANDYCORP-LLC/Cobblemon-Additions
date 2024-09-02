package net.dandycorp.dccobblemon.block;

import net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions;
import net.dandycorp.dccobblemon.block.custom.WalkerMagmaBlock;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class Blocks {

    public static final Block WALKER_MAGMA = registerBlock("walker_magma",new WalkerMagmaBlock(FabricBlockSettings.copy(net.minecraft.block.Blocks.MAGMA_BLOCK).ticksRandomly()));

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(DANDYCORPCobblemonAdditions.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, new Identifier(DANDYCORPCobblemonAdditions.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));
    }

    public static void registerAllBlocks() {
        DANDYCORPCobblemonAdditions.LOGGER.info("Registering Blocks");
    }

}
