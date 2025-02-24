package net.dandycorp.dccobblemon.mixin;

import net.dandycorp.dccobblemon.util.ScreenShakeController;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions.RANDOM;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Shadow
    protected abstract void moveBy(double x, double y, double z);

    @Shadow public abstract void reset();

    @Inject(
            method = "update",
            at = @At(
                    // Inject before the call to clipToSpace
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Camera;setPos(DDD)V",
                    shift = At.Shift.BY,
                    by = 1
            )
    )
    private void camerashake$onUpdate(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        if (ScreenShakeController.isShaking()) {
            float intensity = ScreenShakeController.getAndUpdateIntensity();
            if (intensity > 0.0f) {
                float offsetY = RANDOM.nextFloat(-intensity, intensity);
                float offsetZ = RANDOM.nextFloat(-intensity, intensity);
                moveBy(0, offsetY, offsetZ);
            }
        }
    }
}
