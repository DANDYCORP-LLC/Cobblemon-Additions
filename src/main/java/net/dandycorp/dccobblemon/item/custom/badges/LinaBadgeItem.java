package net.dandycorp.dccobblemon.item.custom.badges;

import com.cobblemon.mod.common.api.types.ElementalType;
import dev.emi.trinkets.api.SlotReference;
import net.dandycorp.dccobblemon.item.custom.BadgeItem;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Fertilizable;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class LinaBadgeItem extends BadgeItem {

    private long tickCounter = 0;

    public LinaBadgeItem(Settings settings, List<ElementalType> elementalTypes) {
        super(settings,elementalTypes);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        super.appendTooltip(itemStack, world, tooltip, tooltipContext);
        tooltip.add(Text.literal(""));
        tooltip.add(Text.literal("Nature's blessing").formatted(Formatting.AQUA));
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if (!entity.getEntityWorld().isClient) {
            tickCounter++;
            if (tickCounter >= 300) {
                tickCounter = 0;
                for(BlockPos pos : BlockPos.iterateOutwards(entity.getBlockPos(), 7,2,7)){
                    if (entity.getEntityWorld() instanceof ServerWorld world) {
                        BlockState state = world.getBlockState(pos);
                        if (state.getBlock() instanceof Fertilizable fertilizable) {
                            if (Math.random() > 0.33 && !state.isOf(Blocks.GRASS_BLOCK) && fertilizable.isFertilizable(world,pos,state,true)) {
                                fertilizable.grow(world, world.random, pos, state);
                                world.spawnParticles(ParticleTypes.HAPPY_VILLAGER, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 6, 0.2, 0.6, 0.2,5);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public int getGradientStartColor() {
        return 0xe19c16;
    }

    @Override
    public int getGradientEndColor() {
        return 0xfdf59f;
    }
}
