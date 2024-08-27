package net.dandycorp.dccobblemon;

import net.dandycorp.dccobblemon.event.AttackEntityHandler;
import net.dandycorp.dccobblemon.item.Items;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DANDYCORPCobblemonAdditions implements ModInitializer {
	public static final String MOD_ID = "dccobblemon";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("\n\n   ███    ███\n" +
				"   ███    ███\n" +
				"   ███    ███\n" +
				"   ███    ███\n" +
				"        \nDANDYCORP Initialized");

		Items.registerAllItems();

		AttackEntityCallback.EVENT.register(new AttackEntityHandler());

	}
}