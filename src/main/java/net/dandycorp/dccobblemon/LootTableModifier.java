package net.dandycorp.dccobblemon;

import net.dandycorp.dccobblemon.item.Items;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.item.Item;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;

public class LootTableModifier {
    public static final Identifier JUNGLE_TEMPLE_ID = new Identifier("minecraft", "chests/jungle_temple");
    public static final Identifier ANCIENT_CITY_ID = new Identifier("minecraft", "chests/ancient_city");
    public static final Identifier BASTION_ID = new Identifier("minecraft", "chests/bastion_treasure");
    public static final Identifier PYRAMID_ID = new Identifier("minecraft", "chests/desert_pyramid");
    public static final Identifier END_CITY_ID = new Identifier("minecraft", "chests/end_city_treasure");
    public static final Identifier NETHER_FORTRESS_ID = new Identifier("minecraft", "chests/nether_bridge");
    public static final Identifier PILLAGER_OUTPOST_ID = new Identifier("minecraft", "chests/pillager_outpost");
    public static final Identifier SHIPWRECK_ID = new Identifier("minecraft", "chests/shipwreck_treasure");
    public static final Identifier DUNGEON_ID = new Identifier("minecraft", "chests/simple_dungeon");
    public static final Identifier STRONGHOLD_ID = new Identifier("minecraft", "chests/stronghold_crossing");
    public static final Identifier MANSION_ID = new Identifier("minecraft", "chests/woodland_mansion");


    public static void modifyLootTables(){
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            // Jungle Temple: Bug, Grass, Poison types
            if (id.equals(JUNGLE_TEMPLE_ID)) {
                LootPool.Builder junglePool = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(2.2F))
                        .with(ItemEntry.builder(Items.VENUSAURITE))
                        .with(ItemEntry.builder(Items.PINSIRITE))
                        .with(ItemEntry.builder(Items.SCIZORITE))
                        .with(ItemEntry.builder(Items.HERACRONITE))
                        .with(ItemEntry.builder(Items.ABOMASITE))
                        .with(ItemEntry.builder(Items.BEEDRILLITE))
                        .with(ItemEntry.builder(Items.SCEPTILITE))
                        .with(ItemEntry.builder(Items.GENGARITE))
                        .conditionally(RandomChanceLootCondition.builder(1.0f)); // 70% chance
                tableBuilder.pool(junglePool.build());
            }

