package net.dandycorp.dccobblemon.mixin;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketsApi;
import net.dandycorp.dccobblemon.item.Items;
import net.dandycorp.dccobblemon.item.custom.BadgeItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DustColorTransitionParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(LivingEntity.class)
public class DamageMixin {

    @Inject(method="damage",at=@At("HEAD"))
    public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {

        if ((LivingEntity) (Object) this instanceof PlayerEntity player && source.getAttacker() instanceof LivingEntity entity) {
            if (!player.getWorld().isClient()){
                TrinketsApi.getTrinketComponent(player).ifPresent(trinketComponent -> {
                    // Get all non-empty equipped trinkets
                    List<Pair<SlotReference, ItemStack>> equippedTrinkets = trinketComponent.getAllEquipped();

                    // Process each equipped trinket to check for badges
                    equippedTrinkets.stream()
                            .map(Pair::getRight) // Get the ItemStack from the pair
                            .map(ItemStack::getItem) // Get the Item from the ItemStack
                            .filter(item -> item instanceof BadgeItem) // Filter for BadgeItems
                            .forEach(item -> {
                                if (item == Items.BUG_BADGE) {
                                    if (entity.damage(player.getDamageSources().generic(), 3)){
                                        player.getWorld().playSound(null,entity.getBlockPos(), SoundEvents.ENCHANT_THORNS_HIT, SoundCategory.PLAYERS,1f, (float) (1.2 + Math.random()));
                                    }
                                }
                            });
                });
            }
        }

        else if (source.getAttacker() instanceof PlayerEntity player) {
            if (!player.getWorld().isClient()){
                LivingEntity entity = (LivingEntity) (Object) this;
                TrinketsApi.getTrinketComponent(player).ifPresent(trinketComponent -> {
                    List<Pair<SlotReference, ItemStack>> equippedTrinkets = trinketComponent.getAllEquipped();

                    // Process each equipped trinket to check for badges
                    equippedTrinkets.stream()
                            .map(Pair::getRight) // Get the ItemStack from the pair
                            .map(ItemStack::getItem) // Get the Item from the ItemStack
                            .filter(item -> item instanceof BadgeItem) // Filter for BadgeItems
                            .forEach(item -> {
                                if (item == Items.SHELLY_BADGE) {
                                    if (player.getHealth()/player.getMaxHealth() <= 0.6){
                                        if(entity.damage(player.getDamageSources().generic(), amount*(1/(player.getHealth()/player.getMaxHealth())))){
                                            player.getWorld().playSound(null,entity.getBlockPos(), SoundEvents.BLOCK_BREWING_STAND_BREW, SoundCategory.PLAYERS,1f, (float) (1.2 + Math.random()));
                                            player.heal(amount);
                                            player.sendMessage(Text.literal("healed "+amount));
                                            player.sendMessage(Text.literal("damage "+amount*(1/(player.getHealth()/player.getMaxHealth()))));
                                            ((ServerWorld) player.getWorld()).spawnParticles(new DustColorTransitionParticleEffect(
                                                        new Vector3f(0.62f, 0.0f, 0.0f),
                                                        new Vector3f(0.0f, 0.0f, 0.0f),
                                                            3),
                                                    entity.getX(),
                                                    entity.getY(),
                                                    entity.getZ(),
                                                    (int) Math.round(2*amount), 0.5, 1, 0.5,20);
                                        }
                                    }
                                }
                            });

                });
            }
        }

    }

}
