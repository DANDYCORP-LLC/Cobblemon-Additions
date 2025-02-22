package net.dandycorp.dccobblemon.mixin;

import net.dandycorp.dccobblemon.util.GradientFormatting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class StackNameMixin implements GradientFormatting {

    @Shadow public abstract Item getItem();

    @Shadow @Nullable public abstract NbtCompound getSubNbt(String string);

    @Inject(method="getName", at=@At("RETURN"), cancellable = true)
    public void gradientName(CallbackInfoReturnable<Text> cir) {
        if (this.getItem() instanceof GradientFormatting gradient) {
            NbtCompound nbtCompound = this.getSubNbt("display");
            if (nbtCompound != null && nbtCompound.contains("Name", 8)) {
                try {
                    Text text = Text.Serializer.fromJson(nbtCompound.getString("Name"));
                    if (text != null) {
                        cir.setReturnValue(gradient.gradientText(text));
                    }
                } catch (Exception e) {
                    nbtCompound.remove("Name");
                }
            }
        }
    }
}