            // Ancient City: Dark, Psychic, Ghost types
            if (id.equals(ANCIENT_CITY_ID)) {
                LootPool.Builder ancientCityPool = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(Items.ABSOLITE))
                        .with(ItemEntry.builder(Items.HOUNDOOMINITE))
                        .with(ItemEntry.builder(Items.MEWTWONITE_X))
                        .with(ItemEntry.builder(Items.SABLENITE))
                        .with(ItemEntry.builder(Items.TYRANITARITE))
                        .with(ItemEntry.builder(Items.ALAKAZITE))
                        .with(ItemEntry.builder(Items.GALLADITE))
                        .with(ItemEntry.builder(Items.GARDEVOIRITE))
                        .with(ItemEntry.builder(Items.MEDICHAMITE))
                        .with(ItemEntry.builder(Items.METAGROSSITE))
                        .with(ItemEntry.builder(Items.MEWTWONITE_Y))
                        .with(ItemEntry.builder(Items.LATIASITE))
                        .with(ItemEntry.builder(Items.LATIOSITE))
                        .with(ItemEntry.builder(Items.BANETTITE))
                        .with(ItemEntry.builder(Items.GENGARITE))
                        .conditionally(RandomChanceLootCondition.builder(0.35f));
                tableBuilder.pool(ancientCityPool.build());
            }

            // Bastion: Steel, Rock, Fighting, Poison types
            if (id.equals(BASTION_ID)) {
                LootPool.Builder bastionPool = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(2.2F))
                        .with(ItemEntry.builder(Items.AGGRONITE))
                        .with(ItemEntry.builder(Items.SCIZORITE))
                        .with(ItemEntry.builder(Items.LUCARIONITE))
                        .with(ItemEntry.builder(Items.MAWILITE))
                        .with(ItemEntry.builder(Items.METAGROSSITE))
                        .with(ItemEntry.builder(Items.STEELIXITE))
                        .with(ItemEntry.builder(Items.TYRANITARITE))
                        .with(ItemEntry.builder(Items.BLAZIKENITE))
                        .with(ItemEntry.builder(Items.GALLADITE))
                        .with(ItemEntry.builder(Items.HERACRONITE))
                        .with(ItemEntry.builder(Items.PINSIRITE))
                        .with(ItemEntry.builder(Items.LOPUNNITE))
                        .with(ItemEntry.builder(Items.MEDICHAMITE))
                        .with(ItemEntry.builder(Items.BEEDRILLITE))
                        .with(ItemEntry.builder(Items.GENGARITE))
                        .with(ItemEntry.builder(Items.VENUSAURITE))
                        .conditionally(RandomChanceLootCondition.builder(1f));
                tableBuilder.pool(bastionPool.build());
            }

            // Pyramid: Ground, Electric, Fire types
            if (id.equals(PYRAMID_ID)) {
                LootPool.Builder pyramidPool = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(Items.CAMERUPTITE))
                        .with(ItemEntry.builder(Items.GARCHOMPITE))
                        .with(ItemEntry.builder(Items.SWAMPERTITE))
                        .with(ItemEntry.builder(Items.AMPHAROSITE))
                        .with(ItemEntry.builder(Items.MANECTITE))
                        .with(ItemEntry.builder(Items.BLAZIKENITE))
                        .with(ItemEntry.builder(Items.HOUNDOOMINITE))
                        .conditionally(RandomChanceLootCondition.builder(0.5f));
                tableBuilder.pool(pyramidPool.build());
            }

            // End City: Dragon, Fairy, Normal, Flying types (and Mewtwo)
            if (id.equals(END_CITY_ID)) {
                LootPool.Builder endCityPool = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.4F))
                        .with(ItemEntry.builder(Items.ALTARIANITE))
                        .with(ItemEntry.builder(Items.GARCHOMPITE))
                        .with(ItemEntry.builder(Items.LATIASITE))
                        .with(ItemEntry.builder(Items.LATIOSITE))
                        .with(ItemEntry.builder(Items.SCEPTILITE))
                        .with(ItemEntry.builder(Items.SALAMENCITE))
                        .with(ItemEntry.builder(Items.CHARIZARDITE_X))
                        .with(ItemEntry.builder(Items.DIANCITE))
                        .with(ItemEntry.builder(Items.GARDEVOIRITE))
                        .with(ItemEntry.builder(Items.MAWILITE))
                        .with(ItemEntry.builder(Items.AUDINITE))
                        .with(ItemEntry.builder(Items.KANGASKHANITE))
                        .with(ItemEntry.builder(Items.AERODACTYLITE))
                        .with(ItemEntry.builder(Items.CHARIZARDITE_Y))
                        .with(ItemEntry.builder(Items.GYARADOSITE))
                        .with(ItemEntry.builder(Items.MEWTWONITE_Y))
                        .with(ItemEntry.builder(Items.MEWTWONITE_X))
                        .conditionally(RandomChanceLootCondition.builder(1.0f));
                tableBuilder.pool(endCityPool.build());
            }

            // Nether Fortress: Fire, Dark, Rock types
            if (id.equals(NETHER_FORTRESS_ID)) {
                LootPool.Builder netherFortressPool = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(Items.BLAZIKENITE))
                        .with(ItemEntry.builder(Items.CAMERUPTITE))
                        .with(ItemEntry.builder(Items.HOUNDOOMINITE))
                        .with(ItemEntry.builder(Items.ABSOLITE))
                        .with(ItemEntry.builder(Items.HOUNDOOMINITE))
                        .with(ItemEntry.builder(Items.MEWTWONITE_X))
                        .with(ItemEntry.builder(Items.SABLENITE))
                        .with(ItemEntry.builder(Items.TYRANITARITE))
                        .with(ItemEntry.builder(Items.AERODACTYLITE))
                        .with(ItemEntry.builder(Items.AGGRONITE))
                        .with(ItemEntry.builder(Items.STEELIXITE))
                        .conditionally(RandomChanceLootCondition.builder(0.7f)); // 70% chance
                tableBuilder.pool(netherFortressPool.build());
            }

            // Pillager Outpost: Flying, Fighting, Normal, Ice types
            if (id.equals(PILLAGER_OUTPOST_ID)) {
                LootPool.Builder pillagerOutpostPool = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(2.2F))
                        .with(ItemEntry.builder(Items.AERODACTYLITE))
                        .with(ItemEntry.builder(Items.ALTARIANITE))
                        .with(ItemEntry.builder(Items.CHARIZARDITE_Y))
                        .with(ItemEntry.builder(Items.GYARADOSITE))
                        .with(ItemEntry.builder(Items.PIDGEOTITE))
                        .with(ItemEntry.builder(Items.SALAMENCITE))
                        .with(ItemEntry.builder(Items.MEWTWONITE_Y))
                        .with(ItemEntry.builder(Items.BLAZIKENITE))
                        .with(ItemEntry.builder(Items.GALLADITE))
                        .with(ItemEntry.builder(Items.HERACRONITE))
                        .with(ItemEntry.builder(Items.PINSIRITE))
                        .with(ItemEntry.builder(Items.LOPUNNITE))
                        .with(ItemEntry.builder(Items.LUCARIONITE))
                        .with(ItemEntry.builder(Items.MEDICHAMITE))
                        .with(ItemEntry.builder(Items.AUDINITE))
                        .with(ItemEntry.builder(Items.KANGASKHANITE))
                        .with(ItemEntry.builder(Items.PIDGEOTITE))
                        .with(ItemEntry.builder(Items.ABOMASITE))
                        .with(ItemEntry.builder(Items.GLALITITE))
                        .conditionally(RandomChanceLootCondition.builder(1f)); // 70% chance
                tableBuilder.pool(pillagerOutpostPool.build());
            }

            // Shipwreck: Water, Normal, Ice types
            if (id.equals(SHIPWRECK_ID)) {
                LootPool.Builder shipwreckPool = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(Items.BLASTOISINITE))
                        .with(ItemEntry.builder(Items.GYARADOSITE))
                        .with(ItemEntry.builder(Items.SLOWBRONITE))
                        .with(ItemEntry.builder(Items.SHARPEDONITE))
                        .with(ItemEntry.builder(Items.SWAMPERTITE))
                        .with(ItemEntry.builder(Items.AUDINITE))
                        .with(ItemEntry.builder(Items.LOPUNNITE))
                        .with(ItemEntry.builder(Items.KANGASKHANITE))
                        .with(ItemEntry.builder(Items.PIDGEOTITE))
                        .with(ItemEntry.builder(Items.ABOMASITE))
                        .with(ItemEntry.builder(Items.GLALITITE))
                        .conditionally(RandomChanceLootCondition.builder(0.7f)); // 70% chance
                tableBuilder.pool(shipwreckPool.build());
            }

            // Dungeon: Any stone with 50% chance, and a second 70% chance for Bug and Grass types
            if (id.equals(DUNGEON_ID)) {
                LootPool.Builder dungeonFirstPool = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .conditionally(RandomChanceLootCondition.builder(0.5f));
                for(Item item : Items.ALL_STONES){
                    dungeonFirstPool.with(ItemEntry.builder(item));
                }
                tableBuilder.pool(dungeonFirstPool.build());

                LootPool.Builder dungeonSecondPool = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(Items.VENUSAURITE))
                        .with(ItemEntry.builder(Items.PINSIRITE))
                        .with(ItemEntry.builder(Items.SCIZORITE))
                        .with(ItemEntry.builder(Items.HERACRONITE))
                        .with(ItemEntry.builder(Items.ABOMASITE))
                        .with(ItemEntry.builder(Items.BEEDRILLITE))
                        .with(ItemEntry.builder(Items.SCEPTILITE))
                        .conditionally(RandomChanceLootCondition.builder(0.7f));
                tableBuilder.pool(dungeonSecondPool.build());
            }

            // Stronghold: Ground, Rock, Grass types
            if (id.equals(STRONGHOLD_ID)) {
                LootPool.Builder strongholdPool = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(Items.CAMERUPTITE))
                        .with(ItemEntry.builder(Items.GARCHOMPITE))
                        .with(ItemEntry.builder(Items.SWAMPERTITE))
                        .with(ItemEntry.builder(Items.AERODACTYLITE))
                        .with(ItemEntry.builder(Items.AGGRONITE))
                        .with(ItemEntry.builder(Items.STEELIXITE))
                        .with(ItemEntry.builder(Items.TYRANITARITE))
                        .with(ItemEntry.builder(Items.ABOMASITE))
                        .with(ItemEntry.builder(Items.SCEPTILITE))
                        .with(ItemEntry.builder(Items.VENUSAURITE))
                        .conditionally(RandomChanceLootCondition.builder(0.5f));
                tableBuilder.pool(strongholdPool.build());
            }

            // Mansion: Psychic, Poison, Electric, Ghost types
            if (id.equals(MANSION_ID)) {
                LootPool.Builder mansionPool = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(Items.ALAKAZITE))
                        .with(ItemEntry.builder(Items.GALLADITE))
                        .with(ItemEntry.builder(Items.GARDEVOIRITE))
                        .with(ItemEntry.builder(Items.MAWILITE))
                        .with(ItemEntry.builder(Items.MEDICHAMITE))
                        .with(ItemEntry.builder(Items.METAGROSSITE))
                        .with(ItemEntry.builder(Items.MEWTWONITE_X))
                        .with(ItemEntry.builder(Items.MEWTWONITE_Y))
                        .with(ItemEntry.builder(Items.LATIASITE))
                        .with(ItemEntry.builder(Items.LATIOSITE))
                        .with(ItemEntry.builder(Items.BEEDRILLITE))
                        .with(ItemEntry.builder(Items.GENGARITE))
                        .with(ItemEntry.builder(Items.VENUSAURITE))
                        .with(ItemEntry.builder(Items.AMPHAROSITE))
                        .with(ItemEntry.builder(Items.MANECTITE))
                        .with(ItemEntry.builder(Items.BANETTITE))
                        .with(ItemEntry.builder(Items.SABLENITE))
                        .conditionally(RandomChanceLootCondition.builder(0.9f));
                tableBuilder.pool(mansionPool.build());
            }

        });
    }
}
