package net.dandycorp.dccobblemon.mixin;

import net.dandycorp.dccobblemon.item.DANDYCORPItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PersistentProjectileEntity.class)
public class ProjectileMixin {

    @Inject(
            method = "onEntityHit",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V",
                    ordinal = 0
            ),
            cancellable = true
    )
    private void paragoniumBounceVelocity(EntityHitResult entityHitResult, CallbackInfo ci){
        if(blockWithParagoniumShield(entityHitResult)) {
            PlayerEntity player = (PlayerEntity) entityHitResult.getEntity();
            PersistentProjectileEntity projectile = (PersistentProjectileEntity) (Object) this;
            double oldSpeed = projectile.getVelocity().length();

            float yawRadians   = (float)Math.toRadians(player.getYaw());
            float pitchRadians = (float)Math.toRadians(player.getPitch());
            
            double dirX = -Math.cos(pitchRadians) * Math.sin(yawRadians);
            double dirY = -Math.sin(pitchRadians);
            double dirZ =  Math.cos(pitchRadians) * Math.cos(yawRadians);

            Vec3d newVel = new Vec3d(dirX, dirY, dirZ).normalize().multiply(oldSpeed);

            projectile.setVelocity(newVel);
            projectile.setYaw(player.getYaw());
            projectile.setPitch(player.getPitch());
            projectile.prevYaw   = projectile.getYaw();
            projectile.prevPitch = projectile.getPitch();
            ci.cancel();
        }
    }

    @Unique
    private boolean blockWithParagoniumShield(EntityHitResult entityHitResult){
        Entity target = entityHitResult.getEntity();
        if(target instanceof PlayerEntity player && !target.getWorld().isClient()) {
            return player.getActiveItem().isOf(DANDYCORPItems.PARAGONIUM_SHIELD);
        }
        return false;
    }
}
