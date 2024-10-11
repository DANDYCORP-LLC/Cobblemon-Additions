package net.dandycorp.dccobblemon.block.custom.grinder;

import com.jozufozu.flywheel.backend.Backend;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
import net.dandycorp.dccobblemon.block.BlockPartialModels;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;

import static com.simibubi.create.content.contraptions.gantry.GantryCarriageRenderer.getAngleForBE;

public class GrinderBlockRenderer extends KineticBlockEntityRenderer<GrinderBlockEntity> {

    public GrinderBlockRenderer(BlockEntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(GrinderBlockEntity be, float partialTicks, MatrixStack ms,
                              VertexConsumerProvider buffer, int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);

        if (Backend.canUseInstancing(be.getWorld()))
            return;

        renderGrinderWheels(be, ms, buffer, light, overlay);
    }

    protected void renderGrinderWheels(GrinderBlockEntity be, MatrixStack ms, VertexConsumerProvider buffer,
                                       int light, int overlay) {
        BlockState blockState = be.getCachedState();
        Direction facing = Direction.NORTH;

        Direction.Axis rotationAxis = getRotationAxis(facing);
        float angle = getAngleForBE(be, be.getPos(), rotationAxis);

        // Adjust the facing angle
        float facingAngle = getFacingAngle(facing);

        SuperByteBuffer leftGrinder = CachedBufferer.partial(BlockPartialModels.GRINDER_FRONT, blockState);
        SuperByteBuffer rightGrinder = CachedBufferer.partial(BlockPartialModels.GRINDER_BACK, blockState);

        // Apply rotations
        leftGrinder.rotateCentered(Direction.UP, facingAngle)
                .rotateCentered(facing, angle)
                .light(light)
                .color(0xFFFFFF)
                .renderInto(ms, buffer.getBuffer(RenderLayer.getCutoutMipped()));

        rightGrinder.rotateCentered(Direction.UP, facingAngle)
                .rotateCentered(facing, -angle)
                .light(light)
                .color(0xFFFFFF)
                .renderInto(ms, buffer.getBuffer(RenderLayer.getCutoutMipped()));
    }

    @Override
    protected SuperByteBuffer getRotatedModel(GrinderBlockEntity be, BlockState state) {
        Direction facing = state.get(GrinderBlock.FACING);
        float facingAngle = getFacingAngle(facing);

        SuperByteBuffer shaft = CachedBufferer.partial(BlockPartialModels.GRINDER_SHAFT, state);
        shaft.rotateCentered(Direction.UP, facingAngle);
        return shaft;
    }

    private Direction.Axis getRotationAxis(Direction facing) {
        // The rotation axis is perpendicular to the facing direction
        return (facing.getAxis() == Direction.Axis.X) ? Direction.Axis.Z : Direction.Axis.X;
    }

    private float getFacingAngle(Direction facing) {
        // Get the rotation angle needed to align the model with the facing direction
        return AngleHelper.rad(AngleHelper.horizontalAngle(facing));
    }
}
