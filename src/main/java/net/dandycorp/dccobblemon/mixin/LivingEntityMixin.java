package net.dandycorp.dccobblemon.mixin;

import com.cobblemon.mod.common.CobblemonSounds;
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
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DustColorTransitionParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions.RANDOM;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow protected abstract void dropLoot(DamageSource damageSource, boolean causedByPlayer);

    @Shadow protected abstract void dropXp();

    @Shadow public abstract boolean damage(DamageSource damageSource, float f);

    @Unique
    private static final Map<LivingEntity, Long> electricBadgeNextAllowedTick = new WeakHashMap<>();

    @ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true)
    private float modifyDamageWithSteelBadge(float originalDamage) {
        LivingEntity target = (LivingEntity) (Object) this;
        if (BadgeItem.isEquipped(target, DANDYCORPItems.STEEL_BADGE)) {
            return originalDamage * 0.5f;
        }
        return originalDamage;
    }

    @Inject(method = "dropEquipment", at = @At("TAIL"))
    private void drop(DamageSource source, int i, boolean bl, CallbackInfo ci) {
        if (source.getAttacker() == null) return;

        LivingEntity entity = (LivingEntity) (Object) this;

        if (source.getAttacker() instanceof PlayerEntity player) {
            if (BadgeItem.isEquipped(player, DANDYCORPItems.DANDY_BADGE)) {
                if (entity.isMobOrPlayer()) {
                    this.dropLoot(source, true);
                    this.dropXp();
                    player.damage(DANDYCORPDamageTypes.of(player.getWorld(), DANDYCORPDamageTypes.DANDY_BADGE), player.getMaxHealth() / 8f);
                    player.maxHurtTime = 0;
                    player.hurtTime = 0;
                }
            }
        }

        boolean isParagonium = StreamSupport.stream(source.getAttacker().getHandItems().spliterator(), false)
                .anyMatch(item -> item.isOf(DANDYCORPItems.PARAGONIUM_AXE) || item.isOf(DANDYCORPItems.PARAGONIUM_SWORD));
        if (isParagonium && source.getSource() != null && !entity.getWorld().isClient()) {
            System.out.println("paragonium killed");
            if (entity instanceof EnderDragonEntity)
                entity.dropItem(net.minecraft.item.Items.DRAGON_HEAD); // guaranteed
            float chance = RANDOM.nextFloat() + (i / 10f);
            if (chance >= 0.3) {
                if (entity instanceof PlayerEntity player) {
                    entity.dropStack(HeadHelper.getPlayerHead(player));
                }
            } else if (chance >= 0.8) {
                if (entity instanceof WitherSkeletonEntity) {
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

    @Inject(method = "applyDamage", at = @At("TAIL"), cancellable = true)
    public void applyDamage(DamageSource source, float amount, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity.getWorld().isClient()) return;

        if (source.getAttacker() instanceof PlayerEntity player) {
            if (BadgeItem.isEquipped(player, DANDYCORPItems.DRAGON_BADGE)) {
                FireManager.setOnFire(entity, Math.round(3 + ((float) entity.getFireTicks() / 40)), FireManager.SOUL_FIRE_TYPE);
            }

            var shield = player.getComponent(DANDYCORPCobblemonAdditions.INFINITY_GUARD);
            if (shield.getMaxHealth() > 0 && !shield.isOnCooldown()) {
                shield.addShield(amount * 0.2f);
            }
        }
    }

    @Inject(method = "damage", at = @At("TAIL"), cancellable = false)
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
            if (attacker.getHealth() / attacker.getMaxHealth() <= 0.6) {
                float addition = (float) (1.5 / (attacker.getHealth() / attacker.getMaxHealth()));
                attacker.heal((f / 2) + addition);
                world.spawnParticles(new DustColorTransitionParticleEffect(
                                new Vector3f(1.0f, 0.94f, 0.25f),
                                new Vector3f(1.0f, 0.39f, 0.56f),
                                2.5f),
                        target.getX(),
                        target.getY() + 0.5,
                        target.getZ(),
                        (int) Math.round(3 * addition), 0.6, 0.6, 0.6, 100);
                world.playSound(null, target.getBlockPos(), DANDYCORPSounds.JUDGEMENT_EVENT, SoundCategory.PLAYERS, 1f, 0.5f + addition);
                target.hurtTime = 0;
                target.maxHurtTime = 0;
                target.damage(source, addition);
            }
        }
        if (BadgeItem.isEquipped(attacker, DANDYCORPItems.ELECTRIC_BADGE)) {
            long currentTick = world.getTime();
            Long nextAllowed = electricBadgeNextAllowedTick.get(attacker);
            if (nextAllowed == null || currentTick >= nextAllowed) {
                doChainLightning(attacker, target, f);
                electricBadgeNextAllowedTick.put(attacker, currentTick + 20);
            }
        }
        if (target instanceof PlayerEntity player && source.getAttacker() instanceof LivingEntity assailant) {
            if (BadgeItem.isEquipped(player, DANDYCORPItems.BUG_BADGE)) {
                System.out.println("bug badge activated");
                if (assailant.damage(player.getDamageSources().thorns(player), 1)) {
                    player.getWorld().playSound(null, assailant.getBlockPos(), SoundEvents.ENCHANT_THORNS_HIT, SoundCategory.PLAYERS, 1f, (float) (1.2 + Math.random()));
                }
            }
        }

        if (StreamSupport.stream(attacker.getHandItems().spliterator(), false)
                .anyMatch(item -> item.isIn(DANDYCORPTags.PARAGONIUM_ITEMS))) {
            float paragoniumDamage = f * 0.10F;
            target.hurtTime = 0;
            target.maxHurtTime = 0;
            target.damage(DANDYCORPDamageTypes.of(world, DANDYCORPDamageTypes.PARAGONIUM), paragoniumDamage);
        }
    }

    @Unique
    private void doChainLightning(LivingEntity attacker, LivingEntity firstTarget, float originalDamage) {
        ServerWorld world = (ServerWorld) attacker.getWorld();
        List<LivingEntity> initialNeighbors = world.getEntitiesByClass(LivingEntity.class,
                firstTarget.getBoundingBox().expand(3.0),
                candidate -> candidate != firstTarget &&
                        candidate.isAlive() &&
                        candidate instanceof Monster);
        if (initialNeighbors.isEmpty()) {
            return;
        }
        spawnParticlesAndSound(attacker, firstTarget, 2.0f);
        final float[] multipliers = {0.6f, 0.4f, 0.2f};
        final float[] pitches = {1.6f, 1.4f, 1.2f};
        Set<LivingEntity> visited = new HashSet<>();
        Queue<LivingEntity> queue = new LinkedList<>();
        visited.add(firstTarget);
        queue.add(firstTarget);

        for (int level = 0; level < multipliers.length; level++) {
            float damage = originalDamage * multipliers[level];
            int currentLevelCount = queue.size();
            Queue<LivingEntity> nextQueue = new LinkedList<>();

            for (int i = 0; i < currentLevelCount; i++) {
                LivingEntity current = queue.poll();
                List<LivingEntity> neighbors = world.getEntitiesByClass(LivingEntity.class,
                        current.getBoundingBox().expand(2.0),
                        candidate -> candidate != current &&
                                candidate.isAlive() &&
                                !visited.contains(candidate) &&
                                candidate instanceof Monster);
                for (LivingEntity neighbor : neighbors) {
                    visited.add(neighbor);
                    neighbor.damage(DANDYCORPDamageTypes.of(world, DamageTypes.LIGHTNING_BOLT), damage);
                    nextQueue.add(neighbor);
                    spawnParticlesAndSound(current, neighbor, pitches[level]);
                }
            }
            if (nextQueue.isEmpty()) break;
            queue = nextQueue;
        }
    }

    @Unique
    private void spawnParticlesAndSound(LivingEntity from, LivingEntity to, float pitch) {
        ServerWorld world = (ServerWorld) from.getWorld();
        double fromX = from.getX(), fromY = from.getEyeY(), fromZ = from.getZ();
        double toX = to.getX(), toY = to.getEyeY(), toZ = to.getZ();
        double midX = (fromX + toX) / 2.0;
        double midY = (fromY + toY) / 2.0;
        double midZ = (fromZ + toZ) / 2.0;
        world.playSound(null, midX, midY, midZ, CobblemonSounds.IMPACT_ELECTRIC,
                SoundCategory.PLAYERS, 0.8F, pitch);
        int steps = 10;
        for (int step = 0; step <= steps; step++) {
            double t = step / (double) steps;
            double x = fromX + (toX - fromX) * t;
            double y = fromY + (toY - fromY) * t;
            double z = fromZ + (toZ - fromZ) * t;
            world.spawnParticles(ParticleTypes.ELECTRIC_SPARK, x, y, z, 1, 0.0, 0.0, 0.0, 0.0);
        }
    }
}
