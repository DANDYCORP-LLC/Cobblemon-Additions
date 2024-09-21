package net.dandycorp.dccobblemon;

import net.dandycorp.dccobblemon.block.Blocks;
import net.dandycorp.dccobblemon.block.custom.VendorBlockEntity;
import net.dandycorp.dccobblemon.event.AttackEntityHandler;
import net.dandycorp.dccobblemon.event.BreakBlockHandler;
import net.dandycorp.dccobblemon.item.Items;
import net.dandycorp.dccobblemon.item.custom.badges.DragonBadgeItem;
import net.dandycorp.dccobblemon.util.VendorDataLoader;
import net.dandycorp.dccobblemon.ui.vendor.VendorScreenHandler;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static net.dandycorp.dccobblemon.block.Blocks.VENDOR_BLOCK;

public class DANDYCORPCobblemonAdditions implements ModInitializer {


	public static final String MOD_ID = "dccobblemon";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Path CONFIG_DIR = FabricLoader.getInstance().getConfigDir().resolve("dccobblemon");


	public static final Identifier GREED = Identifier.of(MOD_ID, "greed");
	public static SoundEvent GREED_EVENT = SoundEvent.of(GREED);

	public static final Identifier JUDGEMENT = Identifier.of(MOD_ID, "judgement");
	public static SoundEvent JUDGEMENT_EVENT = SoundEvent.of(JUDGEMENT);

	public static final Identifier COMPLIMENT = Identifier.of(MOD_ID, "compliment");
	public static SoundEvent COMPLIMENT_EVENT = SoundEvent.of(COMPLIMENT);

	public static final Identifier VENDOR_OPEN = Identifier.of(MOD_ID, "vendor_open");
	public static SoundEvent VENDOR_OPEN_EVENT = SoundEvent.of(VENDOR_OPEN);

	public static final Identifier VENDOR_CLICK = Identifier.of(MOD_ID, "vendor_click");
	public static SoundEvent VENDOR_CLICK_EVENT = SoundEvent.of(VENDOR_CLICK);

	public static final Identifier VENDOR_BUY = Identifier.of(MOD_ID, "vendor_buy");
	public static SoundEvent VENDOR_BUY_EVENT = SoundEvent.of(VENDOR_BUY);

	public static final Identifier VENDOR_POOR = Identifier.of(MOD_ID, "vendor_poor");
	public static SoundEvent VENDOR_POOR_EVENT = SoundEvent.of(VENDOR_POOR);

	public static final ScreenHandlerType<VendorScreenHandler> VENDOR_SCREEN_HANDLER =
			new ScreenHandlerType<>(VendorScreenHandler::new, FeatureFlags.VANILLA_FEATURES);

	public static final BlockEntityType<VendorBlockEntity> VENDOR_BLOCK_ENTITY;

	static {
		VENDOR_BLOCK_ENTITY = Registry.register(
				Registries.BLOCK_ENTITY_TYPE,
				new Identifier(MOD_ID, "vendor_block_entity"),
				BlockEntityType.Builder.create(VendorBlockEntity::new, VENDOR_BLOCK).build(null)
		);
	}

	// Register the Screen Handler Type with Fabric's Registry
	public static void registerScreenHandlerTypes() {
		Registry.register(Registries.SCREEN_HANDLER, new Identifier(MOD_ID, "vendor"), VENDOR_SCREEN_HANDLER);
	}

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("\n\n   ███    ███\n" +
				"   ███    ███\n" +
				"   ███    ███\n" +
				"   ███    ███\n" +
				"        \nDANDYCORP Initializing...");

		if(!Files.exists(CONFIG_DIR)) {
			try {
				Files.createDirectory(CONFIG_DIR);
				LOGGER.info("config directory DNE, created.");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		VendorDataLoader.loadVendorData();

		Registry.register(Registries.SOUND_EVENT, GREED, GREED_EVENT);
		Registry.register(Registries.SOUND_EVENT, JUDGEMENT, JUDGEMENT_EVENT);
		Registry.register(Registries.SOUND_EVENT, COMPLIMENT, COMPLIMENT_EVENT);
		Registry.register(Registries.SOUND_EVENT, VENDOR_OPEN, VENDOR_OPEN_EVENT);
		Registry.register(Registries.SOUND_EVENT, VENDOR_CLICK, VENDOR_CLICK_EVENT);
		Registry.register(Registries.SOUND_EVENT, VENDOR_BUY, VENDOR_BUY_EVENT);
		Registry.register(Registries.SOUND_EVENT, VENDOR_POOR, VENDOR_POOR_EVENT);

		Items.registerAllItems();
		Blocks.registerAllBlocks();
		registerScreenHandlerTypes();




		AttackEntityCallback.EVENT.register(new AttackEntityHandler());
		PlayerBlockBreakEvents.BEFORE.register(new BreakBlockHandler());



		DragonBadgeItem.registerFlight();

		LOGGER.info("DANDYCORP initialized!");

	}
}