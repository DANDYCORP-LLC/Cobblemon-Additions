package net.dandycorp.dccobblemon.mixin;

import it.crystalnest.soul_fire_d.api.FireManager;
import net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions;
import net.dandycorp.dccobblemon.DANDYCORPDamageTypes;
import net.dandycorp.dccobblemon.DANDYCORPSounds;
import net.dandycorp.dccobblemon.DANDYCORPTags;
import net.dandycorp.dccobblemon.item.DANDYCORPItems;
import net.dandycorp.dccobblemon.item.custom.BadgeItem;
import net.dandycorp.dccobblemon.util.HeadHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DustColorTransitionParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions.RANDOM;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow protected abstract void dropLoot(DamageSource damageSource, boolean causedByPlayer);

    @Shadow protected abstract void dropXp();

    @Shadow public abstract boolean damage(DamageSource damageSource, float f);

    @Inject(method="dropEquipment",at=@At("TAIL"))
    private void drop(DamageSource source, int i, boolean bl, CallbackInfo ci) {
        if(source.getAttacker() == null) return;

        LivingEntity entity = (LivingEntity) (Object) this;

        if(source.getAttacker() instanceof PlayerEntity player) {
            if(BadgeItem.isEquipped(player, DANDYCORPItems.DANDY_BADGE)){
                if(entity.isMobOrPlayer()) {
                    this.dropLoot(source, true);
                    this.dropXp();
                    player.damage(DANDYCORPDamageTypes.of(player.getWorld(), DANDYCORPDamageTypes.DANDY_BADGE), player.getMaxHealth() / 8f);
                    player.maxHurtTime = 0;
                    player.hurtTime = 0;
                }
            }
        }

        Stream<ItemStack> heldItems = StreamSupport.stream(source.getAttacker().getHandItems().spliterator(), false);
        boolean isParagonium = heldItems.anyMatch(item -> item.isOf(DANDYCORPItems.PARAGONIUM_AXE) || item.isOf(DANDYCORPItems.PARAGONIUM_SWORD));
        if(isParagonium && source.getSource() != null && !entity.getWorld().isClient()){
            if (entity instanceof EnderDragonEntity)
                entity.dropItem(net.minecraft.item.Items.DRAGON_HEAD); // guaranteed
            float chance = RANDOM.nextFloat() + (i / 10f);
            if(chance >= 0.8) {
                if (entity instanceof PlayerEntity player) {
                    entity.dropStack(HeadHelper.getPlayerHead(player.getUuid()));
                } else if (entity instanceof WitherSkeletonEntity) {
                    entity.dropItem(net.minecraft.item.Items.WITHER_SKELETON_SKULL);
                } else if (entity instanceof SkeletonEntity) {
                    entity.dropItem(net.minecraft.item.Items.SKELETON_SKULL);
                } else if (entity instanceof ZombieEntity) {
                    entity.dropItem(net.minecraft.item.Items.ZOMBIE_HEAD);
                } else if (entity instanceof CreeperEntity) {
                    entity.dropItem(net.minecraft.item.Items.CREEPER_HEAD);
                } else if (entity instanceof PiglinEntity) {
                    entity.dropItem(net.minecraft.item.Items.PIGLIN_HEAD);
                }
            }
        }
    }

    @Inject(method="applyDamage",at=@At("TAIL"), cancellable = true)
    public void damage(DamageSource source, float amount, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity.getWorld().isClient()) return;

        // if player is attacking
        if (source.getAttacker() instanceof PlayerEntity player) {
            if (BadgeItem.isEquipped(player,DANDYCORPItems.DRAGON_BADGE)) {
                FireManager.setOnFire(entity, Math.round(3+((float) entity.getFireTicks() /40)),FireManager.SOUL_FIRE_TYPE);
            }

            var shield = player.getComponent(DANDYCORPCobblemonAdditions.INFINITY_GUARD);
            if(shield.getMaxHealth() > 0 && !shield.isOnCooldown()){
                shield.addShield(amount * 0.2f);
            }
        }

        // if player is attacked
        if (entity instanceof PlayerEntity player && source.getAttacker() instanceof LivingEntity assailant) {
            if (BadgeItem.isEquipped(assailant, DANDYCORPItems.BUG_BADGE)) {
                if (assailant.damage(player.getDamageSources().generic(), 3)) {
                    player.getWorld().playSound(null, assailant.getBlockPos(), SoundEvents.ENCHANT_THORNS_HIT, SoundCategory.PLAYERS, 1f, (float) (1.2 + Math.random()));
                }
            }
        }
    }

    @Inject(
            method = "damage",
            at = @At("TAIL"),
            cancellable = false
    )
    private void dandycorpExtraDamage(DamageSource source, float f, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) return;

        LivingEntity target = (LivingEntity) (Object) this;
        if (!target.isAlive()) {
            return;
        }
        ServerWorld world = (ServerWorld) target.getWorld();

        if (!(source.getAttacker() instanceof LivingEntity attacker)) {
            return;
        }
        if (BadgeItem.isEquipped(attacker, DANDYCORPItems.SHELLY_BADGE)) {
            if (attacker.getHealth()/attacker.getMaxHealth() <= 0.6) {
                float addition = (float) (1.5 / (attacker.getHealth() / attacker.getMaxHealth()));
                attacker.heal((f / 2) + addition);
                ((ServerWorld) attacker.getWorld()).spawnParticles(new DustColorTransitionParticleEffect(
                                new Vector3f(1.0f, 0.94f, 0.25f),
                                new Vector3f(1.0f, 0.39f, 0.56f),
                                2.5f),
                        target.getX(),
                        target.getY() + 0.5,
                        target.getZ(),
                        (int) Math.round(3 * addition), 0.6, 0.6, 0.6, 100);
                world.playSound(null, target.getBlockPos(), DANDYCORPSounds.JUDGEMENT_EVENT, SoundCategory.PLAYERS, 1f, 0.5f + addition);
                target.hurtTime=0;
                target.maxHurtTime = 0;
                target.damage(source, addition);
            }
        }

        if (StreamSupport.stream(attacker.getHandItems().spliterator(),false).anyMatch(item -> item.isIn(DANDYCORPTags.PARAGONIUM_ITEMS))){
            float paragoniumDamage = f * 0.10F;
            target.hurtTime=0;
            target.maxHurtTime = 0;
            target.damage(DANDYCORPDamageTypes.of(world,DANDYCORPDamageTypes.PARAGONIUM), paragoniumDamage);
        }
    }
}
