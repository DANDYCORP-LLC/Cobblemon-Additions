package net.dandycorp.dccobblemon.event;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketsApi;
import net.dandycorp.dccobblemon.item.Items;
import net.dandycorp.dccobblemon.item.custom.BadgeItem;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Pair;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BreakBlockHandler implements PlayerBlockBreakEvents.After {

    @Override
    public void afterBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {


        if (!world.isClient()) {


            TrinketsApi.getTrinketComponent(player).ifPresent(trinketComponent -> {
                // Get all non-empty equipped trinkets
                List<Pair<SlotReference, ItemStack>> equippedTrinkets = trinketComponent.getAllEquipped();

                // Process each equipped trinket to check for badges
                equippedTrinkets.stream()
                        .map(Pair::getRight) // Get the ItemStack from the pair
                        .map(ItemStack::getItem) // Get the Item from the ItemStack
                        .filter(item -> item instanceof BadgeItem) // Filter for BadgeItems
                        .forEach(item -> {


                            if (item == Items.GROUND_BADGE) {

                                ItemStack heldItem = player.getMainHandStack();

                                if (heldItem.isIn(ItemTags.SHOVELS) || heldItem.isIn(ItemTags.PICKAXES)) {
                                    BlockHitResult hitResult = (BlockHitResult) player.raycast(5.0, 1.0f, false);
                                    Direction direction = hitResult.getSide();

                                    //player.sendMessage(Text.literal(direction.getName()));
                                    for (int x = -1; x <= 1; x++) {
                                        for (int y = -1; y <= 1; y++) {
                                            BlockPos breakPos = calculateBreakPos(pos, direction, x, y);
                                            BlockState breakState = world.getBlockState(breakPos);

                                            if (canBreakBlock(world, breakPos, breakState, heldItem) && !(x == 0 && y == 0)) {
                                                world.breakBlock(breakPos, !player.isCreative(), player);
                                                heldItem.damage(1, player, playerEntity -> playerEntity.sendToolBreakStatus(playerEntity.getActiveHand()));
                                            }
                                        }
                                    }
                                }
                            }

                        });
            });
        }
    }


    private BlockPos calculateBreakPos(BlockPos pos, Direction direction, int x, int y) {
        switch (direction) {
            case UP:
            case DOWN:
                return pos.add(x, 0, y);
            case NORTH:
            case SOUTH:
                return pos.add(x, y, 0);
            case EAST:
            case WEST:
                return pos.add(0, y, x);
            default:
                return pos;
        }
    }

    private boolean canBreakBlock(World world, BlockPos blockPos, BlockState blockState, ItemStack itemStack) {
        float blockHardness = blockState.getHardness(world,blockPos);
        float toolSpeed = itemStack.getMiningSpeedMultiplier(blockState);

        return toolSpeed > 0 && (blockHardness < toolSpeed-1) &&
                (blockState.isIn(BlockTags.PICKAXE_MINEABLE) && itemStack.isIn(ItemTags.PICKAXES) || blockState.isIn(BlockTags.SHOVEL_MINEABLE) && itemStack.isIn(ItemTags.SHOVELS));
    }


}
