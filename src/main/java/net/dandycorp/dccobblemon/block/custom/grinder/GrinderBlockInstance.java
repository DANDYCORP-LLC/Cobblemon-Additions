package net.dandycorp.dccobblemon.block.custom.grinder;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.materials.oriented.OrientedData;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityInstance;
import com.simibubi.create.content.kinetics.base.ShaftInstance;
import com.simibubi.create.content.kinetics.base.SingleRotatingInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlock;
import com.simibubi.create.foundation.render.AllMaterialSpecs;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.dandycorp.dccobblemon.block.BlockPartialModels;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;
import static com.simibubi.create.content.kinetics.base.DirectionalKineticBlock.FACING;

public class GrinderBlockInstance extends KineticBlockEntityInstance<GrinderBlockEntity> {

    private final RotatingData frontGrinderInstance;
    private final RotatingData backGrinderInstance;
    private final RotatingData shaftInstance;
    final Direction direction;

    public GrinderBlockInstance(MaterialManager materialManager, GrinderBlockEntity blockEntity) {
        super(materialManager, blockEntity);

        direction = blockEntity.getCachedState().get(FACING);

        frontGrinderInstance = materialManager.defaultCutout()
                .material(AllMaterialSpecs.ROTATING)
                .getModel(BlockPartialModels.GRINDER_FRONT, blockEntity.getCachedState(), direction)
                .createInstance();
        setup(frontGrinderInstance);

        backGrinderInstance = materialManager.defaultCutout()
                .material(AllMaterialSpecs.ROTATING)
                .getModel(BlockPartialModels.GRINDER_BACK, blockEntity.getCachedState(), direction)
                .createInstance();
        setup(backGrinderInstance);

        shaftInstance = materialManager.defaultCutout()
                .material(AllMaterialSpecs.ROTATING)
                .getModel(BlockPartialModels.GRINDER_SHAFT, blockEntity.getCachedState(), direction)
                .createInstance();
        setup(shaftInstance);

        if(direction == Direction.NORTH || direction == Direction.SOUTH) {
            frontGrinderInstance.nudge(0, .625f, .4375f);
            backGrinderInstance.nudge(0, .625f, -.4375f);
        }
        else{
            frontGrinderInstance.nudge(.4375f, .625f,0);
            backGrinderInstance.nudge(-.4375f, .625f, 0);
        }

        updateGrinder(backGrinderInstance,false);
        updateGrinder(frontGrinderInstance,true);
    }

    @Override
    public void update() {
        updateGrinder(frontGrinderInstance,true);
        updateGrinder(backGrinderInstance,false);
        updateRotation(shaftInstance);
    }

    private void updateGrinder(RotatingData instance, boolean isFront) {
        float speed = Math.abs(getBlockEntitySpeed())/2;
        Direction.Axis axis = getRotationAxis();

        boolean reverseRotation;
        switch (direction) {
            case EAST:
            case WEST:
                reverseRotation = !isFront;
                break;
            default:
                reverseRotation = isFront;
                break;
        }

        float adjustedSpeed = reverseRotation ? -speed : speed;

        instance.setRotationAxis(axis)
                .setRotationOffset(getRotationOffset(axis))
                .setRotationalSpeed(adjustedSpeed)
                .setColor(blockEntity);
    }




    @Override
    public void updateLight() {
        super.updateLight();
        relight(pos.up(), frontGrinderInstance);
        relight(pos.up(), backGrinderInstance);
        relight(pos, shaftInstance);
    }

    @Override
    public void remove() {
        frontGrinderInstance.delete();
        backGrinderInstance.delete();
        shaftInstance.delete();
    }
}