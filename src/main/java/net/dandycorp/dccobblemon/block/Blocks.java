package net.dandycorp.dccobblemon.block;

import net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions;
import net.dandycorp.dccobblemon.block.custom.ChromiumBlock;
import net.dandycorp.dccobblemon.block.custom.VendorBlock;
import net.dandycorp.dccobblemon.block.custom.WalkerMagmaBlock;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

public class Blocks {

    public static final Block WALKER_MAGMA = registerBlock("walker_magma",new WalkerMagmaBlock(FabricBlockSettings.copy(net.minecraft.block.Blocks.MAGMA_BLOCK).ticksRandomly().dropsNothing().solidBlock(net.minecraft.block.Blocks::never)));
    public static final Block CHROMIUM_BLOCK = registerBlock("chromium_block",new ChromiumBlock(FabricBlockSettings.copy(net.minecraft.block.Blocks.IRON_BLOCK)));
    public static final Block RAW_CHROMIUM_BLOCK = registerBlock("raw_chromium_block",new ChromiumBlock(FabricBlockSettings.copy(net.minecraft.block.Blocks.RAW_IRON_BLOCK)));
    public static final Block VENDOR_BLOCK = registerBlock("vendor",new VendorBlock(FabricBlockSettings.copy(net.minecraft.block.Blocks.IRON_DOOR).luminance((state) -> {
        return state.get(Properties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER ? 8 : 0;
    }).nonOpaque()));


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
