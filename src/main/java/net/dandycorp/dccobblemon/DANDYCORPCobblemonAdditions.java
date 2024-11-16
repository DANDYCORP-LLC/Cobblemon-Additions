package net.dandycorp.dccobblemon;

import com.simibubi.create.foundation.data.CreateRegistrate;
import net.dandycorp.dccobblemon.block.BlockEntities;
import net.dandycorp.dccobblemon.block.BlockPartialModels;
import net.dandycorp.dccobblemon.block.Blocks;
import net.dandycorp.dccobblemon.event.AttackEntityHandler;
import net.dandycorp.dccobblemon.event.BreakBlockHandler;
import net.dandycorp.dccobblemon.item.Items;
import net.dandycorp.dccobblemon.item.custom.badges.DragonBadgeItem;
import net.dandycorp.dccobblemon.ui.vendor.VendorScreenHandler;
import net.dandycorp.dccobblemon.util.GrinderPointGenerator;
import net.dandycorp.dccobblemon.util.HeadHelper;
import net.dandycorp.dccobblemon.util.VendorDataLoader;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

public class DANDYCORPCobblemonAdditions implements ModInitializer {


	public static final String MOD_ID = "dccobblemon";

	public static final Random RANDOM = new Random();

	public static final Logger LOGGER = LoggerFactory.getLogger("DANDYCORP Cobblemon Additions");
	public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);

	public static Path CONFIG_DIR = FabricLoader.getInstance().getConfigDir().resolve("dccobblemon");


	public static final ScreenHandlerType<VendorScreenHandler> VENDOR_SCREEN_HANDLER =
			new ScreenHandlerType<>(VendorScreenHandler::new, FeatureFlags.VANILLA_FEATURES);



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


		Items.registerAllItems();
		BlockPartialModels.register();
		Blocks.registerAllBlocks();
		BlockEntities.registerAllBlockEntities();
		registerScreenHandlerTypes();
		HeadHelper.initializeCache();
		DANDYCORPSounds.registerSounds();
		REGISTRATE.register();



		AttackEntityCallback.EVENT.register(new AttackEntityHandler());
		PlayerBlockBreakEvents.BEFORE.register(new BreakBlockHandler());
		ServerLifecycleEvents.SERVER_STARTED.register(GrinderPointGenerator::initializePointValues);

		DragonBadgeItem.registerFlight();

		LOGGER.info("DANDYCORP initialized!");

	}
}