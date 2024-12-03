package net.dandycorp.dccobblemon;

import com.simibubi.create.foundation.ponder.PonderRegistrationHelper;
import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import net.dandycorp.dccobblemon.block.BlockPartialModels;
import net.dandycorp.dccobblemon.block.Blocks;
import net.dandycorp.dccobblemon.compat.rei.DANDYCORPREIClientPlugin;
import net.dandycorp.dccobblemon.util.GrinderDataCache;
import net.dandycorp.dccobblemon.util.VendorDataCache;
import net.dandycorp.dccobblemon.compat.ponder.GrinderScenes;
import net.dandycorp.dccobblemon.item.Items;
import net.dandycorp.dccobblemon.renderer.BadgeRenderer;
import net.dandycorp.dccobblemon.renderer.ElytraRegister;
import net.dandycorp.dccobblemon.ui.vendor.VendorScreen;
import net.dandycorp.dccobblemon.ui.vendor.VendorScreenHandler;
import net.dandycorp.dccobblemon.util.VendorData;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

import static net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions.MOD_ID;

public class Client implements ClientModInitializer {

    //private static final KeyBinding BEGIN = new KeyBinding("ui.dccobblemon.vendor", GLFW.GLFW_KEY_G, "key.categories.misc");

    static final PonderRegistrationHelper HELPER = new PonderRegistrationHelper(MOD_ID);

    @Override
    public void onInitializeClient() {
        TrinketRendererRegistry.registerRenderer(Items.BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(Items.BUG_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(Items.DANDY_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(Items.DARK_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(Items.DRAGON_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(Items.FIRE_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(Items.FLYING_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(Items.GROUND_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(Items.ICE_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(Items.JEFF_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(Items.LINA_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(Items.NATE_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(Items.POISON_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(Items.PUMPKIN_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(Items.ROCK_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(Items.SHELLY_BADGE, new BadgeRenderer());
        TrinketRendererRegistry.registerRenderer(Items.WATER_BADGE, new BadgeRenderer());
        ElytraRegister.registerCapeRenderer();
        ElytraRegister.registerRenderer();
        BlockPartialModels.register();

        HandledScreens.register(
                DANDYCORPCobblemonAdditions.VENDOR_SCREEN_HANDLER,
                (VendorScreenHandler handler, PlayerInventory inventory, Text title) -> new VendorScreen(handler, inventory, title)
        );

        HELPER.forComponents(Blocks.GRINDER_BLOCK)
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
    }
}
