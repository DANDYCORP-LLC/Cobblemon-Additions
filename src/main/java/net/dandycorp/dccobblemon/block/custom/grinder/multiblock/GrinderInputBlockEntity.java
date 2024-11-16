package net.dandycorp.dccobblemon.block.custom.grinder.multiblock;

import com.simibubi.create.content.kinetics.belt.BeltBlockEntity;
import com.simibubi.create.content.kinetics.belt.BeltHelper;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.item.SmartInventory;
import io.github.fabricators_of_create.porting_lib.transfer.TransferUtil;
import net.dandycorp.dccobblemon.block.custom.grinder.GrinderBlockEntity;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;

import static net.dandycorp.dccobblemon.block.custom.grinder.multiblock.MultiblockPartBlock.FACING;


public class GrinderInputBlockEntity extends MultiblockEntity implements SidedStorageBlockEntity {

    private final Direction facing;
    private SmartInventory inventory;
    private GrinderBlockEntity grinder;

    public GrinderInputBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
        facing = getCachedState().get(FACING).getOpposite();
        lazyInit();
    }

    private void lazyInit(){
        if (world == null) return;
        if (grinder == null) {
            BlockPos mainPos = ((IMultiblockPart) getCachedState().getBlock()).findMainBlock(world, pos, getCachedState());
            if (mainPos != null && world.getBlockEntity(mainPos) instanceof GrinderBlockEntity gbe) {
                grinder = gbe;
            } else return;
        }
        if (inventory == null) {
            inventory = grinder.getInputStorage();
        }
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        behaviours.add(new DirectBeltInputBehaviour(this).allowingBeltFunnels());
    }

    @Override
    public @Nullable Storage<ItemVariant> getItemStorage(@Nullable Direction side) {
        if (world == null)
            return null;
        BlockPos mainBlockPos = ((IMultiblockPart) getCachedState().getBlock()).findMainBlock(world, pos, getCachedState());
        if (mainBlockPos == null) {
            return null;
        }
        BlockEntity be = world.getBlockEntity(mainBlockPos);
        if (be instanceof GrinderBlockEntity grinderBE) {

            return grinderBE.getInputStorage();
        }
        return null;
    }

    @Override
    public void tick() {
        super.tick();
        if (world == null) return;
        if(inventory == null || grinder == null){
            lazyInit();
            if (inventory == null || grinder == null) return;
        }
        Storage<ItemVariant> inputStorage = inventory;
        if(inputStorage == null){
            return;
        }
        BeltBlockEntity belt = BeltHelper.getSegmentBE(world, pos.offset(facing.getOpposite()));
        if (belt != null && belt.getMovementFacing() == this.getCachedState().get(FACING)) {
            try (Transaction t = TransferUtil.getTransaction()) {
                Storage<ItemVariant> beltStorage = belt.getItemStorage(null);
                if (beltStorage == null) {
                    t.abort();
                    return;
                }

                for (StorageView<ItemVariant> view : beltStorage.nonEmptyViews()) {
                    ItemVariant variant = view.getResource();
                    long extracted = beltStorage.extract(variant, 1, t);
                    if (extracted > 0) {
                        long inserted = inputStorage.insert(variant, 1, t);
                        if (inserted > 0) {
                            continue;
                        } else {
                            t.abort();
                            break;
                        }
                    }
                }
                t.commit();
            }
        }
    }
}