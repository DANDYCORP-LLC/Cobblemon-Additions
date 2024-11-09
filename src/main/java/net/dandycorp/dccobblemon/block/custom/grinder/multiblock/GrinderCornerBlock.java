package net.dandycorp.dccobblemon.block.custom.grinder.multiblock;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

import java.util.function.Function;

public class GrinderCornerBlock extends MultiblockPartBlock {

    public GrinderCornerBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState blockState, BlockView blockView, BlockPos blockPos, ShapeContext shapeContext) {
        VoxelShape shape = VoxelShapes.fullCube();
        Direction coreFacing = blockState.get(CORE_FACING);
        Direction facing = blockState.get(FACING);
        MultiblockPosition position = blockState.get(POSITION);

        if (position == MultiblockPosition.LEFT) {
            if (facing == coreFacing) { // Back left corner
                shape = getBackLeftShape(coreFacing);
            } else { // Front left corner
                shape = getFrontLeftShape(coreFacing);
            }
        } else if (position == MultiblockPosition.RIGHT) {
            if (facing == coreFacing) { // Back right corner
                shape = getBackRightShape(coreFacing);
            } else { // Front right corner
                shape = getFrontRightShape(coreFacing);
            }
        }
        return shape;
    }

    private VoxelShape getBackLeftShape(Direction coreFacing) {
        switch (coreFacing) {
            case NORTH:
                return VoxelShapes.union(
                        Block.createCuboidShape(1, 15, 0, 16, 19, 12),
                        Block.createCuboidShape(1, 0, 0, 16, 15, 15)
                );
            case SOUTH:
                return VoxelShapes.union(
                        Block.createCuboidShape(1, 15, 4, 16, 19, 16),
                        Block.createCuboidShape(1, 0, 1, 16, 15, 16)
                );
            case EAST:
                return VoxelShapes.union(
                        Block.createCuboidShape(4, 15, 1, 16, 19, 16),
                        Block.createCuboidShape(1, 0, 1, 16, 15, 16)
                );
            case WEST:
                return VoxelShapes.union(
                        Block.createCuboidShape(0, 15, 0, 12, 19, 15),
                        Block.createCuboidShape(0, 0, 0, 15, 15, 15)
                );
            default:
                return VoxelShapes.fullCube();
        }
    }

    private VoxelShape getFrontLeftShape(Direction coreFacing) {
        switch (coreFacing) {
            case NORTH:
                //System.out.println("creating N-FL shape");
                return VoxelShapes.union(
                        Block.createCuboidShape(1, 15, 4, 16, 19, 16),
                        Block.createCuboidShape(1, 0, 1, 16, 15, 16)
                );
            case SOUTH:
                return VoxelShapes.union(
                        Block.createCuboidShape(1, 15, 0, 16, 19, 12),
                        Block.createCuboidShape(1, 0, 0, 16, 15, 15)
                );
            case EAST:
                return VoxelShapes.union(
                        Block.createCuboidShape(0, 15, 1, 12, 19, 16),
                        Block.createCuboidShape(0, 0, 1, 15, 15, 16)
                );
            case WEST:
                return VoxelShapes.union(
                        Block.createCuboidShape(4, 15, 0, 16, 19, 15),
                        Block.createCuboidShape(1, 0, 0, 16, 15, 15)
                );
            default:
                return VoxelShapes.fullCube();
        }
    }

    private VoxelShape getBackRightShape(Direction coreFacing) {
        switch (coreFacing) {
            case NORTH:
                return VoxelShapes.union(
                        Block.createCuboidShape(0, 15, 0, 15, 19, 12),
                        Block.createCuboidShape(0, 0, 0, 15, 15, 15)
                );
            case SOUTH:
                return VoxelShapes.union(
                        Block.createCuboidShape(0, 15, 4, 15, 19, 16),
                        Block.createCuboidShape(0, 0, 1, 15, 15, 16)
                );
            case EAST:
                return VoxelShapes.union(
                        Block.createCuboidShape(4, 15, 0, 16, 19, 15),
                        Block.createCuboidShape(1, 0, 0, 16, 15, 15)
                );
            case WEST:
                return VoxelShapes.union(
                        Block.createCuboidShape(0, 15, 1, 12, 19, 16),
                        Block.createCuboidShape(0, 0, 1, 15, 15, 16)
                );
            default:
                return VoxelShapes.fullCube();
        }
    }

    private VoxelShape getFrontRightShape(Direction coreFacing) {
        switch (coreFacing) {
            case NORTH:
                //System.out.println("creating N-FR shape");
                return VoxelShapes.union(
                        Block.createCuboidShape(0, 15, 4, 15, 19, 16),
                        Block.createCuboidShape(0, 0, 1, 15, 15, 16)
                );
            case SOUTH:
                return VoxelShapes.union(
                        Block.createCuboidShape(0, 15, 0, 15, 19, 12),
                        Block.createCuboidShape(0, 0, 0, 15, 15, 15)
                );
            case EAST:
                return VoxelShapes.union(
                        Block.createCuboidShape(0, 15, 0, 12, 19, 15),
                        Block.createCuboidShape(0, 0, 0, 15, 15, 15)
                );
            case WEST:
                return VoxelShapes.union(
                        Block.createCuboidShape(4, 15, 1, 16, 19, 16),
                        Block.createCuboidShape(1, 0, 1, 16, 15, 16)
                );
            default:
                return VoxelShapes.fullCube();
        }
    }
}
