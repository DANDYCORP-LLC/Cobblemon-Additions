package net.dandycorp.dccobblemon.item.custom.badges;

import dev.emi.trinkets.api.SlotReference;
import net.dandycorp.dccobblemon.item.Items;
import net.dandycorp.dccobblemon.item.custom.BadgeItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import virtuoel.pehkui.api.ScaleTypes;

import java.util.List;

public class BugBadgeItem extends BadgeItem {

    public BugBadgeItem(Settings settings) {
        super(settings);
    }

    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add(Text.literal("Size reduced by 33%").formatted(Formatting.GRAY));
        tooltip.add(Text.literal("Bioluminescence").formatted(Formatting.GRAY));
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {

        if(!entity.getEntityWorld().isClient) {
            tickCounter++;

            if (!stack.isOf(Items.BUG_BADGE) && tickCounter >= 20) {
                ScaleTypes.HEIGHT.getScaleData(entity).setScale(1);
                ScaleTypes.WIDTH.getScaleData(entity).setScale(1);

            }
        }
    }

    @Override
    public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if(!entity.getEntityWorld().isClient) {
            ScaleTypes.HEIGHT.getScaleData(entity).setScale(1f);
            ScaleTypes.WIDTH.getScaleData(entity).setScale(1f);

        }
    }

    @Override
    public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if(!entity.getEntityWorld().isClient) {
            if (stack.isOf(Items.BUG_BADGE)) {
                ScaleTypes.HEIGHT.getScaleData(entity).setScale(0.66f);
                ScaleTypes.WIDTH.getScaleData(entity).setScale(0.66f);
                //ScaleTypes.STEP_HEIGHT.getScaleData(entity).setScale(1.5f);
            }
        }
    }

}
