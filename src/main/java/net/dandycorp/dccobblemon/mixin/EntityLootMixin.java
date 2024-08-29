package net.dandycorp.dccobblemon.mixin;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketsApi;
import net.dandycorp.dccobblemon.DANDYCORPDamageTypes;
import net.dandycorp.dccobblemon.item.Items;
import net.dandycorp.dccobblemon.item.custom.BadgeItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(LivingEntity.class)
public abstract class EntityLootMixin {

    @Shadow protected abstract void dropLoot(DamageSource damageSource, boolean causedByPlayer);

    @Shadow protected abstract void dropXp();

    @Inject(method="drop",at=@At("TAIL"))
    private void drop(DamageSource source, CallbackInfo ci) {
        if(source.getAttacker() instanceof PlayerEntity player) {
            LivingEntity entity = (LivingEntity) (Object) this;

            TrinketsApi.getTrinketComponent(player).ifPresent(trinketComponent -> {
                // Get all non-empty equipped trinkets
                List<Pair<SlotReference, ItemStack>> equippedTrinkets = trinketComponent.getAllEquipped();

                // Process each equipped trinket to check for badges
                equippedTrinkets.stream()
                        .map(Pair::getRight) // Get the ItemStack from the pair
                        .map(ItemStack::getItem) // Get the Item from the ItemStack
                        .filter(item -> item instanceof BadgeItem) // Filter for BadgeItems
                        .forEach(item -> {
                            if (item == Items.DANDY_BADGE) {
                                if(entity.isMobOrPlayer()){
                                    this.dropLoot(source, true);
                                    this.dropXp();
                                    player.damage(DANDYCORPDamageTypes.of(player.getWorld(), DANDYCORPDamageTypes.DANDY_BADGE), 2);
                                    player.hurtTime = 0;
                                }
                            }
                        });
            });
        }
    }

}
