package net.dandycorp.dccobblemon.block.custom.grinder;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.materials.oriented.OrientedData;
import com.simibubi.create.content.kinetics.base.SingleRotatingInstance;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlock;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.dandycorp.dccobblemon.block.BlockPartialModels;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;

public class GrinderBlockInstance extends SingleRotatingInstance<GrinderBlockEntity> {

    private final OrientedData frontGrinderInstance;

    public GrinderBlockInstance(MaterialManager materialManager, GrinderBlockEntity blockEntity) {
        super(materialManager, blockEntity);

        frontGrinderInstance = materialManager.defaultSolid()
                .material(Materials.ORIENTED)
                .getModel(BlockPartialModels.GRINDER_BACK)
                .createInstance();

        Quaternionf q = RotationAxis.POSITIVE_Y
                .rotationDegrees(AngleHelper.horizontalAngle(blockState.get(GrinderBlock.FACING).rotateClockwise(Direction.Axis.Y)));

        frontGrinderInstance.setRotation(q);
    }

    @Override
    public void updateLight() {
        super.updateLight();
        relight(pos.up(), frontGrinderInstance);
    }

    @Override
    public void remove() {
        super.remove();
        frontGrinderInstance.delete();
    }
}