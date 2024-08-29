package net.dandycorp.dccobblemon;

import net.dandycorp.dccobblemon.event.AttackEntityHandler;
import net.dandycorp.dccobblemon.event.BreakBlockHandler;
import net.dandycorp.dccobblemon.item.Items;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DANDYCORPCobblemonAdditions implements ModInitializer {


	public static final String MOD_ID = "dccobblemon";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final Identifier GREED = Identifier.of(MOD_ID, "greed");
	public static SoundEvent GREED_EVENT = SoundEvent.of(GREED);


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

		Registry.register(Registries.SOUND_EVENT, GREED, GREED_EVENT);

		Items.registerAllItems();

		AttackEntityCallback.EVENT.register(new AttackEntityHandler());
		PlayerBlockBreakEvents.BEFORE.register(new BreakBlockHandler());

	}
}