package net.dandycorp.dccobblemon.renderer;

import net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions;
import net.dandycorp.dccobblemon.item.custom.paragonium.ParagoniumArmorItem;
import net.minecraft.util.Identifier;
import org.apache.http.client.utils.Idn;
import software.bernie.geckolib.model.GeoModel;

public class ParagoniumArmorModel extends GeoModel<ParagoniumArmorItem> {
    @Override
    public Identifier getModelResource(ParagoniumArmorItem animatable) {
        return new Identifier(DANDYCORPCobblemonAdditions.MOD_ID, "geo/paragonium_armor.geo.json");
    }

    @Override
    public Identifier getTextureResource(ParagoniumArmorItem animatable) {
        return new Identifier(DANDYCORPCobblemonAdditions.MOD_ID, "textures/item/metals/paragonium_armor.png");
    }

    @Override
    public Identifier getAnimationResource(ParagoniumArmorItem animatable) {
        return new Identifier(DANDYCORPCobblemonAdditions.MOD_ID, "animations/paragonium_armor.animation.json");
    }
}
