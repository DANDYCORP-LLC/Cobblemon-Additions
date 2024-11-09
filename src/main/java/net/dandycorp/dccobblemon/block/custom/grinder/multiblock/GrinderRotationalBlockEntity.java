package net.dandycorp.dccobblemon.block.custom.grinder.multiblock;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public class GrinderRotationalBlockEntity extends KineticBlockEntity {
    private BlockPos mainBlockPos;

    public GrinderRotationalBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    public void setMainBlockPos(BlockPos mainBlockPos) {
        this.mainBlockPos = mainBlockPos;
    }

    public BlockPos getMainBlockPos() {
        return mainBlockPos;
    }
}

