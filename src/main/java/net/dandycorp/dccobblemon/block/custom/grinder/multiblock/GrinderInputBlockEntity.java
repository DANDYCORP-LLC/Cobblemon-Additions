package net.dandycorp.dccobblemon.block.custom.grinder.multiblock;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public class GrinderInputBlockEntity extends MultiblockEntity{
    public GrinderInputBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }
}
