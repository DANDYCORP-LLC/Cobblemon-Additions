package net.dandycorp.dccobblemon;

import net.dandycorp.dccobblemon.item.DANDYCORPItems;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.item.Item;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
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
                        .with(ItemEntry.builder(DANDYCORPItems.VENUSAURITE))
                        .with(ItemEntry.builder(DANDYCORPItems.PINSIRITE))
                        .with(ItemEntry.builder(DANDYCORPItems.SCIZORITE))
                        .with(ItemEntry.builder(DANDYCORPItems.HERACRONITE))
                        .with(ItemEntry.builder(DANDYCORPItems.ABOMASITE))
                        .with(ItemEntry.builder(DANDYCORPItems.BEEDRILLITE))
                        .with(ItemEntry.builder(DANDYCORPItems.SCEPTILITE))
                        .with(ItemEntry.builder(DANDYCORPItems.GENGARITE))
                        .conditionally(RandomChanceLootCondition.builder(0.8f));
                tableBuilder.pool(junglePool.build());
            }

            // Ancient City: Dark, Psychic, Ghost types
            if (id.equals(ANCIENT_CITY_ID)) {
                LootPool.Builder ancientCityPool = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(DANDYCORPItems.ABSOLITE))
                        .with(ItemEntry.builder(DANDYCORPItems.HOUNDOOMINITE))
                        .with(ItemEntry.builder(DANDYCORPItems.MEWTWONITE_X))
                        .with(ItemEntry.builder(DANDYCORPItems.SABLENITE))
                        .with(ItemEntry.builder(DANDYCORPItems.TYRANITARITE))
                        .with(ItemEntry.builder(DANDYCORPItems.ALAKAZITE))
                        .with(ItemEntry.builder(DANDYCORPItems.GALLADITE))
                        .with(ItemEntry.builder(DANDYCORPItems.GARDEVOIRITE))
                        .with(ItemEntry.builder(DANDYCORPItems.MEDICHAMITE))
                        .with(ItemEntry.builder(DANDYCORPItems.METAGROSSITE))
                        .with(ItemEntry.builder(DANDYCORPItems.MEWTWONITE_Y))
                        .with(ItemEntry.builder(DANDYCORPItems.LATIASITE))
                        .with(ItemEntry.builder(DANDYCORPItems.LATIOSITE))
                        .with(ItemEntry.builder(DANDYCORPItems.BANETTITE))
                        .with(ItemEntry.builder(DANDYCORPItems.GENGARITE))
                        .conditionally(RandomChanceLootCondition.builder(0.2f));
                tableBuilder.pool(ancientCityPool.build());
            }

            // Bastion: Steel, Rock, Fighting, Poison types
            if (id.equals(BASTION_ID)) {
                LootPool.Builder bastionPool = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(2.2F))
                        .with(ItemEntry.builder(DANDYCORPItems.AGGRONITE))
                        .with(ItemEntry.builder(DANDYCORPItems.SCIZORITE))
                        .with(ItemEntry.builder(DANDYCORPItems.LUCARIONITE))
                        .with(ItemEntry.builder(DANDYCORPItems.MAWILITE))
                        .with(ItemEntry.builder(DANDYCORPItems.METAGROSSITE))
                        .with(ItemEntry.builder(DANDYCORPItems.STEELIXITE))
                        .with(ItemEntry.builder(DANDYCORPItems.TYRANITARITE))
                        .with(ItemEntry.builder(DANDYCORPItems.BLAZIKENITE))
                        .with(ItemEntry.builder(DANDYCORPItems.GALLADITE))
                        .with(ItemEntry.builder(DANDYCORPItems.HERACRONITE))
                        .with(ItemEntry.builder(DANDYCORPItems.PINSIRITE))
                        .with(ItemEntry.builder(DANDYCORPItems.LOPUNNITE))
                        .with(ItemEntry.builder(DANDYCORPItems.MEDICHAMITE))
                        .with(ItemEntry.builder(DANDYCORPItems.BEEDRILLITE))
                        .with(ItemEntry.builder(DANDYCORPItems.GENGARITE))
                        .with(ItemEntry.builder(DANDYCORPItems.VENUSAURITE))
                        .conditionally(RandomChanceLootCondition.builder(1f));
                tableBuilder.pool(bastionPool.build());
            }

            // Pyramid: Ground, Electric, Fire types
            if (id.equals(PYRAMID_ID)) {
                LootPool.Builder pyramidPool = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(DANDYCORPItems.CAMERUPTITE))
                        .with(ItemEntry.builder(DANDYCORPItems.GARCHOMPITE))
                        .with(ItemEntry.builder(DANDYCORPItems.SWAMPERTITE))
                        .with(ItemEntry.builder(DANDYCORPItems.AMPHAROSITE))
                        .with(ItemEntry.builder(DANDYCORPItems.MANECTITE))
                        .with(ItemEntry.builder(DANDYCORPItems.BLAZIKENITE))
                        .with(ItemEntry.builder(DANDYCORPItems.HOUNDOOMINITE))
                        .conditionally(RandomChanceLootCondition.builder(0.5f));
                tableBuilder.pool(pyramidPool.build());
            }

            // End City: Dragon, Fairy, Normal, Flying types (and Mewtwo)
            if (id.equals(END_CITY_ID)) {
                LootPool.Builder endCityPool = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.4F))
                        .with(ItemEntry.builder(DANDYCORPItems.ALTARIANITE))
                        .with(ItemEntry.builder(DANDYCORPItems.GARCHOMPITE))
                        .with(ItemEntry.builder(DANDYCORPItems.LATIASITE))
                        .with(ItemEntry.builder(DANDYCORPItems.LATIOSITE))
                        .with(ItemEntry.builder(DANDYCORPItems.SCEPTILITE))
                        .with(ItemEntry.builder(DANDYCORPItems.SALAMENCITE))
                        .with(ItemEntry.builder(DANDYCORPItems.CHARIZARDITE_X))
                        .with(ItemEntry.builder(DANDYCORPItems.DIANCITE))
                        .with(ItemEntry.builder(DANDYCORPItems.GARDEVOIRITE))
                        .with(ItemEntry.builder(DANDYCORPItems.MAWILITE))
                        .with(ItemEntry.builder(DANDYCORPItems.AUDINITE))
                        .with(ItemEntry.builder(DANDYCORPItems.KANGASKHANITE))
                        .with(ItemEntry.builder(DANDYCORPItems.AERODACTYLITE))
                        .with(ItemEntry.builder(DANDYCORPItems.CHARIZARDITE_Y))
                        .with(ItemEntry.builder(DANDYCORPItems.GYARADOSITE))
                        .with(ItemEntry.builder(DANDYCORPItems.MEWTWONITE_Y))
                        .with(ItemEntry.builder(DANDYCORPItems.MEWTWONITE_X))
                        .conditionally(RandomChanceLootCondition.builder(0.5f));
                tableBuilder.pool(endCityPool.build());
            }

            // Nether Fortress: Fire, Dark, Rock types
            if (id.equals(NETHER_FORTRESS_ID)) {
                LootPool.Builder netherFortressPool = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(DANDYCORPItems.BLAZIKENITE))
                        .with(ItemEntry.builder(DANDYCORPItems.CAMERUPTITE))
                        .with(ItemEntry.builder(DANDYCORPItems.HOUNDOOMINITE))
                        .with(ItemEntry.builder(DANDYCORPItems.ABSOLITE))
                        .with(ItemEntry.builder(DANDYCORPItems.HOUNDOOMINITE))
                        .with(ItemEntry.builder(DANDYCORPItems.MEWTWONITE_X))
                        .with(ItemEntry.builder(DANDYCORPItems.SABLENITE))
                        .with(ItemEntry.builder(DANDYCORPItems.TYRANITARITE))
                        .with(ItemEntry.builder(DANDYCORPItems.AERODACTYLITE))
                        .with(ItemEntry.builder(DANDYCORPItems.AGGRONITE))
                        .with(ItemEntry.builder(DANDYCORPItems.STEELIXITE))
                        .conditionally(RandomChanceLootCondition.builder(0.3f));
                tableBuilder.pool(netherFortressPool.build());
            }

            // Pillager Outpost: Flying, Fighting, Normal, Ice types
            if (id.equals(PILLAGER_OUTPOST_ID)) {
                LootPool.Builder pillagerOutpostPool = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(2.2F))
                        .with(ItemEntry.builder(DANDYCORPItems.AERODACTYLITE))
                        .with(ItemEntry.builder(DANDYCORPItems.ALTARIANITE))
                        .with(ItemEntry.builder(DANDYCORPItems.CHARIZARDITE_Y))
                        .with(ItemEntry.builder(DANDYCORPItems.GYARADOSITE))
                        .with(ItemEntry.builder(DANDYCORPItems.PIDGEOTITE))
                        .with(ItemEntry.builder(DANDYCORPItems.SALAMENCITE))
                        .with(ItemEntry.builder(DANDYCORPItems.MEWTWONITE_Y))
                        .with(ItemEntry.builder(DANDYCORPItems.BLAZIKENITE))
                        .with(ItemEntry.builder(DANDYCORPItems.GALLADITE))
                        .with(ItemEntry.builder(DANDYCORPItems.HERACRONITE))
                        .with(ItemEntry.builder(DANDYCORPItems.PINSIRITE))
                        .with(ItemEntry.builder(DANDYCORPItems.LOPUNNITE))
                        .with(ItemEntry.builder(DANDYCORPItems.LUCARIONITE))
                        .with(ItemEntry.builder(DANDYCORPItems.MEDICHAMITE))
                        .with(ItemEntry.builder(DANDYCORPItems.AUDINITE))
                        .with(ItemEntry.builder(DANDYCORPItems.KANGASKHANITE))
                        .with(ItemEntry.builder(DANDYCORPItems.PIDGEOTITE))
                        .with(ItemEntry.builder(DANDYCORPItems.ABOMASITE))
                        .with(ItemEntry.builder(DANDYCORPItems.GLALITITE))
                        .conditionally(RandomChanceLootCondition.builder(0.8f));
                tableBuilder.pool(pillagerOutpostPool.build());
            }

            // Shipwreck: Water, Normal, Ice types
            if (id.equals(SHIPWRECK_ID)) {
                LootPool.Builder shipwreckPool = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(DANDYCORPItems.BLASTOISINITE))
                        .with(ItemEntry.builder(DANDYCORPItems.GYARADOSITE))
                        .with(ItemEntry.builder(DANDYCORPItems.SLOWBRONITE))
                        .with(ItemEntry.builder(DANDYCORPItems.SHARPEDONITE))
                        .with(ItemEntry.builder(DANDYCORPItems.SWAMPERTITE))
                        .with(ItemEntry.builder(DANDYCORPItems.AUDINITE))
                        .with(ItemEntry.builder(DANDYCORPItems.LOPUNNITE))
                        .with(ItemEntry.builder(DANDYCORPItems.KANGASKHANITE))
                        .with(ItemEntry.builder(DANDYCORPItems.PIDGEOTITE))
                        .with(ItemEntry.builder(DANDYCORPItems.ABOMASITE))
                        .with(ItemEntry.builder(DANDYCORPItems.GLALITITE))
                        .conditionally(RandomChanceLootCondition.builder(0.2f));
                tableBuilder.pool(shipwreckPool.build());
            }

            if (id.equals(DUNGEON_ID)) {
                LootPool.Builder dungeonFirstPool = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.2F))
                        .conditionally(RandomChanceLootCondition.builder(0.1f));
                for(Item item : DANDYCORPItems.ALL_STONES){
                    dungeonFirstPool.with(ItemEntry.builder(item));
                }
                tableBuilder.pool(dungeonFirstPool.build());

                LootPool.Builder dungeonSecondPool = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(DANDYCORPItems.VENUSAURITE))
                        .with(ItemEntry.builder(DANDYCORPItems.PINSIRITE))
                        .with(ItemEntry.builder(DANDYCORPItems.SCIZORITE))
                        .with(ItemEntry.builder(DANDYCORPItems.HERACRONITE))
                        .with(ItemEntry.builder(DANDYCORPItems.ABOMASITE))
                        .with(ItemEntry.builder(DANDYCORPItems.BEEDRILLITE))
                        .with(ItemEntry.builder(DANDYCORPItems.SCEPTILITE))
                        .conditionally(RandomChanceLootCondition.builder(0.4f));
                tableBuilder.pool(dungeonSecondPool.build());
            }

            // Stronghold: Ground, Rock, Grass types
            if (id.equals(STRONGHOLD_ID)) {
                LootPool.Builder strongholdPool = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(DANDYCORPItems.CAMERUPTITE))
                        .with(ItemEntry.builder(DANDYCORPItems.GARCHOMPITE))
                        .with(ItemEntry.builder(DANDYCORPItems.SWAMPERTITE))
                        .with(ItemEntry.builder(DANDYCORPItems.AERODACTYLITE))
                        .with(ItemEntry.builder(DANDYCORPItems.AGGRONITE))
                        .with(ItemEntry.builder(DANDYCORPItems.STEELIXITE))
                        .with(ItemEntry.builder(DANDYCORPItems.TYRANITARITE))
                        .with(ItemEntry.builder(DANDYCORPItems.ABOMASITE))
                        .with(ItemEntry.builder(DANDYCORPItems.SCEPTILITE))
                        .with(ItemEntry.builder(DANDYCORPItems.VENUSAURITE))
                        .conditionally(RandomChanceLootCondition.builder(0.2f));
                tableBuilder.pool(strongholdPool.build());
            }

            // Mansion: Psychic, Poison, Electric, Ghost types
            if (id.equals(MANSION_ID)) {
                LootPool.Builder mansionPool = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(DANDYCORPItems.ALAKAZITE))
                        .with(ItemEntry.builder(DANDYCORPItems.GALLADITE))
                        .with(ItemEntry.builder(DANDYCORPItems.GARDEVOIRITE))
                        .with(ItemEntry.builder(DANDYCORPItems.MAWILITE))
                        .with(ItemEntry.builder(DANDYCORPItems.MEDICHAMITE))
                        .with(ItemEntry.builder(DANDYCORPItems.METAGROSSITE))
                        .with(ItemEntry.builder(DANDYCORPItems.MEWTWONITE_X))
                        .with(ItemEntry.builder(DANDYCORPItems.MEWTWONITE_Y))
                        .with(ItemEntry.builder(DANDYCORPItems.LATIASITE))
                        .with(ItemEntry.builder(DANDYCORPItems.LATIOSITE))
                        .with(ItemEntry.builder(DANDYCORPItems.BEEDRILLITE))
                        .with(ItemEntry.builder(DANDYCORPItems.GENGARITE))
                        .with(ItemEntry.builder(DANDYCORPItems.VENUSAURITE))
                        .with(ItemEntry.builder(DANDYCORPItems.AMPHAROSITE))
                        .with(ItemEntry.builder(DANDYCORPItems.MANECTITE))
                        .with(ItemEntry.builder(DANDYCORPItems.BANETTITE))
                        .with(ItemEntry.builder(DANDYCORPItems.SABLENITE))
                        .conditionally(RandomChanceLootCondition.builder(0.9f));
                tableBuilder.pool(mansionPool.build());
            }

        });
    }
}
