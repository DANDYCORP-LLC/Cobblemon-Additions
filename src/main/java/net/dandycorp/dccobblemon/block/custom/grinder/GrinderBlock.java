package net.dandycorp.dccobblemon.block.custom.grinder;

import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.utility.Iterate;
import io.github.fabricators_of_create.porting_lib.transfer.TransferUtil;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemHandlerHelper;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandler;
import net.dandycorp.dccobblemon.block.BlockEntities;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class GrinderBlock extends DirectionalKineticBlock implements IBE<GrinderBlockEntity> {

    public GrinderBlock(Settings properties) {
        super(properties);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        Direction preferred = getPreferredFacing(context);
        if (preferred == null || (context.getPlayer() != null && context.getPlayer()
                .isSneaking())) {
            Direction facingDirection = context.getHorizontalPlayerFacing();
            return getDefaultState().with(FACING, context.getPlayer() != null && context.getPlayer()
                    .isSneaking() ? facingDirection : facingDirection.getOpposite());
        }

        //TODO: fix this to not just be a rotation of source code but properly prioritize nearby shafts left/right
        preferred = preferred.rotateClockwise(Direction.Axis.Y);
        return getDefaultState().with(FACING, preferred.getOpposite());
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        Direction facing = state.get(FACING);
        if (facing == Direction.NORTH || facing == Direction.SOUTH) {
            return Direction.Axis.X;
        } else {
            return Direction.Axis.Z;
        }
    }

    @Override
    public boolean hasShaftTowards(WorldView world, BlockPos pos, BlockState state, Direction face) {
        Direction facing = state.get(FACING);
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
    public void onEntityLand(BlockView blockView, Entity entity) {
        super.onEntityLand(blockView, entity);

        if (entity.getWorld().isClient)
            return;
        if (!entity.isAlive())
            return;
        if (entity instanceof ItemEntity itemEntity) {

            GrinderBlockEntity grinder = null;
            for (BlockPos pos : Iterate.hereAndBelow(entity.getBlockPos()))
                if (grinder == null)
                    grinder = getBlockEntity(blockView, pos);

            if (grinder != null) {
                Storage<ItemVariant> handler = grinder.getItemStorage(null);
                if (handler != null) {
                    try (Transaction t = TransferUtil.getTransaction()) {
                        ItemStack inEntity = itemEntity.getStack();
                        long inserted = handler.insert(ItemVariant.of(inEntity), inEntity.getCount(), t);
                        if (inserted == inEntity.getCount())
                            itemEntity.discard();
                        else
                            itemEntity.setStack(ItemHandlerHelper.copyStackWithSize(inEntity, (int) (inEntity.getCount() - inserted)));
                        t.commit();
                    }
                }
            }
        }
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
        return BlockEntities.GRINDER_BLOCK_ENTITY.get();
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new GrinderBlockEntity(BlockEntities.GRINDER_BLOCK_ENTITY.get(),blockPos, blockState);
    }

}
