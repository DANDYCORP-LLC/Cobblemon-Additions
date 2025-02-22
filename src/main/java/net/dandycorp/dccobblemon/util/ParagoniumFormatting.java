package net.dandycorp.dccobblemon.util;

public interface ParagoniumFormatting extends GradientFormatting {
    default int getGradientStartColor() {
        return 0x9600ff;
    }

    default int getGradientEndColor() {
        return 0x3a00ce;
    }

    @Override
    default boolean isBold() {
        return true;
    }
}
