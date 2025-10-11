package net.dandycorp.dccobblemon.util;

import net.dandycorp.dccobblemon.DANDYCORPDamageTypes;
import net.dandycorp.dccobblemon.sound.DANDYCORPSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.*;

import static net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions.RANDOM;

public final class CannonController {

    private CannonController() {}

    private static final Map<UUID, Integer> PENDING = new HashMap<>();

    public static void schedule(ServerPlayerEntity player, int delayTicks) {
        if (player == null) return;
        PENDING.put(player.getUuid(), delayTicks);
    }

    public static void cancel(ServerPlayerEntity player) {
        if (player != null) PENDING.remove(player.getUuid());
    }

    public static void tick(MinecraftServer server) {
        if (PENDING.isEmpty()) return;

        List<UUID> toProcess = new ArrayList<>(PENDING.keySet());
        for (UUID id : toProcess) {
            Integer remaining = PENDING.get(id);
            if (remaining == null) continue;

            remaining -= 1;

            ServerPlayerEntity sp = server.getPlayerManager().getPlayer(id);
            if (sp == null) {
                PENDING.remove(id);
                continue;
            }

            if (remaining > 0) {
                PENDING.put(id, remaining);
                sp.setNoGravity(true);
                sp.setVelocity(0,0,0);
                continue;
            }

            if (sp.isAlive()) {
                ServerWorld world = sp.getServerWorld();
                world.playSound(null, sp.getBlockPos(), DANDYCORPSounds.CANNON_PLAYER_HIT_EVENT, SoundCategory.PLAYERS, 4f, RANDOM.nextFloat(0.8f, 1.2f));
                Vec3d origin = sp.getPos().add(0, 1.0, 0); // chest height feels good
                world.spawnParticles(
                        ParticleTypes.FIREWORK,
                        origin.x, origin.y, origin.z,
                        100,
                        0.8f, 0.8f, 0.8f,
                        0.25f
                );
                sp.setNoGravity(false);
                sp.kill();
            }

            PENDING.remove(id);
        }
    }

    public static void clear() {
        PENDING.clear();
    }
}
