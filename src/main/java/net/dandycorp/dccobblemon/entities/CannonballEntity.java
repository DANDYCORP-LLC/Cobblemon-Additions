package net.dandycorp.dccobblemon.entities;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.dandycorp.dccobblemon.DANDYCORPDamageTypes;
import net.dandycorp.dccobblemon.item.DANDYCORPItems;
import net.dandycorp.dccobblemon.sound.DANDYCORPSounds;
import net.dandycorp.dccobblemon.util.CannonController;
import net.dandycorp.dccobblemon.util.ScreenShake;
import net.dandycorp.dccobblemon.util.ScreenShakeController;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions.RANDOM;

public class CannonballEntity extends PersistentProjectileEntity implements FlyingItemEntity {

    private static ItemStack STACK = ItemStack.EMPTY;
    private static boolean FLAME = false;
    private static boolean CHANNELING = false;

    @Override protected ItemStack asItemStack() { return new ItemStack(Items.NETHERITE_BLOCK); }

    public ItemStack getStack() {
        return new ItemStack(Items.NETHERITE_BLOCK);
    }

    public CannonballEntity(EntityType<? extends CannonballEntity> type, World world) { super(type, world); }
    public CannonballEntity(World world, LivingEntity shooter, ItemStack stack) {
        super(DANDYCORPEntities.CANNONBALL, shooter, world);
        STACK = stack;
        FLAME = EnchantmentHelper.getLevel(Enchantments.FLAME,STACK) > 0;
        CHANNELING = EnchantmentHelper.getLevel(Enchantments.CHANNELING,STACK) > 0;
        this.pickupType = PickupPermission.DISALLOWED;
        this.setOwner(shooter);
    }

    @Override public void tick() {
        super.tick();
        ProjectileUtil.setRotationFromVelocity(this, 1.0F);
        if (this.getWorld().isClient) {
            double d = this.getX() + random.nextDouble() - 0.5;
            double e = this.getY() + random.nextDouble() - 0.5;
            double f = this.getZ() + random.nextDouble() - 0.5;
            this.getWorld().addParticle(ParticleTypes.LARGE_SMOKE, d, e, f, 0.0, 0.0, 0.0);
            if (FLAME){
                this.getWorld().addParticle(ParticleTypes.FLAME, d, e, f, 0.0, 0.0, 0.0);
            }
            else if (CHANNELING) {
                this.getWorld().addParticle(ParticleTypes.WAX_OFF, d, e, f, 0.0, 0.0, 0.0);
            }
            else {
                this.getWorld().addParticle(ParticleTypes.SMOKE, d, e, f, 0.0, 0.0, 0.0);
            }
        }
    }

    protected SoundEvent getHitSound() {
        return DANDYCORPSounds.CANNON_IMPACT_EVENT;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        boom();
        this.discard();
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);

