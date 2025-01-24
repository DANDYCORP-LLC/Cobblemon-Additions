package net.dandycorp.dccobblemon.mixin;

import dev.emi.trinkets.api.TrinketsApi;
import net.dandycorp.dccobblemon.item.DANDYCORPItems;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public class EnchantmentMixin {


    @Inject(method = "getSwiftSneakSpeedBoost", at=@At("RETURN"), cancellable = true)
    private static void getSwiftSneakSpeedBoost(LivingEntity entity, CallbackInfoReturnable<Float> cir) {
        if (entity instanceof PlayerEntity player) {
            TrinketsApi.getTrinketComponent(player).ifPresent(trinketComponent -> {
                if (trinketComponent.isEquipped(DANDYCORPItems.PUMPKIN_BADGE)) {
                    cir.setReturnValue(cir.getReturnValue() + 0.9f);
                }
            });
        }
    }

    @Inject(method = "getDepthStrider", at=@At("RETURN"), cancellable = true)
    private static void getDepthStrider(LivingEntity entity, CallbackInfoReturnable<Integer> cir) {
        if (entity instanceof PlayerEntity player) {
            TrinketsApi.getTrinketComponent(player).ifPresent(trinketComponent -> {
                if (trinketComponent.isEquipped(DANDYCORPItems.WATER_BADGE)) {
                    cir.setReturnValue(cir.getReturnValueI()+3);
                }
            });
        }
    }

    @Inject(method = "hasAquaAffinity", at=@At("RETURN"), cancellable = true)
    private static void hasAquaAffinity(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof PlayerEntity player) {
            TrinketsApi.getTrinketComponent(player).ifPresent(trinketComponent -> {
                if (trinketComponent.isEquipped(DANDYCORPItems.WATER_BADGE)) {
                    cir.setReturnValue(true);
                }
            });
        }
    }

    @Inject(method = "getEfficiency", at=@At("RETURN"), cancellable = true)
    private static void getEfficiency(LivingEntity entity, CallbackInfoReturnable<Integer> cir) {
        if (entity instanceof PlayerEntity player) {
            TrinketsApi.getTrinketComponent(player).ifPresent(trinketComponent -> {
                if (trinketComponent.isEquipped(DANDYCORPItems.GROUND_BADGE)) {
                    cir.setReturnValue(cir.getReturnValueI()+3);
                }
            });
        }
    }


}
