package net.dandycorp.dccobblemon.block.custom;

import net.dandycorp.dccobblemon.DANDYCORPSounds;
import net.dandycorp.dccobblemon.block.DANDYCORPBlockEntities;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
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
import net.minecraft.world.WorldView;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

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

    @Override
    public boolean canPlaceAt(BlockState state, WorldView worldView, BlockPos pos) {
        BlockState upState = worldView.getBlockState(pos.up());
        if (upState.isReplaceable()){
            return true;
        }
        else return false;
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
            //world.playSound(null,pos, DANDYCORPCobblemonAdditions.COMPLIMENT_EVENT, SoundCategory.MASTER);
            NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);

            if (screenHandlerFactory != null) {
                player.openHandledScreen(screenHandlerFactory);
                world.playSound(null,pos, DANDYCORPSounds.VENDOR_OPEN_EVENT, SoundCategory.BLOCKS,1.0f,1.0f);
            }
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
        DoubleBlockHalf half = state.get(HALF);
        BlockPos otherHalfPos = half == DoubleBlockHalf.LOWER ? pos.up() : pos.down();
        BlockState otherHalfState = world.getBlockState(otherHalfPos);
        if (otherHalfState.isOf(this) && otherHalfState.contains(HALF)) {
            world.setBlockState(otherHalfPos, Blocks.AIR.getDefaultState(), 35);
        }
        if (!world.isClient && !player.isCreative()) {
            dropStacks(state, world, pos, world.getBlockEntity(pos), player, player.getMainHandStack());
        }
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
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new VendorBlockEntity(pos, state);
    }

    @Override
    public @Nullable ExtendedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof ExtendedScreenHandlerFactory) {
            return (ExtendedScreenHandlerFactory) blockEntity;
        }
        return null;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, DANDYCORPBlockEntities.VENDOR_BLOCK_ENTITY, VendorBlockEntity::tick);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) { // Check if the block is being replaced with a different block
            DoubleBlockHalf half = state.get(HALF);
            BlockPos otherHalfPos = half == DoubleBlockHalf.LOWER ? pos.up() : pos.down(); // Determine the position of the other half
            BlockState otherHalfState = world.getBlockState(otherHalfPos);

            if (otherHalfState.isOf(this)) { // Check if the other half is the same block
                world.setBlockState(otherHalfPos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL); // Remove the other half
            }
        }
        super.onStateReplaced(state, world, pos, newState, moved); // Call the superclass method
    }
}
