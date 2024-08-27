package net.dandycorp.dccobblemon.renderer;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.client.TrinketRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RotationAxis;


public class BadgeRenderer implements TrinketRenderer {


    @Override
    public void render(ItemStack stack, SlotReference slotReference, EntityModel<? extends LivingEntity> contextModel, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {

        if (contextModel instanceof PlayerEntityModel<?> playerModel) {
            PlayerEntityModel<AbstractClientPlayerEntity> playerEntityModel = (PlayerEntityModel<AbstractClientPlayerEntity>) contextModel;

            // Translate to chest position
            TrinketRenderer.translateToChest(matrices, playerEntityModel, (AbstractClientPlayerEntity) entity);
            matrices.translate(-0.15F, -0.2F, 0.0F);
            matrices.scale(0.2F,0.2F,0.2F);
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180.0F));

            //LOGGER.info(texture.getPath());

            ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
            BakedModel model = itemRenderer.getModels().getModel(stack);

            // Render the badge item using the item renderer
            itemRenderer.renderItem(stack, ModelTransformationMode.FIXED, false, matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV, model);
        }
    }
}
