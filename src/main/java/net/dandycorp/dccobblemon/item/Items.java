package net.dandycorp.dccobblemon.item;

import net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions;
import net.dandycorp.dccobblemon.item.custom.BadgeItem;
import net.dandycorp.dccobblemon.item.custom.Ticket;
import net.dandycorp.dccobblemon.item.custom.badges.*;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;


public class Items {


    // custom creative menu (item group)
    // ▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄
    public static final RegistryKey<ItemGroup> DANDYCORP_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(DANDYCORPCobblemonAdditions.MOD_ID, "item_group"));
    public static final ItemGroup DANDYCORP_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(Items.BADGE))
            .displayName(Text.translatable("itemGroup.dccobblemon"))
            .build();
    // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀



    // items
    // ▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄
    public static final Item BUG_BADGE = registerItem("bug_badge",
            new BugBadgeItem(new Item.Settings().fireproof().maxCount(1).rarity(Rarity.EPIC)));
    public static final Item DANDY_BADGE = registerItem("dandy_badge",
            new DandyBadgeItem(new Item.Settings().fireproof().maxCount(1).rarity(Rarity.EPIC)));
    public static final Item DARK_BADGE = registerItem("dark_badge",
            new DarkBadgeItem(new Item.Settings().fireproof().maxCount(1).rarity(Rarity.EPIC)));
    public static final Item DRAGON_BADGE = registerItem("dragon_badge",
            new DragonBadgeItem(new Item.Settings().fireproof().maxCount(1).rarity(Rarity.EPIC)));
    public static final Item FIRE_BADGE = registerItem("fire_badge",
            new FireBadgeItem(new Item.Settings().fireproof().maxCount(1).rarity(Rarity.EPIC)));
    public static final Item FLYING_BADGE = registerItem("flying_badge",
            new FlyingBadgeItem(new Item.Settings().fireproof().maxCount(1).rarity(Rarity.EPIC)));
    public static final Item GROUND_BADGE = registerItem("ground_badge",
            new GroundBadgeItem(new Item.Settings().fireproof().maxCount(1).rarity(Rarity.EPIC)));
    public static final Item ICE_BADGE = registerItem("ice_badge",
            new IceBadgeItem(new Item.Settings().fireproof().maxCount(1).rarity(Rarity.EPIC)));
    public static final Item JEFF_BADGE = registerItem("jeff_badge",
            new JeffBadgeItem(new Item.Settings().fireproof().maxCount(1).rarity(Rarity.EPIC)));
    public static final Item LINA_BADGE = registerItem("lina_badge",
            new LinaBadgeItem(new Item.Settings().fireproof().maxCount(1).rarity(Rarity.EPIC)));
    public static final Item NATE_BADGE = registerItem("nate_badge",
            new NateBadgeItem(new Item.Settings().fireproof().maxCount(1).rarity(Rarity.EPIC)));
    public static final Item POISON_BADGE = registerItem("poison_badge",
            new PoisonBadgeItem(new Item.Settings().fireproof().maxCount(1).rarity(Rarity.EPIC)));
    public static final Item PUMPKIN_BADGE = registerItem("pumpkin_badge",
            new PumpkinBadgeItem(new Item.Settings().fireproof().maxCount(1).rarity(Rarity.EPIC)));
    public static final Item ROCK_BADGE = registerItem("rock_badge",
            new RockBadgeItem(new Item.Settings().fireproof().maxCount(1).rarity(Rarity.EPIC)));
    public static final Item SHELLY_BADGE = registerItem("shelly_badge",
            new ShellyBadgeItem(new Item.Settings().fireproof().maxCount(1).rarity(Rarity.EPIC)));
    public static final Item WATER_BADGE = registerItem("water_badge",
            new WaterBadgeItem(new Item.Settings().fireproof().maxCount(1).rarity(Rarity.EPIC)));
    public static final Item BADGE = registerItem("badge",
            new BadgeItem(new Item.Settings().fireproof().maxCount(1).rarity(Rarity.EPIC)));


    public static final Item TICKET = registerItem("ticket", new Ticket(new Item.Settings().rarity(Rarity.RARE)));
    // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    private static Item registerItem(String name, Item item){ //item registering helper function
        return Registry.register(Registries.ITEM, Identifier.of(DANDYCORPCobblemonAdditions.MOD_ID, name), item);
    }

    public static void registerAllItems() {
        DANDYCORPCobblemonAdditions.LOGGER.info("Registering items for " + DANDYCORPCobblemonAdditions.MOD_ID);

        Registry.register(Registries.ITEM_GROUP, DANDYCORP_GROUP_KEY, DANDYCORP_GROUP);
        ItemGroupEvents.modifyEntriesEvent(DANDYCORP_GROUP_KEY).register(entries -> {
            entries.add(BADGE);
            entries.add(BUG_BADGE);
            entries.add(DANDY_BADGE);
            entries.add(DARK_BADGE);
            entries.add(DRAGON_BADGE);
            entries.add(FIRE_BADGE);
            entries.add(FLYING_BADGE);
            entries.add(GROUND_BADGE);
            entries.add(ICE_BADGE);
            entries.add(JEFF_BADGE);
            entries.add(LINA_BADGE);
            entries.add(NATE_BADGE);
            entries.add(POISON_BADGE);
            entries.add(PUMPKIN_BADGE);
            entries.add(ROCK_BADGE);
            entries.add(SHELLY_BADGE);
            entries.add(WATER_BADGE);

            entries.add(TICKET);
        });
    }
}
