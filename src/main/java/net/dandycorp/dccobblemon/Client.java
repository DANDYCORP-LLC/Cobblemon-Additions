package net.dandycorp.dccobblemon;

import com.simibubi.create.foundation.ponder.PonderRegistrationHelper;
import com.simibubi.create.infrastructure.ponder.AllPonderTags;
import com.simibubi.create.infrastructure.ponder.scenes.KineticsScenes;
import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import net.dandycorp.dccobblemon.block.BlockPartialModels;
import net.dandycorp.dccobblemon.block.Blocks;
import net.dandycorp.dccobblemon.compat.ponder.GrinderScenes;
import net.dandycorp.dccobblemon.item.Items;
import net.dandycorp.dccobblemon.renderer.BadgeRenderer;
import net.dandycorp.dccobblemon.renderer.ElytraRegister;
import net.dandycorp.dccobblemon.ui.vendor.VendorScreen;
import net.dandycorp.dccobblemon.ui.vendor.VendorScreenHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions.MOD_ID;

public class Client implements ClientModInitializer {

    //private static final KeyBinding BEGIN = new KeyBinding("ui.dccobblemon.vendor", GLFW.GLFW_KEY_G, "key.categories.misc");
    private static final Identifier FULL_GRINDER_MODEL_ID = new ModelIdentifier(MOD_ID,"full_grinder","#inventory");

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

        ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, out) -> {
            out.accept(FULL_GRINDER_MODEL_ID);
        });

        HELPER.forComponents(Blocks.GRINDER_BLOCK)
                .addStoryBoard("grinder_scene", GrinderScenes::grind);

    }
}
