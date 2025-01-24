package net.dandycorp.dccobblemon.attribute;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

public class InfinityGuardComponent implements AutoSyncedComponent {
    private final PlayerEntity player;
    public float shieldHealth;
    public int renderTime;
    public int cooldown;

    public static final int MAX_COOLDOWN = 160;

    public InfinityGuardComponent(PlayerEntity player) {
        this.player = player;
        shieldHealth = 0;
        renderTime = 0;
        cooldown = 0;
    }

    public boolean isOnCooldown(){
        return cooldown > 0;
    }

    public boolean shouldRenderShield() {
        return renderTime > 0;
    }

    public void addShield(float f){
        shieldHealth = MathHelper.clamp(shieldHealth + f, 0f, getMaxHealth());
        DANDYCORPCobblemonAdditions.INFINITY_GUARD.sync(this.player);
    }

    public void damageShield(float damage) {
        shieldHealth = MathHelper.clamp(shieldHealth - damage, 0f, getMaxHealth());
        if (shieldHealth > 0) {
            renderTime = 10;
            cooldown = 50;
        }
        if (shieldHealth == 0) {
            if (cooldown == 0) {
                renderTime = 30;
            }
            cooldown = MAX_COOLDOWN;
        }
        DANDYCORPCobblemonAdditions.INFINITY_GUARD.sync(this.player);
    }

    public void tickShield() {
        if (player.getWorld() == null) return;

        // Prevent overshields
        if (shieldHealth > getMaxHealth()) {
            shieldHealth = getMaxHealth();
        }

        if (shouldRenderShield()) {
            renderTime--;
            DANDYCORPCobblemonAdditions.INFINITY_GUARD.sync(player);
        }

        // No shield, no render
        if (getMaxHealth() == 0) {
            renderTime = 0;
            shieldHealth = 0;
        }

        if (cooldown > 0) {
            cooldown--;
        } else if (cooldown < 0) {
            cooldown = 0; // this shouldn't happen but just in case
        }
    }

    public float getMaxHealth() {
        float result = 0.0f;
        if (this.player.getAttributes().hasAttribute(DANDYCORPAttributes.INFINITY_GUARD)) {
            result = (float) this.player.getAttributes().getValue(DANDYCORPAttributes.INFINITY_GUARD);
        }
        return result;
    }

    @Override
    public void readFromNbt(@NotNull NbtCompound tag) {
        shieldHealth = tag.getFloat("health");
        renderTime = tag.getInt("rendertime");
        cooldown = tag.getInt("cooldown");
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound tag) {
        tag.putFloat("health", shieldHealth);
        tag.putInt("rendertime", renderTime);
        tag.putInt("cooldown", cooldown);
    }
}
