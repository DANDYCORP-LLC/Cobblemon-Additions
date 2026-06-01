package net.dandycorp.dccobblemon.block.custom;

import com.simibubi.create.AllBlocks;
import net.dandycorp.dccobblemon.sound.DANDYCORPSounds;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoorHinge;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

import static net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions.RANDOM;
import static net.minecraft.block.TallPlantBlock.withWaterloggedState;

public class DandyBotStatueBlock extends HorizontalFacingBlock {

    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final EnumProperty<DoubleBlockHalf> HALF = Properties.DOUBLE_BLOCK_HALF;
    private static int COOLDOWN_TIMER = 0;

    private static final VoxelShape TOP_NS = VoxelShapes.union(
            Block.createCuboidShape(1.75,-1,6.25,14.25,8,9.75),
            Block.createCuboidShape(4.75,8,4.75,11.25,14.25,11.25)
    );
    private static final VoxelShape BOTTOM_NS = VoxelShapes.union(
            Block.createCuboidShape(0,0,0,16,3,16),
            Block.createCuboidShape(2,3,2,14,6,14),
            Block.createCuboidShape(4.75,6,6.25,11.25,15,9.75)
    );
    private static final VoxelShape TOP_EW = VoxelShapes.union(
            Block.createCuboidShape(6.25,-1,1.75,9.75,8,14.25),
            Block.createCuboidShape(4.75,8,4.75,11.25,14.25,11.25)
    );
    private static final VoxelShape BOTTOM_EW = VoxelShapes.union(
            Block.createCuboidShape(0,0,0,16,3,16),
            Block.createCuboidShape(2,3,2,14,6,14),
            Block.createCuboidShape(6.25,6,4.75,9.75,15,11.25)
    );

