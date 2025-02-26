package net.dandycorp.dccobblemon;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.battles.BattleStartedPreEvent;
import com.cobblemon.mod.common.api.events.entity.SpawnEvent;
import com.cobblemon.mod.common.api.events.pokeball.PokemonCatchRateEvent;
import com.cobblemon.mod.common.api.events.pokemon.*;
import com.cobblemon.mod.common.api.pokemon.experience.SidemodExperienceSource;
import com.cobblemon.mod.common.api.spawning.context.SpawningContext;
import com.cobblemon.mod.common.api.types.ElementalType;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.helditem.CobblemonHeldItemManager;
import com.simibubi.create.foundation.data.CreateRegistrate;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import kotlin.Unit;
import net.dandycorp.dccobblemon.attribute.DANDYCORPAttributes;
import net.dandycorp.dccobblemon.block.DANDYCORPBlockEntities;
import net.dandycorp.dccobblemon.block.DANDYCORPBlocks;
import net.dandycorp.dccobblemon.attribute.InfinityGuardComponent;
import net.dandycorp.dccobblemon.command.DANDYCORPCommands;
import net.dandycorp.dccobblemon.effect.SparklingPowerEffect;
import net.dandycorp.dccobblemon.event.AttackEntityHandler;
import net.dandycorp.dccobblemon.event.BreakBlockHandler;
import net.dandycorp.dccobblemon.item.DANDYCORPItems;
import net.dandycorp.dccobblemon.item.custom.BadgeItem;
import net.dandycorp.dccobblemon.item.custom.badges.DragonBadgeItem;
import net.dandycorp.dccobblemon.ui.vendor.VendorScreenHandler;
import net.dandycorp.dccobblemon.util.*;
import net.dandycorp.dccobblemon.util.grinder.GrinderPointGenerator;
import net.dandycorp.dccobblemon.util.HeadHelper;
import net.dandycorp.dccobblemon.util.vendor.VendorData;
import net.dandycorp.dccobblemon.util.vendor.VendorDataLoader;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class DANDYCORPCobblemonAdditions implements ModInitializer, EntityComponentInitializer {


	public static final String MOD_ID = "dccobblemon";

	public static final Identifier VENDOR_DATA_SYNC = new Identifier(MOD_ID, "vendor_data_sync");
	public static final Identifier GRINDER_DATA_SYNC = new Identifier(MOD_ID, "grinder_data_sync");
	public static final Identifier SHAKE_PACKET_ID = new Identifier(MOD_ID, "camera_shake");

	public static final Random RANDOM = new Random();

	public static final Logger LOGGER = LoggerFactory.getLogger("DANDYCORP Cobblemon Additions");
	public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);

	public static Path CONFIG_DIR = FabricLoader.getInstance().getConfigDir().resolve("dccobblemon");

	public static final ScreenHandlerType<VendorScreenHandler> VENDOR_SCREEN_HANDLER =
			Registry.register(Registries.SCREEN_HANDLER, new Identifier(MOD_ID, "vendor"),
					new ExtendedScreenHandlerType<>(VendorScreenHandler::new));

	public static final StatusEffect SPARKLING_POWER = new SparklingPowerEffect();

	public static final ComponentKey<InfinityGuardComponent> INFINITY_GUARD =
			ComponentRegistry.getOrCreate(new Identifier(MOD_ID, "infinity_guard"), InfinityGuardComponent.class);

	@Override
	public void onInitialize() {

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


		DANDYCORPItems.registerAllItems();
		DANDYCORPBlocks.registerAllBlocks();
		DANDYCORPTags.initialize();
		DANDYCORPBlockEntities.registerAllBlockEntities();
		LootTableModifier.modifyLootTables();
		DANDYCORPAttributes.initialize();
		HeadHelper.initializeCache();
		DANDYCORPSounds.registerSounds();
		REGISTRATE.register();

		Registry.register(Registries.STATUS_EFFECT, new Identifier(MOD_ID, "sparkling_power"), SPARKLING_POWER);

		AttackEntityCallback.EVENT.register(new AttackEntityHandler());
		PlayerBlockBreakEvents.BEFORE.register(new BreakBlockHandler());
		ServerLifecycleEvents.SERVER_STARTED.register(GrinderPointGenerator::initializePointValues);
		CobblemonEvents.BATTLE_STARTED_PRE.subscribe(Priority.NORMAL,this::onBattleStart);
		CobblemonEvents.HELD_ITEM_POST.subscribe(Priority.LOW,this::onHeldItemChanged);
		CobblemonEvents.POKEMON_ENTITY_SPAWN.subscribe(Priority.HIGHEST,this::onPokemonEntitySpawn);
		CobblemonEvents.FRIENDSHIP_UPDATED.subscribe(Priority.HIGH,this::onFriendshipUpdated);
		CobblemonEvents.EXPERIENCE_GAINED_EVENT_POST.subscribe(Priority.NORMAL,this::onExperienceGained);
		CobblemonEvents.POKEMON_CATCH_RATE.subscribe(Priority.NORMAL,this::badgeCatchRate);
		CobblemonEvents.POKEMON_SENT_PRE.subscribe(Priority.HIGHEST, this::onSentPre);

		DragonBadgeItem.registerFlight();

		DANDYCORPCommands.registerCommands();

		ServerTickEvents.END_SERVER_TICK.register(server -> {
			ScreenShakeController.tickDelayedShakes();
		});

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

		CobblemonHeldItemManager.INSTANCE.registerRemap(DANDYCORPItems.BOOSTER_ENERGY,"boosterenergy");

//		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
//			FoodTracker.initialize();
//		});

		LOGGER.info("DANDYCORP initialized!");
	}

	private Unit onSentPre(PokemonSentPreEvent pre) {
		Pokemon pokemon = pre.getPokemon();
		System.out.println(pokemon.getAspects());
		System.out.println(pokemon.getFeatures());
		if(pre.getPokemon().getAspects().contains("omega") || pre.getPokemon().getAspects().contains("god")) {
			ScreenShake shake = new ScreenShake(0.6f,20,80, ScreenShakeController.FadeType.REVERSE_EXPONENTIAL);
			ScreenShakeController.causeTremorWithDelay(
					pre.getLevel(),
					pre.getPosition().getX(),
					pre.getPosition().getY(),
					pre.getPosition().getZ(),
					20,
					shake,
					ScreenShakeController.DistanceFalloff.LINEAR,
					15);
		}
		return Unit.INSTANCE;
	}

	private Unit onExperienceGained(ExperienceGainedPostEvent xpEvent) {
		if (BadgeItem.isEquipped(xpEvent.getPokemon().getOwnerPlayer(),DANDYCORPItems.PSYCHIC_BADGE)){
			if(xpEvent.getSource().isBattle()) {
				int xp = xpEvent.getExperience();
				xpEvent.getPokemon().addExperience(new SidemodExperienceSource(MOD_ID), (int) (xp * 0.5));
			}
		}
		return Unit.INSTANCE;
	}


	private Unit onBattleStart(BattleStartedPreEvent event){
		event.getBattle().getPlayers().forEach(player -> {
			Set<Identifier> keyItems = Cobblemon.playerData.get(player).getKeyItems();
			if (MegaUtils.hasKeystone(player)) {
				keyItems.add(new Identifier("cobblemon", "key_stone"));
			} else {
				keyItems.remove(new Identifier("cobblemon", "key_stone"));
			}
		});
		return Unit.INSTANCE;
	}

	private Unit onHeldItemChanged(HeldItemEvent.Post post){
		if(MegaUtils.hasKeystone(post.getPokemon().getOwnerPlayer())){
			MegaUtils.evolveOrDevolve(post.getPokemon().getOwnerPlayer());
		}
		return Unit.INSTANCE;
	}

	private Unit onPokemonEntitySpawn(SpawnEvent<PokemonEntity> event) {
		PokemonEntity pokemonEntity = event.getEntity();
		SpawningContext ctx = event.getCtx();
		if (pokemonEntity != null && ctx.getCause().getEntity() instanceof PlayerEntity player) {
			int rolls = 1;
			float rate = Cobblemon.config.getShinyRate();
			float chance = RANDOM.nextFloat(rate);

			Optional<TrinketComponent> component = TrinketsApi.getTrinketComponent(player);
			if (component.isPresent()) {
				for (Pair<SlotReference, ItemStack> p : component.get().getAllEquipped().stream().toList()) {

					if(p.getRight().isOf(DANDYCORPItems.SHINY_CHARM)){
						rolls += 2;
					}

					if(p.getRight().getItem() instanceof BadgeItem badge && BadgeItem.isChallenge(p.getRight())){
						List<ElementalType> pokemonTypes = (List<ElementalType>) pokemonEntity.getPokemon().getTypes();
						if (pokemonTypes.stream().anyMatch(badge.getTypes()::contains)) {
							rolls += 3;
						}
					}
					chance = (float) Math.floor(chance);
				}
			}
			if (player.hasStatusEffect(SPARKLING_POWER)){
				rolls += Objects.requireNonNull(player.getStatusEffect(SPARKLING_POWER)).getAmplifier() + 1;
			}
			if (chance <= rolls) {
				pokemonEntity.getPokemon().setShiny(true);
			}
		}
		return Unit.INSTANCE;
	}

	private Unit onFriendshipUpdated(FriendshipUpdatedEvent friendshipUpdatedEvent) {
		int newFriendship = friendshipUpdatedEvent.getNewFriendship();
		int difference = newFriendship - friendshipUpdatedEvent.getPokemon().getFriendship() ;

		if (BadgeItem.isEquipped(friendshipUpdatedEvent.getPokemon().getOwnerPlayer(), DANDYCORPItems.FAIRY_BADGE)) {
			friendshipUpdatedEvent.setNewFriendship(newFriendship + (difference * 2));
		}
		return Unit.INSTANCE;
	}

	private Unit badgeCatchRate(PokemonCatchRateEvent event) {
		Optional<TrinketComponent> component = TrinketsApi.getTrinketComponent(event.getThrower());
		if (component.isPresent()) {
			for (Pair<SlotReference, ItemStack> p : component.get().getAllEquipped().stream().toList()) {
				if(p.getRight().getItem() instanceof BadgeItem badge && BadgeItem.isChallenge(p.getRight())){
					List<ElementalType> pokemonTypes = (List<ElementalType>) event.getPokemonEntity().getPokemon().getTypes();
					if (pokemonTypes.stream().anyMatch(badge.getTypes()::contains)) {
						event.setCatchRate(event.getCatchRate() * 2);
					}
				}
			}
		}
		return Unit.INSTANCE;
	}

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerForPlayers(INFINITY_GUARD, InfinityGuardComponent::new, RespawnCopyStrategy.INVENTORY);
	}
}