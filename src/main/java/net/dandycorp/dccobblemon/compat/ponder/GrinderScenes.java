package net.dandycorp.dccobblemon.compat.ponder;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import net.dandycorp.dccobblemon.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class GrinderScenes {
    public static void grind(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("grinder_grind", "The DANDYCORP Industrial Grinder");
        scene.configureBasePlate(0,1,7);
        scene.showBasePlate();
        scene.idle(5);
        scene.world.showSection(util.select.fromTo(7,0,0,7,0,6), Direction.WEST);
        scene.idle(2);
        scene.world.showSection(util.select.layersFrom(1), Direction.DOWN);
        scene.idle(5);
        scene.world.setKineticSpeed(util.select.position(7, 0, 2), 32);
        scene.world.setKineticSpeed(util.select.position(7, 1, 3), -64);
        scene.idle(5);
    }
}
