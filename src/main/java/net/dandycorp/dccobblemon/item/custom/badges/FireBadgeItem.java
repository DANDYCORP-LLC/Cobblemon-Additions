package net.dandycorp.dccobblemon.item.custom.badges;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.Trinket;
import net.dandycorp.dccobblemon.item.Items;
import net.dandycorp.dccobblemon.item.custom.BadgeItem;
import net.minecraft.block.*;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;

public class FireBadgeItem extends BadgeItem implements Trinket {

    public FireBadgeItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add(Text.literal("Inflicts fire on-hit").formatted(Formatting.GRAY));
        tooltip.add(Text.literal("Grants fire resistance").formatted(Formatting.GRAY));
        tooltip.add(Text.literal("Grants lava-walking").formatted(Formatting.GRAY));
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if (entity.getWorld() instanceof ServerWorld world) {
            if (entity.age % 20 == 0) {
                if (this.isEquipped(entity, Items.FIRE_BADGE)) {
                    entity.setFireTicks(0);
                    entity.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 600, 0, false, false));

                }
            }
            if (entity.isOnGround()) {
                BlockPos blockPos = entity.getBlockPos();
                BlockState blockState = net.dandycorp.dccobblemon.block.Blocks.WALKER_MAGMA.getDefaultState();
                int i = Math.min(16, 2);
                BlockPos.Mutable mutable = new BlockPos.Mutable();

                for (BlockPos blockPos2 : BlockPos.iterate(blockPos.add(-i, -1, -i), blockPos.add(i, -1, i))) {
                    if (blockPos2.isWithinDistance(entity.getPos(), (double) i)) {
                        mutable.set(blockPos2.getX(), blockPos2.getY() + 1, blockPos2.getZ());
                        BlockState blockState2 = world.getBlockState(mutable);
                        if (blockState2.isAir()) {
                            BlockState blockState3 = world.getBlockState(blockPos2);
                            if (blockState3 == Blocks.LAVA.getDefaultState() && blockState.canPlaceAt(world, blockPos2) && world.canPlace(blockState, blockPos2, ShapeContext.absent())) {
                                world.setBlockState(blockPos2, blockState);
                                world.scheduleBlockTick(blockPos2, net.dandycorp.dccobblemon.block.Blocks.WALKER_MAGMA, MathHelper.nextInt(entity.getRandom(), 60, 120));
                            }
                        }
                    }
                }

            }
        }
    }

    @Override
    public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        entity.removeStatusEffect(StatusEffects.FIRE_RESISTANCE);
    }

    @Override
    public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        entity.setFireTicks(0);
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 600, 0, false, false));
    }

}
