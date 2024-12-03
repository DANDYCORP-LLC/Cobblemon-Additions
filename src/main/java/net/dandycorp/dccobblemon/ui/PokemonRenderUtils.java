package net.dandycorp.dccobblemon.ui;

import com.cobblemon.mod.common.api.gui.GuiUtilsKt;
import com.cobblemon.mod.common.client.render.models.blockbench.PoseableEntityState;
import com.cobblemon.mod.common.client.render.models.blockbench.pokemon.PokemonFloatingState;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.client.util.math.MatrixStack;

public class PokemonRenderUtils {

    private static final float BASE_MODEL_SIZE = 21.0f;

    public static void renderPokemon(Pokemon pokemon, MatrixStack stack, int x, int y, int width, int height, boolean reversed, float partialTicks) {
        stack.push();

        stack.translate(x, y, 0);

        float scaleX = (float) width / BASE_MODEL_SIZE;
        float scaleY = (float) height / BASE_MODEL_SIZE;
        float scale = Math.min(scaleX, scaleY);

        double centerX = width / 2.0;
        double centerY = height / 2.0;

        stack.translate(centerX, centerY, 0);
        stack.scale(scale, scale, 1);
        stack.translate(0.0, -BASE_MODEL_SIZE, 0.0);

        PoseableEntityState<PokemonEntity> state = new PokemonFloatingState();
        GuiUtilsKt.drawPortraitPokemon(pokemon.getSpecies(), pokemon.getAspects(), stack, 13f, reversed, state, partialTicks);

        stack.pop();
    }
}

