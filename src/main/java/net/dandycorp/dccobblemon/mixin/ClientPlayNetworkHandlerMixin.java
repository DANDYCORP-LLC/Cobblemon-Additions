package net.dandycorp.dccobblemon.mixin;

import net.dandycorp.dccobblemon.item.DANDYCORPItems;
import net.dandycorp.dccobblemon.item.custom.BadgeItem;
import net.dandycorp.dccobblemon.util.ScreenShakeController;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {

    @Inject(
            method = "getActiveTotemOfUndying(Lnet/minecraft/entity/player/PlayerEntity;)Lnet/minecraft/item/ItemStack;",
            at = @At("RETURN"),
            cancellable = true
    )
    private static void injectCustomActiveTotem(PlayerEntity player, CallbackInfoReturnable<ItemStack> cir) {
        if (BadgeItem.isEquipped(player, DANDYCORPItems.DAVID_BADGE)) {
            cir.setReturnValue(new ItemStack(DANDYCORPItems.DAVID_BADGE));
        }
        ScreenShakeController.startShake(0.3f,10,20, ScreenShakeController.FadeType.LINEAR);
    }
}
