package net.dandycorp.dccobblemon.block;

import com.jozufozu.flywheel.core.PartialModel;
import net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions;
import net.minecraft.util.Identifier;

public class BlockPartialModels {

    public static final PartialModel GRINDER_SHAFT = block("grinder/shaft");
    public static final PartialModel GRINDER_FRONT = block("grinder/front_grinder");
    public static final PartialModel GRINDER_BACK = block("grinder/back_grinder");

    public static void register(){
        DANDYCORPCobblemonAdditions.LOGGER.info("Registering partial models...");
    }

    private static PartialModel block(String path) {
        return new PartialModel(new Identifier(DANDYCORPCobblemonAdditions.MOD_ID, "block/" + path));
    }
}
