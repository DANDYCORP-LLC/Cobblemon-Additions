package net.dandycorp.dccobblemon.item.custom;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtil;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ShinySandwichItem extends Item {
    public ShinySandwichItem(Settings settings) {
        super(settings);
    }

    public void appendTooltip(ItemStack itemStack, @Nullable World world, List<Text> list, TooltipContext tooltipContext) {
        if(itemStack.isFood()) {
            List<StatusEffectInstance> effects = getFoodComponent().getStatusEffects()
                    .stream()
                    .map(Pair::getFirst)
                    .toList();
            PotionUtil.buildTooltip(effects, list, 1.0f);
        }
        super.appendTooltip(itemStack, world, list, tooltipContext);
    }
}
