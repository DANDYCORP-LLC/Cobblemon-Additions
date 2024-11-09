package net.dandycorp.dccobblemon.block.custom.grinder.multiblock;

import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public class MultiblockEntity extends BlockEntity implements IHaveGoggleInformation {

    public MultiblockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

}
