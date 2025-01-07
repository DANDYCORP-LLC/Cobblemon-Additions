package net.dandycorp.dccobblemon.renderer;

import net.dandycorp.dccobblemon.item.custom.paragonium.ParagoniumArmorItem;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class ParagoniumArmorRenderer extends GeoArmorRenderer<ParagoniumArmorItem> {
    public ParagoniumArmorRenderer() {
        super(new ParagoniumArmorModel());
    }
}
