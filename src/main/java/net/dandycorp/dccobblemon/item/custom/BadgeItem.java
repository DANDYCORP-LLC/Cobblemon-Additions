package net.dandycorp.dccobblemon.item.custom;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Pair;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BadgeItem extends TrinketItem {

    public BadgeItem(Settings settings) {
        super(settings);
    }

    public static boolean isEquipped(LivingEntity entity, Item badge){
        return TrinketsApi.getTrinketComponent(entity)
                .map(trinketComponent ->
                        trinketComponent.getAllEquipped().stream()
                                .map(Pair::getRight) // Get the ItemStack from the pair
                                .map(ItemStack::getItem) // Get the Item from the ItemStack
                                .anyMatch(item -> item == badge)
                )
                .orElse(false);
    }

    @Override
    public boolean canEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if (stack.hasNbt() && stack.getNbt().containsUuid("Owner")) {
            return stack.getNbt().getUuid("Owner").equals(entity.getUuid());
        } else {
            return false;
        }
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        if (stack.hasNbt() && stack.getNbt().contains("Challenge")) {
            return stack.getNbt().getBoolean("Challenge");
        } else {
            return false;
        }
    }

    @Override
    public Text getName(ItemStack stack) {
        if(hasGlint(stack)){
            return super.getName(stack).copy().formatted(Formatting.BOLD);
        }
        return super.getName(stack);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext tooltipContext) {
        super.appendTooltip(stack, world, tooltip, tooltipContext);

        if (stack.hasNbt() && stack.getNbt().contains("OwnerName")) {
            tooltip.add(Text.literal(stack.getNbt().getString("OwnerName")));
            tooltip.add(Text.literal(""));
        }

        if (hasGlint(stack)) {
            tooltip.add(Text.literal("(Challenge Mode)").styled(style -> style.withBold(true).withColor(Formatting.GOLD)));
            tooltip.add(Text.literal(""));
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int i, boolean bl) {
        if((stack.hasNbt() && stack.getNbt().containsUuid("Owner") && stack.getNbt().contains("OwnerName")) || !entity.isPlayer()) return;
        NbtCompound nbt = stack.getOrCreateNbt();
        if (!nbt.containsUuid("Owner")) {
            nbt.putUuid("Owner", entity.getUuid());
        }
        if (!nbt.contains("OwnerName")){
            nbt.putString("OwnerName", entity.getName().getString());
        }
    }
}
