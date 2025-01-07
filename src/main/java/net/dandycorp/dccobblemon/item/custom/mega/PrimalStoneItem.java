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

public class PrimalStoneItem extends Item{

    String speciesName;

    public PrimalStoneItem (Item.Settings settings, String species) {
        super(settings);
        this.speciesName = species;
    }

    public String getSpeciesName() {
        return speciesName;
    }

    @Override
    public void appendTooltip(ItemStack itemStack, @Nullable World world, List<Text> list, TooltipContext tooltipContext) {
        super.appendTooltip(itemStack, world, list, tooltipContext);
        Species species = PokemonSpecies.INSTANCE.getByName(getSpeciesName());
        list.addAll(
                TextUtils.wrapText(Text.translatable("item.dccobblemon.primal_stone.description1")
                                .append(species.getTranslatedName())
                                .append(Text.translatable("item.dccobblemon.primal_stone.description2")),
                        30
                )
        );
    }
}
