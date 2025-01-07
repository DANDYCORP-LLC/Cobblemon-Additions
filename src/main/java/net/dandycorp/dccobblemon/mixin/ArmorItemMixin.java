package net.dandycorp.dccobblemon.mixin;

import com.google.common.collect.ImmutableMultimap;
import net.dandycorp.dccobblemon.item.DANDYCORPArmorMaterials;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ArmorItem.class)
public class ArmorItemMixin {

    @Redirect(
            method = "<init>(Lnet/minecraft/item/ArmorMaterial;Lnet/minecraft/item/ArmorItem$Type;Lnet/minecraft/item/Item$Settings;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/google/common/collect/ImmutableMultimap$Builder;build()Lcom/google/common/collect/ImmutableMultimap;"
            )
    )
    private ImmutableMultimap<EntityAttribute, EntityAttributeModifier> constructor(
            ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder,
            ArmorMaterial armorMaterial,
            ArmorItem.Type type,
            Item.Settings settings)
    {
        if(armorMaterial == DANDYCORPArmorMaterials.PARAGONIUM){
            builder.put(EntityAttributes.GENERIC_MOVEMENT_SPEED, new EntityAttributeModifier("paragonium_speed", 0.08, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
            builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier("paragonium_damage", 0.025, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
        }
        return builder.build();
    }
}
