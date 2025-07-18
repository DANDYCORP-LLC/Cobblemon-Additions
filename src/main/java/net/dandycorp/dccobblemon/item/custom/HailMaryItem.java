package net.dandycorp.dccobblemon.item.custom;

import net.dandycorp.dccobblemon.entities.CannonballEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class HailMaryItem extends Item {
    public HailMaryItem(Settings settings) { super(settings); }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!world.isClient) {
            Vec3d shoulder = hand == Hand.MAIN_HAND
                    ? user.getRotationVector().crossProduct(new Vec3d(0,1,0)).normalize().multiply(0.4)
                    : user.getRotationVector().crossProduct(new Vec3d(0,-1,0)).normalize().multiply(0.4);
            ((ServerWorld)world).spawnParticles(ParticleTypes.FLAME,
                    user.getX()+shoulder.x, user.getEyeY()-0.2+shoulder.y, user.getZ()+shoulder.z,
                    20, 0.1, 0.1, 0.1, 0);
            world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_TNT_PRIMED,
                    SoundCategory.PLAYERS, 3.0F, 2.0F);
            world.getServer().execute(() -> spawnProjectile(world, user, hand));
            user.getItemCooldownManager().set(this, 30);
        }
        return TypedActionResult.success(stack, world.isClient());
    }

    private void spawnProjectile(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!world.isClient) {
            CannonballEntity ball = new CannonballEntity(world, user);
            ball.setVelocity(user, user.getPitch(), user.getYaw(), 0f, 4.0f, 0.0f);
            world.spawnEntity(ball);
        }
    }
}
