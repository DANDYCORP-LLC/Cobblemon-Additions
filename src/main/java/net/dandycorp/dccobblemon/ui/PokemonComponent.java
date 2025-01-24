package net.dandycorp.dccobblemon.ui;

import com.cobblemon.mod.common.api.gui.GuiUtilsKt;
import com.cobblemon.mod.common.api.pokemon.Natures;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.api.pokemon.stats.Stat;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.client.render.models.blockbench.PoseableEntityState;
import com.cobblemon.mod.common.client.render.models.blockbench.pokemon.PokemonFloatingState;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Gender;
import com.cobblemon.mod.common.pokemon.Nature;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.Species;
import io.wispforest.owo.ui.base.BaseComponent;
import io.wispforest.owo.ui.core.OwoUIDrawContext;
import io.wispforest.owo.ui.core.Sizing;
import net.dandycorp.dccobblemon.item.DANDYCORPItems;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.cobblemon.mod.common.CobblemonItems.CHERISH_BALL;

public class PokemonComponent extends BaseComponent {
    private static final float BASE_MODEL_SIZE = 21.0f;
    private String pokemonName;
    private final boolean reversed;
    private final int componentWidth;
    private final int componentHeight;
    private static final Map<Integer, Stat> IV_MAP = Map.of(
            0,Stats.SPEED,
            1,Stats.HP,
            2,Stats.ATTACK,
            3,Stats.DEFENCE,
            4,Stats.SPECIAL_ATTACK,
            5,Stats.SPECIAL_DEFENCE
    );

    public PokemonComponent(String pokemonName, boolean reversed, int componentWidth, int componentHeight) {
        this.pokemonName = pokemonName;
        this.reversed = reversed;
        this.componentWidth = componentWidth;
        this.componentHeight = componentHeight;

        this.horizontalSizing.set(Sizing.fixed(componentWidth));
        this.verticalSizing.set(Sizing.fixed(componentHeight));
    }

    @Override
    public void draw(OwoUIDrawContext context, int mouseX, int mouseY, float partialTicks, float delta) {
        MatrixStack stack = context.getMatrices();
        stack.push();

        stack.translate(this.x,this.y,0);
        context.enableScissor(this.x, this.y, this.x + this.width, this.y + this.height);
        stack.push();

        String[] tokens = pokemonName.split("_");
        String lastToken = tokens[tokens.length - 1];

        if (lastToken.equalsIgnoreCase("random")) {
            ItemStack randomStack = DANDYCORPItems.BADGE.getDefaultStack();
            stack.scale(this.width/16f,this.height/16f,1);
            context.drawItem(randomStack,0,0);
            stack.pop();
            context.disableScissor();
            stack.pop();
            return;
        }

        float scaleX = (float) this.width / BASE_MODEL_SIZE;
        float scaleY = (float) this.height / BASE_MODEL_SIZE;
        float scale = Math.min(scaleX, scaleY);

        double centerX = this.width / 2.0;
        double centerY = this.height / 2.0;

        stack.translate(centerX, centerY, 0);
        stack.scale(scale, scale, 1);
        stack.translate(0.0, -BASE_MODEL_SIZE, 0.0);

        Species species = PokemonSpecies.INSTANCE.getByName(lastToken);
        if (species == null) {
            System.err.println("PokemonSpecies not found for name: " + pokemonName);
            stack.pop();
            context.disableScissor();
            stack.pop();
            return;
        }

        PoseableEntityState<PokemonEntity> state = new PokemonFloatingState();
        Pokemon pokemon = pokemonFromSplitString(tokens);
        if (pokemon != null) {
            GuiUtilsKt.drawPortraitPokemon(species, pokemon.getAspects(), stack, 13f, reversed, state, partialTicks);
        }

        stack.pop();
        context.disableScissor();
        stack.pop();
    }

    @Override
    protected int determineHorizontalContentSize(Sizing sizing) {
        return componentWidth;
    }

    @Override
    protected int determineVerticalContentSize(Sizing sizing) {
        return componentHeight;
    }


    public static Pokemon pokemonFromSplitString(String[] tokens){
        Pokemon pokemon = new Pokemon();
        if (tokens.length == 0) {
            return null;
        }

        Species species;

        if(tokens[tokens.length - 1].equalsIgnoreCase("random")){
            species = PokemonSpecies.INSTANCE.random();
        } else {
            species = PokemonSpecies.INSTANCE.getByName(tokens[tokens.length - 1]);
        }
        if(species == null){
            return null;
        }
        pokemon.setSpecies(species);
        pokemon.setTradeable(false);
        pokemon.setCaughtBall(CHERISH_BALL.getPokeBall());
        for (String token : tokens){
            switch (token){
                case "shiny":
                    pokemon.setShiny(true);
                    break;
                case "male":
                    if (!(pokemon.getGender() == Gender.GENDERLESS)){
                        pokemon.setGender(Gender.MALE);
                    }
                    break;
                case "female":
                    if (!(pokemon.getGender() == Gender.GENDERLESS)){
                        pokemon.setGender(Gender.FEMALE);
                    }
                    break;
                case "tradeable":
                    pokemon.setTradeable(true);
                    break;
                default:
                    if (token.startsWith("level") || token.startsWith("lvl")){
                        String levelStr = token.replace("level", "").replace("lvl", "");
                        try {
                            pokemon.setLevel(Integer.parseInt(levelStr));
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid level: " + levelStr);
                        }
                    } else if (token.endsWith("nature")){
                        String natureName = token.substring(0, token.length() - "nature".length());
                        Nature nature = Natures.INSTANCE.getNature(natureName);
                        if (nature == null) {
                            System.err.println("Invalid nature: " + natureName);
                        } else {
                            pokemon.setNature(nature);
                        }
                    } else if (token.matches("[1-6]iv")) {
                        setPokemonRandomIVs(pokemon,Integer.parseInt(token.substring(0, 1)));
                    }
                    break;
            }
        }
        return pokemon;
    }



    public static void setPokemonRandomIVs(Pokemon pokemon, int quantity){
        if (quantity>6){
            quantity = 6;
        }
        Random random = new Random();
        List<Integer> visited = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            boolean set = false;
            while (!set) {
                int idx = random.nextInt(IV_MAP.size());
                if(!visited.contains(idx)){
                    Stat stat = IV_MAP.get(idx);
                    pokemon.setIV(stat,31);
                    visited.add(idx);
                    set = true;
                }
            }
        }
    }

    public void setPokemonName(String pokemonName) {
        this.pokemonName = pokemonName;
    }
}
