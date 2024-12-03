package net.dandycorp.dccobblemon.compat;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions.LOGGER;

public class ChippedCompat {
    private static Map<Item, Item> chippedToBaseItemMap = new HashMap<>();

    public static void initialize() {
        if (!FabricLoader.getInstance().isModLoaded("chipped")) {
            return;
        }
        LOGGER.info("chipped detected! providing mod support");
        try {
            Class<?> modBlocksClass = Class.forName("earth.terrarium.chipped.common.registry.ModBlocks");
            Field[] fields = modBlocksClass.getDeclaredFields();
            for (Field field : fields) {
                if (field.getType().getSimpleName().equals("ChippedPaletteRegistry")) {
                    field.setAccessible(true);
                    Object registry = field.get(null);

                    Method getBaseMethod = registry.getClass().getMethod("getBase");
                    Object baseBlock = getBaseMethod.invoke(registry);
                    Item baseItem = ((Block) baseBlock).asItem();

                    Method getEntriesMethod = registry.getClass().getMethod("getEntries");
                    Collection<?> entries = (Collection<?>) getEntriesMethod.invoke(registry);
                    for (Object entry : entries) {
                        Method getMethod = entry.getClass().getMethod("get");
                        Object block = getMethod.invoke(entry);
                        Item chippedItem = ((Block) block).asItem();
                        chippedToBaseItemMap.put(chippedItem, baseItem);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Item getChippedBaseItem(Item chippedItem) {
        return chippedToBaseItemMap.get(chippedItem);
    }
}
