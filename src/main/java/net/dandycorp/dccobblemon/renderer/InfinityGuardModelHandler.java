package net.dandycorp.dccobblemon.renderer;

import net.dandycorp.dccobblemon.mixin.accessor.EntityModelLayersAccessor;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

import static net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions.MOD_ID;

public class InfinityGuardModelHandler {
    public static final EntityModelLayer INFINITY_GUARD_SWIRL = model("infinity_guard_swirl");

    public static EntityModelLayer model(String name, String layer) {
        var result = new EntityModelLayer(new Identifier(MOD_ID,name), layer);
        EntityModelLayersAccessor.getLAYERS().add(result);
        return result;
    }

    public static EntityModelLayer model(String name) {
        return model(name, "main");
    }
}
