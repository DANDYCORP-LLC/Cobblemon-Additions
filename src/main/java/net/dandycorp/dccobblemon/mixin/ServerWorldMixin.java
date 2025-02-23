package net.dandycorp.dccobblemon.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.Monster;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import net.dandycorp.dccobblemon.item.DANDYCORPItems;
import net.dandycorp.dccobblemon.item.custom.BadgeItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {

    @Unique
    private static final double BADGE_RADIUS = 96.0;

    @Inject(method = "spawnEntity", at = @At("HEAD"), cancellable = true)
    private void cancelMobSpawnIfNearBadge(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof Monster) {
            ServerWorld world = (ServerWorld) (Object) this;
            for (ServerPlayerEntity player : world.getPlayers()) {
                if (BadgeItem.isEquipped(player, DANDYCORPItems.FAIRY_BADGE)) {
                    if (player.squaredDistanceTo(entity) <= BADGE_RADIUS * BADGE_RADIUS) {
                        cir.setReturnValue(false);
                        return;
                    }
                }
            }
        }
    }
}
