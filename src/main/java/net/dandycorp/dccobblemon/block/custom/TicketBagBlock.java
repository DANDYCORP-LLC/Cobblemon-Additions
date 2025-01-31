package net.dandycorp.dccobblemon.block.custom;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class TicketBagBlock extends FallingBlock {


    public static final Property<Direction> FACING = Properties.HORIZONTAL_FACING;

    public TicketBagBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        super.appendProperties(builder);
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext context) {
        Direction[] directions = context.getPlacementDirections();
        BlockState blockState = this.getDefaultState();
        WorldView worldView = context.getWorld();
        BlockPos blockPos = context.getBlockPos();

        for (Direction direction : directions) {
            if (direction.getAxis().isHorizontal()) {
                Direction direction2 = direction.getOpposite();
                blockState = blockState.with(FACING, direction2);
                if (blockState.canPlaceAt(worldView, blockPos)) {
                    return blockState;
                }
            }
        }

        return blockState.with(FACING, context.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockView blockView, BlockPos blockPos, ShapeContext shapeContext) {
        return Block.createCuboidShape(2.5,0,2.5,13.5,10,13.5);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState blockState, BlockView blockView, BlockPos blockPos, ShapeContext shapeContext) {
        return VoxelShapes.union(
                Block.createCuboidShape(2.5,0,2.5,13.5,5,13.5),
                Block.createCuboidShape(3.5, 5, 3.5,12.5,10,12.5)
        );
    }

    @Override
    public BlockState rotate(BlockState blockState, BlockRotation blockRotation) {
        return blockState.with(FACING, blockRotation.rotate(blockState.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState, BlockMirror blockMirror) {
        return blockState.rotate(blockMirror.getRotation(blockState.get(FACING)));
    }

    @Override
    public boolean canPlaceAt(BlockState blockState, WorldView worldView, BlockPos blockPos) {
        return sideCoversSmallSquare(worldView, blockPos.down(), Direction.UP) || !worldView.getBlockState(blockPos.offset(blockState.get(FACING).getOpposite())).isAir();
    }

    @Override
    protected void configureFallingBlockEntity(FallingBlockEntity fallingBlockEntity) {
        super.configureFallingBlockEntity(fallingBlockEntity);
        fallingBlockEntity.setHurtEntities(2.0F, 40);
    }
}
