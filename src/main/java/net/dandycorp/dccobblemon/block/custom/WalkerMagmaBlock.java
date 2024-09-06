package net.dandycorp.dccobblemon.block.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class WalkerMagmaBlock extends Block {

    // this code is copied from FrostedIceBlock with a few changes

    public static final int MAX_AGE = 3;
    public static final IntProperty AGE;
    private static final int NEIGHBORS_CHECKED_ON_SCHEDULED_TICK = 4;
    private static final int NEIGHBORS_CHECKED_ON_NEIGHBOR_UPDATE = 2;

    public WalkerMagmaBlock(Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)((BlockState)this.stateManager.getDefaultState()).with(AGE, 0));
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        super.onBroken(world, pos, state);
        world.setBlockState(pos,Blocks.LAVA.getDefaultState(),2);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if ((random.nextInt(3) == 0 || this.canMelt(world, pos, 4)) && this.increaseAge(state, world, pos)) {
            BlockPos.Mutable mutable = new BlockPos.Mutable();
            Direction[] var6 = Direction.values();
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
                Direction direction = var6[var8];
                mutable.set(pos, direction);
                BlockState blockState = world.getBlockState(mutable);
                if (blockState.isOf(this) && !this.increaseAge(blockState, world, mutable)) {
                    world.scheduleBlockTick(mutable, this, MathHelper.nextInt(random, 20, 40));
                }
            }

        } else {
            world.scheduleBlockTick(pos, this, MathHelper.nextInt(random, 20, 40));
        }
    }

    private boolean increaseAge(BlockState state, World world, BlockPos pos) {
        int i = (Integer)state.get(AGE);
        if (i < 3) {
            world.setBlockState(pos, (BlockState)state.with(AGE, i + 1), 2);
            return false;
        } else {
            this.melt(state, world, pos);
            return true;
        }
    }

    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (sourceBlock.getDefaultState().isOf(this) && this.canMelt(world, pos, 2)) {
            this.melt(state, world, pos);
        }

        super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, notify);
    }

    private void melt(BlockState state, World world, BlockPos pos) {
        world.setBlockState(pos, Blocks.LAVA.getDefaultState(), 2);
        world.updateNeighbor(pos, Blocks.LAVA, pos);
    }

    private boolean canMelt(BlockView world, BlockPos pos, int maxNeighbors) {
        int i = 0;
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        Direction[] var6 = Direction.values();
        int var7 = var6.length;

        for(int var8 = 0; var8 < var7; ++var8) {
            Direction direction = var6[var8];
            mutable.set(pos, direction);
            if (world.getBlockState(mutable).isOf(this)) {
                ++i;
                if (i >= maxNeighbors) {
                    return false;
                }
            }
        }

        return true;
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{AGE});
    }

    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return Items.OBSIDIAN.getDefaultStack();
    }

    static {
        AGE = Properties.AGE_3;
    }

}
