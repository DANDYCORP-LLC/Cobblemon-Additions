package net.dandycorp.dccobblemon.item;

import net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions;
import net.dandycorp.dccobblemon.block.DANDYCORPBlocks;
import net.dandycorp.dccobblemon.item.custom.BadgeItem;
import net.dandycorp.dccobblemon.item.custom.ShinyCharmItem;
import net.dandycorp.dccobblemon.item.custom.ShinySandwichItem;
import net.dandycorp.dccobblemon.item.custom.Ticket;
import net.dandycorp.dccobblemon.item.custom.badges.*;
import net.dandycorp.dccobblemon.item.custom.mega.MegaFormType;
import net.dandycorp.dccobblemon.item.custom.mega.MegaStoneItem;
import net.dandycorp.dccobblemon.item.custom.mega.MegaTrinketItem;
import net.dandycorp.dccobblemon.item.custom.mega.PrimalStoneItem;
import net.dandycorp.dccobblemon.item.custom.paragonium.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.List;

import static net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions.SPARKLING_POWER;


public class DANDYCORPItems {


    // custom creative menu (item group)
    // ▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄
    public static final RegistryKey<ItemGroup> DANDYCORP_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(DANDYCORPCobblemonAdditions.MOD_ID, "item_group"));
    public static final ItemGroup DANDYCORP_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(DANDYCORPItems.TICKET))
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
    public static final Item VENDOR = registerItem("vendor", new TallBlockItem(DANDYCORPBlocks.VENDOR_BLOCK,new Item.Settings().rarity(Rarity.UNCOMMON)));
    public static final Item GRINDER_BLADES = registerItem("grinder_blades", new Item(new Item.Settings().fireproof()));
    public static final Item CHROMIUM_INGOT = registerItem("chromium_ingot", new Item(new Item.Settings()));
    public static final Item CHROMIUM_NUGGET = registerItem("chromium_nugget", new Item(new Item.Settings()));
    public static final Item CHROMIUM_DUST = registerItem("chromium_dust", new Item(new Item.Settings()));
    public static final Item RAW_CHROMIUM = registerItem("raw_chromium", new Item(new Item.Settings()));
    public static final Item CHROMIUM_HELMET = registerItem("chromium_helmet",
            new ArmorItem(DANDYCORPArmorMaterials.CHROMIUM, ArmorItem.Type.HELMET, new Item.Settings()));
    public static final Item CHROMIUM_CHESTPLATE = registerItem("chromium_chestplate",
            new ArmorItem(DANDYCORPArmorMaterials.CHROMIUM, ArmorItem.Type.CHESTPLATE, new Item.Settings()));
    public static final Item CHROMIUM_LEGGINGS = registerItem("chromium_leggings",
            new ArmorItem(DANDYCORPArmorMaterials.CHROMIUM, ArmorItem.Type.LEGGINGS, new Item.Settings()));
    public static final Item CHROMIUM_BOOTS = registerItem("chromium_boots",
            new ArmorItem(DANDYCORPArmorMaterials.CHROMIUM, ArmorItem.Type.BOOTS, new Item.Settings()));
    public static final Item CHROMIUM_SWORD = registerItem("chromium_sword",
            new SwordItem(DANDYCORPToolMaterials.CHROMIUM,3,-2.4f,new Item.Settings()));
    public static final Item CHROMIUM_PICKAXE = registerItem("chromium_pickaxe",
            new PickaxeItem(DANDYCORPToolMaterials.CHROMIUM,1,-2.8f,new Item.Settings()));
    public static final Item CHROMIUM_SHOVEL = registerItem("chromium_shovel",
            new ShovelItem(DANDYCORPToolMaterials.CHROMIUM,1,-2.8f,new Item.Settings()));
    public static final Item CHROMIUM_AXE = registerItem("chromium_axe",
            new AxeItem(DANDYCORPToolMaterials.CHROMIUM,5,-3f,new Item.Settings()));
    public static final Item CHROMIUM_HOE = registerItem("chromium_hoe",
            new HoeItem(DANDYCORPToolMaterials.CHROMIUM,-3,0.0f,new Item.Settings()));

    public static final Item PARAGONIUM_INGOT = registerItem("paragonium_ingot",new ParagoniumItem(new Item.Settings().rarity(Rarity.EPIC).fireproof()));
    public static final Item PARAGONIUM_BLOCK_ITEM = registerItem("paragonium_block",new ParagoniumBlockItem(DANDYCORPBlocks.PARAGONIUM_BLOCK, new FabricItemSettings().fireproof().rarity(Rarity.EPIC)));
    public static final Item PARAGONIUM_NUGGET = registerItem("paragonium_nugget",new ParagoniumItem(new Item.Settings().rarity(Rarity.EPIC).fireproof()));
    public static final Item PARAGONIUM_HELMET = registerItem("paragonium_helmet",
            new ParagoniumArmorItem(DANDYCORPArmorMaterials.PARAGONIUM, ParagoniumArmorItem.Type.HELMET, new Item.Settings().rarity(Rarity.EPIC).fireproof()));
    public static final Item PARAGONIUM_CHESTPLATE = registerItem("paragonium_chestplate",
            new ParagoniumArmorItem(DANDYCORPArmorMaterials.PARAGONIUM, ParagoniumArmorItem.Type.CHESTPLATE, new Item.Settings().rarity(Rarity.EPIC).fireproof()));
    public static final Item PARAGONIUM_LEGGINGS = registerItem("paragonium_leggings",
            new ParagoniumArmorItem(DANDYCORPArmorMaterials.PARAGONIUM, ParagoniumArmorItem.Type.LEGGINGS, new Item.Settings().rarity(Rarity.EPIC).fireproof()));
    public static final Item PARAGONIUM_BOOTS = registerItem("paragonium_boots",
            new ParagoniumArmorItem(DANDYCORPArmorMaterials.PARAGONIUM, ParagoniumArmorItem.Type.BOOTS, new Item.Settings().rarity(Rarity.EPIC).fireproof()));
    public static final Item PARAGONIUM_SWORD = registerItem("paragonium_sword",
            new ParagoniumSwordItem(DANDYCORPToolMaterials.PARAGONIUM,3,-2.2f,new Item.Settings().rarity(Rarity.EPIC).fireproof()));
    public static final Item PARAGONIUM_PICKAXE = registerItem("paragonium_pickaxe",
            new ParagoniumPickaxeItem(DANDYCORPToolMaterials.PARAGONIUM,1,-2.8f,new Item.Settings().rarity(Rarity.EPIC).fireproof()));
    public static final Item PARAGONIUM_SHOVEL = registerItem("paragonium_shovel",
            new ParagoniumShovelItem(DANDYCORPToolMaterials.PARAGONIUM,1,-2.8f,new Item.Settings().rarity(Rarity.EPIC).fireproof()));
    public static final Item PARAGONIUM_AXE = registerItem("paragonium_axe",
            new ParagoniumAxeItem(DANDYCORPToolMaterials.PARAGONIUM,8,-3f,new Item.Settings().rarity(Rarity.EPIC).fireproof()));
    public static final Item PARAGONIUM_HOE = registerItem("paragonium_hoe",
            new ParagoniumHoeItem(DANDYCORPToolMaterials.PARAGONIUM,-3,0.0f,new Item.Settings().rarity(Rarity.EPIC).fireproof()));
    public static final Item PARAGONIUM_BOW = registerItem("paragonium_bow",
            new ParagoniumBowItem(new Item.Settings().rarity(Rarity.EPIC).fireproof()));
    public static final Item PARAGONIUM_SHIELD = registerItem("paragonium_shield",
            new ParagoniumShieldItem(new Item.Settings().rarity(Rarity.EPIC).fireproof()));

    public static final Item ABOMASITE = registerItem("abomasite", new MegaStoneItem(new Item.Settings().fireproof().rarity(Rarity.RARE).maxCount(1), "abomasnow", "abomasite", MegaFormType.MEGA));
    public static final Item ABSOLITE = registerItem("absolite", new MegaStoneItem(new Item.Settings().fireproof().rarity(Rarity.RARE).maxCount(1), "absol", "absolite", MegaFormType.MEGA));
    public static final Item AERODACTYLITE = registerItem("aerodactylite", new MegaStoneItem(new Item.Settings().fireproof().rarity(Rarity.RARE).maxCount(1), "aerodactyl", "aerodactylite", MegaFormType.MEGA));
    public static final Item AGGRONITE = registerItem("aggronite", new MegaStoneItem(new Item.Settings().fireproof().rarity(Rarity.RARE).maxCount(1), "aggron", "aggronite", MegaFormType.MEGA));
    public static final Item ALAKAZITE = registerItem("alakazite", new MegaStoneItem(new Item.Settings().fireproof().rarity(Rarity.RARE).maxCount(1), "alakazam", "alakazite", MegaFormType.MEGA));
    public static final Item ALTARIANITE = registerItem("altarianite", new MegaStoneItem(new Item.Settings().fireproof().rarity(Rarity.RARE).maxCount(1), "altaria", "altarianite", MegaFormType.MEGA));
    public static final Item AMPHAROSITE = registerItem("ampharosite", new MegaStoneItem(new Item.Settings().fireproof().rarity(Rarity.RARE).maxCount(1), "ampharos", "ampharosite", MegaFormType.MEGA));
    public static final Item AUDINITE = registerItem("audinite", new MegaStoneItem(new Item.Settings().fireproof().rarity(Rarity.RARE).maxCount(1), "audino", "audinite", MegaFormType.MEGA));
    public static final Item BANETTITE = registerItem("banettite", new MegaStoneItem(new Item.Settings().fireproof().rarity(Rarity.RARE).maxCount(1), "banette", "banettite", MegaFormType.MEGA));
    public static final Item BEEDRILLITE = registerItem("beedrillite", new MegaStoneItem(new Item.Settings().fireproof().rarity(Rarity.RARE).maxCount(1), "beedrill", "beedrillite", MegaFormType.MEGA));
    public static final Item BLASTOISINITE = registerItem("blastoisinite", new MegaStoneItem(new Item.Settings().fireproof().rarity(Rarity.RARE).maxCount(1), "blastoise", "blastoisinite", MegaFormType.MEGA));
    public static final Item BLAZIKENITE = registerItem("blazikenite", new MegaStoneItem(new Item.Settings().fireproof().rarity(Rarity.RARE).maxCount(1), "blaziken", "blazikenite", MegaFormType.MEGA));
    public static final Item CAMERUPTITE = registerItem("cameruptite", new MegaStoneItem(new Item.Settings().fireproof().rarity(Rarity.RARE).maxCount(1), "camerupt", "cameruptite", MegaFormType.MEGA));
    public static final Item CHARIZARDITE_X = registerItem("charizardite-x", new MegaStoneItem(new Item.Settings().fireproof().rarity(Rarity.RARE).maxCount(1), "charizard", "charizardite-x", MegaFormType.MEGA_X));
    public static final Item CHARIZARDITE_Y = registerItem("charizardite-y", new MegaStoneItem(new Item.Settings().fireproof().rarity(Rarity.RARE).maxCount(1), "charizard", "charizardite-y", MegaFormType.MEGA_Y));
    public static final Item DIANCITE = registerItem("diancite", new MegaStoneItem(new Item.Settings().fireproof().rarity(Rarity.RARE).maxCount(1), "diancie", "diancite", MegaFormType.MEGA));
    public static final Item GALLADITE = registerItem("galladite", new MegaStoneItem(new Item.Settings().fireproof().rarity(Rarity.RARE).maxCount(1), "gallade", "galladite", MegaFormType.MEGA));
    public static final Item GARCHOMPITE = registerItem("garchompite", new MegaStoneItem(new Item.Settings().fireproof().rarity(Rarity.RARE).maxCount(1), "garchomp", "garchompite", MegaFormType.MEGA));
    public static final Item GARDEVOIRITE = registerItem("gardevoirite", new MegaStoneItem(new Item.Settings().fireproof().rarity(Rarity.RARE).maxCount(1), "gardevoir", "gardevoirite", MegaFormType.MEGA));
    public static final Item GENGARITE = registerItem("gengarite", new MegaStoneItem(new Item.Settings().fireproof().rarity(Rarity.RARE).maxCount(1), "gengar", "gengarite", MegaFormType.MEGA));
    public static final Item GLALITITE = registerItem("glalitite", new MegaStoneItem(new Item.Settings().fireproof().rarity(Rarity.RARE).maxCount(1), "glalie", "glalitite", MegaFormType.MEGA));
    public static final Item GYARADOSITE = registerItem("gyaradosite", new MegaStoneItem(new Item.Settings().fireproof().rarity(Rarity.RARE).maxCount(1), "gyarados", "gyaradosite", MegaFormType.MEGA));
    public static final Item HERACRONITE = registerItem("heracronite", new MegaStoneItem(new Item.Settings().fireproof().rarity(Rarity.RARE).maxCount(1), "heracross", "heracronite", MegaFormType.MEGA));
    public static final Item HOUNDOOMINITE = registerItem("houndoominite", new MegaStoneItem(new Item.Settings().fireproof().rarity(Rarity.RARE).maxCount(1), "houndoom", "houndoominite", MegaFormType.MEGA));
    public static final Item KANGASKHANITE = registerItem("kangaskhanite", new MegaStoneItem(new Item.Settings().fireproof().rarity(Rarity.RARE).maxCount(1), "kangaskhan", "kangaskhanite", MegaFormType.MEGA));
    public static final Item LATIASITE = registerItem("latiasite", new MegaStoneItem(new Item.Settings().fireproof().rarity(Rarity.RARE).maxCount(1), "latias", "latiasite", MegaFormType.MEGA));
    public static final Item LATIOSITE = registerItem("latiosite", new MegaStoneItem(new Item.Settings().fireproof().rarity(Rarity.RARE).maxCount(1), "latios", "latiosite", MegaFormType.MEGA));
    public static final Item LOPUNNITE = registerItem("lopunnite", new MegaStoneItem(new Item.Settings().fireproof().rarity(Rarity.RARE).maxCount(1), "lopunny", "lopunnite", MegaFormType.MEGA));
    public static final Item LUCARIONITE = registerItem("lucarionite", new MegaStoneItem(new Item.Settings().fireproof().rarity(Rarity.RARE).maxCount(1), "lucario", "lucarionite", MegaFormType.MEGA));
    public static final Item MANECTITE = registerItem("manectite", new MegaStoneItem(new Item.Settings().fireproof().rarity(Rarity.RARE).maxCount(1), "manectric", "manectite", MegaFormType.MEGA));
    public static final Item MAWILITE = registerItem("mawilite", new MegaStoneItem(new Item.Settings().fireproof().rarity(Rarity.RARE).maxCount(1), "mawile", "mawilite", MegaFormType.MEGA));
    public static final Item MEDICHAMITE = registerItem("medichamite", new MegaStoneItem(new Item.Settings().fireproof().rarity(Rarity.RARE).maxCount(1), "medicham", "medichamite", MegaFormType.MEGA));
    public static final Item METAGROSSITE = registerItem("metagrossite", new MegaStoneItem(new Item.Settings().fireproof().rarity(Rarity.RARE).maxCount(1), "metagross", "metagrossite", MegaFormType.MEGA));
    public static final Item MEWTWONITE_X = registerItem("mewtwonite-x", new MegaStoneItem(new Item.Settings().fireproof().rarity(Rarity.RARE).maxCount(1), "mewtwo", "mewtwonite-x", MegaFormType.MEGA_X));
    public static final Item MEWTWONITE_Y = registerItem("mewtwonite-y", new MegaStoneItem(new Item.Settings().fireproof().rarity(Rarity.RARE).maxCount(1), "mewtwo", "mewtwonite-y", MegaFormType.MEGA_Y));
    public static final Item PIDGEOTITE = registerItem("pidgeotite", new MegaStoneItem(new Item.Settings().fireproof().rarity(Rarity.RARE).maxCount(1), "pidgeot", "pidgeotite", MegaFormType.MEGA));
    public static final Item PINSIRITE = registerItem("pinsirite", new MegaStoneItem(new Item.Settings().fireproof().rarity(Rarity.RARE).maxCount(1), "pinsir", "pinsirite", MegaFormType.MEGA));
    public static final Item SABLENITE = registerItem("sablenite", new MegaStoneItem(new Item.Settings().fireproof().rarity(Rarity.RARE).maxCount(1), "sableye", "sablenite", MegaFormType.MEGA));
    public static final Item SALAMENCITE = registerItem("salamencite", new MegaStoneItem(new Item.Settings().fireproof().rarity(Rarity.RARE).maxCount(1), "salamence", "salamencite", MegaFormType.MEGA));
    public static final Item SCEPTILITE = registerItem("sceptilite", new MegaStoneItem(new Item.Settings().fireproof().rarity(Rarity.RARE).maxCount(1), "sceptile", "sceptilite", MegaFormType.MEGA));
    public static final Item SCIZORITE = registerItem("scizorite", new MegaStoneItem(new Item.Settings().fireproof().rarity(Rarity.RARE).maxCount(1), "scizor", "scizorite", MegaFormType.MEGA));
    public static final Item SHARPEDONITE = registerItem("sharpedonite", new MegaStoneItem(new Item.Settings().fireproof().rarity(Rarity.RARE).maxCount(1), "sharpedo", "sharpedonite", MegaFormType.MEGA));
    public static final Item SLOWBRONITE = registerItem("slowbronite", new MegaStoneItem(new Item.Settings().fireproof().rarity(Rarity.RARE).maxCount(1), "slowbro", "slowbronite", MegaFormType.MEGA));
    public static final Item STEELIXITE = registerItem("steelixite", new MegaStoneItem(new Item.Settings().fireproof().rarity(Rarity.RARE).maxCount(1), "steelix", "steelixite", MegaFormType.MEGA));
    public static final Item SWAMPERTITE = registerItem("swampertite", new MegaStoneItem(new Item.Settings().fireproof().rarity(Rarity.RARE).maxCount(1), "swampert", "swampertite", MegaFormType.MEGA));
    public static final Item TYRANITARITE = registerItem("tyranitarite", new MegaStoneItem(new Item.Settings().fireproof().rarity(Rarity.RARE).maxCount(1), "tyranitar", "tyranitarite", MegaFormType.MEGA));
    public static final Item VENUSAURITE = registerItem("venusaurite", new MegaStoneItem(new Item.Settings().fireproof().rarity(Rarity.RARE).maxCount(1), "venusaur", "venusaurite", MegaFormType.MEGA));

    public static final Item BLUE_ORB = registerItem("blue_orb", new PrimalStoneItem(new Item.Settings().fireproof().rarity(Rarity.EPIC).maxCount(1), "kyogre"));
    public static final Item RED_ORB = registerItem("red_orb", new PrimalStoneItem(new Item.Settings().fireproof().rarity(Rarity.EPIC).maxCount(1), "groudon"));

    public static final Item KEYSTONE = registerItem("keystone",
            new MegaTrinketItem(new Item.Settings().rarity(Rarity.EPIC).fireproof()));
    public static final Item SHINY_CHARM = registerItem("shiny_charm",
            new ShinyCharmItem(new Item.Settings().rarity(Rarity.UNCOMMON).fireproof().maxCount(1)));

    public static final Item SHINY_SANDWICH_1 = registerItem("shiny_sandwich_1",
            new ShinySandwichItem(new Item.Settings().food(
                    new FoodComponent.Builder()
                            .alwaysEdible()
                            .hunger(8)
                            .saturationModifier(1.2f)
                            .statusEffect(new StatusEffectInstance(SPARKLING_POWER,24000,0,true,true),1f).build())
            ));

    public static final Item SHINY_SANDWICH_2 = registerItem("shiny_sandwich_2",
            new ShinySandwichItem(new Item.Settings().food(
                    new FoodComponent.Builder()
                            .alwaysEdible()
                            .hunger(12)
                            .saturationModifier(1.2f)
                            .statusEffect(new StatusEffectInstance(SPARKLING_POWER,48000,1,true,true),1f).build())
                    .rarity(Rarity.UNCOMMON)
            ));

    public static final Item SHINY_SANDWICH_3 = registerItem("shiny_sandwich_3",
            new ShinySandwichItem(new Item.Settings().food(
                    new FoodComponent.Builder()
                            .alwaysEdible()
                            .hunger(16)
                            .saturationModifier(1.2f)
                            .statusEffect(new StatusEffectInstance(SPARKLING_POWER,72000,2,true,true),1f).build())
                    .rarity(Rarity.RARE)
            ));

    // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    public static final List<Item> ALL_STONES = List.of(ABSOLITE, AERODACTYLITE, AGGRONITE, ALAKAZITE, ALTARIANITE,
            AMPHAROSITE, AUDINITE, BANETTITE, BEEDRILLITE, BLASTOISINITE, BLAZIKENITE, CAMERUPTITE, CHARIZARDITE_X,
            CHARIZARDITE_Y, DIANCITE, GALLADITE, GARCHOMPITE, GARDEVOIRITE, GENGARITE, GLALITITE, GYARADOSITE,
            HERACRONITE, HOUNDOOMINITE, KANGASKHANITE, LATIASITE, LATIOSITE, LOPUNNITE, LUCARIONITE,MANECTITE, MAWILITE,
            MEDICHAMITE, METAGROSSITE, MEWTWONITE_X, MEWTWONITE_Y, PIDGEOTITE, PINSIRITE, SABLENITE, SALAMENCITE,
            SCEPTILITE, SCIZORITE, SHARPEDONITE, SLOWBRONITE, STEELIXITE, SWAMPERTITE, TYRANITARITE, VENUSAURITE);

    private static Item registerItem(String name, Item item){ //item registering helper function
        return Registry.register(Registries.ITEM, Identifier.of(DANDYCORPCobblemonAdditions.MOD_ID, name), item);
    }

    public static void registerAllItems() {
        DANDYCORPCobblemonAdditions.LOGGER.info("Registering items for " + DANDYCORPCobblemonAdditions.MOD_ID);

        Registry.register(Registries.ITEM_GROUP, DANDYCORP_GROUP_KEY, DANDYCORP_GROUP);
        ItemGroupEvents.modifyEntriesEvent(DANDYCORP_GROUP_KEY).register(entries -> {
            // badges
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

            // vendor / grinder
            entries.add(TICKET);
            entries.add(DANDYCORPBlocks.TICKET_BAG);
            entries.add(VENDOR);
            entries.add(GRINDER_BLADES);
            entries.add(DANDYCORPBlocks.GRINDER_BLOCK);

            // chromium
            entries.add(CHROMIUM_INGOT);
            entries.add(CHROMIUM_NUGGET);
            entries.add(CHROMIUM_DUST);
            entries.add(RAW_CHROMIUM);
            entries.add(DANDYCORPBlocks.CHROMIUM_BLOCK);
            entries.add(DANDYCORPBlocks.RAW_CHROMIUM_BLOCK);
            entries.add(CHROMIUM_HELMET);
            entries.add(CHROMIUM_CHESTPLATE);
            entries.add(CHROMIUM_LEGGINGS);
            entries.add(CHROMIUM_BOOTS);
            entries.add(CHROMIUM_SWORD);
            entries.add(CHROMIUM_SHOVEL);
            entries.add(CHROMIUM_PICKAXE);
            entries.add(CHROMIUM_AXE);
            entries.add(CHROMIUM_HOE);

            // paragonium
            entries.add(PARAGONIUM_INGOT);
            entries.add(PARAGONIUM_BLOCK_ITEM);
            entries.add(PARAGONIUM_NUGGET);
            entries.add(PARAGONIUM_HELMET);
            entries.add(PARAGONIUM_CHESTPLATE);
            entries.add(PARAGONIUM_LEGGINGS);
            entries.add(PARAGONIUM_BOOTS);
            entries.add(PARAGONIUM_SWORD);
            entries.add(PARAGONIUM_SHOVEL);
            entries.add(PARAGONIUM_PICKAXE);
            entries.add(PARAGONIUM_AXE);
            entries.add(PARAGONIUM_HOE);
            entries.add(PARAGONIUM_BOW);

            // stones
            entries.add(ABOMASITE);
            entries.add(ABSOLITE);
            entries.add(AERODACTYLITE);
            entries.add(AGGRONITE);
            entries.add(ALAKAZITE);
            entries.add(ALTARIANITE);
            entries.add(AMPHAROSITE);
            entries.add(AUDINITE);
            entries.add(BANETTITE);
            entries.add(BEEDRILLITE);
            entries.add(BLASTOISINITE);
            entries.add(BLAZIKENITE);
            entries.add(CAMERUPTITE);
            entries.add(CHARIZARDITE_X);
            entries.add(CHARIZARDITE_Y);
            entries.add(DIANCITE);
            entries.add(GALLADITE);
            entries.add(GARCHOMPITE);
            entries.add(GARDEVOIRITE);
            entries.add(GENGARITE);
            entries.add(GLALITITE);
            entries.add(GYARADOSITE);
            entries.add(HERACRONITE);
            entries.add(HOUNDOOMINITE);
            entries.add(KANGASKHANITE);
            entries.add(LATIASITE);
            entries.add(LATIOSITE);
            entries.add(LOPUNNITE);
            entries.add(LUCARIONITE);
            entries.add(MANECTITE);
            entries.add(MAWILITE);
            entries.add(MEDICHAMITE);
            entries.add(METAGROSSITE);
            entries.add(MEWTWONITE_X);
            entries.add(MEWTWONITE_Y);
            entries.add(PIDGEOTITE);
            entries.add(PINSIRITE);
            entries.add(SABLENITE);
            entries.add(SALAMENCITE);
            entries.add(SCEPTILITE);
            entries.add(SCIZORITE);
            entries.add(SHARPEDONITE);
            entries.add(SLOWBRONITE);
            entries.add(STEELIXITE);
            entries.add(SWAMPERTITE);
            entries.add(TYRANITARITE);
            entries.add(VENUSAURITE);

            entries.add(BLUE_ORB);
            entries.add(RED_ORB);

            entries.add(KEYSTONE);
            entries.add(SHINY_CHARM);
            entries.add(SHINY_SANDWICH_1);
            entries.add(SHINY_SANDWICH_2);
            entries.add(SHINY_SANDWICH_3);
        });
    }
}
