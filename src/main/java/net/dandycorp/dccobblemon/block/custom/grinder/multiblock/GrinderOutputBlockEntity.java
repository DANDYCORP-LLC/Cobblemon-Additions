package net.dandycorp.dccobblemon.block.custom.grinder.multiblock;

import net.dandycorp.dccobblemon.block.custom.grinder.GrinderBlockEntity;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public class GrinderOutputBlockEntity extends MultiblockEntity implements SidedStorageBlockEntity {
    public GrinderOutputBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public @Nullable Storage<ItemVariant> getItemStorage(@Nullable Direction side) {
        if (world == null)
            return null;
        BlockPos mainBlockPos = ((IMultiblockPart) getCachedState().getBlock()).findMainBlock(world, pos, getCachedState());
        if (mainBlockPos == null) {
            return null;
        }
        BlockEntity be = world.getBlockEntity(mainBlockPos);
        if (be instanceof GrinderBlockEntity grinderBE) {
            return grinderBE.getOutputStorage();
        }
        return null;
    }
}
