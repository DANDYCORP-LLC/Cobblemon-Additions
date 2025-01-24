package net.dandycorp.dccobblemon.renderer;

import net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions;
import net.dandycorp.dccobblemon.attribute.InfinityGuardComponent;
import net.dandycorp.dccobblemon.util.ColorUtils;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class InfinityGuardRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {


    public static final Identifier SWIRL_TEXTURE =
            new Identifier("dccobblemon", "textures/models/infinity_guard.png");

    private final PlayerEntityModel<AbstractClientPlayerEntity> swirlModel;

    public InfinityGuardRenderer(
            FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context,
            EntityModelLoader loader
    ) {
        super(context);

        this.swirlModel = new PlayerEntityModel<>(
                loader.getModelPart(InfinityGuardModelHandler.INFINITY_GUARD_SWIRL),
                false
        );
    }

    @Override
    public void render(
            MatrixStack matrices, VertexConsumerProvider vertexConsumers,
            int light, AbstractClientPlayerEntity entity,
            float limbAngle, float limbDistance,
            float tickDelta, float animationProgress,
            float headYaw, float headPitch
    ) {
        InfinityGuardComponent shield = entity.getComponent(DANDYCORPCobblemonAdditions.INFINITY_GUARD);
        if (!shield.shouldRenderShield()) {
            return;
        }
        float swirlTime = entity.age + tickDelta;
        VertexConsumer swirlConsumer = vertexConsumers.getBuffer(
                RenderLayer.getEnergySwirl(SWIRL_TEXTURE, (swirlTime * 0.005f) % 1.0f, (swirlTime * 0.005f) % 1.0f)
        );

        this.swirlModel.animateModel(entity, limbAngle, limbDistance, tickDelta);
        this.getContextModel().copyStateTo(this.swirlModel);
        this.swirlModel.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);

        matrices.push();
        matrices.scale(1.05f, 1.05f, 1.05f);

        // Render swirl

        float maxHealth = shield.getMaxHealth();
        float fraction = maxHealth > 0 ? (shield.shieldHealth / maxHealth) : 0.0f;
        float[] swirlColor = colorFromShield(fraction);
        this.swirlModel.render(matrices, swirlConsumer, light, OverlayTexture.DEFAULT_UV,
                swirlColor[0], swirlColor[1], swirlColor[2], swirlColor[3]);

        matrices.pop();
    }

    private float[] colorFromShield(float fraction) {
        int startColor = 0x6900b2;
        int endColor = 0x020069;

        int interpolated = ColorUtils.interpolateColor(endColor, startColor, fraction);
        return ColorUtils.colorToFloats(interpolated);
    }
}
