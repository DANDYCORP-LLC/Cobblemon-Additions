package net.dandycorp.dccobblemon.datagen;

import net.dandycorp.dccobblemon.block.Blocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;

public class LootTableProvider extends FabricBlockLootTableProvider {
    public LootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        addDrop(Blocks.CHROMIUM_BLOCK);
        addDrop(Blocks.RAW_CHROMIUM_BLOCK);
    }
}
