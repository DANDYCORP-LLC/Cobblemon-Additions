package net.dandycorp.dccobblemon.ui.vendor;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions;
import net.dandycorp.dccobblemon.DANDYCORPDamageTypes;
import net.dandycorp.dccobblemon.block.custom.VendorBlockEntity;
import net.dandycorp.dccobblemon.item.Items;
import net.dandycorp.dccobblemon.ui.PokemonComponent;
import net.dandycorp.dccobblemon.util.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cobblemon.mod.common.CobblemonSounds.FOSSIL_MACHINE_FINISHED;

public class VendorScreenHandler extends ScreenHandler {

    private final BlockPos blockPos;
    private VendorBlockEntity blockEntity;
    private final PlayerInventory playerInventory;
    private final VendorData data;
    private final Map<Integer, VendorEntry> idEntryMap = new HashMap<>();
    private final List<Integer> specialIds = Arrays.asList(1000, 1001, 1002);

    public VendorScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(DANDYCORPCobblemonAdditions.VENDOR_SCREEN_HANDLER, syncId);
        this.playerInventory = playerInventory;
        this.blockPos = playerInventory.player.getBlockPos();
        addPlayerInventorySlots(playerInventory);
        this.data = VendorDataLoader.loadVendorData();
        buildButtonIdToVendorItemMap();
    }

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
            int cost = 1;
            if(canAfford(cost)) {
                spendTickets(player, cost);
                switch (id) {
                    case 1000: // compliment
                        player.getWorld().playSound(null,blockPos,DANDYCORPCobblemonAdditions.COMPLIMENT_EVENT,SoundCategory.BLOCKS,0.7f,1.1f);
                        player.closeHandledScreen();
                        return true;
                    case 1001: // retirement
                        player.closeHandledScreen();
                        if (player instanceof ServerPlayerEntity serverPlayer) {
                            player.damage(DANDYCORPDamageTypes.of(serverPlayer.getServerWorld(),DANDYCORPDamageTypes.VENDOR), player.getHealth());
                            System.out.println("dealing " + player.getHealth() + " damage to " + player.getName());
                            serverPlayer.networkHandler.disconnect(Text.translatable("ui.dccobblemon.vendor.retire_kick"));
                        }
                        return true;
                    case 1002: // donor head
                        player.getWorld().playSound(null, blockPos, DANDYCORPCobblemonAdditions.VENDOR_BUY_EVENT, SoundCategory.MASTER, 1.0f, (float) (0.9f + (0.2 * Math.random())));
                        ItemStack donorHead = HeadHelper.getPlayerHead(HeadHelper.getRandomDonor());
                        spawnItems(donorHead);
                        return true;
                }
            } else {
                player.sendMessage(Text.translatable("ui.dccobblemon.vendor.poor"));
                player.playSound(DANDYCORPCobblemonAdditions.VENDOR_POOR_EVENT, SoundCategory.MASTER, 1.0f, (float) (0.9f + (0.2 * Math.random())));
                return true;
            }
        }

        VendorEntry vendorEntry = idEntryMap.get(id);
        if (vendorEntry != null && !vendorEntry.getItems().isEmpty()) {
            int cost = vendorEntry.getCost();
            if (canAfford(cost)) {
                player.getWorld().playSound(null, blockPos, DANDYCORPCobblemonAdditions.VENDOR_BUY_EVENT, SoundCategory.MASTER, 1.0f, (float) (0.9f + (0.2 * Math.random())));
                purchase(player, vendorEntry.getItems(), cost);
                return true;
            } else {
                player.sendMessage(Text.translatable("ui.dccobblemon.vendor.poor"));
                player.playSound(DANDYCORPCobblemonAdditions.VENDOR_POOR_EVENT, SoundCategory.MASTER, 1.0f, (float) (0.9f + (0.2 * Math.random())));
            }
        } else {
            player.sendMessage(Text.literal("Invalid button ID: " + id));
        }
        return false;
    }

    private void purchase(PlayerEntity player, List<VendorItem> items, int cost) {
        if (canAfford(cost)) {
            spendTickets(player, cost);
            if (items != null) {
                for (VendorItem item : items) {
                    String id = item.getId();
                    if(id.startsWith("pokemon:")) {
                        id = id.substring("pokemon:".length());
                        if(player instanceof ServerPlayerEntity serverPlayer) {
                            Pokemon pokemon = PokemonComponent.pokemonFromSplitString(id.split("_"));
                            if(pokemon != null) {
                                Cobblemon.INSTANCE.getStorage().getParty(serverPlayer).add(pokemon);
                                serverPlayer.playSound(FOSSIL_MACHINE_FINISHED,SoundCategory.MASTER,1.0f,1.0f);
                                //player.sendMessage(Text.of(id));
                            }
                        }
                    }
                    else {
                        ItemStack stack = getItemStackFromID(item.getId());
                        if (stack != null) {
                            int quantity = item.getQuantity();
                            // Spawn items one at a time
                            for (int i = 0; i < quantity; i++) {
                                ItemStack singleItemStack = stack.copy();
                                singleItemStack.setCount(1);
                                spawnItems(singleItemStack);
                            }
                        }
                    }
                }
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

    private void spawnItems(ItemStack stack) {
        if (blockEntity != null) {
            blockEntity.addToDispenseQueue(stack.copy());
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
                if (category.getEntries() != null) {
                    for (VendorEntry entry : category.getEntries()) {
                        idEntryMap.put(entry.getButtonID(), entry);
                    }
                }
            }
        }
    }

    public ItemStack getItemStackFromID(String itemID) {
        ItemStack stack = null;

        if (itemID.startsWith("minecraft:player_head#")) {
            stack = HeadHelper.getStackFromItemID(itemID);
            System.out.println("getting player head stack");
        } else {
            Identifier identifier = new Identifier(itemID);
            if (Registries.ITEM.containsId(identifier)) {
                Item item = Registries.ITEM.get(identifier);
                stack = new ItemStack(item);
            } else {
                System.err.println("Item not found: " + itemID);
                return null;
            }
        }
        return stack;
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
