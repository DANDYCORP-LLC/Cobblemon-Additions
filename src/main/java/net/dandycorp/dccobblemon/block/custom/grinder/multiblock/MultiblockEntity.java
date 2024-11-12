package net.dandycorp.dccobblemon.block.custom.grinder.multiblock;

import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.List;

import static net.dandycorp.dccobblemon.block.custom.grinder.multiblock.MultiblockPartBlock.FACING;

public class MultiblockEntity extends SmartBlockEntity implements IHaveGoggleInformation, IMultiblockPart {

    public MultiblockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(new DirectBeltInputBehaviour(this).allowingBeltFunnels());
    }

    @Override
    public boolean addToGoggleTooltip(List<Text> tooltip, boolean isPlayerSneaking) {
        BlockState state = getCachedState();
        BlockPos mainPos = findMainBlock(world,pos,state);
        return true;
    }

    @Override
    public Direction getMainBlockDirection(BlockState state) {
        return getCachedState().get(FACING);
    }
}
