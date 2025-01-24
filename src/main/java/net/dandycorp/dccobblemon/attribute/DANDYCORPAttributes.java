package net.dandycorp.dccobblemon.attribute;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import static net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions.MOD_ID;

public class DANDYCORPAttributes {
    public static final EntityAttribute INFINITY_GUARD
            = register("infinity_guard",
            new ClampedEntityAttribute(
                    "attribute.name.generic.dccobblemon.infinity_guard",
                    0, 0, 2048
            ).setTracked(true)
    );

    public static void initialize(){}

    private static EntityAttribute register(String string, EntityAttribute entityAttribute) {
        return Registry.register(Registries.ATTRIBUTE, Identifier.of(MOD_ID,string), entityAttribute);
    }
}
