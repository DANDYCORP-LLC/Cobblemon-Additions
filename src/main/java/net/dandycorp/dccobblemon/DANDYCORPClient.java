package net.dandycorp.dccobblemon;

import com.simibubi.create.foundation.ponder.PonderRegistrationHelper;
import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import net.dandycorp.dccobblemon.block.BlockPartialModels;
import net.dandycorp.dccobblemon.block.DANDYCORPBlocks;
import net.dandycorp.dccobblemon.compat.rei.DANDYCORPREIClientPlugin;
import net.dandycorp.dccobblemon.compat.ponder.GrinderScenes;
import net.dandycorp.dccobblemon.item.DANDYCORPItems;
import net.dandycorp.dccobblemon.renderer.BadgeRenderer;
import net.dandycorp.dccobblemon.renderer.ElytraRegister;
import net.dandycorp.dccobblemon.renderer.InfinityGuardModelHandler;
import net.dandycorp.dccobblemon.renderer.InfinityGuardRenderer;
import net.dandycorp.dccobblemon.ui.InfinityGuardHUD;
import net.dandycorp.dccobblemon.ui.vendor.VendorScreen;
import net.dandycorp.dccobblemon.ui.vendor.VendorScreenHandler;
import net.dandycorp.dccobblemon.util.grinder.GrinderDataCache;
import net.dandycorp.dccobblemon.util.vendor.VendorData;
import net.dandycorp.dccobblemon.util.vendor.VendorDataCache;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

import static net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions.MOD_ID;

public class DANDYCORPClient implements ClientModInitializer {

    //private static final KeyBinding BEGIN = new KeyBinding("ui.dccobblemon.vendor", GLFW.GLFW_KEY_G, "key.categories.misc");

    static final PonderRegistrationHelper HELPER = new PonderRegistrationHelper(MOD_ID);

    @Override
    public void onInitializeClient() {

        TrinketRendererRegistry.registerRenderer(DANDYCORPItems.BADGE, new BadgeRenderer());

        TrinketRendererRegistry.registerRenderer(DANDYCORPItems.BUG_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(DANDYCORPItems.DARK_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(DANDYCORPItems.DRAGON_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(DANDYCORPItems.ELECTRIC_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(DANDYCORPItems.FAIRY_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(DANDYCORPItems.FIGHTING_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(DANDYCORPItems.FIRE_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(DANDYCORPItems.FLYING_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(DANDYCORPItems.GHOST_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(DANDYCORPItems.GRASS_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(DANDYCORPItems.GROUND_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(DANDYCORPItems.ICE_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(DANDYCORPItems.NORMAL_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(DANDYCORPItems.POISON_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(DANDYCORPItems.PSYCHIC_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(DANDYCORPItems.ROCK_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(DANDYCORPItems.STEEL_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(DANDYCORPItems.WATER_BADGE, new BadgeRenderer());

        TrinketRendererRegistry.registerRenderer(DANDYCORPItems.DANDY_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(DANDYCORPItems.DAVID_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(DANDYCORPItems.JEFF_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(DANDYCORPItems.LINA_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(DANDYCORPItems.NATE_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(DANDYCORPItems.PUMPKIN_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(DANDYCORPItems.SHELLY_BADGE, new BadgeRenderer());

        ElytraRegister.registerCapeRenderer();
        ElytraRegister.registerRenderer();
        BlockPartialModels.register();

        HandledScreens.register(
                DANDYCORPCobblemonAdditions.VENDOR_SCREEN_HANDLER,
                (VendorScreenHandler handler, PlayerInventory inventory, Text title) -> new VendorScreen(handler, inventory, title)
        );

        HELPER.forComponents(DANDYCORPBlocks.GRINDER_BLOCK)
                .addStoryBoard("grinder_scene", GrinderScenes::grind)
                .addStoryBoard("grinder_scene_2", GrinderScenes::points);

        ClientPlayNetworking.registerGlobalReceiver(DANDYCORPCobblemonAdditions.VENDOR_DATA_SYNC, (client, handler, buf, responseSender) -> {
            VendorData data = VendorData.fromPacket(buf);
            client.execute(() -> {
                VendorDataCache.setVendorData(data);
                DANDYCORPREIClientPlugin.registerVendorDisplays();
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(DANDYCORPCobblemonAdditions.GRINDER_DATA_SYNC, (client, handler, buf, responseSender) -> {
            Map<Item, Float> pointMap = new HashMap<>();
            int size = buf.readVarInt();
            for (int i = 0; i < size; i++) {
                Identifier itemId = buf.readIdentifier();
                float value = buf.readFloat();
                Item item = Registries.ITEM.get(itemId);
                if (item != net.minecraft.item.Items.AIR) {
                    pointMap.put(item, value);
                }
            }
            client.execute(() -> {
                GrinderDataCache.setCalculatedPointValues(pointMap);
                DANDYCORPREIClientPlugin.registerGrinderDisplays();
            });
        });

        ModelPredicateProviderRegistry.register(
                DANDYCORPItems.PARAGONIUM_BOW,
                new Identifier("pull"),
                (stack, world, entity, seed) -> {
                    if (entity == null) {
                        return 0.0F;
                    }
                    if (entity.getActiveItem() != stack) {
                        return 0.0F;
                    }
                    // same logic that BowItem uses
                    int useTicks = stack.getMaxUseTime() - entity.getItemUseTimeLeft();
                    return BowItem.getPullProgress(useTicks);
                }
        );
        ModelPredicateProviderRegistry.register(
                DANDYCORPItems.PARAGONIUM_BOW,
                new Identifier("pulling"),
                (stack, world, entity, seed) -> {
                    if (entity != null && entity.isUsingItem() && entity.getActiveItem() == stack) {
                        return 1.0F;
                    }
                    return 0.0F;
                }
        );
        ModelPredicateProviderRegistry.register(
                DANDYCORPItems.PARAGONIUM_SHIELD,
                new Identifier("blocking"),
                (stack, world, entity, seed) -> {
                    if (entity != null && entity.isUsingItem() && entity.getActiveItem() == stack) {
                        return 1.0F;
                    }
                    return 0.0F;
                }
        );

        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {
            if (entityType == EntityType.PLAYER) {
                registrationHelper.register(
                        new InfinityGuardRenderer(
                                (FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>>) entityRenderer,
                                context.getModelLoader()
                        )
                );
            }
        });

        InfinityGuardHUD.init();

        EntityModelLayerRegistry.registerModelLayer(
                InfinityGuardModelHandler.INFINITY_GUARD_SWIRL,
                () -> TexturedModelData.of(
                        PlayerEntityModel.getTexturedModelData(new Dilation(1.15f), false),
                        64,
                        64
                )
        );

        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {
            if(entityType == EntityType.PLAYER) {
                registrationHelper.register(
                        new InfinityGuardRenderer(
                                (FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>>) entityRenderer,
                                context.getModelLoader()
                        )
                );
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> InfinityGuardHUD.tick());
    }
}
