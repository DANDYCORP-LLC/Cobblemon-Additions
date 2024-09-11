package net.dandycorp.dccobblemon.ui;

import net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions;
import net.dandycorp.dccobblemon.block.custom.VendorBlockEntity;
import net.dandycorp.dccobblemon.item.Items;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.ServerNetworkIo;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class VendorScreenHandler extends ScreenHandler{

    private final BlockPos blockPos;
    private VendorBlockEntity blockEntity;
    private final PlayerInventory playerInventory;


    public VendorScreenHandler(int syncId, PlayerInventory playerInventory){
        super(DANDYCORPCobblemonAdditions.VENDOR_SCREEN_HANDLER, syncId);
        this.playerInventory = playerInventory;
        this.blockPos = playerInventory.player.getBlockPos();
        addPlayerInventorySlots(playerInventory);
    }

    // This constructor gets called from the BlockEntity on the server without calling the other constructor first, the server knows the inventory of the container
    // and can therefore directly provide it as an argument. This inventory will then be synced to the client.
    public VendorScreenHandler(int syncId, PlayerInventory playerInventory, BlockPos pos) {
        super(DANDYCORPCobblemonAdditions.VENDOR_SCREEN_HANDLER, syncId);
        this.playerInventory = playerInventory;
        this.blockPos = pos;
        this.blockEntity = (VendorBlockEntity) playerInventory.player.getWorld().getBlockEntity(pos);
        addPlayerInventorySlots(playerInventory);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return player.getInventory() == this.playerInventory;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        return ItemStack.EMPTY;
    }

    public boolean onButtonClick(PlayerEntity player, int id) {
        try {
            if (id == 101) { // engineering item 1
                int cost = 20;
                if (canAfford(cost)) {
                    purchase( player, net.minecraft.item.Items.AZALEA.getDefaultStack(), 20, cost);
                    return true;
                } else {player.sendMessage(Text.translatable("ui.dccobblemon.vendor.poor"));}

            } else if (id == 102) { // engineering item 2
                int cost = 5;
                if (canAfford(cost)) {
                    purchase(player, net.minecraft.item.Items.APPLE.getDefaultStack(), 10, cost);
                    return true;
                } else {player.sendMessage(Text.translatable("ui.dccobblemon.vendor.poor"));}
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    protected void purchase(PlayerEntity player, @Nullable ItemStack stack, int amount, int cost) {
        if (canAfford(cost)) {
            spendTickets(player, cost);
            if (stack != null) {
                spawnItems(stack, amount);
            }
        }
    }

    private void spendTickets(PlayerEntity player, int cost) {
        if (!player.getWorld().isClient()) {

            for (Slot slot : this.slots) {
                ItemStack stack = slot.getStack();
                if (!stack.isEmpty() && stack.getItem() == Items.TICKET) {
                    int toRemove = Math.min(cost, stack.getCount());
                    stack.decrement(toRemove);
                    cost -= toRemove;

                    slot.setStack(stack);
                    slot.markDirty();
                    if (cost == 0) break;
                }

            }
        }
        playerInventory.markDirty();
        player.playerScreenHandler.sendContentUpdates();
        this.sendContentUpdates();
    }

    public boolean canAfford(int cost) {
        return getBalance(playerInventory) >= cost;
    }

    public int getBalance(PlayerInventory playerInventory) {
        int ticketCount = 0;
        for (ItemStack stack : playerInventory.main) {
            if (stack.getItem() == Items.TICKET) {
                ticketCount += stack.getCount();
            }
        }
        for (ItemStack stack : playerInventory.offHand) {
            if (stack.getItem() == Items.TICKET) {
                ticketCount += stack.getCount();
            }
        }
        return ticketCount;
    }

    private void spawnItems(ItemStack stack, int amount) {
        if(blockEntity != null) {
            for (int i = 0; i < amount; i++) {
                ItemStack itemStackToDispense = stack.copy();  // Copy the stack to ensure correct item and quantity
                itemStackToDispense.setCount(1);  // Assuming each item dispensed is just 1 in quantity
                blockEntity.addToDispenseQueue(itemStackToDispense);
            }
        }
    }

    private void addPlayerInventorySlots(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; i++) {  // Main inventory slots
            for (int j = 0; j < 9; j++) {
                int slotIndex = 9 + i * 9 + j;
                int xPosition = 8 + j * 18;
                int yPosition = 84 + i * 18;
                this.addSlot(new Slot(playerInventory, slotIndex, xPosition, yPosition));
            }
        }
        for (int i = 0; i < 9; i++) {  // Hotbar slots
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
        this.addSlot(new Slot(playerInventory, 40, 8, 164));  // Offhand slot
    }


    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);

        player.playerScreenHandler.sendContentUpdates();
        this.sendContentUpdates();
    }

}
