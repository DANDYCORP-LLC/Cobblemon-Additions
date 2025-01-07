package net.dandycorp.dccobblemon.util.grinder;

import net.minecraft.item.Item;

import java.util.HashMap;
import java.util.Map;

public class GrinderDataCache {
    private static Map<Item, Float> calculatedPointValues = new HashMap<>();

    public static Map<Item, Float> getCalculatedPointValues() {
        return calculatedPointValues;
    }

    public static void setCalculatedPointValues(Map<Item, Float> calculatedPointValues) {
        GrinderDataCache.calculatedPointValues = calculatedPointValues;
    }
}
