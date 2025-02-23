package net.dandycorp.dccobblemon.item.custom.badges;

import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotReference;
import net.dandycorp.dccobblemon.item.DANDYCORPItems;
import net.dandycorp.dccobblemon.item.custom.BadgeItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import virtuoel.pehkui.api.ScaleTypes;

import java.util.List;
import java.util.UUID;

public class DavidBadgeItem extends BadgeItem {

    public DavidBadgeItem(Settings settings) {
        super(settings);
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
