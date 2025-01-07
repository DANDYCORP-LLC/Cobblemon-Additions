package net.dandycorp.dccobblemon.util;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.pokemon.feature.FlagSpeciesFeature;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import dev.emi.trinkets.api.TrinketsApi;
import net.dandycorp.dccobblemon.item.custom.mega.MegaFormType;
import net.dandycorp.dccobblemon.item.custom.mega.MegaStoneItem;
import net.dandycorp.dccobblemon.item.custom.mega.MegaTrinketItem;
import net.dandycorp.dccobblemon.item.custom.mega.PrimalStoneItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.List;

public class MegaUtils {

    public static void evolve(ServerPlayerEntity p){
        if(!hasKeystone(p)) return;
        PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(p);
        if(party == null) return;
        party.forEach(pokemon -> {
            if (pokemon.heldItem().getItem() instanceof MegaStoneItem stone) {
                List<String> aspects = pokemon.getForm().getAspects();
                if((stone.getType() == MegaFormType.MEGA && aspects.contains("mega"))
                        || (stone.getType() == MegaFormType.MEGA_X && aspects.contains("mega-x"))
                        || (stone.getType() == MegaFormType.MEGA_Y && aspects.contains("mega-y"))) return;
                if(pokemon.showdownId().equals(stone.getSpeciesName())){
                    switch(stone.getType()){
                        case MEGA -> new FlagSpeciesFeature("mega",true).apply(pokemon);
                        case MEGA_X -> new FlagSpeciesFeature("mega-x",true).apply(pokemon);
                        case MEGA_Y -> new FlagSpeciesFeature("mega-y",true).apply(pokemon);
                    }
                    pokemon.updateAspects();
                    pokemon.updateForm();
                }
            }
            if (pokemon.heldItem().getItem() instanceof PrimalStoneItem stone) {
                List<String> aspects = pokemon.getForm().getAspects();
                if(aspects.contains("primal")) return;
                if(pokemon.showdownId().equals(stone.getSpeciesName())){
                    new FlagSpeciesFeature("primal",true).apply(pokemon);
                }
                pokemon.updateAspects();
                pokemon.updateForm();
            }
        });
    }

    public static void devolve(ServerPlayerEntity p){
        PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(p);
        if(party == null) return;
        party.forEach(pokemon -> {
            new FlagSpeciesFeature("mega",false).apply(pokemon);
            new FlagSpeciesFeature("mega-x",false).apply(pokemon);
            new FlagSpeciesFeature("mega-y",false).apply(pokemon);
            new FlagSpeciesFeature("primal",false).apply(pokemon);
            pokemon.updateAspects();
            pokemon.updateForm();
        });
    }

    public static void evolveOrDevolve(ServerPlayerEntity p){
        if(!hasKeystone(p)) return;
        PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(p);
        if(party == null) return;
        party.forEach(pokemon -> {
            if (pokemon.heldItem().getItem() instanceof MegaStoneItem stone) {
                List<String> aspects = pokemon.getForm().getAspects();
                if((stone.getType() == MegaFormType.MEGA && aspects.contains("mega"))
                        || (stone.getType() == MegaFormType.MEGA_X && aspects.contains("mega-x"))
                        || (stone.getType() == MegaFormType.MEGA_Y && aspects.contains("mega-y"))) return;
                if(pokemon.showdownId().equals(stone.getSpeciesName())){
                    switch(stone.getType()){
                        case MEGA -> new FlagSpeciesFeature("mega",true).apply(pokemon);
                        case MEGA_X -> new FlagSpeciesFeature("mega-x",true).apply(pokemon);
                        case MEGA_Y -> new FlagSpeciesFeature("mega-y",true).apply(pokemon);
                    }
                } else {
                    devolve(p);
                }
            }
            else if (pokemon.heldItem().getItem() instanceof PrimalStoneItem stone) {
                List<String> aspects = pokemon.getForm().getAspects();
                if(aspects.contains("primal")) return;
                if(pokemon.showdownId().equals(stone.getSpeciesName())){
                    new FlagSpeciesFeature("primal",true).apply(pokemon);
                } else {
                    devolve(p);
                }
                pokemon.updateAspects();
                pokemon.updateForm();
            }
            else {
                devolve(p);
            }
            pokemon.updateAspects();
            pokemon.updateForm();
        });
    }

    public static boolean hasKeystone(ServerPlayerEntity p){
        return TrinketsApi.getTrinketComponent(p).map(comp ->
                comp.getAllEquipped().stream().anyMatch(
                        pair -> {
                            ItemStack stack = pair.getRight();
                            return stack.getItem() instanceof MegaTrinketItem &&
                                    stack.getOrCreateNbt().getBoolean("keyStone");
                        }
                )).orElse(false);
    }
}
