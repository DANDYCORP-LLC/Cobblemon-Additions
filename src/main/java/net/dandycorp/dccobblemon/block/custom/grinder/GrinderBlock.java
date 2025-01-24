package net.dandycorp.dccobblemon.block.custom.grinder;

import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandler;
import net.dandycorp.dccobblemon.block.DANDYCORPBlockEntities;
import net.dandycorp.dccobblemon.block.custom.grinder.multiblock.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class GrinderBlock extends HorizontalKineticBlock implements IBE<GrinderBlockEntity> {

    private static final Vec3i START_POS = new Vec3i(-1, 0, -1);
    private static final GrinderPartTypes[][][] MULTIBLOCK = {
            {   // Bottom Layer (Y = 0)
                    {GrinderPartTypes.CORNER, GrinderPartTypes.OUTPUT, GrinderPartTypes.CORNER},
                    {GrinderPartTypes.ROTATIONAL, GrinderPartTypes.MAIN, GrinderPartTypes.ROTATIONAL},
                    {GrinderPartTypes.CORNER, GrinderPartTypes.INPUT, GrinderPartTypes.CORNER}
            },
            {   // Top Layer (Y = 1)
                    {GrinderPartTypes.EMPTY, GrinderPartTypes.EMPTY, GrinderPartTypes.EMPTY},
                    {GrinderPartTypes.INPUT, GrinderPartTypes.INPUT, GrinderPartTypes.INPUT},
                    {GrinderPartTypes.EMPTY, GrinderPartTypes.EMPTY, GrinderPartTypes.EMPTY}
            }
    };

    public GrinderBlock(Settings properties) {
        super(properties);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        Direction preferred = getPreferredHorizontalFacing(context);
        if (preferred == null || (context.getPlayer() != null && context.getPlayer()
                .isSneaking())) {
            Direction facingDirection = context.getHorizontalPlayerFacing();
            return getDefaultState().with(HORIZONTAL_FACING, context.getPlayer() != null && context.getPlayer()
                    .isSneaking() ? facingDirection : facingDirection.getOpposite());
        }

        //TODO: fix this to not just be a rotation of source code but properly prioritize nearby shafts left/right
        preferred = preferred.rotateClockwise(Direction.Axis.Y);
        return getDefaultState().with(HORIZONTAL_FACING, preferred.getOpposite());
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        Direction facing = state.get(HORIZONTAL_FACING);
        if (facing == Direction.NORTH || facing == Direction.SOUTH) {
            return Direction.Axis.X;
        } else {
            return Direction.Axis.Z;
        }
    }

    @Override
    public boolean hasShaftTowards(WorldView world, BlockPos pos, BlockState state, Direction face) {
        Direction facing = state.get(HORIZONTAL_FACING);
        return ((facing == Direction.NORTH || facing == Direction.SOUTH) && (face == Direction.WEST || face == Direction.EAST))
        || ((facing == Direction.EAST || facing == Direction.WEST) && (face == Direction.NORTH || face == Direction.SOUTH));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
                              BlockHitResult hit) {
        if (!player.getStackInHand(hand).isEmpty())
            return ActionResult.PASS;
        if (world.isClient)
            return ActionResult.SUCCESS;

        withBlockEntityDo(world, pos, grinder -> {
            boolean emptyOutput = true;
            ItemStackHandler inv = grinder.output;
            for (int slot = 0; slot < inv.getSlots().size(); slot++) {
                ItemStack stackInSlot = inv.getStackInSlot(slot);
                if (!stackInSlot.isEmpty())
                    emptyOutput = false;
                player.getInventory().offerOrDrop(stackInSlot);
                inv.setStackInSlot(slot, ItemStack.EMPTY);
            }

            if (emptyOutput) {
                inv = grinder.input;
                for (int slot = 0; slot < inv.getSlots().size(); slot++) {
                    player.getInventory().offerOrDrop(inv.getStackInSlot(slot));
                    inv.setStackInSlot(slot, ItemStack.EMPTY);
                }
            }

            grinder.markDirty();
            grinder.sendData();
        });

        return ActionResult.SUCCESS;
    }


    @Override
    public SpeedLevel getMinimumRequiredSpeedLevel() {
        return SpeedLevel.MEDIUM;
    }

    @Override
    public Class<GrinderBlockEntity> getBlockEntityClass() {
        return GrinderBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends GrinderBlockEntity> getBlockEntityType() {
        return DANDYCORPBlockEntities.GRINDER_BLOCK_ENTITY.get();
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new GrinderBlockEntity(DANDYCORPBlockEntities.GRINDER_BLOCK_ENTITY.get(),blockPos, blockState);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView worldView, BlockPos pos) {
        for (int y = 0; y < MULTIBLOCK.length; y++) {
            for (int x = 0; x < MULTIBLOCK[y].length; x++) {
                for (int z = 0; z < MULTIBLOCK[y][x].length; z++) {
                    GrinderPartTypes partType = MULTIBLOCK[y][x][z];
                    if (partType == GrinderPartTypes.EMPTY) {
                        continue;
                    }
                    int relX = x + START_POS.getX();
                    int relY = y + START_POS.getY();
                    int relZ = z + START_POS.getZ();

                    BlockPos offset = rotateOffset(state.get(HORIZONTAL_FACING), relX, relY, relZ);
                    BlockPos partPos = pos.add(offset);
                    if (!worldView.getBlockState(partPos).isReplaceable())
                        return false;
                }
            }
        }
        return true;
    }

    private BlockPos rotateOffset(Direction facing, int x, int y, int z) {
        switch (facing) {
            case NORTH:
                return new BlockPos(z, y, -x);
            case EAST:
                return new BlockPos(x, y, z);
            case SOUTH:
                return new BlockPos(z, y, x);
            case WEST:
                return new BlockPos(-x, y, -z);
            default:
                return new BlockPos(-z, y, x);
        }
    }


    // Inside the onPlaced method
    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        Direction facing = state.get(HORIZONTAL_FACING);

        for (int y = 0; y < MULTIBLOCK.length; y++) {
            for (int x = 0; x < MULTIBLOCK[y].length; x++) {
                for (int z = 0; z < MULTIBLOCK[y][x].length; z++) {
                    GrinderPartTypes partType = MULTIBLOCK[y][x][z];
                    if (partType == GrinderPartTypes.EMPTY || partType == GrinderPartTypes.MAIN) {
                        continue;
                    }


                    // Calculate the relative position
                    int relX = x + START_POS.getX();
                    int relY = y + START_POS.getY();
                    int relZ = z + START_POS.getZ();
                    BlockPos offset = rotateOffset(facing, relX, relY, relZ);

                    BlockPos partPos = pos.add(offset);

                    MultiblockPosition position;
                    if (relZ == -1) {
                        position = MultiblockPosition.LEFT;
                    } else if (relZ == 0) {
                        position = MultiblockPosition.MIDDLE;
                    } else {
                        position = MultiblockPosition.RIGHT;
                    }

                    Direction facingToMainBlock;
                    DoubleBlockHalf half = DoubleBlockHalf.LOWER;

                    if (partType == GrinderPartTypes.CORNER) {
                        if(x == 0) {
                            facingToMainBlock = facing;
                        } else {
                            facingToMainBlock = facing.getOpposite();
                        }
                    }
                    else if(y == 0) {
                        facingToMainBlock = getDirectionToMainBlock(partPos, pos);
                    } else {
                        facingToMainBlock = Direction.DOWN;
                        half = DoubleBlockHalf.UPPER;
                    }


                    BlockState partState;

                    if(partType == GrinderPartTypes.INPUT) {
                        partState = partType.getBlock().getDefaultState()
                                .with(MultiblockPartBlock.FACING, facingToMainBlock)
                                .with(MultiblockPartBlock.CORE_FACING, facing)
                                .with(MultiblockPartBlock.POSITION, position)
                                .with(GrinderInputBlock.HALF, half);
                    }
                    else {
                        partState = partType.getBlock().getDefaultState()
                                .with(MultiblockPartBlock.CORE_FACING, facing)
                                .with(MultiblockPartBlock.POSITION, position)
                                .with(MultiblockPartBlock.FACING, facingToMainBlock);
                    }

                    world.setBlockState(partPos, partState);
                }
            }
        }
    }

    private Direction getDirectionToMainBlock(BlockPos partPos, BlockPos mainPos) {
        int dx = mainPos.getX() - partPos.getX();
        int dy = mainPos.getY() - partPos.getY();
        int dz = mainPos.getZ() - partPos.getZ();

        int nx = Integer.signum(dx);
        int ny = Integer.signum(dy);
        int nz = Integer.signum(dz);

        Vec3i directionVector = new Vec3i(nx, ny, nz);

        for (Direction direction : Direction.values()) {
            if (direction.getVector().equals(directionVector)) {
                return direction;
            }
        }
        return Direction.NORTH;
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBreak(world, pos, state, player);
        deconstruct(world, pos, state, player,true);
    }

    @Override
    public ActionResult onSneakWrenched(BlockState state, ItemUsageContext context) {
        super.onSneakWrenched(state,context);
        deconstruct(context.getWorld(), context.getBlockPos(), state, context.getPlayer(),false);
        return ActionResult.SUCCESS;
    }

    public void deconstruct(World world, BlockPos pos, BlockState state, PlayerEntity player, boolean drop) {
        Direction facing = state.get(HORIZONTAL_FACING);
        for (int y = 0; y <= 1; y++) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    BlockPos offset = rotateOffset(facing, x, y, z);
                    BlockPos partPos = pos.add(offset);

                    // Remove the block if it's part of the grinder
                    Block block = world.getBlockState(partPos).getBlock();
                    if (block instanceof GrinderRotationalBlock || block instanceof MultiblockPartBlock) {
                        world.setBlockState(partPos, Blocks.AIR.getDefaultState());
                    } else if (block instanceof GrinderBlock) {
                        world.breakBlock(partPos, drop && player != null && !player.isCreative());
                    }
                }
            }
        }
    }
}
