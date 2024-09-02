package net.dandycorp.dccobblemon.mixin;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketsApi;
import it.crystalnest.soul_fire_d.api.FireManager;
import net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions;
import net.dandycorp.dccobblemon.item.Items;
import net.dandycorp.dccobblemon.item.custom.BadgeItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DustColorTransitionParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Pair;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(LivingEntity.class)
public class DamageMixin {

    @Inject(method="damage",at=@At("HEAD"), cancellable = true)
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

        if (source.getAttacker() instanceof PlayerEntity player) {
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
                                        float addition = (float) (1.5/(player.getHealth()/player.getMaxHealth()));
                                        player.heal((amount/2)+addition);
                                        ((ServerWorld) player.getWorld()).spawnParticles(new DustColorTransitionParticleEffect(
                                                            new Vector3f(1.0f, 0.94f, 0.25f),
                                                            new Vector3f(1.0f, 0.39f, 0.56f),
                                                            2.5f),
                                                    entity.getX(),
                                                    entity.getY()+0.5,
                                                    entity.getZ(),
                                                    (int) Math.round(3 * addition), 0.6, 0.6, 0.6, 100);
                                        player.getWorld().playSound(null,entity.getBlockPos(), DANDYCORPCobblemonAdditions.JUDGEMENT_EVENT, SoundCategory.PLAYERS,1f, 0.5f+addition);
                                        cir.setReturnValue(entity.damage(source,amount+addition));
                                    }
                                }
                                else if (item == Items.DRAGON_BADGE) {
                                    FireManager.setOnFire(entity, Math.round(3+((float) entity.getFireTicks() /40)),FireManager.SOUL_FIRE_TYPE);
                                }
                            });
                });
            }
        }

    }



}
