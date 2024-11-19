package net.dandycorp.dccobblemon.block.custom.grinder.multiblock;

import net.dandycorp.dccobblemon.block.Blocks;
import net.dandycorp.dccobblemon.block.custom.grinder.GrinderBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TransparentBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class MultiblockPartBlock extends TransparentBlock implements IMultiblockPart {
    public static final DirectionProperty FACING = DirectionProperty.of("facing", Direction.values());
    public static final DirectionProperty CORE_FACING = DirectionProperty.of("core_facing",Direction.values());
    public static final EnumProperty<MultiblockPosition> POSITION = EnumProperty.of("position", MultiblockPosition.class);

    public MultiblockPartBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(CORE_FACING, Direction.NORTH)
                .with(POSITION, MultiblockPosition.MIDDLE));
    }

    @Override
    protected void appendProperties(Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(CORE_FACING);
        builder.add(POSITION);
    }

    @Override
    public Direction getMainBlockDirection(BlockState state) {
        return state.get(FACING);
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
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            BlockPos mainBlockPos = findMainBlock(world, pos, state);
            if (mainBlockPos != null) {
                BlockState mainState = world.getBlockState(mainBlockPos);
                return mainState.getBlock().onUse(mainState, world, mainBlockPos, player, hand, hit);
            }
        }
        return ActionResult.PASS;
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient) {
            BlockPos mainBlockPos = findMainBlock(world, pos, state);
            if (mainBlockPos != null) {
                BlockState mainState = world.getBlockState(mainBlockPos);
                mainState.getBlock().onBreak(world, mainBlockPos, mainState, player);
            }
        }
    }

    @Override
    public ItemStack getPickStack(BlockView blockView, BlockPos blockPos, BlockState blockState) {
        return Blocks.GRINDER_BLOCK.asItem().getDefaultStack();
    }
}
