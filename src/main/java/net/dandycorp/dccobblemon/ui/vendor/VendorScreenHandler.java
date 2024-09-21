package net.dandycorp.dccobblemon.ui.vendor;

import net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions;
import net.dandycorp.dccobblemon.block.custom.VendorBlockEntity;
import net.dandycorp.dccobblemon.item.Items;
import net.dandycorp.dccobblemon.util.VendorCategory;
import net.dandycorp.dccobblemon.util.VendorData;
import net.dandycorp.dccobblemon.util.VendorDataLoader;
import net.dandycorp.dccobblemon.util.VendorItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.*;

//TODO: make purchase and spawn logic accept a list of item stacks rather than just one stack and an amount (tuple pairs?)

public class VendorScreenHandler extends ScreenHandler{

    private final BlockPos blockPos;
    private VendorBlockEntity blockEntity;
    private final PlayerInventory playerInventory;
    private final VendorData data;
    private final Map<Integer, VendorItem> idItemMap = new HashMap<>();
    private final List<Integer> specialIds = Arrays.asList(1000,1001,1002);


    public VendorScreenHandler(int syncId, PlayerInventory playerInventory){
        super(DANDYCORPCobblemonAdditions.VENDOR_SCREEN_HANDLER, syncId);
        this.playerInventory = playerInventory;
        this.blockPos = playerInventory.player.getBlockPos();
        addPlayerInventorySlots(playerInventory);
        this.data = VendorDataLoader.loadVendorData();
        buildButtonIdToVendorItemMap();
    }

    // This constructor gets called from the BlockEntity on the server without calling the other constructor first, the server knows the inventory of the container
    // and can therefore directly provide it as an argument. This inventory will then be synced to the client.
    public VendorScreenHandler(int syncId, PlayerInventory playerInventory, BlockPos pos) {
        super(DANDYCORPCobblemonAdditions.VENDOR_SCREEN_HANDLER, syncId);
        this.playerInventory = playerInventory;
        this.blockPos = pos;
        this.blockEntity = (VendorBlockEntity) playerInventory.player.getWorld().getBlockEntity(pos);
        addPlayerInventorySlots(playerInventory);
        this.data = VendorDataLoader.loadVendorData();
        buildButtonIdToVendorItemMap();
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return player.getInventory() == this.playerInventory;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        if (specialIds.contains(id)) {
            switch (id){
                case 1000: // compliment
                case 1001: // retirement plan
                case 1002: //
            }
        }
        VendorItem vendorItem = idItemMap.get(id);
        if (vendorItem != null) {
            int cost = vendorItem.getCost();
            if (canAfford(cost)) {
                ItemStack itemStack = getItemStackFromID(vendorItem.getItemID(), vendorItem.getQuantity());
                if (itemStack != null) {
                    player.getWorld().playSound(null,blockPos,DANDYCORPCobblemonAdditions.VENDOR_BUY_EVENT, SoundCategory.MASTER, 1.0f,(float) (0.9f + (0.2*Math.random())));
                    purchase(player, itemStack, vendorItem.getQuantity(), cost);
                    return true;
                } else {
                    player.sendMessage(Text.literal("Item not found: " + vendorItem.getItemID()));
                }
            } else {
                player.sendMessage(Text.translatable("ui.dccobblemon.vendor.poor"));
                player.playSound(DANDYCORPCobblemonAdditions.VENDOR_POOR_EVENT, SoundCategory.MASTER,1.0f,(float) (0.9f + (0.2*Math.random())));
            }
        } else {
            player.sendMessage(Text.literal("Invalid button ID: " + id));
        }
        return false;
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

    private void buildButtonIdToVendorItemMap() {
        if (data != null && data.getCategories() != null) {
            for (VendorCategory category : data.getCategories()) {
                if (category.getItems() != null) {
                    for (VendorItem item : category.getItems()) {
                        idItemMap.put(item.getButtonID(), item);
                    }
                }
            }
        }
    }

    public ItemStack getItemStackFromID(String itemID, int quantity) {
        Identifier identifier = new Identifier(itemID);
        if (Registries.ITEM.containsId(identifier)) {
            Item item = Registries.ITEM.get(identifier);
            return new ItemStack(item, quantity);
        } else {
            System.err.println("Item not found: " + itemID);
            return null;
        }
    }


    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);

        player.playerScreenHandler.sendContentUpdates();
        this.sendContentUpdates();
    }

    public VendorData getVendorData() {
        return data;
    }
}
