package net.dandycorp.dccobblemon.block.custom.grinder.multiblock;

import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.dandycorp.dccobblemon.block.custom.grinder.GrinderBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class GrinderRotationalBlockEntity extends KineticBlockEntity implements IHaveGoggleInformation {
    private BlockPos mainBlockPos;

    public GrinderRotationalBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public boolean addToGoggleTooltip(List<Text> tooltip, boolean isPlayerSneaking) {
        if (world == null) return false;

        // Find the main block entity
        BlockPos mainBlockPos = ((IMultiblockPart) getCachedState().getBlock()).findMainBlock(world, pos, getCachedState());
        if (mainBlockPos == null) return false;

        BlockEntity be = world.getBlockEntity(mainBlockPos);
        if (be instanceof GrinderBlockEntity grinderBE) {
            // Delegate the tooltip generation to the main GrinderBlockEntity
            return grinderBE.addToGoggleTooltip(tooltip, isPlayerSneaking);
        }

        return false;
    }

    public void setMainBlockPos(BlockPos mainBlockPos) {
        this.mainBlockPos = mainBlockPos;
    }

    public BlockPos getMainBlockPos() {
        return mainBlockPos;
    }
}

