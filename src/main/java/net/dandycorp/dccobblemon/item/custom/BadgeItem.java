package net.dandycorp.dccobblemon.item.custom;

import dev.emi.trinkets.api.TrinketItem;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;

public class BadgeItem extends TrinketItem {

    public BadgeItem(Settings settings) {
        super(settings);
    }

    public boolean isEquipped(LivingEntity entity, Item badge){
        return TrinketsApi.getTrinketComponent(entity)
                .map(trinketComponent ->
                        trinketComponent.getAllEquipped().stream()
                                .map(Pair::getRight) // Get the ItemStack from the pair
                                .map(ItemStack::getItem) // Get the Item from the ItemStack
                                .anyMatch(item -> item == badge)
                )
                .orElse(false);
    }

}
