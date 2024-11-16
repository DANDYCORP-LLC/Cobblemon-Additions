package net.dandycorp.dccobblemon.block.custom.grinder.multiblock;

import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.dandycorp.dccobblemon.block.BlockEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GrinderInputBlock extends MultiblockPartBlock implements IBE<GrinderInputBlockEntity> {

    public static final EnumProperty<DoubleBlockHalf> HALF = Properties.DOUBLE_BLOCK_HALF;

    public GrinderInputBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(HALF);
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext itemPlacementContext) {
        return this.getDefaultState().with(HALF, DoubleBlockHalf.LOWER);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState blockState, BlockView blockView, BlockPos blockPos, ShapeContext shapeContext) {
        VoxelShape shape = VoxelShapes.fullCube();
        if (blockState.get(HALF) == DoubleBlockHalf.LOWER) {
            shape = getLowerShape(blockState);
        } else if (blockState.get(HALF) == DoubleBlockHalf.UPPER) {
            shape = getUpperShape(blockState);
        }
        return shape;
    }

    private VoxelShape getLowerShape(BlockState blockState) {
        VoxelShape shape = VoxelShapes.fullCube();
        shape = switch (blockState.get(CORE_FACING)) {
            case NORTH -> VoxelShapes.union(
                    Block.createCuboidShape(0, 0, 1, 16, 16, 16),
                    Block.createCuboidShape(0, 0, 0, 16, 2, 1),
                    Block.createCuboidShape(0, 14, 0, 16, 16, 1),
                    Block.createCuboidShape(0, 0, 0, 2, 16, 1),
                    Block.createCuboidShape(14, 0, 0, 16, 16, 1),
                    Block.createCuboidShape(0, 16, 4, 16, 19, 16));
            case SOUTH -> VoxelShapes.union(
                    Block.createCuboidShape(0, 0, 0, 16, 16, 15),
                    Block.createCuboidShape(0, 14, 15, 16, 16, 16),
                    Block.createCuboidShape(0, 0, 15, 16, 2, 16),
                    Block.createCuboidShape(0, 0, 15, 2, 16, 16),
                    Block.createCuboidShape(14, 0, 15, 16, 16, 16),
                    Block.createCuboidShape(0, 16, 0, 16, 19, 12));
            case EAST -> VoxelShapes.union(
                    Block.createCuboidShape(0, 0, 0, 15, 16, 16),
                    Block.createCuboidShape(15, 0, 0, 16, 2, 16),
                    Block.createCuboidShape(15, 14, 0, 16, 16, 16),
                    Block.createCuboidShape(15, 0, 0, 16, 16, 2),
                    Block.createCuboidShape(15, 0, 14, 16, 16, 16),
                    Block.createCuboidShape(0, 16, 0, 12, 19, 16));
            case WEST -> VoxelShapes.union(
                    Block.createCuboidShape(1, 0, 0, 16, 16, 16),
                    Block.createCuboidShape(0, 0, 0, 1, 2, 16),
                    Block.createCuboidShape(0, 14, 0, 1, 16, 16),
                    Block.createCuboidShape(0, 0, 0, 1, 16, 2),
                    Block.createCuboidShape(0, 0, 14, 1, 16, 16),
                    Block.createCuboidShape(4, 16, 0, 16, 19, 16));
            default -> shape;
        };
        return shape;
    }

    private VoxelShape getUpperShape(BlockState blockState) {
        VoxelShape shape = VoxelShapes.fullCube();

        if(blockState.get(POSITION) == MultiblockPosition.LEFT){
            shape = switch (blockState.get(CORE_FACING)) {
                case NORTH, SOUTH -> Block.createCuboidShape(1, 0, -7, 16, 8, 23);
                case EAST -> Block.createCuboidShape(-7, 0, 1, 23, 8, 16);
                case WEST -> Block.createCuboidShape(-7, 0, 0, 23, 8, 15);
                default -> shape;
            };
        }
        else if(blockState.get(POSITION) == MultiblockPosition.RIGHT){
            shape = switch (blockState.get(CORE_FACING)) {
                case NORTH, SOUTH -> Block.createCuboidShape(0, 0, -7, 15, 8, 23);
                case EAST -> Block.createCuboidShape(-7, 0, 0, 23, 8, 15);
                case WEST -> Block.createCuboidShape(-7, 0, 1, 23, 8, 16);
                default -> shape;
            };
        }
        else {
            shape = switch (blockState.get(CORE_FACING)) {
                case NORTH, SOUTH -> Block.createCuboidShape(0, 0, -7, 16, 8, 23);
                case EAST, WEST -> Block.createCuboidShape(-7, 0, 0, 23, 8, 16);
                default -> shape;
            };
        }
        return shape;
    }

    @Override
    public Class<GrinderInputBlockEntity> getBlockEntityClass() {
        return GrinderInputBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends GrinderInputBlockEntity> getBlockEntityType() {
        return BlockEntities.GRINDER_INPUT_BLOCK_ENTITY.get();
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new GrinderInputBlockEntity(BlockEntities.GRINDER_INPUT_BLOCK_ENTITY.get(),blockPos, blockState);
    }
}
