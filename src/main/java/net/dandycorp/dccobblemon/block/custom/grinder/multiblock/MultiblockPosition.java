package net.dandycorp.dccobblemon.block.custom.grinder.multiblock;

import net.minecraft.util.StringIdentifiable;

public enum MultiblockPosition implements StringIdentifiable {
    LEFT("left"),
    MIDDLE("middle"),
    RIGHT("right");

    private final String name;

    MultiblockPosition(String name) {
        this.name = name;
    }

    @Override
    public String asString() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
