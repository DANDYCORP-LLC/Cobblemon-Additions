package net.dandycorp.dccobblemon;

import com.simibubi.create.foundation.data.CreateRegistrate;
import net.dandycorp.dccobblemon.block.BlockEntities;
import net.dandycorp.dccobblemon.block.Blocks;
import net.dandycorp.dccobblemon.event.AttackEntityHandler;
import net.dandycorp.dccobblemon.event.BreakBlockHandler;
import net.dandycorp.dccobblemon.item.Items;
import net.dandycorp.dccobblemon.item.custom.badges.DragonBadgeItem;
import net.dandycorp.dccobblemon.ui.vendor.VendorScreenHandler;
import net.dandycorp.dccobblemon.util.GrinderPointGenerator;
import net.dandycorp.dccobblemon.util.HeadHelper;
import net.dandycorp.dccobblemon.util.VendorData;
import net.dandycorp.dccobblemon.util.VendorDataLoader;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class DANDYCORPCobblemonAdditions implements ModInitializer {


	public static final String MOD_ID = "dccobblemon";

	public static final Identifier VENDOR_DATA_SYNC = new Identifier(MOD_ID, "vendor_data_sync");
	public static final Identifier GRINDER_DATA_SYNC = new Identifier(MOD_ID, "grinder_data_sync");

	public static final Random RANDOM = new Random();

	public static final Logger LOGGER = LoggerFactory.getLogger("DANDYCORP Cobblemon Additions");
	public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);

	public static Path CONFIG_DIR = FabricLoader.getInstance().getConfigDir().resolve("dccobblemon");


	public static final ScreenHandlerType<VendorScreenHandler> VENDOR_SCREEN_HANDLER =
			Registry.register(Registries.SCREEN_HANDLER, new Identifier(MOD_ID, "vendor"),
					new ExtendedScreenHandlerType<>(VendorScreenHandler::new));

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
		Blocks.registerAllBlocks();
		BlockEntities.registerAllBlockEntities();
		HeadHelper.initializeCache();
		DANDYCORPSounds.registerSounds();
		REGISTRATE.register();



		AttackEntityCallback.EVENT.register(new AttackEntityHandler());
		PlayerBlockBreakEvents.BEFORE.register(new BreakBlockHandler());
		ServerLifecycleEvents.SERVER_STARTED.register(GrinderPointGenerator::initializePointValues);

		DragonBadgeItem.registerFlight();

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayerEntity player = handler.player;
			VendorData data = VendorDataLoader.loadVendorData();
			PacketByteBuf buf = PacketByteBufs.create();
			data.toPacket(buf);
			ServerPlayNetworking.send(player, VENDOR_DATA_SYNC, buf);
		});

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayerEntity player = handler.player;
			if (!GrinderPointGenerator.isInitialized()) {
				GrinderPointGenerator.initializePointValues(server);
			}
			Map<Item, Float> pointMap = GrinderPointGenerator.getCalculatedValues();
			List<Map.Entry<Item, Float>> nonZeroEntries = pointMap.entrySet().stream()
					.filter(entry -> entry.getValue() > 0f)
					.toList();
			PacketByteBuf buf = PacketByteBufs.create();
			buf.writeVarInt(nonZeroEntries.size());
			for (Map.Entry<Item, Float> entry : nonZeroEntries) {
				Identifier itemId = Registries.ITEM.getId(entry.getKey());
				buf.writeIdentifier(itemId);
				buf.writeFloat(entry.getValue());
			}
			ServerPlayNetworking.send(player, GRINDER_DATA_SYNC, buf);
		});

		LOGGER.info("DANDYCORP initialized!");

	}
}