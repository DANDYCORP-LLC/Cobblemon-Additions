package net.dandycorp.dccobblemon.block.custom;

import com.google.common.collect.ImmutableMap;
import net.dandycorp.dccobblemon.ui.VendorScreenHandler;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Function;

public class VendorBlock extends BlockWithEntity implements BlockEntityProvider {

    public static final DirectionProperty FACING;
    public static final EnumProperty<DoubleBlockHalf> HALF = Properties.DOUBLE_BLOCK_HALF;



    private static final VoxelShape TOP_NORTH = VoxelShapes.union(
            Block.createCuboidShape(1,-1,7,15,16,16),
            Block.createCuboidShape(1, 11, 6,15,16,16), //curve 1
            Block.createCuboidShape(1, 12, 5,15,16,16) //curve 2
    );
    private static final VoxelShape BOTTOM_NORTH = VoxelShapes.union(
            Block.createCuboidShape(0,12,1,16,15,16), //white part
            Block.createCuboidShape(1,0,3,15,14,16) //bottom part
    );


    private static final VoxelShape TOP_EAST = VoxelShapes.union(
            Block.createCuboidShape(0, -1, 1, 9, 16, 15),
            Block.createCuboidShape(0, 11, 1, 10, 16, 15),
            Block.createCuboidShape(0, 12, 1, 11, 16, 15)
    );
    private static final VoxelShape BOTTOM_EAST = VoxelShapes.union(
            Block.createCuboidShape(0, 12, 0, 15, 15, 16),
            Block.createCuboidShape(0, 0, 1, 13, 14, 15)
    );



    private static final VoxelShape TOP_WEST = VoxelShapes.union(
            Block.createCuboidShape(7, -1, 1, 16, 16, 15),
            Block.createCuboidShape(6, 11, 1, 16, 16, 15),
            Block.createCuboidShape(5, 12, 1, 16, 16, 15)
    );
    private static final VoxelShape BOTTOM_WEST = VoxelShapes.union(
            Block.createCuboidShape(1, 12, 0, 16, 15, 16),
            Block.createCuboidShape(3, 0, 1, 16, 14, 15)
    );



    private static final VoxelShape TOP_SOUTH = VoxelShapes.union(
            Block.createCuboidShape(1, -1, 0, 15, 16, 9),
            Block.createCuboidShape(1, 11, 0, 15, 16, 10),
            Block.createCuboidShape(1, 12, 0, 15, 16, 11)
    );
    private static final VoxelShape BOTTOM_SOUTH = VoxelShapes.union(
            Block.createCuboidShape(0, 12, 0, 16, 15, 15),
            Block.createCuboidShape(1, 0, 0, 15, 14, 13)
    );



    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return (BlockState)state.with(FACING, rotation.rotate((Direction)state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation((Direction)state.get(FACING)));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction facing = ctx.getHorizontalPlayerFacing().getOpposite();
        return this.getDefaultState().with(FACING, facing).with(HALF, DoubleBlockHalf.LOWER);
    }

    static {
        FACING = Properties.HORIZONTAL_FACING;
    }


    public VendorBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(Properties.HORIZONTAL_FACING, Direction.NORTH)

                .with(HALF, DoubleBlockHalf.LOWER));
    }


    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            player.openHandledScreen(new );
        }
        return ActionResult.SUCCESS;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_FACING, HALF);
    }


    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        world.setBlockState(pos.up(), this.getDefaultState()
                .with(HALF, DoubleBlockHalf.UPPER)
                .with(Properties.HORIZONTAL_FACING, state.get(Properties.HORIZONTAL_FACING)), 3);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        // Get the half property of the block being broken (upper or lower half)
        DoubleBlockHalf half = state.get(HALF);

        // Determine the position of the other half of the block
        BlockPos otherHalfPos = half == DoubleBlockHalf.LOWER ? pos.up() : pos.down();

        // Get the BlockState of the other half
        BlockState otherHalfState = world.getBlockState(otherHalfPos);

        // Check if the other half is the same block type and has the HALF property before modifying it
        if (otherHalfState.isOf(this) && otherHalfState.contains(HALF)) {
            // Only set the other half to air without checking its properties further
            world.setBlockState(otherHalfPos, Blocks.AIR.getDefaultState(), 35);
        }

        // Break the current block
        super.onBreak(world, pos, state, player);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        // Return the correct shape for the outline based on block state
        return getShape(state.get(HALF), state.get(FACING));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        // Return the correct shape for the collision box based on block state
        return getShape(state.get(HALF), state.get(FACING));
    }

    private VoxelShape getShape(DoubleBlockHalf half, Direction facing) {
        return switch (half) {
            case UPPER -> switch (facing) {
                case NORTH -> TOP_NORTH;
                case SOUTH -> TOP_SOUTH;
                case EAST -> TOP_EAST;
                case WEST -> TOP_WEST;
                default -> TOP_NORTH;
            };
            case LOWER -> switch (facing) {
                case NORTH -> BOTTOM_NORTH;
                case SOUTH -> BOTTOM_SOUTH;
                case EAST -> BOTTOM_EAST;
                case WEST -> BOTTOM_WEST;
                default -> BOTTOM_NORTH;
            };
        };
    }


    @Override
    public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
        world.createExplosion(null,pos.getX(),pos.getY(),pos.getZ(),8, World.ExplosionSourceType.BLOCK);
    }


    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new VendorBlockEntity(pos,state);
    }
}
