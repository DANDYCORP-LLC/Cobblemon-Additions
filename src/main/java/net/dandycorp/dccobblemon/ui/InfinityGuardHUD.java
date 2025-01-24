package net.dandycorp.dccobblemon.ui;

import com.mojang.blaze3d.systems.RenderSystem;
import io.wispforest.owo.ui.component.TextureComponent;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.core.*;
import io.wispforest.owo.ui.hud.Hud;
import net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions;
import net.dandycorp.dccobblemon.util.ColorUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import static net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions.MOD_ID;

public class InfinityGuardHUD {

    public static final Identifier INFINITY_GUARD_HUD = new Identifier(MOD_ID, "infinity_guard_hud");
    public static final String INFINITY_GUARD_BACKGROUND_ID = "infinity_guard_background";
    public static final String INFINITY_GUARD_COMPONENT_ID = "infinity_guard_overlay";
    public static final Identifier INFINITY_GUARD_HUD_TEXTURE = new Identifier(MOD_ID, "textures/gui/infinity_guard.png");

    public static final Identifier CARMOT_SHIELD_ROOT_ID = new Identifier("mythicmetals", "shield_overlay");
    public static final String CARMOT_SHIELD_OVERLAY_ID = "shield_overlay";

    public static void init() {
        Hud.add(INFINITY_GUARD_HUD, () ->
                Containers.draggable(Sizing.content(), Sizing.content(),
                                Containers.verticalFlow(Sizing.content(), Sizing.content())
                                        .child(new InfinityGuardHUDComponent(INFINITY_GUARD_HUD_TEXTURE, 0, 16, 64, 16, 64, 32)
                                                .id(INFINITY_GUARD_BACKGROUND_ID))
                                        .child(new InfinityGuardHUDComponent(INFINITY_GUARD_HUD_TEXTURE, 0, 0, 64, 16, 64, 32)
                                                .id(INFINITY_GUARD_COMPONENT_ID)
                                                .positioning(Positioning.absolute(0, 0))
                                        ))
                        .positioning(Positioning.relative(2, 2))
        );
        var player = MinecraftClient.getInstance().player;
        if(player != null) {
            DANDYCORPCobblemonAdditions.INFINITY_GUARD.sync(player);
        }
    }

    public static void tick() {
        if (Hud.hasComponent(INFINITY_GUARD_HUD) && MinecraftClient.getInstance().player != null) {
            var player = MinecraftClient.getInstance().player;
            var shield = player.getComponent(DANDYCORPCobblemonAdditions.INFINITY_GUARD);

            var infinityGuardRoot = Hud.getComponent(INFINITY_GUARD_HUD);

            // mythic metals support ===================================================
            var mmShieldRoot = Hud.getComponent(CARMOT_SHIELD_ROOT_ID);
            boolean carmotShieldVisible = false;
            if (mmShieldRoot != null) {
                var mmShieldOverlay = (TextureComponent)
                        ((ParentComponent) mmShieldRoot).childById(TextureComponent.class, CARMOT_SHIELD_OVERLAY_ID);
                if (mmShieldOverlay != null) {
                    var area = mmShieldOverlay.visibleArea();
                    if (area.get().width() > 0 && area.get().height() > 0) {
                        carmotShieldVisible = true;
                    }
                }
            }

            if (carmotShieldVisible) {
                infinityGuardRoot.positioning(Positioning.relative(2, 8));
            } else {
                infinityGuardRoot.positioning(Positioning.relative(2, 2));
            }
            // ===========================================================================

            var shieldBar = (InfinityGuardHUDComponent) ((ParentComponent) infinityGuardRoot)
                    .childById(TextureComponent.class, INFINITY_GUARD_COMPONENT_ID);
            var background = (InfinityGuardHUDComponent) ((ParentComponent) infinityGuardRoot)
                    .childById(TextureComponent.class, INFINITY_GUARD_BACKGROUND_ID);

            if (shield.getMaxHealth() == 0) {
                shieldBar.visibleArea(PositionedRectangle.of(0, 0, 0, 0));
                background.visibleArea(PositionedRectangle.of(0, 0, 0, 0));
                return;
            }

            boolean isShieldBroken = shield.shieldHealth == 0;
            int shieldX = MathHelper.ceil(16 + 46 * (shield.shieldHealth / shield.getMaxHealth()));
            InfinityGuardHUDComponent.displayDamage = player.hurtTime > 0 || isShieldBroken;

            if (isShieldBroken) {
                shieldBar.visibleArea(PositionedRectangle.of(0, 0, 0, 0));
            } else {
                shieldBar.visibleArea(PositionedRectangle.of(0, 0, Size.of(shieldX, 16)));
            }
            background.visibleArea(PositionedRectangle.of(0, 0, Size.of(64, 16)));
        }
    }



    public static class InfinityGuardHUDComponent extends TextureComponent {

        public static final int COLOR_START = 0x9600ff;
        public static final int COLOR_END   = 0x4200ff;
        public static final int COLOR_BROKEN = 0xE0343A;
        public static boolean displayDamage = false;

        protected InfinityGuardHUDComponent(
                Identifier texture, int u, int v,
                int regionWidth, int regionHeight,
                int textureWidth, int textureHeight
        ) {
            super(texture, u, v, regionWidth, regionHeight, textureWidth, textureHeight);
        }

        @Override
        public void draw(OwoUIDrawContext context, int mouseX, int mouseY, float partialTicks, float delta) {
            var mc = MinecraftClient.getInstance();
            if (mc.world == null) {
                setShaderColorInt(COLOR_START);
                super.draw(context, mouseX, mouseY, partialTicks, delta);
                RenderSystem.setShaderColor(1, 1, 1, 1);
                return;
            }

            var player = mc.player;
            var shieldComp = player.getComponent(DANDYCORPCobblemonAdditions.INFINITY_GUARD);
            float shieldHP = shieldComp.shieldHealth;
            if (shieldHP <= 0) {
                setShaderColorInt(COLOR_BROKEN);
                super.draw(context, mouseX, mouseY, partialTicks, delta);
                RenderSystem.setShaderColor(1, 1, 1, 1);
                return;
            }
            int color = getColor(partialTicks, mc);
            setShaderColorInt(color);
            super.draw(context, mouseX, mouseY, partialTicks, delta);
            RenderSystem.setShaderColor(1, 1, 1, 1);
        }

        private int getColor(float partialTicks, MinecraftClient mc) {
            long worldTime = mc.world.getTime();
            long cycleLength = 200;
            float wave = ((worldTime % cycleLength) + partialTicks) / (float) cycleLength;
            wave *= 2.0f;
            if (wave > 1.0f) {
                wave = 2.0f - wave;
            }
            float t = wave;
            int color = ColorUtils.interpolateColor(COLOR_START, COLOR_END, t);

            if (displayDamage) {
                color = 0xFFFFFF;
            }
            return color;
        }

        private static void setShaderColorInt(int color) {
            float r = ((color >> 16) & 0xFF) / 255f;
            float g = ((color >> 8)  & 0xFF) / 255f;
            float b =  ( color       & 0xFF) / 255f;
            RenderSystem.setShaderColor(r, g, b, 1.0f);
        }
    }
}
