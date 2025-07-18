// CannonballEntity.java
package net.dandycorp.dccobblemon.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.mob.FlyingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class CannonballEntity extends PersistentProjectileEntity implements FlyingItemEntity {

    @Override protected ItemStack asItemStack() { return new ItemStack(Items.NETHERITE_BLOCK); }

    public ItemStack getStack() {
        return new ItemStack(Items.NETHERITE_BLOCK);
    }

    public CannonballEntity(EntityType<? extends CannonballEntity> type, World world) { super(type, world); }
    public CannonballEntity(World world, LivingEntity shooter) {
        super(DANDYCORPEntities.CANNONBALL, shooter, world);
        this.setDamage(20);
        this.setPunch(3);
        this.pickupType = PickupPermission.DISALLOWED;
    }

    /* ─────────────────────────  Behaviour  ───────────────────────── */

    // Heavy projectile – lower gravity than arrows / snowballs
    @Override public void tick() {
        super.tick();
        if (this.getWorld().isClient) {
            this.getWorld().addParticle(ParticleTypes.FLAME,
                    this.getX(), this.getY(), this.getZ(),
                    0, 0, 0);
        }
    }
    @Override protected float getDragInWater() { return 0.88f; }
    @Override public boolean hasNoGravity()         { return false; }
    @Override public void setNoGravity(boolean x)   { }

    /* ───── Impact handling ───── */
    @Override protected void onEntityHit(EntityHitResult hit) {
        super.onEntityHit(hit);
        hit.getEntity().addVelocity(0, 0.6, 0);
    }

    @Override
    protected void onCollision(HitResult hit) {
        super.onCollision(hit);
        if (!getWorld().isClient) {
            getWorld().createExplosion(
                    this,
                    this.getOwner() == null
                            ? this.getWorld().getDamageSources().explosion(null)
                            : this.getWorld().getDamageSources().explosion(this,getOwner()),
                    null,
                    getX(), getY(), getZ(),
                    5.0f,
                    false,
                    World.ExplosionSourceType.NONE);
        }
        this.discard();
    }
}
