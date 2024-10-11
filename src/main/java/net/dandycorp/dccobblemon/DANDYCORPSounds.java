package net.dandycorp.dccobblemon;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class DANDYCORPSounds {

    public static final Identifier GREED = Identifier.of(DANDYCORPCobblemonAdditions.MOD_ID, "greed");
    public static final Identifier JUDGEMENT = Identifier.of(DANDYCORPCobblemonAdditions.MOD_ID, "judgement");
    public static final Identifier COMPLIMENT = Identifier.of(DANDYCORPCobblemonAdditions.MOD_ID, "compliment");
    public static final Identifier VENDOR_OPEN = Identifier.of(DANDYCORPCobblemonAdditions.MOD_ID, "vendor_open");
    public static final Identifier VENDOR_CLICK = Identifier.of(DANDYCORPCobblemonAdditions.MOD_ID, "vendor_click");
    public static final Identifier VENDOR_BUY = Identifier.of(DANDYCORPCobblemonAdditions.MOD_ID, "vendor_buy");
    public static final Identifier VENDOR_POOR = Identifier.of(DANDYCORPCobblemonAdditions.MOD_ID, "vendor_poor");
    public static final Identifier GRINDER_GRIND = Identifier.of(DANDYCORPCobblemonAdditions.MOD_ID, "grinder_grind");
    public static final Identifier GRINDER_ACTIVE = Identifier.of(DANDYCORPCobblemonAdditions.MOD_ID, "grinder_active");
    public static final Identifier GRINDER_POWERED = Identifier.of(DANDYCORPCobblemonAdditions.MOD_ID, "grinder_powered");

    public static SoundEvent VENDOR_POOR_EVENT = SoundEvent.of(VENDOR_POOR);
    public static SoundEvent VENDOR_BUY_EVENT = SoundEvent.of(VENDOR_BUY);
    public static SoundEvent VENDOR_CLICK_EVENT = SoundEvent.of(VENDOR_CLICK);
    public static SoundEvent VENDOR_OPEN_EVENT = SoundEvent.of(VENDOR_OPEN);
    public static SoundEvent COMPLIMENT_EVENT = SoundEvent.of(COMPLIMENT);
    public static SoundEvent JUDGEMENT_EVENT = SoundEvent.of(JUDGEMENT);
    public static SoundEvent GREED_EVENT = SoundEvent.of(GREED);
    public static SoundEvent GRINDER_GRIND_EVENT  = SoundEvent.of(GRINDER_GRIND);
    public static SoundEvent GRINDER_ACTIVE_EVENT = SoundEvent.of(GRINDER_ACTIVE);
    public static SoundEvent GRINDER_POWERED_EVENT = SoundEvent.of(GRINDER_POWERED);

    public static void registerSounds() {
        Registry.register(Registries.SOUND_EVENT, GREED, GREED_EVENT);
        Registry.register(Registries.SOUND_EVENT, JUDGEMENT, JUDGEMENT_EVENT);
        Registry.register(Registries.SOUND_EVENT, COMPLIMENT, COMPLIMENT_EVENT);
        Registry.register(Registries.SOUND_EVENT, VENDOR_OPEN, VENDOR_OPEN_EVENT);
        Registry.register(Registries.SOUND_EVENT, VENDOR_CLICK, VENDOR_CLICK_EVENT);
        Registry.register(Registries.SOUND_EVENT, VENDOR_BUY, VENDOR_BUY_EVENT);
        Registry.register(Registries.SOUND_EVENT, VENDOR_POOR, VENDOR_POOR_EVENT);
        Registry.register(Registries.SOUND_EVENT, GRINDER_GRIND, GRINDER_GRIND_EVENT);
        Registry.register(Registries.SOUND_EVENT, GRINDER_ACTIVE, GRINDER_ACTIVE_EVENT);
        Registry.register(Registries.SOUND_EVENT, GRINDER_POWERED, GRINDER_POWERED_EVENT);
    }
}
