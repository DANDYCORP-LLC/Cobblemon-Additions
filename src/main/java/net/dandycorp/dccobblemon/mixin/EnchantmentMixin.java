package net.dandycorp.dccobblemon.mixin;

import dev.emi.trinkets.api.TrinketsApi;
import net.dandycorp.dccobblemon.item.Items;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.enchantment.EnchantmentHelper.getEquipmentLevel;

@Mixin(EnchantmentHelper.class)
public class EnchantmentMixin {


    @Inject(method = "getSwiftSneakSpeedBoost", at=@At("RETURN"), cancellable = true)
    private static void getSwiftSneakSpeedBoost(LivingEntity entity, CallbackInfoReturnable<Float> cir) {
        if (entity instanceof PlayerEntity player) {
            TrinketsApi.getTrinketComponent(player).ifPresent(trinketComponent -> {
                if (trinketComponent.isEquipped(Items.PUMPKIN_BADGE)) {
                    cir.setReturnValue(cir.getReturnValue() + 0.9f);
                }
            });
        }
    }

    @Inject(method = "getDepthStrider", at=@At("RETURN"), cancellable = true)
    private static void getDepthStrider(LivingEntity entity, CallbackInfoReturnable<Integer> cir) {
        if (entity instanceof PlayerEntity player) {
            TrinketsApi.getTrinketComponent(player).ifPresent(trinketComponent -> {
                if (trinketComponent.isEquipped(Items.WATER_BADGE)) {
                    cir.setReturnValue(cir.getReturnValueI()+3);
                }
            });
        }
    }

    @Inject(method = "getEfficiency", at=@At("RETURN"), cancellable = true)
    private static void getEfficiency(LivingEntity entity, CallbackInfoReturnable<Integer> cir) {
        if (entity instanceof PlayerEntity player) {
            TrinketsApi.getTrinketComponent(player).ifPresent(trinketComponent -> {
                if (trinketComponent.isEquipped(Items.GROUND_BADGE)) {
                    cir.setReturnValue(cir.getReturnValueI()+3);
                }
            });
        }
    }


}
