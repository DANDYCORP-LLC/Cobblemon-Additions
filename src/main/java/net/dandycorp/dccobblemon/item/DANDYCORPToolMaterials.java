package net.dandycorp.dccobblemon.item;

import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

import java.util.function.Supplier;

public enum DANDYCORPToolMaterials implements ToolMaterial {

    CHROMIUM(4, 1520, 15f, 5F, 22, () -> {
        return Ingredient.ofItems(new ItemConvertible[]{DANDYCORPItems.CHROMIUM_INGOT});}
    ),

    PARAGONIUM(6, 0, 28f, 11F, 10, () -> {
        return Ingredient.ofItems(new ItemConvertible[]{DANDYCORPItems.PARAGONIUM_INGOT});}
    );

    private final int miningLevel;
    private final int itemDurability;
    private final float miningSpeed;
    private final float attackDamage;
    private final int enchantability;
    private final Supplier<Ingredient> repairIngredient;


    DANDYCORPToolMaterials(int miningLevel, int itemDurability, float miningSpeed, float attackDamage, int enchantability, Supplier<Ingredient> repairIngredient) {
        this.miningLevel = miningLevel;
        this.itemDurability = itemDurability;
        this.miningSpeed = miningSpeed;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
        this.repairIngredient = repairIngredient;
    }

    public int getDurability() {
        return this.itemDurability;
    }

    public float getMiningSpeedMultiplier() {
        return this.miningSpeed;
    }

    public float getAttackDamage() {
        return this.attackDamage;
    }

    public int getMiningLevel() {
        return this.miningLevel;
    }

    public int getEnchantability() {
        return this.enchantability;
    }

    public Ingredient getRepairIngredient() {
        return (Ingredient)this.repairIngredient.get();
    }
}
