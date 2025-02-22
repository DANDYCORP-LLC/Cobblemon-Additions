package net.dandycorp.dccobblemon.renderer;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.client.TrinketRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RotationAxis;


public class BadgeRenderer implements TrinketRenderer {
    @Override
    public void render(ItemStack stack, SlotReference slotReference,
                       net.minecraft.client.render.entity.model.EntityModel<? extends LivingEntity> contextModel,
                       MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
                       LivingEntity entity, float limbAngle, float limbDistance, float tickDelta,
                       float animationProgress, float headYaw, float headPitch) {

        if (contextModel instanceof BipedEntityModel<?> bipedModel) {
            //boolean hasChestplate = !entity.getEquippedStack(EquipmentSlot.CHEST).isEmpty();
            //if (hasChestplate) {
                //translateToChestWithArmor(matrices, bipedModel, entity);
            //} else {
                translateToChestDefault(matrices, bipedModel, entity);
            //}
            matrices.translate(-0.15F, -0.2F, 0.01F);
            matrices.scale(0.2F, 0.2F, 0.2F);
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180.0F));

            ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
            BakedModel model = itemRenderer.getModels().getModel(stack);
            itemRenderer.renderItem(stack, ModelTransformationMode.FIXED, false,
                    matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV, model);
        }
    }

    private void translateToChestDefault(MatrixStack matrices, BipedEntityModel<?> model, LivingEntity entity) {
        if (entity.isSneaking() && !model.riding && !entity.isSwimming()) {
            matrices.translate(0.0F, 0.2F, 0.0F);
            matrices.multiply(RotationAxis.POSITIVE_X.rotation(model.body.pitch));
        }
        matrices.multiply(RotationAxis.POSITIVE_Y.rotation(model.body.yaw));
        matrices.translate(0.0F, 0.4F, -0.16F);
    }

    private void translateToChestWithArmor(MatrixStack matrices, BipedEntityModel<?> model, LivingEntity entity) {
        if (entity.isSneaking() && !model.riding && !entity.isSwimming()) {
            matrices.translate(0.0F, 0.2F, 0.0F);
            matrices.multiply(RotationAxis.POSITIVE_X.rotation(model.body.pitch));
        }
        matrices.multiply(RotationAxis.POSITIVE_Y.rotation(model.body.yaw));
        matrices.translate(0.0F, 0.4F, -0.2F);
    }
}
