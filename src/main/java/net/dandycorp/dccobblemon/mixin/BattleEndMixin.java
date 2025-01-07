package net.dandycorp.dccobblemon.mixin;

import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import net.dandycorp.dccobblemon.util.MegaUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PokemonBattle.class)
public class BattleEndMixin {
    @Inject(method = "end", at = @At("RETURN"), remap = false)
    private void end(CallbackInfo ci){
        PokemonBattle battle = (PokemonBattle) (Object) this;
        battle.getPlayers().forEach(MegaUtils::evolve);
    }
}
