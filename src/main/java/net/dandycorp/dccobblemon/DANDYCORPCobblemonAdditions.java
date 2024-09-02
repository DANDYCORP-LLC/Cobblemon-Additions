package net.dandycorp.dccobblemon;

import it.crystalnest.soul_fire_d.api.FireManager;
import net.dandycorp.dccobblemon.block.Blocks;
import net.dandycorp.dccobblemon.event.AttackEntityHandler;
import net.dandycorp.dccobblemon.event.BreakBlockHandler;
import net.dandycorp.dccobblemon.item.Items;
import net.dandycorp.dccobblemon.item.custom.badges.DragonBadgeItem;
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

	public static final Identifier JUDGEMENT = Identifier.of(MOD_ID, "judgement");
	public static SoundEvent JUDGEMENT_EVENT = SoundEvent.of(JUDGEMENT);

	public static final Identifier COMPLIMENT = Identifier.of(MOD_ID, "compliment");
	public static SoundEvent COMPLIMENT_EVENT = SoundEvent.of(COMPLIMENT);


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
		Registry.register(Registries.SOUND_EVENT, JUDGEMENT, JUDGEMENT_EVENT);
		Registry.register(Registries.SOUND_EVENT, COMPLIMENT, COMPLIMENT_EVENT);

		Items.registerAllItems();
		Blocks.registerAllBlocks();

		AttackEntityCallback.EVENT.register(new AttackEntityHandler());
		PlayerBlockBreakEvents.BEFORE.register(new BreakBlockHandler());

		DragonBadgeItem.registerFlight();

	}
}