    public DandyBotStatueBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(HALF, DoubleBlockHalf.LOWER)
                .with(FACING, Direction.NORTH)
        );
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction facing = ctx.getHorizontalPlayerFacing().getOpposite();
        return this.getDefaultState().with(FACING, facing).with(HALF, DoubleBlockHalf.LOWER);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return getShape(state.get(HALF), state.get(FACING));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return getShape(state.get(HALF), state.get(FACING));
    }

    private VoxelShape getShape(DoubleBlockHalf half, Direction facing) {
        return switch (half) {
            case UPPER -> switch (facing) {
                case EAST, WEST -> TOP_EW;
                case NORTH, SOUTH -> TOP_NS;
                default -> TOP_NS;
            };
            case LOWER -> switch (facing) {
                case EAST, WEST -> BOTTOM_EW;
                case NORTH, SOUTH -> BOTTOM_NS;
                default -> BOTTOM_NS;
            };
        };
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        world.setBlockState(pos.up(), this.getDefaultState()
                .with(HALF, DoubleBlockHalf.UPPER)
                .with(Properties.HORIZONTAL_FACING, state.get(Properties.HORIZONTAL_FACING)), 3);
        world.playSound(null, pos, SoundEvents.BLOCK_AMETHYST_BLOCK_PLACE, net.minecraft.sound.SoundCategory.BLOCKS, 0.5f, 1.0f);
        world.playSound(null, pos, SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, net.minecraft.sound.SoundCategory.BLOCKS, 0.5f, 0.0f);
        for(int i = 0; i < 20; i++) {
            world.addParticle(
                    ParticleTypes.WAX_OFF,
                    pos.getX() + 0.5f + RANDOM.nextFloat(-0.5f,0.5f),
                    pos.getY() + 0.6f,
                    pos.getZ() + 0.5f + RANDOM.nextFloat(-0.5f,0.5f),
                    RANDOM.nextInt(-10,10),
                    6,
                    RANDOM.nextInt(-10,10)
            );
        }
        super.onPlaced(world,pos,state,placer,itemStack);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView worldView, BlockPos pos) {
        BlockState upState = worldView.getBlockState(pos.up());
        return upState.isReplaceable();
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
        world.playSound(null, pos, SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK, net.minecraft.sound.SoundCategory.BLOCKS, 0.5f, 1.0f);
        world.playSound(null, pos, SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, net.minecraft.sound.SoundCategory.BLOCKS, 0.5f, 0.0f);
        for(int i = 0; i < 20; i++) {
            world.addParticle(
                    ParticleTypes.WAX_OFF,
                    pos.getX() + 0.5f + RANDOM.nextFloat(-0.5f,0.5f),
                    pos.getY() + 0.6f,
                    pos.getZ() + 0.5f + RANDOM.nextFloat(-0.5f,0.5f),
                    RANDOM.nextInt(-10,10),
                    6,
                    RANDOM.nextInt(-10,10)
            );
        }
        super.onBreak(world, pos, state, player);
    }

    @Override
    public BlockState getStateForNeighborUpdate(
            BlockState blockState, Direction direction, BlockState blockState2, WorldAccess worldAccess, BlockPos blockPos, BlockPos blockPos2
    ) {
        DoubleBlockHalf doubleBlockHalf = blockState.get(HALF);
        if (direction.getAxis() != Direction.Axis.Y
                || doubleBlockHalf == DoubleBlockHalf.LOWER != (direction == Direction.UP)
                || blockState2.isOf(this) && blockState2.get(HALF) != doubleBlockHalf) {
            return doubleBlockHalf == DoubleBlockHalf.LOWER && direction == Direction.DOWN && !blockState.canPlaceAt(worldAccess, blockPos)
                    ? Blocks.AIR.getDefaultState()
                    : super.getStateForNeighborUpdate(blockState, direction, blockState2, worldAccess, blockPos, blockPos2);
        } else {
            return Blocks.AIR.getDefaultState();
        }
    }

    @Override
    protected void spawnBreakParticles(World world, PlayerEntity playerEntity, BlockPos blockPos, BlockState blockState) {
        super.spawnBreakParticles(world, playerEntity, blockPos, AllBlocks.BRASS_BLOCK.getDefaultState());
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (random.nextInt(3) > 0) return;
        Direction facing = state.get(FACING);
        double speed = 1.0;
        double x = pos.getX() + (facing.getAxis() == Direction.Axis.X
                ? 0.25 + random.nextDouble() * 0.5
                : random.nextDouble());
        double y = pos.getY() + random.nextDouble();
        double z = pos.getZ() + (facing.getAxis() == Direction.Axis.Z
                ? 0.25 + random.nextDouble() * 0.5
                : random.nextDouble());
        world.addParticle(
                ParticleTypes.WAX_OFF,
                x, y, z,
                (x - pos.getX() - 0.5) * speed,
                random.nextDouble() * 0.5,
                (z - pos.getZ() - 0.5) * speed
        );
    }

    @Override
    public void randomTick(BlockState blockState, ServerWorld serverWorld, BlockPos blockPos, Random random) {
        super.randomTick(blockState, serverWorld, blockPos, random);
        serverWorld.playSound(null, blockPos, SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, net.minecraft.sound.SoundCategory.BLOCKS, 0.5f, random.nextFloat());
    }

    @Override
    public ActionResult onUse(BlockState blockState, World world, BlockPos blockPos, PlayerEntity playerEntity, Hand hand, BlockHitResult blockHitResult) {
        if(COOLDOWN_TIMER <= 0 && !world.isClient()) {
            world.playSound(null, blockPos, SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.BLOCKS, 1.0f, 0.0f);
            world.playSound(null, blockPos, DANDYCORPSounds.COMPLIMENT_EVENT, SoundCategory.BLOCKS, 0.4f, 1.1f);
            COOLDOWN_TIMER = 100;
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    public static void tickCooldown() {
        if(COOLDOWN_TIMER > 0) {
            COOLDOWN_TIMER--;
        }
    }
}
