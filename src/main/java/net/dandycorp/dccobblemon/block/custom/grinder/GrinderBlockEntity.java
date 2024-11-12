package net.dandycorp.dccobblemon.block.custom.grinder;

import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.item.SmartInventory;
import net.dandycorp.dccobblemon.DANDYCORPDamageTypes;
import net.dandycorp.dccobblemon.DANDYCORPSounds;
import net.dandycorp.dccobblemon.item.Items;
import net.dandycorp.dccobblemon.util.GrinderPointGenerator;
import net.dandycorp.dccobblemon.util.LoopingSoundInstance;
import net.dandycorp.dccobblemon.util.TextUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtFloat;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.*;
import org.jetbrains.annotations.Nullable;


import java.util.List;

import static com.simibubi.create.content.kinetics.base.DirectionalKineticBlock.FACING;
import static net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions.RANDOM;
import static net.dandycorp.dccobblemon.block.custom.grinder.multiblock.MultiblockPartBlock.CORE_FACING;

public class GrinderBlockEntity extends KineticBlockEntity implements SidedStorageBlockEntity{

    protected SmartInventory input;
    protected SmartInventory output;
    protected GrinderInventoryHandler itemStorage;
    protected int processingTime;
    protected final int maxProcessingTime = 300;
    protected float points;
    protected int ticketPointValue = 2000;
    protected Box hurtBox;

    @Nullable
    private LoopingSoundInstance poweredSoundInstance;
    @Nullable
    private LoopingSoundInstance grindingSoundInstance;

    public GrinderBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        hurtBox = switch(state.get(FACING)) {
            case EAST,WEST -> new Box(pos.getX()-0.4, pos.getY() + 1, pos.getZ()-1,
                pos.getX() + 1.4, pos.getY() + 1.6, pos.getZ() + 2);
            default -> new Box(pos.getX()-1, pos.getY() + 1, pos.getZ()-0.4,
                    pos.getX() + 2, pos.getY() + 1.6, pos.getZ() + 1.4);
        };
        input = new SmartInventory(1, this)
                .allowInsertion()
                .forbidExtraction()
                .whenContentsChanged(this::onContentsChanged)
                .withMaxStackSize(64);

        output = new SmartInventory(1, this)
                .forbidInsertion()
                .allowExtraction()
                .whenContentsChanged(this::onContentsChanged)
                .withMaxStackSize(64);

        itemStorage = new GrinderInventoryHandler();
        //capability = new GrinderInventoryHandler();
    }

    private void onContentsChanged() {
        this.markDirty();
        this.sendData();
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
        compound.put("Points", NbtFloat.of(points));
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(NbtCompound compound, boolean clientPacket) {
        input.deserializeNBT(compound.getCompound("InputInventory"));
        output.deserializeNBT(compound.getCompound("OutputInventory"));
        points = compound.getFloat("Points");
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
                        0.1f,
                        MathHelper.clamp((0.4f+(Math.abs(getSpeed())/170)), 0.4f, 1.8f) + RANDOM.nextFloat(0.0f, 0.4f));
                handlePoints(input.getStackInSlot(0).getItem());
            }
        } else processingTime = 0;
        applyEntityDamage();
    }

    private void handlePoints(Item input){
        if(world == null) return;

        float increment = GrinderPointGenerator.calculatedPointValues.get(input);
        points = (float) (Math.floor((points + increment) * 1000) / 1000);

        System.out.println("points: " + points + " increment: " + increment);
        if(points >= ticketPointValue) {
            points =  (float) (Math.floor((points - ticketPointValue) * 1000) / 1000);
            if (output.getStackInSlot(0).isEmpty()) {
                output.setStackInSlot(0, Items.TICKET.getDefaultStack());
            } else if (output.getStackInSlot(0).isOf(Items.TICKET)) {
                output.getStackInSlot(0).increment(1);
            }
        }
    }

    @Override
    public boolean addToGoggleTooltip(List<Text> tooltip, boolean isPlayerSneaking) {
        super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        tooltip.add(Text.literal(""));
        tooltip.add(Text.literal(spacing).append(Text.translatable("block.dccobblemon.grinder.progress").formatted(Formatting.GRAY)));
        tooltip.add(Text.literal(spacing).append(TextUtils.progressBar(points / ticketPointValue)));
        if (isPlayerSneaking) {
            tooltip.add(Text.literal(spacing).append(Text.of(points + " / " + ticketPointValue)));
        }
        return true;
    }


    @Override
    public void lazyTick() {
        super.lazyTick();
        if (world == null) return;

        List<ItemEntity> items = world.getEntitiesByClass(ItemEntity.class, hurtBox, ItemEntity::isAlive);

        if (!items.isEmpty()) {
            Storage<ItemVariant> inputStorage = this.getItemStorage(null);
            if (inputStorage != null) {
                ItemEntity itemEntity = items.get(0);
                ItemStack stack = itemEntity.getStack();
                long inserted = 0;
                try (Transaction transaction = Transaction.openOuter()) {
                    inserted = inputStorage.insert(ItemVariant.of(stack), stack.getCount(), transaction);
                    transaction.commit();
                }
                if (inserted == stack.getCount()) {
                    itemEntity.discard();
                } else if (inserted > 0) {
                    stack.decrement((int) inserted);
                    itemEntity.setStack(stack);
                }
            }
        }
    }

    private void applyEntityDamage() {
        List<Entity> entities = world.getEntitiesByClass(Entity.class, hurtBox, entity -> entity instanceof LivingEntity);

            for (Entity entity : entities) {
                if (entity instanceof LivingEntity livingEntity) {
                    livingEntity.damage(DANDYCORPDamageTypes.of(world, DANDYCORPDamageTypes.GRINDER), (Math.abs(getSpeed())/32f));
                }
            }
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
                        0.2f, // Volume
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
                        0.3f, // Volume
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

    public SmartInventory getInputStorage() {
        return input;
    }

    public SmartInventory getOutputStorage() {
        return output;
    }

    @Nullable
    @Override
    public Storage<ItemVariant> getItemStorage(@Nullable Direction direction) {
        return itemStorage;
    }

    private class GrinderInventoryHandler extends CombinedStorage<ItemVariant, Storage<ItemVariant>> {

        public GrinderInventoryHandler() {
            super(List.of(input, output));
        }

        @Override
        public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction) {
            long inserted = input.insert(resource, maxAmount, transaction);
            System.out.println("GrinderInventoryHandler: Attempted to insert " + maxAmount + " of " + resource.getItem().getName().getString() + ", inserted: " + inserted +
                    "\n current inventory: " + input.getStackInSlot(0) + " empty? " + input.isEmpty());
            return inserted;
        }

        @Override
        public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
            System.out.println("attempting extraction");
            return output.extract(resource, maxAmount, transaction);
        }
    }
}
