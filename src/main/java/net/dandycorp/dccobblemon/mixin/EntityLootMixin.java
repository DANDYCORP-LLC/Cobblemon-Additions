package net.dandycorp.dccobblemon.mixin;

import net.dandycorp.dccobblemon.DANDYCORPDamageTypes;
import net.dandycorp.dccobblemon.item.Items;
import net.dandycorp.dccobblemon.item.custom.BadgeItem;
import net.dandycorp.dccobblemon.util.HeadHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions.RANDOM;

@Mixin(LivingEntity.class)
public abstract class EntityLootMixin {

    @Shadow protected abstract void dropLoot(DamageSource damageSource, boolean causedByPlayer);

    @Shadow protected abstract void dropXp();

    @Shadow public abstract @Nullable LivingEntity getAttacker();

    @Shadow public abstract boolean blockedByShield(DamageSource damageSource);

    @Inject(method="dropEquipment",at=@At("TAIL"))
    private void drop(DamageSource source, int i, boolean bl, CallbackInfo ci) {
        if(source.getAttacker() == null) return;

        LivingEntity entity = (LivingEntity) (Object) this;

        if(source.getAttacker() instanceof PlayerEntity player) {
            if(BadgeItem.isEquipped(player,Items.DANDY_BADGE)){
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
        boolean isParagonium = heldItems.anyMatch(item -> item.isOf(Items.PARAGONIUM_AXE) || item.isOf(Items.PARAGONIUM_SWORD));
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
}
