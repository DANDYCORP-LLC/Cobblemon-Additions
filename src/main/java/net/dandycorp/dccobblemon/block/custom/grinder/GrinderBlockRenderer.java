package net.dandycorp.dccobblemon.block.custom.grinder;

import com.jozufozu.flywheel.backend.Backend;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.AngleHelper;
import net.dandycorp.dccobblemon.block.BlockPartialModels;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;

import static com.simibubi.create.content.kinetics.base.DirectionalKineticBlock.FACING;

public class GrinderBlockRenderer extends KineticBlockEntityRenderer<GrinderBlockEntity> {

    public GrinderBlockRenderer(BlockEntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(GrinderBlockEntity be, float partialTicks, MatrixStack ms,
                              VertexConsumerProvider buffer, int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);

        // If instancing is available, skip traditional rendering
        if (Backend.canUseInstancing(be.getWorld()))
            return;

        renderGrinderWheels(be, ms, buffer, light, overlay);
    }

    protected void renderGrinderWheels(GrinderBlockEntity be, MatrixStack ms, VertexConsumerProvider buffer,
                                       int light, int overlay) {
        BlockState blockState = be.getCachedState();
        Direction direction = blockState.get(FACING);

        // Determine the rotation axis for the grinders based on block facing
        Direction.Axis grinderAxis;
        if (direction == Direction.NORTH || direction == Direction.SOUTH) {
            grinderAxis = Direction.Axis.X;
        } else {
            grinderAxis = Direction.Axis.Z;
        }

        // Get the speed, ensure it's positive, and scale to half
        float speed = Math.abs(be.getSpeed()) / 2f;

        // Determine reverse rotation for front and back grinders
        boolean reverseRotationFront;
        boolean reverseRotationBack;

        if (direction == Direction.EAST || direction == Direction.WEST) {
            reverseRotationFront = false;
            reverseRotationBack = true;
        } else {
            reverseRotationFront = true;
            reverseRotationBack = false;
        }

        // Apply the reverse rotation if needed
        float adjustedSpeedFront = reverseRotationFront ? -speed : speed;
        float adjustedSpeedBack = reverseRotationBack ? -speed : speed;

        // Calculate angles with rotation offsets
        float angleFront = getAngleForBE(be, grinderAxis, adjustedSpeedFront);
        float angleBack = getAngleForBE(be, grinderAxis, adjustedSpeedBack);

        // Get SuperByteBuffer instances with correct facing
        SuperByteBuffer frontGrinder = CachedBufferer.partialFacing(BlockPartialModels.GRINDER_FRONT, blockState, direction);
        SuperByteBuffer backGrinder = CachedBufferer.partialFacing(BlockPartialModels.GRINDER_BACK, blockState, direction);

        // Render the front grinder
        ms.push();
        ms.translate(0.5, 0.5, 0.5); // Center the model
        if (direction == Direction.NORTH || direction == Direction.SOUTH) {
            ms.translate(0, .625f, 0.4375f);
        } else {
            ms.translate(0.4375f, .625f, 0);
        }
        // Apply rotation around the grinder axis
        applyRotation(ms, grinderAxis, angleFront);
        ms.translate(-0.5, -0.5, -0.5); // Translate back to block space

        frontGrinder.light(light)
                .renderInto(ms, buffer.getBuffer(RenderLayer.getCutoutMipped()));
        ms.pop();

        // Render the back grinder
        ms.push();
        ms.translate(0.5, 0.5, 0.5);
        if (direction == Direction.NORTH || direction == Direction.SOUTH) {
            ms.translate(0, .625f, -0.4375f);
        } else {
            ms.translate(-0.4375f, .625f, 0);
        }
        applyRotation(ms, grinderAxis, angleBack);
        ms.translate(-0.5, -0.5, -0.5);

        backGrinder.light(light)
                .renderInto(ms, buffer.getBuffer(RenderLayer.getCutoutMipped()));
        ms.pop();
    }

    private void applyRotation(MatrixStack ms, Direction.Axis axis, float angle) {
        switch (axis) {
            case X:
                ms.multiply(RotationAxis.POSITIVE_X.rotationDegrees(angle));
                break;
            case Y:
                ms.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(angle));
                break;
            case Z:
                ms.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(angle));
                break;
        }
    }

    private float getAngleForBE(GrinderBlockEntity be, Direction.Axis axis, float speed) {
        float time = AnimationTickHolder.getRenderTime(be.getWorld());
        float offset = getRotationOffsetForPosition(be, axis);
        float angle = ((speed * time * 3f / 10f) % 360) + offset;
        return angle;
    }

    private float getRotationOffsetForPosition(GrinderBlockEntity be, Direction.Axis axis) {
        float offset = 0;
        double d = (((axis == Direction.Axis.X) ? 0 : be.getPos().getX())
                + ((axis == Direction.Axis.Y) ? 0 : be.getPos().getY())
                + ((axis == Direction.Axis.Z) ? 0 : be.getPos().getZ())) % 2;
        if (d == 0)
            offset = 180;
        return offset;
    }

    @Override
    protected SuperByteBuffer getRotatedModel(GrinderBlockEntity be, BlockState state) {
        Direction direction = state.get(GrinderBlock.HORIZONTAL_FACING);

        SuperByteBuffer shaft = CachedBufferer.partialFacing(BlockPartialModels.GRINDER_SHAFT, state, direction);

        return shaft;
    }
}
