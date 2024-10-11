package net.dandycorp.dccobblemon.block;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions;
import net.dandycorp.dccobblemon.block.custom.grinder.GrinderBlockEntity;
import net.dandycorp.dccobblemon.block.custom.grinder.GrinderBlockInstance;
import net.dandycorp.dccobblemon.block.custom.grinder.GrinderBlockRenderer;
import net.dandycorp.dccobblemon.block.custom.VendorBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions.REGISTRATE;
import static net.dandycorp.dccobblemon.block.Blocks.VENDOR_BLOCK;

public class BlockEntities {

    public static final BlockEntityType<VendorBlockEntity> VENDOR_BLOCK_ENTITY;

    static {
        VENDOR_BLOCK_ENTITY = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier(DANDYCORPCobblemonAdditions.MOD_ID, "vendor_block_entity"),
                BlockEntityType.Builder.create(VendorBlockEntity::new, VENDOR_BLOCK).build(null)
        );
    }

    public static final BlockEntityEntry<GrinderBlockEntity> GRINDER_BLOCK_ENTITY = REGISTRATE
            .blockEntity("grinder", GrinderBlockEntity::new)
            .instance(() -> GrinderBlockInstance::new)
            .renderer(() -> GrinderBlockRenderer::new)
            .validBlocks(Blocks.GRINDER_BLOCK)
            .register();

    public static void registerAllBlockEntities() {
        DANDYCORPCobblemonAdditions.LOGGER.info("Registering Block Entities");
    }
}