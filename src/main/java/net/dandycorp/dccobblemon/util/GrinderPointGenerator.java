package net.dandycorp.dccobblemon.util;

import net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;

import java.util.HashMap;
import java.util.Map;

public class GrinderPointGenerator {

    public static Map<Item, Float> basePointValues = new HashMap<>();
    public static Map<Item, Float> calculatedPointValues = new HashMap<>();

    public static void initializePointValues(){
        DANDYCORPCobblemonAdditions.LOGGER.info("Initializing DANDYCORP Grinder Point Values");
        calculatedPointValues.clear();
        Registries.ITEM.forEach( item -> {
            calculatedPointValues.put(item, 123.456f);
        });
        //System.out.println(calculatedPointValues);
    }
}
