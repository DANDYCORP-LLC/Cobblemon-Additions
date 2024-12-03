package net.dandycorp.dccobblemon.block;

import com.simibubi.create.AllTags;
import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions;
import net.dandycorp.dccobblemon.block.custom.ChromiumBlock;
import net.dandycorp.dccobblemon.block.custom.grinder.GrinderBlock;
import net.dandycorp.dccobblemon.block.custom.VendorBlock;
import net.dandycorp.dccobblemon.block.custom.WalkerMagmaBlock;
import net.dandycorp.dccobblemon.block.custom.grinder.multiblock.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;
import static net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions.REGISTRATE;

public class Blocks {

    public static final Block WALKER_MAGMA = registerBlock("walker_magma",new WalkerMagmaBlock(FabricBlockSettings.copy(net.minecraft.block.Blocks.MAGMA_BLOCK).ticksRandomly().dropsNothing().solidBlock(net.minecraft.block.Blocks::never)),true);
    public static final Block CHROMIUM_BLOCK = registerBlock("chromium_block",new ChromiumBlock(FabricBlockSettings.copy(net.minecraft.block.Blocks.IRON_BLOCK)),true);
    public static final Block RAW_CHROMIUM_BLOCK = registerBlock("raw_chromium_block",new ChromiumBlock(FabricBlockSettings.copy(net.minecraft.block.Blocks.RAW_IRON_BLOCK)),true);
    public static final Block VENDOR_BLOCK = registerBlock("vendor",new VendorBlock(FabricBlockSettings.copy(net.minecraft.block.Blocks.IRON_DOOR).pistonBehavior(PistonBehavior.DESTROY).luminance((state) -> {
        return state.get(Properties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER ? 8 : 0;
    }).nonOpaque()),false);

    public static final BlockEntry<GrinderBlock> GRINDER_BLOCK = REGISTRATE
            .block("grinder", GrinderBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .properties(p -> p
                    .mapColor(MapColor.LIGHT_GRAY)
                    .allowsSpawning(net.minecraft.block.Blocks::never)
                    .strength(30f)
                    .requiresTool()
                    .resistance(3600000f)
                    .nonOpaque()
                    .pistonBehavior(PistonBehavior.BLOCK)
                    .sounds(BlockSoundGroup.COPPER))
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .tag(AllTags.AllBlockTags.NON_MOVABLE.tag)
            .transform(pickaxeOnly())
            .transform(BlockStressDefaults.setImpact(16.0))
            .item()
                .properties(p -> p.rarity(Rarity.UNCOMMON))
                .transform(b -> b.model((c, p) -> {
                            p.withExistingParent("grinder",
                                    p.modLoc("block/grinder/full_grinder"));
                        }))
                .build()
            .register();

    public static final BlockEntry<GrinderInputBlock> GRINDER_INPUT_BLOCK = REGISTRATE
            .block("grinder_input", GrinderInputBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .properties(p -> p
                    .mapColor(MapColor.LIGHT_GRAY)
                    .nonOpaque()
                    .strength(30f)
                    .requiresTool()
                    .resistance(3600000f)
                    .pistonBehavior(PistonBehavior.BLOCK)
                    .allowsSpawning(net.minecraft.block.Blocks::never)
                    .solidBlock(net.minecraft.block.Blocks::never)
                    .suffocates(net.minecraft.block.Blocks::never)
                    .blockVision(net.minecraft.block.Blocks::never)
                    .sounds(BlockSoundGroup.COPPER))
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .tag(AllTags.AllBlockTags.NON_MOVABLE.tag)
            .transform(pickaxeOnly())
            .register();

    public static final BlockEntry<GrinderOutputBlock> GRINDER_OUTPUT_BLOCK = REGISTRATE
            .block("grinder_output", GrinderOutputBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .properties(p -> p
                    .mapColor(MapColor.LIGHT_GRAY)
                    .nonOpaque()
                    .strength(30f)
                    .requiresTool()
                    .resistance(3600000f)
                    .pistonBehavior(PistonBehavior.BLOCK)
                    .allowsSpawning(net.minecraft.block.Blocks::never)
                    .solidBlock(net.minecraft.block.Blocks::never)
                    .suffocates(net.minecraft.block.Blocks::never)
                    .blockVision(net.minecraft.block.Blocks::never)
                    .sounds(BlockSoundGroup.COPPER))
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .tag(AllTags.AllBlockTags.NON_MOVABLE.tag)
            .transform(pickaxeOnly())
            .register();

    public static final BlockEntry<GrinderRotationalBlock> GRINDER_ROTATIONAL_BLOCK = REGISTRATE
            .block("grinder_rotational", GrinderRotationalBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .properties(p -> p
                    .mapColor(MapColor.LIGHT_GRAY)
                    .nonOpaque()
                    .strength(30f)
                    .requiresTool()
                    .resistance(3600000f)
                    .pistonBehavior(PistonBehavior.BLOCK)
                    .allowsSpawning(net.minecraft.block.Blocks::never)
                    .solidBlock(net.minecraft.block.Blocks::never)
                    .suffocates(net.minecraft.block.Blocks::never)
                    .blockVision(net.minecraft.block.Blocks::never)
                    .sounds(BlockSoundGroup.COPPER))
            .transform(BlockStressDefaults.setImpact(0.0))
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .tag(AllTags.AllBlockTags.NON_MOVABLE.tag)
            .transform(pickaxeOnly())
            .register();

    public static final BlockEntry<GrinderCornerBlock> GRINDER_CORNER_BLOCK = REGISTRATE
            .block("grinder_corner", GrinderCornerBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .properties(p -> p
                    .mapColor(MapColor.LIGHT_GRAY)
                    .nonOpaque()
                    .strength(30f)
                    .requiresTool()
                    .resistance(3600000f)
                    .pistonBehavior(PistonBehavior.BLOCK)
                    .allowsSpawning(net.minecraft.block.Blocks::never)
                    .solidBlock(net.minecraft.block.Blocks::never)
                    .suffocates(net.minecraft.block.Blocks::never)
                    .blockVision(net.minecraft.block.Blocks::never)
                    .sounds(BlockSoundGroup.COPPER))
            .transform(BlockStressDefaults.setImpact(0.0))
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .tag(AllTags.AllBlockTags.NON_MOVABLE.tag)
            .transform(pickaxeOnly())
            .register();

    private static Block registerBlock(String name, Block block, boolean createItem) {
        if(createItem) registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(DANDYCORPCobblemonAdditions.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, new Identifier(DANDYCORPCobblemonAdditions.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));
    }

    public static void registerAllBlocks() {
        DANDYCORPCobblemonAdditions.LOGGER.info("Registering Blocks");
    }

}
