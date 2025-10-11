package net.dandycorp.dccobblemon.mixin;

import net.dandycorp.dccobblemon.DANDYCORPDamageTypes;
import net.dandycorp.dccobblemon.entities.CannonballEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static com.oracle.truffle.js.builtins.RegExpPrototypeBuiltins.RegExpPrototypeGetterBuiltins.RegExpPrototypeGetters.source;
import static net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions.LOGGER;

@Mixin(Explosion.class)
public class ExplosionMixin {
    @Redirect(
            method = "collectBlocksAndDamageEntities",
            at     = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Entity;"
                            + "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"
            )
    )
    private boolean dccobblemon$redirectExplosionDamage(Entity instance, DamageSource damageSource, float f) {
        if (damageSource.isOf(DANDYCORPDamageTypes.HAIL_MARY_EXPLOSION)) {
            if(damageSource.getAttacker() == instance) {
                f *= 0.2f;
            }
        }
        return instance.damage(damageSource, f);
    }
}
