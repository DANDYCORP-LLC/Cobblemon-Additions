package net.dandycorp.dccobblemon.block.custom.grinder.multiblock;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import net.dandycorp.dccobblemon.block.DANDYCORPBlocks;
import net.dandycorp.dccobblemon.block.custom.grinder.GrinderBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public interface IMultiblockPart extends IWrenchable {
    Direction getMainBlockDirection(BlockState state);

    default BlockPos findMainBlock(World world, BlockPos pos, BlockState state) {
        int maxDepth = 5; // Maximum number of steps to search
        for (int i = 0; i < maxDepth; i++) {
            Direction direction = getMainBlockDirection(state);
            pos = pos.offset(direction);
            state = world.getBlockState(pos);
            Block block = state.getBlock();
            if(!(block instanceof MultiblockPartBlock || block instanceof GrinderRotationalBlock))
            {
                if (block instanceof GrinderBlock) {
                    return pos; // Found the main block
                } else {
                    return null; // found a non-multiblock block (something is wrong)
                }
            }
        }
        return null; // after 5 loops main block was not found (cycle is somewhere)
    }

    @Override
    default ActionResult onSneakWrenched(BlockState state, ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos mainPos = findMainBlock(world, context.getBlockPos(), state);
        PlayerEntity player = context.getPlayer();

        if (mainPos == null)
            return ActionResult.FAIL;

        if (world instanceof ServerWorld) {
            BlockState mainState = world.getBlockState(mainPos);
            Block mainBlock = mainState.getBlock();
            if (mainBlock instanceof GrinderBlock grinder) {
                if (player != null && !player.isCreative()) {
                    player.getInventory().offerOrDrop(DANDYCORPBlocks.GRINDER_BLOCK.asStack());
                }
                grinder.deconstruct(world, mainPos, mainState, player,false);
                state.onStacksDropped((ServerWorld) world, mainPos, ItemStack.EMPTY, true);
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    default ActionResult onWrenched(BlockState state, ItemUsageContext context) {
        return ActionResult.PASS;
    }
}
