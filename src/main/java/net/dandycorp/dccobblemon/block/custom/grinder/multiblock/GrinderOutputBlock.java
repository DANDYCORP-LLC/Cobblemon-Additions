package net.dandycorp.dccobblemon.block.custom.grinder.multiblock;

import com.simibubi.create.foundation.block.IBE;
import net.dandycorp.dccobblemon.block.BlockEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

public class GrinderOutputBlock extends MultiblockPartBlock implements IBE<GrinderOutputBlockEntity> {
    public GrinderOutputBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState blockState, BlockView blockView, BlockPos blockPos, ShapeContext shapeContext) {
        VoxelShape shape = VoxelShapes.fullCube();
        shape = switch (blockState.get(CORE_FACING)) {
            case NORTH -> VoxelShapes.union(
                    Block.createCuboidShape(0, 0, 0, 16, 16, 15),
                    Block.createCuboidShape(0, 14, 15, 16, 16, 16),
                    Block.createCuboidShape(0, 0, 15, 16, 2, 16),
                    Block.createCuboidShape(0, 0, 15, 2, 16, 16),
                    Block.createCuboidShape(14, 0, 15, 16, 16, 16),
                    Block.createCuboidShape(0, 16, 0, 16, 19, 12));
            case SOUTH -> VoxelShapes.union(
                    Block.createCuboidShape(0, 0, 1, 16, 16, 16),
                    Block.createCuboidShape(0, 0, 0, 16, 2, 1),
                    Block.createCuboidShape(0, 14, 0, 16, 16, 1),
                    Block.createCuboidShape(0, 0, 0, 2, 16, 1),
                    Block.createCuboidShape(14, 0, 0, 16, 16, 1),
                    Block.createCuboidShape(0, 16, 4, 16, 19, 16));
            case EAST -> VoxelShapes.union(
                    Block.createCuboidShape(1, 0, 0, 16, 16, 16),
                    Block.createCuboidShape(0, 0, 0, 1, 2, 16),
                    Block.createCuboidShape(0, 14, 0, 1, 16, 16),
                    Block.createCuboidShape(0, 0, 0, 1, 16, 2),
                    Block.createCuboidShape(0, 0, 14, 1, 16, 16),
                    Block.createCuboidShape(4, 16, 0, 16, 19, 16));
            case WEST -> VoxelShapes.union(
                    Block.createCuboidShape(0, 0, 0, 15, 16, 16),
                    Block.createCuboidShape(15, 0, 0, 16, 2, 16),
                    Block.createCuboidShape(15, 14, 0, 16, 16, 16),
                    Block.createCuboidShape(15, 0, 0, 16, 16, 2),
                    Block.createCuboidShape(15, 0, 14, 16, 16, 16),
                    Block.createCuboidShape(0, 16, 0, 12, 19, 16));
            default -> shape;
        };
        return shape;
    }

    @Override
    public Class<GrinderOutputBlockEntity> getBlockEntityClass() {
        return GrinderOutputBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends GrinderOutputBlockEntity> getBlockEntityType() {
        return BlockEntities.GRINDER_OUTPUT_BLOCK_ENTITY.get();
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new GrinderOutputBlockEntity(BlockEntities.GRINDER_OUTPUT_BLOCK_ENTITY.get(),blockPos, blockState);
    }
}