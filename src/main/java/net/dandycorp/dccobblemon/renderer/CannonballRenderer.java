package net.dandycorp.dccobblemon.renderer;

import net.dandycorp.dccobblemon.entities.CannonballEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class CannonballRenderer extends FlyingItemEntityRenderer<CannonballEntity> {
    private final ItemRenderer itemRenderer;
    private final float scale;

    public CannonballRenderer(EntityRendererFactory.Context context, float f, boolean bl) {
        super(context, f, bl);
        this.scale = f;
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(CannonballEntity entity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        float yaw   = MathHelper.lerp(g, entity.prevYaw,   entity.getYaw());

        if (entity.age < 2
                && this.dispatcher.camera.getFocusedEntity().squaredDistanceTo(entity) < 12.25)
            return;
        matrixStack.push();

        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - yaw));
        float spinDegPerTick = 30f;
        float roll = (entity.age + g) * spinDegPerTick;
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(roll));
        matrixStack.scale(scale, scale, scale);

        itemRenderer.renderItem(
                entity.getStack(),
                ModelTransformationMode.NONE,
                i,
                OverlayTexture.DEFAULT_UV,
                matrixStack, vertexConsumerProvider,
                entity.getWorld(),
                entity.getId());

        matrixStack.pop();

        if (this.hasLabel(entity)) {
            this.renderLabelIfPresent(entity, entity.getDisplayName(), matrixStack, vertexConsumerProvider, i);
        }
    }
}
