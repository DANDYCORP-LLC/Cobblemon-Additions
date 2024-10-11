package net.dandycorp.dccobblemon.block.custom.grinder;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.item.ItemHelper;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandler;
import net.dandycorp.dccobblemon.DANDYCORPDamageTypes;
import net.dandycorp.dccobblemon.DANDYCORPSounds;
import net.dandycorp.dccobblemon.util.LoopingSoundInstance;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.*;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions.RANDOM;

public class GrinderBlockEntity extends KineticBlockEntity implements SidedStorageBlockEntity {

    protected ItemStackHandler input;
    protected ItemStackHandler output;
    protected int processingTime;
    protected final int maxProcessingTime = 300;
    protected int points;
    protected GrinderInventoryHandler capability;
    protected Box aabb = new Box(pos.getX(), pos.getY() + 1, pos.getZ(),
            pos.getX() + 1, pos.getY() + 1.5, pos.getZ() + 1);

    @Nullable
    private LoopingSoundInstance poweredSoundInstance;
    @Nullable
    private LoopingSoundInstance grindingSoundInstance;

    public GrinderBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        input = new ItemStackHandler(1){
            @Override
            protected void onContentsChanged(int slot) {
                sendData();
            }
        };
        output = new ItemStackHandler(1){
            @Override
            protected void onContentsChanged(int slot) {
                sendData();
            }
        };
        capability = new GrinderInventoryHandler();
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        behaviours.add(new DirectBeltInputBehaviour(this).allowingBeltFunnels());
    }

    @Override
    public void destroy() {
        super.destroy();
        ItemHelper.dropContents(world, pos, input);
        ItemHelper.dropContents(world, pos, output);
    }

    @Override
    public void write(NbtCompound compound, boolean clientPacket) {
        compound.put("InputInventory", input.serializeNBT());
        compound.put("OutputInventory", output.serializeNBT());
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(NbtCompound compound, boolean clientPacket) {
        input.deserializeNBT(compound.getCompound("InputInventory"));
        output.deserializeNBT(compound.getCompound("OutputInventory"));
        super.read(compound, clientPacket);
    }

    private boolean canProcess(ItemStack stack) {
        return true; // For now, always return true
    }


    @Override
    public void tick() {
        super.tick();

        if (getSpeed() == 0 || !isSpeedRequirementFulfilled()) return;
        if (world == null) return;

        //System.out.println(input.getStackInSlot(0));
        if (!input.getStackInSlot(0).isEmpty()) {
            //System.out.println("not empty. process time = " + processingTime);
            processingTime += (int) (getSpeed() / 16);
            if (processingTime > maxProcessingTime) {
                processingTime = 0;
                input.getStackInSlot(0).decrement(1);
                world.playSound(null,
                        pos,DANDYCORPSounds.GRINDER_GRIND_EVENT,
                        SoundCategory.BLOCKS,
                        0.3f,
                        MathHelper.clamp((0.4f+(Math.abs(getSpeed())/170)), 0.4f, 1.8f) + RANDOM.nextFloat(0.0f, 0.2f));
            }
        } else processingTime = 0;
        applyEntityDamage();
    }

    private void applyEntityDamage() {
        List<Entity> entities = world.getEntitiesByClass(Entity.class, aabb, entity -> entity instanceof LivingEntity);

            for (Entity entity : entities) {
                if (entity instanceof LivingEntity livingEntity) {
                    livingEntity.damage(DANDYCORPDamageTypes.of(world, DANDYCORPDamageTypes.GRINDER), (Math.abs(getSpeed())/32f));
                }
            }
    }

    private class GrinderInventoryHandler extends CombinedStorage<ItemVariant, ItemStackHandler> {

        public GrinderInventoryHandler() {
            super(List.of(input, output));
        }

        @Override
        public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction) {
            if (canProcess(resource.toStack()))
                return input.insert(resource, maxAmount, transaction);
            return 0;
        }

        @Override
        public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
            return output.extract(resource, maxAmount, transaction);
        }
    }

    @Nullable
    @Override
    public Storage<ItemVariant> getItemStorage(@Nullable Direction direction) {
        return capability;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void tickAudio() {
        if(!isNoisy() || world == null){
            return;
        }

        float pitch = MathHelper.clamp((0.5f+(Math.abs(getSpeed())/170)), 0.5f, 1.85f);
        SoundManager soundManager = MinecraftClient.getInstance().getSoundManager();

        // grinder is powered at minimum required speed
        if (isSpeedRequirementFulfilled()) {
            if (poweredSoundInstance == null) {
                SoundEvent soundEventA = DANDYCORPSounds.GRINDER_POWERED_EVENT;
                Vec3d position = new Vec3d(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5); // Center position

                poweredSoundInstance = new LoopingSoundInstance(
                        soundEventA,
                        SoundCategory.BLOCKS,
                        position,
                        0.4f, // Volume
                        pitch
                );

                soundManager.play(poweredSoundInstance);
            } else {
                if (!poweredSoundInstance.isPlaying())
                    poweredSoundInstance.resumeSound(soundManager);
                if (poweredSoundInstance.getPitch() != pitch) {
                    poweredSoundInstance.setPitch(pitch);
                    poweredSoundInstance.resetSound(soundManager);
                }
            }
        } else {
            if (poweredSoundInstance != null) {
                poweredSoundInstance.stopSound(soundManager);
                poweredSoundInstance = null;
            }
        }

        // item present in input inventory
        if (!input.getStackInSlot(0).isEmpty() && isSpeedRequirementFulfilled()) {
            world.addParticle(ParticleTypes.ELECTRIC_SPARK,
                    pos.getX() + RANDOM.nextDouble(0.3, 0.7),
                    pos.getY() + 1,
                    pos.getZ() + RANDOM.nextDouble(0.3, 0.7),
                    RANDOM.nextDouble(-0.5, 0.5),
                    (Math.abs(getSpeed())/48f),
                    RANDOM.nextDouble(-0.5, 0.5));

            ItemStackParticleEffect particle = new ItemStackParticleEffect(ParticleTypes.ITEM, input.getStackInSlot(0));
            world.addParticle(particle,
                    pos.getX() + RANDOM.nextDouble(0.2, 0.8),
                    pos.getY() + 1,
                    pos.getZ() + RANDOM.nextDouble(0.2, 0.8),
                    RANDOM.nextDouble(-0.1, 0.1),
                    MathHelper.clamp((Math.abs(getSpeed())/300f),0.3,0.5),
                    RANDOM.nextDouble(-0.1, 0.1));

            if (grindingSoundInstance == null) {
                SoundEvent soundEventB = DANDYCORPSounds.GRINDER_ACTIVE_EVENT;
                Vec3d position = new Vec3d(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5); // Center position

                grindingSoundInstance = new LoopingSoundInstance(
                        soundEventB,
                        SoundCategory.BLOCKS,
                        position,
                        0.6f, // Volume
                        pitch
                );
                soundManager.play(grindingSoundInstance);
            } else {
                if (!grindingSoundInstance.isPlaying())
                    grindingSoundInstance.resumeSound(soundManager);
                if (grindingSoundInstance.getPitch() != pitch) {
                    grindingSoundInstance.setPitch(pitch);
                    grindingSoundInstance.resetSound(soundManager);
                }
            }
        } else {
            if (grindingSoundInstance != null) {
                grindingSoundInstance.stopSound(soundManager);
                grindingSoundInstance = null;
            }
        }
    }

    @Override
    public void remove() {
        super.remove();
        if (world != null && world.isClient()) {
            SoundManager soundManager = MinecraftClient.getInstance().getSoundManager();
            soundManager.stop(poweredSoundInstance);
            soundManager.stop(grindingSoundInstance);
            poweredSoundInstance = null;
            grindingSoundInstance = null;
        }
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        if (world != null && world.isClient()) {
            SoundManager soundManager = MinecraftClient.getInstance().getSoundManager();
            soundManager.stop(poweredSoundInstance);
            soundManager.stop(grindingSoundInstance);
            poweredSoundInstance = null;
            grindingSoundInstance = null;
        }
    }

    @Override
    protected Box createRenderBoundingBox() {
        return new Box(pos).stretch(-1.5, -1.5, -1.5)
                .stretch(1.5, 1.5, 1.5);
    }
}
