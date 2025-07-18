package net.dandycorp.dccobblemon.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;

public class TagUtils {
    public static void cleanInventory(ServerPlayerEntity player) {
        for(ItemStack stack : player.getInventory().main) {
            wipeIfEmpty(stack);
        }
        wipeIfEmpty(player.getOffHandStack());
        for(ItemStack stack : player.getInventory().armor) {
            wipeIfEmpty(stack);
        }
    }

    public static void wipeIfEmpty(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return;
        NbtCompound tag = stack.getNbt();
        if (tag != null && tag.isEmpty()) {
            stack.setNbt(null);
        }
    }
}
