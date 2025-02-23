package net.dandycorp.dccobblemon.item.custom;

import com.cobblemon.mod.common.api.types.ElementalType;
import com.cobblemon.mod.common.api.types.ElementalTypes;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import dev.emi.trinkets.api.TrinketsApi;
import net.dandycorp.dccobblemon.item.DANDYCORPItems;
import net.dandycorp.dccobblemon.util.GradientFormatting;
import net.dandycorp.dccobblemon.util.TextUtils;
import net.minecraft.client.MinecraftClient;
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
import java.util.StringJoiner;

public class BadgeItem extends TrinketItem implements GradientFormatting {

    public final List<ElementalType> types;

    public BadgeItem(Settings settings, List<ElementalType> elementalTypes) {
        super(settings);
        types = elementalTypes;
    }

    public static <T extends LivingEntity> boolean isEquipped(T entity, Item badge){
        return TrinketsApi.getTrinketComponent(entity)
                .map(trinketComponent ->
                        trinketComponent.getAllEquipped().stream()
                                .map(Pair::getRight)
                                .map(ItemStack::getItem)
                                .anyMatch(item -> item == badge)
                )
                .orElse(false);
    }

    public static boolean isChallenge(ItemStack stack){
        if (stack.hasNbt() && stack.getNbt() != null) {
            return stack.getNbt().getBoolean("Challenge");
        }
        return false;
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
        return isChallenge(stack);
    }

    @Override
    public Text getName(ItemStack stack) {
        if(isChallenge(stack)){
            return super.getName(stack).copy().formatted(Formatting.BOLD);
        }
        return super.getName(stack);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext tooltipContext) {
        super.appendTooltip(stack, world, tooltip, tooltipContext);
        if (stack.hasNbt() && stack.getNbt().contains("OwnerName") && stack.getNbt().contains("Owner")) {
            tooltip.add(Text.literal(stack.getNbt().getString("OwnerName")).styled(style ->
                    MinecraftClient.getInstance().getSession().getUuidOrNull().equals(stack.getNbt().getUuid("Owner"))
                    ? style.withColor(Formatting.WHITE) : style.withColor(Formatting.DARK_RED)));
        }

        if (isChallenge(stack)) {
            tooltip.add(Text.literal(""));
            tooltip.add(gradientText(Text.literal("(Challenge Mode)")));
            tooltip.add(Text.literal(""));
            if(types.equals(ElementalTypes.INSTANCE.all())){
                tooltip.addAll(
                        TextUtils.wrapText(Text.literal("Increased shiny odds and catch rates for all pokemon").formatted(Formatting.WHITE),40));
            }
            else {
                tooltip.addAll(TextUtils.wrapText(
                        Text.literal("Increased shiny odds and catch rates for " + getTypeNames(getTypes()) + " pokemon").formatted(Formatting.WHITE),40));
            }
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

    public List<ElementalType> getTypes() {
        return types;
    }

    private String getTypeNames(List<ElementalType> types) {
        List<String> names = types.stream()
                .map(type -> type.getDisplayName().getString())
                .toList();
        int size = names.size();
        if (size == 0) {
            return "";
        } else if (size == 1) {
            return names.get(0);
        } else if (size == 2) {
            return names.get(0) + " and " + names.get(1);
        } else {
            StringJoiner joiner = new StringJoiner(", ");
            for (int i = 0; i < size - 1; i++) {
                joiner.add(names.get(i));
            }
            return joiner.toString() + ", and " + names.get(size - 1);
        }
    }

    @Override
    public int getGradientStartColor() {
        return 0x727A9A;
    }

    @Override
    public int getGradientEndColor() {
        return 0xD8DBE9;
    }

    @Override
    public boolean isBold() {
        return false;
    }
}
