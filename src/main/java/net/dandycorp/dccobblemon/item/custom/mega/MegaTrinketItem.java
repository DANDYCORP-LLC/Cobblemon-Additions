package net.dandycorp.dccobblemon.item.custom.mega;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.pokemon.feature.FlagSpeciesFeature;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import dev.emi.trinkets.api.TrinketsApi;
import net.dandycorp.dccobblemon.util.MegaUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;

import java.util.Set;

public class MegaTrinketItem extends TrinketItem {
    public MegaTrinketItem(Settings settings) {
        super(settings);
    }

    public boolean isEquipped(LivingEntity entity){
        return TrinketsApi.getTrinketComponent(entity).stream().anyMatch(p -> p instanceof MegaTrinketItem);
    }

    @Override
    public boolean onStackClicked(ItemStack itemStack, Slot slot, ClickType clickType, PlayerEntity playerEntity) {
        NbtCompound nbtCompound = itemStack.getOrCreateNbt();
        nbtCompound.putBoolean("keyStone",true);
        itemStack.setNbt(nbtCompound);
        return super.onStackClicked(itemStack, slot, clickType, playerEntity);
    }

    @Override
    public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        super.onEquip(stack, slot, entity);
        if(entity instanceof ServerPlayerEntity p){
            MegaUtils.evolve(p);
        }
        NbtCompound nbtCompound = stack.getOrCreateNbt();
        nbtCompound.putBoolean("keyStone",true);
        stack.setNbt(nbtCompound);
    }

    @Override
    public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        super.onUnequip(stack, slot, entity);
        if(entity instanceof ServerPlayerEntity p){
            MegaUtils.devolve(p);
        }
        NbtCompound nbtCompound = stack.getOrCreateNbt();
        nbtCompound.putBoolean("keyStone",true);
        stack.setNbt(nbtCompound);
    }

    @Override
    public ItemStack getDefaultStack() {
        ItemStack defaultStack = super.getDefaultStack();
        NbtCompound nbtCompound = defaultStack.getOrCreateNbt();
        nbtCompound.putBoolean("keyStone",true);
        defaultStack.setNbt(nbtCompound);
        return defaultStack;
    }
}
