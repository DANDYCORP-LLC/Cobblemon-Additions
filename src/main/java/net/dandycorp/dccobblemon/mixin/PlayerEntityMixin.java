package net.dandycorp.dccobblemon.mixin;

import net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions;
import net.dandycorp.dccobblemon.attribute.DANDYCORPAttributes;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void infinityGuardTick(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player.isSpectator()) return;

        var shield = DANDYCORPCobblemonAdditions.INFINITY_GUARD.get(player);
        shield.tickShield();
    }

    @ModifyVariable(
            method = "applyDamage",
            at = @At(value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;modifyAppliedDamage(Lnet/minecraft/entity/damage/DamageSource;F)F"),
            ordinal = 0,
            argsOnly = true)
    public float infinityGuardCancel(float amount) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        var shield = DANDYCORPCobblemonAdditions.INFINITY_GUARD.get(player);
        if (shield.getMaxHealth() > 0) {
            float health = shield.shieldHealth;
            shield.damageShield(amount);
            return amount > health ? amount - health : 0;
        }
        return amount;
    }

    @Inject(method = "createPlayerAttributes", at = @At("RETURN"))
    private static void addInfinityGuardAttribute(
            CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir
    ) {
        cir.getReturnValue().add(DANDYCORPAttributes.INFINITY_GUARD);
    }
}
