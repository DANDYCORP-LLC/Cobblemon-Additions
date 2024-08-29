package net.dandycorp.dccobblemon;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class DANDYCORPDamageTypes {

    public static final RegistryKey<DamageType> DANDY_BADGE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier("dccobblemon", "dandy_badge"));

    public static DamageSource of(World world, RegistryKey<DamageType> key) {
        return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(key));
    }

}
