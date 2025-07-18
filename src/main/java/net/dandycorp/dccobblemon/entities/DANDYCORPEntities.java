package net.dandycorp.dccobblemon.entities;

import net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.dandycorp.dccobblemon.entities.CannonballEntity;

public class DANDYCORPEntities {
    public static final EntityType<CannonballEntity> CANNONBALL =
            Registry.register(Registries.ENTITY_TYPE,
                    new Identifier(DANDYCORPCobblemonAdditions.MOD_ID, "cannonball"),
                    FabricEntityTypeBuilder.<CannonballEntity>create(SpawnGroup.MISC, CannonballEntity::new)
                            .dimensions(EntityDimensions.fixed(0.75f, 0.75f))
                            .trackRangeBlocks(96)
                            .trackedUpdateRate(10)
                            .build());

    public static void init() {}
}
