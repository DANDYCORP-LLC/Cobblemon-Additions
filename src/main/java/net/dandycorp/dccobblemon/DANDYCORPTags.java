package net.dandycorp.dccobblemon;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import static net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions.MOD_ID;

public class DANDYCORPTags {
    public static final TagKey<Item> PARAGONIUM_ITEMS = TagKey.of(RegistryKeys.ITEM, new Identifier(MOD_ID, "paragonium_equipment"));

    public static void initialize(){
    }
}
