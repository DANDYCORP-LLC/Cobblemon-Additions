package net.dandycorp.dccobblemon.mixin;

import dev.emi.trinkets.api.TrinketsApi;
import net.dandycorp.dccobblemon.item.Items;
import net.minecraft.block.PowderSnowBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PowderSnowBlock.class)
public class PowderedSnowMixin {

    @Inject(method="canWalkOnPowderSnow",at=@At("HEAD"), cancellable = true)
    private static void canWalkOnPowderSnow(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof LivingEntity livingEntity) {
            //entity.sendMessage(Text.literal("mixin!"));
            // Check if the entity is wearing the ICE_BADGE
            boolean hasIceBadge = TrinketsApi.getTrinketComponent(livingEntity).map(trinketComponent ->
                    trinketComponent.isEquipped(Items.ICE_BADGE)
            ).orElse(false);

            if (hasIceBadge) {
                //entity.sendMessage(Text.literal("badge!"));
                cir.setReturnValue(true);
            }
        }
    }

}
