package net.dandycorp.dccobblemon.item.custom.paragonium;

import net.dandycorp.dccobblemon.util.GradientFormatting;
import net.dandycorp.dccobblemon.util.ParagoniumFormatting;
import net.dandycorp.dccobblemon.util.TextUtils;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ParagoniumBowItem extends BowItem implements ParagoniumFormatting {
    public ParagoniumBowItem(Settings settings) {
        super(settings);
    }

    @Override
    public void onStoppedUsing(ItemStack bowStack, World world, LivingEntity user, int remainingUseTicks) {
        if (!(user instanceof PlayerEntity player)) {
            return;
        }

        ItemStack arrowStack = findSpecialArrow(player);
        boolean isSpecialArrow = !arrowStack.isEmpty();

        if (!isSpecialArrow) {
            arrowStack = new ItemStack(Items.ARROW);
        }

        int chargeTicks = this.getMaxUseTime(bowStack) - remainingUseTicks;
        float pullProgress = BowItem.getPullProgress(chargeTicks);
        if (pullProgress < 0.1F) {
            return;
        }

        if (!world.isClient) {
            ArrowItem arrowItem = (ArrowItem)(
                    arrowStack.getItem() instanceof ArrowItem
                            ? arrowStack.getItem()
                            : Items.ARROW
            );

            PersistentProjectileEntity arrowEntity = arrowItem.createArrow(world, arrowStack, player);
            arrowEntity.setVelocity(
                    player,
                    player.getPitch(),
                    player.getYaw(),
                    0.0F,
                    pullProgress * 3.0F,
                    1.0F
            );

            if (pullProgress == 1.0F) {
                arrowEntity.setCritical(true);
            }

            int powerLevel = EnchantmentHelper.getLevel(Enchantments.POWER, bowStack);
            if (powerLevel > 0) {
                arrowEntity.setDamage(
                        arrowEntity.getDamage() + (double)powerLevel * 0.5 + 0.5
                );
            }

            int punchLevel = EnchantmentHelper.getLevel(Enchantments.PUNCH, bowStack);
            if (punchLevel > 0) {
                arrowEntity.setPunch(punchLevel);
            }

            if (EnchantmentHelper.getLevel(Enchantments.FLAME, bowStack) > 0) {
                arrowEntity.setOnFireFor(100);
            }

            bowStack.damage(1, player, (p) -> p.sendToolBreakStatus(player.getActiveHand()));

            if (isSpecialArrow && !player.getAbilities().creativeMode) {
                arrowStack.decrement(1);
                if (arrowStack.isEmpty()) {
                    player.getInventory().removeOne(arrowStack);
                }
            } else {
                arrowEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
            }
            world.spawnEntity(arrowEntity);
        }

        world.playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.ENTITY_ARROW_SHOOT,
                SoundCategory.PLAYERS,
                1.0F,
                1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + pullProgress * 0.5F
        );

        player.incrementStat(Stats.USED.getOrCreateStat(this));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        ItemStack itemStack = playerEntity.getStackInHand(hand);
        playerEntity.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }

    private ItemStack findSpecialArrow(PlayerEntity player) {
        for (int i = 0; i < player.getInventory().size(); i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if (isSpecialArrow(stack)) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }


    private boolean isSpecialArrow(ItemStack stack) {
        return stack.isIn(ItemTags.ARROWS) && !stack.isOf(Items.ARROW);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, @Nullable World world, List<Text> list, TooltipContext tooltipContext) {
        super.appendTooltip(itemStack, world, list, tooltipContext);
        list.addAll(TextUtils.wrapText(Text.translatable("item.dccobblemon.paragonium_tool.description").formatted(Formatting.GRAY),30));
        list.addAll(TextUtils.wrapText(Text.translatable("item.dccobblemon.paragonium_bow.description").formatted(Formatting.GRAY),30));
    }

    @Override
    public Text getName(ItemStack stack) {
        return gradientName(stack);
    }

    @Override
    public int getRange() {
        return 25;
    }


}

