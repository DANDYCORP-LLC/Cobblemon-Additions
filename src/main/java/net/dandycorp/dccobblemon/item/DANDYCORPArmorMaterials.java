package net.dandycorp.dccobblemon.item;

import net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

import java.util.function.Supplier;

public enum DANDYCORPArmorMaterials implements ArmorMaterial {
    CHROMIUM("chromium",40,new int[] {4,9,7,4}, 19,
            SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 4f, 0.1f, () -> Ingredient.ofItems(DANDYCORPItems.CHROMIUM_INGOT))
    ,

    PARAGONIUM("paragonium", 0 ,new int[] {6,11,9,6},30,
            SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE, 5f, 0.25f, () -> Ingredient.ofItems(DANDYCORPItems.PARAGONIUM_INGOT))
    ;

    private final String name;
    private final int durabilityMultiplier;
    private final int[] protectionAmounts;
    private final int enchantability;
    private final SoundEvent equipSound;
    private final float toughness;
    private final float knockbackResistance;
    private final Supplier<Ingredient> repairIngredient;

    private static final int[] BASE_DURABILITY = new int[] {11, 16, 15, 13};

    DANDYCORPArmorMaterials(String name, int durabilityMultiplier, int[] protectionAmounts, int enchantability, SoundEvent equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.protectionAmounts = protectionAmounts;
        this.enchantability = enchantability;
        this.equipSound = equipSound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getDurability(ArmorItem.Type type) {
        return (int) this.durabilityMultiplier * BASE_DURABILITY[type.ordinal()];
    }

    @Override
    public int getProtection(ArmorItem.Type type) {
        return protectionAmounts[type.ordinal()];
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.equipSound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    @Override
    public String getName() {
        return DANDYCORPCobblemonAdditions.MOD_ID + ":" + this.name;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}
