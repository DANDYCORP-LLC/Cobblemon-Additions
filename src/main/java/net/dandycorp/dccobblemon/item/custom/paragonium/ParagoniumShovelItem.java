package net.dandycorp.dccobblemon.item.custom.paragonium;

import net.dandycorp.dccobblemon.util.GradientFormatting;
import net.dandycorp.dccobblemon.util.ParagoniumFormatting;
import net.dandycorp.dccobblemon.util.ScreenShakeController;
import net.dandycorp.dccobblemon.util.TextUtils;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ParagoniumShovelItem extends ShovelItem implements ParagoniumFormatting {
    public ParagoniumShovelItem(ToolMaterial toolMaterial, float f, float g, Settings settings) {
        super(toolMaterial, f, g, settings);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, @Nullable World world, List<Text> list, TooltipContext tooltipContext) {
        super.appendTooltip(itemStack, world, list, tooltipContext);
        list.addAll(TextUtils.wrapText(Text.translatable("item.dccobblemon.paragonium_tool.description").formatted(Formatting.GRAY),30));
    }

    @Override
    public Text getName(ItemStack stack) {
        return gradientName(stack);
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        if(playerEntity instanceof ServerPlayerEntity serverPlayer) {
            ScreenShakeController.sendShakeToClient(serverPlayer,0.6f, 40, 120, ScreenShakeController.FadeType.REVERSE_EXPONENTIAL);
        }
        return super.use(world, playerEntity, hand);
    }
}
