package net.dandycorp.dccobblemon.mixin;

import net.dandycorp.dccobblemon.item.DANDYCORPItems;
import net.dandycorp.dccobblemon.item.custom.BadgeItem;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ExperienceOrbEntity.class)
public abstract class ExperienceOrbMixin {

    @Redirect(
            method = "onPlayerCollision",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;addExperience(I)V"
            )
    )
    private void redirectAddExperience(PlayerEntity player, int experience) {
        if (BadgeItem.isEquipped(player, DANDYCORPItems.PSYCHIC_BADGE)) {
            player.addExperience((int) Math.ceil(experience * 1.5));
        } else {
            player.addExperience(experience);
        }
    }
}

