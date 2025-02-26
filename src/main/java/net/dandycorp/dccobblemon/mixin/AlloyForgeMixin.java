package net.dandycorp.dccobblemon.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.oracle.truffle.api.impl.asm.Opcodes;
import net.dandycorp.dccobblemon.item.DANDYCORPItems;
import net.dandycorp.dccobblemon.util.ScreenShake;
import net.dandycorp.dccobblemon.util.ScreenShakeController;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import wraith.alloyforgery.block.ForgeControllerBlockEntity;
import wraith.alloyforgery.recipe.AlloyForgeRecipe;

@Mixin(ForgeControllerBlockEntity.class)
public abstract class AlloyForgeMixin {

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lwraith/alloyforgery/recipe/AlloyForgeRecipe;craft(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/registry/DynamicRegistryManager;)Lnet/minecraft/item/ItemStack;",
                    shift = At.Shift.AFTER
            )
    )
    private void craft(CallbackInfo ci, @Local(ordinal = 0) ItemStack recipeOutput) {
        ForgeControllerBlockEntity self = (ForgeControllerBlockEntity)(Object)this;
        World world = self.getWorld();
        if (world == null || world.isClient) return;

        if (recipeOutput.isOf(DANDYCORPItems.PARAGONIUM_INGOT)) {
            ScreenShake shake = new ScreenShake(0.4f, 20, 120, ScreenShakeController.FadeType.REVERSE_EXPONENTIAL);
            ScreenShakeController.causeTremorWithSound(
                    self.getWorld(),
                    self.getPos(),
                    20,
                    shake,
                    ScreenShakeController.DistanceFalloff.LINEAR,
                    SoundEvents.ITEM_TOTEM_USE,
                    SoundCategory.BLOCKS,
                    0.1f,
                    1.9f
            );
        }
    }
}
