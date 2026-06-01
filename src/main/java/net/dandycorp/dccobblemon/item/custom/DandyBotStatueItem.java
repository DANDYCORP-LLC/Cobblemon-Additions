package net.dandycorp.dccobblemon.item.custom;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.dandycorp.dccobblemon.util.GradientFormatting;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TallBlockItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

import static net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions.RANDOM;

public class DandyBotStatueItem extends TallBlockItem implements GradientFormatting {

    private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

    public DandyBotStatueItem(Block block, Settings settings) {
        super(block, settings);

        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder =
                ImmutableMultimap.builder();

        builder.put(
                EntityAttributes.GENERIC_ATTACK_DAMAGE,
                new EntityAttributeModifier(
                        ATTACK_DAMAGE_MODIFIER_ID,
                        "Weapon modifier",
                        9,
                        EntityAttributeModifier.Operation.ADDITION
                )
        );
        builder.put(
                EntityAttributes.GENERIC_ATTACK_SPEED,
                new EntityAttributeModifier(
                        ATTACK_SPEED_MODIFIER_ID,
                        "Weapon modifier",
                        -3f,
                        EntityAttributeModifier.Operation.ADDITION
                )
        );
        this.attributeModifiers = builder.build();
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(
            EquipmentSlot slot) {

        return slot == EquipmentSlot.MAINHAND
                ? this.attributeModifiers
                : super.getAttributeModifiers(slot);
    }

    @Override
    public Text getName(ItemStack stack) {
        return gradientName(stack);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        super.appendTooltip(itemStack, world, tooltip, tooltipContext);
        tooltip.add(Text.literal(""));
        tooltip.add(Text.literal("A radiant tribute to the spirit of DANDYCORP").formatted(Formatting.GRAY));
        tooltip.add(Text.literal("reserved for employees who have demonstrated").formatted(Formatting.GRAY));
        tooltip.add(Text.literal("a truly exceptional financial commitment to").formatted(Formatting.GRAY));
        tooltip.add(Text.literal("DANDYCORP's vision.").formatted(Formatting.GRAY));
        tooltip.add(Text.literal(""));
        tooltip.add(Text.literal("(Can be placed)").formatted(Formatting.GRAY));
    }

    @Override
    public boolean postHit(ItemStack itemStack, LivingEntity livingEntity, LivingEntity livingEntity2) {
        if(livingEntity.getWorld() instanceof ClientWorld) return super.postHit(itemStack, livingEntity, livingEntity2);
        ServerWorld world = (ServerWorld) livingEntity.getWorld();
        BlockPos pos = livingEntity.getBlockPos();
        world.playSound(null, pos, SoundEvents.BLOCK_ANVIL_PLACE, net.minecraft.sound.SoundCategory.BLOCKS, 0.2f, RANDOM.nextFloat(1.1f, 1.5f));
        world.playSound(null, pos, SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, net.minecraft.sound.SoundCategory.BLOCKS, 0.5f, 0.0f);
        world.spawnParticles(
                ParticleTypes.WAX_OFF,
                livingEntity.getX(),
                livingEntity.getBodyY(0.5),
                livingEntity.getZ(),
                10,
                0.5,
                0.5,
                0.5,
                0.2
        );
        return super.postHit(itemStack, livingEntity, livingEntity2);
    }

    @Override
    public int getGradientStartColor() {
        return 0xe19c16;
    }

    @Override
    public int getGradientEndColor() {
        return 0xfdf59f;
    }

    @Override
    public boolean isBold() {
        return true;
    }
}
