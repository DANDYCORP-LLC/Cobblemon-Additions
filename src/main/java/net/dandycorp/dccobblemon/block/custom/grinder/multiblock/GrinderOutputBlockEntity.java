package net.dandycorp.dccobblemon.block.custom.grinder.multiblock;

import com.simibubi.create.content.kinetics.belt.BeltBlockEntity;
import com.simibubi.create.content.kinetics.belt.BeltHelper;
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
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;

import static net.dandycorp.dccobblemon.block.custom.grinder.multiblock.MultiblockPartBlock.FACING;

public class GrinderOutputBlockEntity extends MultiblockEntity implements SidedStorageBlockEntity {

    private final Direction facing;
    private SmartInventory inventory;
    private GrinderBlockEntity grinder;

    public GrinderOutputBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        facing = getCachedState().get(FACING).getOpposite();
        lazyInit();
    }

    private void lazyInit(){
        if (world == null) return;
        if (grinder == null) {
            if (world.getBlockEntity(findMainBlock(world,pos,getCachedState())) instanceof GrinderBlockEntity gbe) {
                grinder = gbe;
            } else return;
        }
        if (inventory == null) {
            inventory = grinder.getOutputStorage();
        }
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
    }

    @Override
    public @Nullable Storage<ItemVariant> getItemStorage(@Nullable Direction side) {
        if (world == null) return null;
        BlockPos mainBlockPos = ((IMultiblockPart) getCachedState().getBlock()).findMainBlock(world, pos, getCachedState());
        if (mainBlockPos == null) {
            //System.out.println("getItemStorage: Main block position not found.");
            return null;
        }
        BlockEntity be = world.getBlockEntity(mainBlockPos);
        if (be instanceof GrinderBlockEntity grinderBE) {
            //System.out.println("getItemStorage: Output inventory found at " + mainBlockPos);
            return grinderBE.getOutputStorage();
        }
        return null;
    }

    @Override
    public void tick() {
        super.tick();
        if (world == null) return;
        if(inventory == null || grinder == null){
            lazyInit();
        }
        BeltBlockEntity belt = BeltHelper.getSegmentBE(world, pos.offset(facing));
        if (belt != null) {
            try (Transaction t = TransferUtil.getTransaction()) {
                for (StorageView<ItemVariant> view : inventory.nonEmptyViews()) {
                    ItemVariant variant = view.getResource();
                    ItemStack stack = variant.toStack(1);
                    if (canOutput(belt, stack, t)) {
                        view.extract(variant, 1, t);
                        t.commit();
                    }
                    else t.abort();
                }
            }
        }
    }

    public boolean canOutput(@Nonnull BeltBlockEntity belt, @Nonnull ItemStack outputItem, @Nonnull TransactionContext ctx) {
        if (outputItem == null || outputItem.isEmpty() || world == null)
            return false;

        Storage<ItemVariant> targetInv = belt.getItemStorage(null);

        if (targetInv == null)
            return false;

        ItemVariant variant = ItemVariant.of(outputItem);
        long inserted = targetInv.insert(variant, 1, ctx);

        if (inserted > 0) return true;
        else return false;
    }
}
