package net.dandycorp.dccobblemon.item.custom;

import blue.endless.jankson.annotation.Nullable;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.simibubi.create.AllSoundEvents;
import io.github.fabricators_of_create.porting_lib.enchant.CustomEnchantingBehaviorItem;
import net.dandycorp.dccobblemon.entities.CannonballEntity;
import net.dandycorp.dccobblemon.sound.DANDYCORPSounds;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

import static net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions.MOD_ID;
import static net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions.RANDOM;

public class HailMaryItem extends Item implements Vanishable, CustomEnchantingBehaviorItem {

    private static final UUID SLOW_ID =
            UUID.fromString("e088b40f-d43d-45d7-8e20-d691faf5421a");
    private static final String LOADED_KEY = "Loaded";
    private static final int PULL_TIME = 35;

    public HailMaryItem(Settings settings) { super(settings); }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (isLoaded(stack)) {
            tooltip.add(Text.translatable("item.dccobblemon.hail_mary_primed").formatted(Formatting.DARK_GREEN));
        }
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(ItemStack stack, EquipmentSlot slot) {
        Multimap<EntityAttribute, EntityAttributeModifier> base = super.getAttributeModifiers(stack, slot);
        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> b = ImmutableMultimap.builder();
            b.putAll(base);
            b.put(EntityAttributes.GENERIC_MOVEMENT_SPEED,
                    new EntityAttributeModifier(
                            SLOW_ID,
                            MOD_ID + ":cannon_weight",
                            -0.5,
                            EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
            return b.build();
        }
        return base;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (hand == Hand.OFF_HAND) return TypedActionResult.pass(stack);

        if(isLoaded(stack)) {
            user.addVelocity(user.getRotationVector().multiply(-1.5 * (1 + (0.15f * EnchantmentHelper.getLevel(Enchantments.POWER, stack)))));
            if(!world.isClient) {
                CannonballEntity ball = new CannonballEntity(world, user, stack);
                ball.setVelocity(
                        user,
                        user.getPitch(),
                        user.getYaw(),
                        0.0F,
                        3.0F * (1 + (0.08f * EnchantmentHelper.getLevel(Enchantments.POWER, stack))),
                        1.0F
                );
                world.spawnEntity(ball);
                world.playSound(null,user.getBlockPos(), DANDYCORPSounds.CANNON_FIRE_EVENT, SoundCategory.PLAYERS,4.0f, 1.0f);

                stack.damage(1, user, e -> e.sendToolBreakStatus(hand));
                user.getItemCooldownManager().set(this, 40);
                user.incrementStat(Stats.USED.getOrCreateStat(this));
            }
            setLoaded(stack, false);
            return TypedActionResult.success(stack, world.isClient);
        }

        user.setCurrentHand(hand);
        return TypedActionResult.consume(stack);
    }

    @Override
    public void usageTick(World world, LivingEntity livingEntity, ItemStack itemStack, int i) {
        float f = (float) (itemStack.getMaxUseTime() - i) / (float) getLoadTime(itemStack);
        if (f >= 1.0F && !isLoaded(itemStack)) {
            AllSoundEvents.FWOOMP.playAt(
                    world,
                    livingEntity.getPos(),
                    2f,
                    RANDOM.nextFloat(0.8f, 1.0f),
                    true
            );
            setLoaded(itemStack, true);
        }
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity livingEntity, int i) {
        int usedTicks = getMaxUseTime(stack) - i;
        if (!isLoaded(stack)) {
            float progress = getLoadProgress(usedTicks, stack);
            if (progress >= 1.0f) {
                if (!world.isClient) {
                    setLoaded(stack, true);
                }
            }
        }
    }

    public int getLoadTime(ItemStack itemStack) {
        int i = EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, itemStack);
        return i == 0 ? PULL_TIME : PULL_TIME - 5 * i;
    }

    public float getLoadProgress(int i, ItemStack stack) {
        return Math.min((float) i / getLoadTime(stack), 1.0f);
    }

    public static boolean isLoaded(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        return nbt != null && nbt.getBoolean(LOADED_KEY);
    }
    public static void setLoaded(ItemStack stack, boolean loaded) {
        stack.getOrCreateNbt().putBoolean(LOADED_KEY, loaded);
    }

    @Override public int getMaxUseTime(ItemStack stack) {
        return getLoadTime(stack) + 3;
    }
    @Override public UseAction getUseAction (ItemStack stack) { return UseAction.BOW; }

    @Override
    public boolean isUsedOnRelease(ItemStack itemStack) {
        return itemStack.isOf(this);
    }

    @Override
    public int getEnchantability() {
        return 1;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (enchantment == Enchantments.QUICK_CHARGE
                || enchantment == Enchantments.POWER
                || enchantment == Enchantments.PUNCH
                || enchantment == Enchantments.FLAME
                || enchantment == Enchantments.CHANNELING) {
            return true;
        }
        return CustomEnchantingBehaviorItem.super.canApplyAtEnchantingTable(stack, enchantment);
    }
}
