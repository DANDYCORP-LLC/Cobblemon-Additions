package net.dandycorp.dccobblemon.compat;

import com.cobblemon.mod.common.api.pokeball.catching.CaptureContext;
import com.cobblemon.mod.common.api.pokeball.catching.CatchRateModifier;
import com.cobblemon.mod.common.api.pokeball.catching.calculators.CaptureCalculator;
import com.cobblemon.mod.common.entity.pokeball.EmptyPokeBallEntity;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class BadgeCatchRateModifier implements CaptureCalculator {
    @Override
    public float getCatchRate(@NotNull LivingEntity livingEntity, @NotNull EmptyPokeBallEntity emptyPokeBallEntity, @NotNull PokemonEntity pokemonEntity, float v) {
        return 0;
    }

    @Override
    public @NotNull String id() {
        return "";
    }

    @Override
    public @NotNull CaptureContext processCapture(@NotNull LivingEntity livingEntity, @NotNull EmptyPokeBallEntity emptyPokeBallEntity, @NotNull PokemonEntity pokemonEntity) {
        return null;
    }
}
