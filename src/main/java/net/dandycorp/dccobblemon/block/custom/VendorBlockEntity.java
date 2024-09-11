package net.dandycorp.dccobblemon.block.custom;

import net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions;
import net.dandycorp.dccobblemon.ui.VendorBalanceManager;
import net.dandycorp.dccobblemon.ui.VendorScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.FabricScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class VendorBlockEntity extends BlockEntity implements NamedScreenHandlerFactory{

    public Queue<ItemStack> toDispense = new LinkedList<>();
    private final ArrayPropertyDelegate propertyDelegate = new ArrayPropertyDelegate(1);

    public VendorBlockEntity(BlockPos pos, BlockState state) {
        super(DANDYCORPCobblemonAdditions.VENDOR_BLOCK_ENTITY, pos, state);
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new VendorScreenHandler(syncId, playerInventory, this.getPos());
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("block.dandycorp.vendor");
    }

    public static void tick(World world, BlockPos pos, BlockState state, VendorBlockEntity blockEntity) {
        if (!blockEntity.toDispense.isEmpty() && !world.isClient) {
            ItemStack itemToDispense = blockEntity.toDispense.poll();
            dispenseItem(world, pos, itemToDispense);
        }
    }

    private static void dispenseItem(World world, BlockPos pos, ItemStack item) {
        if (!world.isClient) {

            double x = pos.getX() + 0.5;
            double z = pos.getZ() + 0.5;
            double y = pos.getY();

            switch (world.getBlockState(pos).get(VendorBlock.HALF)){
                case LOWER -> y += 0.15;
                case UPPER -> y -= 0.85;
            }

            ItemEntity itemEntity;

            switch (world.getBlockState(pos).get(VendorBlock.FACING)) {
                case NORTH:
                    itemEntity = new ItemEntity(world, x, y, z-0.5, item);
                    itemEntity.setVelocity(-0.25 + Math.random()/2, 5, -0.7 + Math.random() * 0.4 - 0.2);
                    world.spawnEntity(itemEntity);
                    break;
                case SOUTH:
                    itemEntity = new ItemEntity(world, x, y, z+0.5, item);
                    itemEntity.setVelocity(-0.25 + Math.random()/2, 5, 0.7 + Math.random() * 0.4 - 0.2);
                    world.spawnEntity(itemEntity);
                    break;
                case WEST:
                    itemEntity = new ItemEntity(world, x-0.5, y, z, item);
                    itemEntity.setVelocity(-0.7 + Math.random() * 0.4 - 0.2, 5, -0.25 + Math.random()/2);
                    world.spawnEntity(itemEntity);
                    break;
                case EAST:
                    itemEntity = new ItemEntity(world, x+0.5, y, z, item);
                    itemEntity.setVelocity(0.7 + Math.random() * 0.4 - 0.2, 5, -0.25 + Math.random()/2);
                    world.spawnEntity(itemEntity);
                    break;
            }

            world.playSound(null, pos, SoundEvents.BLOCK_DISPENSER_LAUNCH, SoundCategory.BLOCKS, 0.2f, (float) (Math.random()*0.5f));
        }
    }

    public void addToDispenseQueue(ItemStack itemStack) {
        this.toDispense.add(itemStack);
    }

}