        Entity target = entityHitResult.getEntity();
        if (target instanceof LivingEntity livingEntity) {
            if(livingEntity.isBlocking() && livingEntity.getActiveItem().isOf(DANDYCORPItems.PARAGONIUM_SHIELD) && livingEntity instanceof PlayerEntity player) {
                // block with paragonium shield
                double oldSpeed = this.getVelocity().length();
                float yawRadians   = (float)Math.toRadians(player.getYaw());
                float pitchRadians = (float)Math.toRadians(player.getPitch());

                double dirX = -Math.cos(pitchRadians) * Math.sin(yawRadians);
                double dirY = -Math.sin(pitchRadians);
                double dirZ =  Math.cos(pitchRadians) * Math.cos(yawRadians);

                Vec3d newVel = new Vec3d(dirX, dirY, dirZ).normalize().multiply(oldSpeed * 1.2);

                this.setVelocity(newVel);
                this.setYaw(player.getYaw());
                this.setPitch(player.getPitch());
                this.prevYaw   = this.getYaw();
                this.prevPitch = this.getPitch();
                this.setOwner(player);
            } else { // hits non-blocking entity
                getWorld().playSound(null, this.getBlockPos(), DANDYCORPSounds.CANNON_HIT_EVENT, SoundCategory.PLAYERS, 4f, RANDOM.nextFloat(0.8f, 1.2f));
                if (livingEntity instanceof PlayerEntity player) {
                    player.closeHandledScreen();
                    getWorld().playSound(null, this.getBlockPos(), DANDYCORPSounds.CANNON_PLAYER_HIT_FLARE_EVENT, SoundCategory.PLAYERS, 4f, RANDOM.nextFloat(0.8f, 1.2f));
                    if (player instanceof ServerPlayerEntity sp) {
                        sp.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 20, 100, false, false, true));
                        CannonController.schedule(sp, 20);
                    }
                } else
                    livingEntity.damage(this.getWorld().getDamageSources().create(DANDYCORPDamageTypes.HAIL_MARY_DIRECT_HIT, getOwner()), 50);
                boom();
                this.discard();
            }
        } else { // hits floor
            boom();
            this.discard();
        }
    }

    private void boom() {
        if (!(this.getWorld() instanceof ServerWorld world)) return;

        world.playSound(
                null,
                this.getBlockPos(),
                SoundEvents.ENTITY_GENERIC_EXPLODE,
                SoundCategory.PLAYERS,
                2f,
                RANDOM.nextFloat(0.0f, 0.5f)
        );

        DamageSource ds = world.getDamageSources().create(DANDYCORPDamageTypes.HAIL_MARY_EXPLOSION, getOwner());

        float power = 2.5f * (1 + (0.5f * EnchantmentHelper.getLevel(Enchantments.PUNCH, STACK)));


        Explosion explosion = getExplosion(world, ds, power, FLAME);

        world.addParticle(
                power >= 2.0f ? ParticleTypes.EXPLOSION_EMITTER : ParticleTypes.EXPLOSION,
                this.getX(), this.getY(), this.getZ(),
                1.0, 0.0, 0.0
        );

        if (CHANNELING && world.isSkyVisible(this.getBlockPos()) && world.isRaining()) {
            for (BlockPos pos : explosion.getAffectedBlocks()) {
                if (this.random.nextInt(39) == 0
                        && world.getBlockState(pos).isAir()
                        && world.getBlockState(pos.down()).isOpaqueFullCube(world, pos.down())) {

                    LightningEntity bolt = EntityType.LIGHTNING_BOLT.create(world);
                    if (bolt != null) {
                        bolt.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(pos));
                        if (this.getOwner() instanceof ServerPlayerEntity sp) {
                            bolt.setChanneler(sp);
                        }
                        world.spawnEntity(bolt);
                    }
                }
            }
        }
        ScreenShakeController.causeTremor(
                this.getWorld(),
                this.getBlockPos(),
                25,
                new ScreenShake(0.5f,10,20, ScreenShakeController.FadeType.LINEAR),
                ScreenShakeController.DistanceFalloff.REVERSE_EXPONENTIAL
        );
    }

    private @NotNull Explosion getExplosion(ServerWorld world, DamageSource ds, float power, boolean flame) {
        System.out.println("creating explosion with power " + power + " and flame " + flame);
        Explosion explosion = new Explosion(
                world,
                this,
                ds,
                null,
                this.getX(),
                this.getY(),
                this.getZ(),
                power,
                flame,
                Explosion.DestructionType.DESTROY_WITH_DECAY
        );

        explosion.collectBlocksAndDamageEntities();

        if (flame) {
            for (BlockPos blockPos3 : explosion.getAffectedBlocks()) {
                if (this.random.nextInt(3) == 0
                        && world.getBlockState(blockPos3).isAir()
                        && world.getBlockState(blockPos3.down()).isOpaqueFullCube(world, blockPos3.down())) {
                    world.setBlockState(blockPos3, AbstractFireBlock.getState(world, blockPos3));
                }
            }
        }

        return explosion;
    }
}
