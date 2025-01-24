package net.dandycorp.dccobblemon.block.custom.grinder.multiblock;

import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.dandycorp.dccobblemon.block.DANDYCORPBlockEntities;
import net.dandycorp.dccobblemon.block.DANDYCORPBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class GrinderRotationalBlock extends DirectionalKineticBlock implements IBE<GrinderRotationalBlockEntity>, IMultiblockPart {
    public static final DirectionProperty FACING = DirectionProperty.of("facing", Direction.values());
    public static final DirectionProperty CORE_FACING = DirectionProperty.of("core_facing",Direction.values());
    public static final EnumProperty<MultiblockPosition> POSITION = EnumProperty.of("position", MultiblockPosition.class);

    public GrinderRotationalBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(CORE_FACING, Direction.NORTH)
                .with(POSITION, MultiblockPosition.MIDDLE));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(CORE_FACING);
        builder.add(POSITION);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.get(FACING).getAxis();
    }

    @Override
    public boolean hasShaftTowards(WorldView world, BlockPos pos, BlockState state, Direction face) {
        Direction facing = state.get(FACING);
        return (face == facing || face == facing.getOpposite());
    }

    @Override
    public Class<GrinderRotationalBlockEntity> getBlockEntityClass() {
        return GrinderRotationalBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends GrinderRotationalBlockEntity> getBlockEntityType() {
        return DANDYCORPBlockEntities.GRINDER_ROTATIONAL_BLOCK_ENTITY.get();
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new GrinderRotationalBlockEntity(DANDYCORPBlockEntities.GRINDER_ROTATIONAL_BLOCK_ENTITY.get(),blockPos, blockState);
    }

    @Override
    public float getAmbientOcclusionLightLevel(BlockState blockState, BlockView blockView, BlockPos blockPos) {
        return 1.0F;
    }

    @Override
    public boolean isTransparent(BlockState blockState, BlockView blockView, BlockPos blockPos) {
        return true;
    }

    @Override
    public Direction getMainBlockDirection(BlockState state) {
        return state.get(FACING);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            BlockPos mainBlockPos = findMainBlock(world, pos, state);
            if (mainBlockPos != null) {
                BlockState mainState = world.getBlockState(mainBlockPos);
                System.out.println("middle found!");
                return mainState.getBlock().onUse(mainState, world, mainBlockPos, player, hand, hit);
            }
        }
        return ActionResult.PASS;
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBreak(world, pos, state, player);
        if (!world.isClient) {
            BlockPos mainBlockPos = findMainBlock(world, pos, state);
            if (mainBlockPos != null) {
                BlockState mainState = world.getBlockState(mainBlockPos);
                mainState.getBlock().onBreak(world, mainBlockPos, mainState, player);
            }
        }
    }

    @Override
    public VoxelShape getOutlineShape(BlockState blockState, BlockView blockView, BlockPos blockPos, ShapeContext shapeContext) {
        VoxelShape shape = super.getOutlineShape(blockState, blockView, blockPos, shapeContext);
        if (blockState.get(POSITION) == MultiblockPosition.LEFT) {
            shape = switch (blockState.get(CORE_FACING)) {
                case NORTH, SOUTH -> VoxelShapes.union(
                        Block.createCuboidShape(1, 0, -1, 16, 18, 17),
                        Block.createCuboidShape(0, 0, -1, 1, 2, 17),
                        Block.createCuboidShape(0, 16, -1, 1, 18, 17),
                        Block.createCuboidShape(0,0,-1,1,18,1),
                        Block.createCuboidShape(0,0,15,1,18,17),
                        Block.createCuboidShape(0,6,6,1,10,10));
                case EAST -> VoxelShapes.union(
                        Block.createCuboidShape(-1, 0, 1, 17, 18, 16),
                        Block.createCuboidShape(-1, 0, 0, 17, 2, 1),
                        Block.createCuboidShape(-1, 16, 0, 17, 18, 1),
                        Block.createCuboidShape(-1, 0, 0, 1, 18, 1),
                        Block.createCuboidShape(15, 0, 0, 17, 18, 1),
                        Block.createCuboidShape(6,6,0,10,10,1));
                case WEST -> VoxelShapes.union(
                        Block.createCuboidShape(-1, 0, 0, 17, 18, 15),
                        Block.createCuboidShape(-1, 0, 15, 17, 2, 16),
                        Block.createCuboidShape(-1, 16, 15, 17, 18, 16),
                        Block.createCuboidShape(-1, 0, 15, 1, 18, 16),
                        Block.createCuboidShape(15, 0, 15, 17, 18, 16),
                        Block.createCuboidShape(6,6,15,10,10,16));
                default -> shape;
            };
        } else if (blockState.get(POSITION) == MultiblockPosition.RIGHT) {
            shape = switch (blockState.get(CORE_FACING)) {
                case NORTH, SOUTH -> VoxelShapes.union(
                        Block.createCuboidShape(0, 0, -1, 15, 18, 17),
                        Block.createCuboidShape(15, 0, -1, 16, 2, 17),
                        Block.createCuboidShape(15, 16, -1, 16, 18, 17),
                        Block.createCuboidShape(15,0,-1,16,18,1),
                        Block.createCuboidShape(15,0,15,16,18,17),
                        Block.createCuboidShape(15,6,6,16,10,10));
                case EAST -> VoxelShapes.union(
                        Block.createCuboidShape(-1, 0, 0, 17, 18, 15),
                        Block.createCuboidShape(-1, 0, 15, 17, 2, 16),
                        Block.createCuboidShape(-1, 16, 15, 17, 18, 16),
                        Block.createCuboidShape(-1, 0, 15, 1, 18, 16),
                        Block.createCuboidShape(15, 0, 15, 17, 18, 16),
                        Block.createCuboidShape(6,6,15,10,10,16));
                case WEST -> VoxelShapes.union(
                        Block.createCuboidShape(-1, 0, 1, 17, 18, 16),
                        Block.createCuboidShape(-1, 0, 0, 17, 2, 1),
                        Block.createCuboidShape(-1, 16, 0, 17, 18, 1),
                        Block.createCuboidShape(-1, 0, 0, 1, 18, 1),
                        Block.createCuboidShape(15, 0, 0, 17, 18, 1),
                        Block.createCuboidShape(6,6,0,10,10,1));
                default -> shape;
            };
        }
        return shape;
    }

    @Override
    public ItemStack getPickStack(BlockView blockView, BlockPos blockPos, BlockState blockState) {
        return DANDYCORPBlocks.GRINDER_BLOCK.asItem().getDefaultStack();
    }
}
