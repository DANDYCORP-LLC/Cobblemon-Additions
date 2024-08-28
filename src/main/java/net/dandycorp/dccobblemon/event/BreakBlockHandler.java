package net.dandycorp.dccobblemon.event;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BreakBlockHandler implements PlayerBlockBreakEvents.Before {
    @Override
    public boolean beforeBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {

        if (!world.isClient()) {
            if (player.getMainHandStack().isIn(ItemTags.SHOVELS) || player.getOffHandStack().isIn(ItemTags.PICKAXES)) {
                Direction direction = player.getHorizontalFacing();

                for (int x = -1; x <= 1; x++){
                    for (int y = -1; y <= 1; y++){
                        BlockPos breakPos = calculateBreakPos(pos, direction, x, y);
                        if ((world.getBlockState(breakPos).isIn(BlockTags.PICKAXE_MINEABLE) && player.getMainHandStack().isIn(ItemTags.PICKAXES))
                            || (world.getBlockState(breakPos).isIn(BlockTags.SHOVEL_MINEABLE) && player.getMainHandStack().isIn(ItemTags.SHOVELS))){
                            world.breakBlock(breakPos, true, player);
                        }
                    }
                }

            }
        }

        return true;
    }



    private BlockPos calculateBreakPos(BlockPos pos, Direction direction, int x, int y) {
        return switch (direction) {
            case UP, DOWN -> pos.add(x, 0, y); // Break along the X and Z axis when looking up or down
            case NORTH, SOUTH -> pos.add(x, y, 0); // Break along the X and Y axis when looking north or south
            case EAST, WEST -> pos.add(0, y, x); // Break along the Z and Y axis when looking east or west
            default -> pos; // Default case, should never be hit
        };
    }

}
