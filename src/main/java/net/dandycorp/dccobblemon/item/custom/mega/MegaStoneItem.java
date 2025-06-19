package net.dandycorp.dccobblemon.item.custom.mega;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.Species;
import net.dandycorp.dccobblemon.util.TextUtils;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MegaStoneItem extends Item {

    String speciesName;
    String stoneName;
    MegaFormType type;

    public MegaStoneItem(Settings settings, String species, String stoneName, MegaFormType type) {
        super(settings);
        this.speciesName = species;
        this.type = type;
        this.stoneName = stoneName;
    }

    public String getSpeciesName() {
        return speciesName;
    }

    public MegaFormType getType() {
        return type;
    }

    @Override
    public void appendTooltip(ItemStack itemStack, @Nullable World world, List<Text> list, TooltipContext tooltipContext) {
        super.appendTooltip(itemStack, world, list, tooltipContext);
        Species species = PokemonSpecies.INSTANCE.getByName(getSpeciesName());
        list.addAll(
                TextUtils.wrapText(Text.translatable("item.dccobblemon.mega_stone.description1")
                                .append(species.getTranslatedName())
                                .append(Text.translatable("item.dccobblemon.mega_stone.description2")),
                        30
                )
            );
    }

    @Override
    public ItemStack getDefaultStack() {
        ItemStack stack = super.getDefaultStack();
        NbtCompound nbt = stack.getNbt();
        if (nbt == null || !stoneName.equals(nbt.getString("megaStone"))) {
            NbtCompound newTag = new NbtCompound();
            newTag.putString("megaStone", stoneName);
            stack.setNbt(newTag);
        }
        return stack;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int i, boolean bl) {
        super.inventoryTick(stack, world, entity, i, bl);
        NbtCompound nbt = stack.getNbt();
        if (nbt == null || !stoneName.equals(nbt.getString("megaStone"))) {
            NbtCompound newTag = new NbtCompound();
            newTag.putString("megaStone", stoneName);
            stack.setNbt(newTag);
        }
    }
}
