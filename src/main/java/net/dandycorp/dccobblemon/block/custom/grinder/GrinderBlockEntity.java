package net.dandycorp.dccobblemon.block.custom.grinder;

import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.item.SmartInventory;
import io.github.fabricators_of_create.porting_lib.transfer.TransferUtil;
import net.dandycorp.dccobblemon.DANDYCORPDamageTypes;
import net.dandycorp.dccobblemon.DANDYCORPSounds;
import net.dandycorp.dccobblemon.block.Blocks;
import net.dandycorp.dccobblemon.block.custom.grinder.multiblock.GrinderInputBlockEntity;
import net.dandycorp.dccobblemon.block.custom.grinder.multiblock.GrinderOutputBlockEntity;
import net.dandycorp.dccobblemon.item.Items;
import net.dandycorp.dccobblemon.util.GrinderPointGenerator;
import net.dandycorp.dccobblemon.util.LoopingSoundInstance;
import net.dandycorp.dccobblemon.util.TextUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.nbt.scanner.NbtScanner;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;


import java.util.List;
import java.util.Map;

import static com.simibubi.create.content.kinetics.base.HorizontalKineticBlock.HORIZONTAL_FACING;
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
        hurtBox = switch(state.get(HORIZONTAL_FACING)) {
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

    @Override
    public void tick() {
        super.tick();

        if (getSpeed() == 0 || !isSpeedRequirementFulfilled()) return;
        if (world == null) return;
        if (processingTime < 0) processingTime = 0;
        ItemStack stack = input.getStackInSlot(0);

        //System.out.println(input.getStackInSlot(0));
        if (!stack.isEmpty()) {
            //System.out.println("not empty. process time = " + processingTime);
            processingTime += (int) (Math.abs(getSpeed()) / 16);
            if (processingTime > maxProcessingTime) {
                processingTime = 0;
                handlePoints(stack);
                try (Transaction t = TransferUtil.getTransaction()) {
                    for (StorageView<ItemVariant> view : input.nonEmptyViews()) {
                        view.extract(view.getResource(), 1, t);
                        // extract exactly 1 of whatever is in the inventory
                    }
                    input.markDirty();
                    world.playSound(null,
                            pos, DANDYCORPSounds.GRINDER_GRIND_EVENT,
                            SoundCategory.BLOCKS,
                            0.1f,
                            MathHelper.clamp((0.4f + (Math.abs(getSpeed()) / 170)), 0.4f, 1.8f) + RANDOM.nextFloat(0.0f, 0.4f));
                    t.commit();
                }
            }
        } else processingTime = 0;
        applyEntityDamage();
    }

    private void handlePoints(ItemStack input){
        if(world == null) return;
        Item item = input.getItem();

        if (item.equals(Blocks.VENDOR_BLOCK.asItem())) {
            if (getCachedState().getBlock() instanceof GrinderBlock gb){
                gb.deconstruct(world,pos,getCachedState(),null,false);
                if (world == null) return;
                LightningEntity lightning = EntityType.LIGHTNING_BOLT.create(world);
                if (lightning != null) {
                    Vec3d lightningPos = Vec3d.ofBottomCenter(pos);
                    lightning.refreshPositionAfterTeleport(lightningPos);
                    world.spawnEntity(lightning);
                }
                world.createExplosion(null,pos.getX(),pos.getY(),pos.getZ(),22, World.ExplosionSourceType.BLOCK);
            }
        }

        if (!GrinderPointGenerator.calculatedPointValues.containsKey(item) || GrinderPointGenerator.calculatedPointValues.get(item) == 0) {
            System.out.println("handlePoints: No point value defined for item " + item.getName().getString());
            for (int i = 0; i < 5; i++){
                world.addImportantParticle(ParticleTypes.LARGE_SMOKE,
                        pos.getX() + RANDOM.nextDouble(0.3, 0.7),
                        pos.getY() + 1,
                        pos.getZ() + RANDOM.nextDouble(0.3, 0.7),
                        RANDOM.nextDouble(-0.1, 0.1),
                        0.1,
                        RANDOM.nextDouble(-0.1, 0.1));
                world.playSound(null,
                        pos,
                        SoundEvents.ITEM_BRUSH_BRUSHING_SAND,
                        SoundCategory.BLOCKS,
                        0.6f,
                        RANDOM.nextFloat(0.0f,0.6f));
            }
            return;
        }

        float increment = GrinderPointGenerator.calculatedPointValues.get(item);
        if (input.hasEnchantments()) {
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(input);
            for (Integer lvl : enchantments.values()) {
                for (int i = 0; i < lvl; i++){
                    increment *= 1.05f;
                    world.addImportantParticle(ParticleTypes.ENCHANT,
                            pos.getX() + RANDOM.nextDouble(-0.8, 1.8),
                            pos.getY() + 6,
                            pos.getZ() + RANDOM.nextDouble(-0.8, 1.8),
                            RANDOM.nextDouble(-0.2, 0.2),
                            -4,
                            RANDOM.nextDouble(-0.2, 0.2));
                }
            }
            world.playSound(null,
                    pos,
                    SoundEvents.BLOCK_RESPAWN_ANCHOR_DEPLETE.comp_349(),
                    SoundCategory.BLOCKS,
                    0.4f,
                    RANDOM.nextFloat(1.4f,2.0f));
            //System.out.println("enchantments detected, points incremented by " + increment + " instead of " + GrinderPointGenerator.calculatedPointValues.get(item));
        }

        points = (float) (Math.floor((points + increment) * 1000) / 1000);
        if(points >= ticketPointValue) {
            int toPrint = 0;
            while (points >= ticketPointValue) {
                toPrint++;
                points =  (float) (Math.floor((points - ticketPointValue) * 1000) / 1000);
            }
            getOutputEntity().notifyUpdate();
            getOutputEntity().markDirty();
            if (output.getStackInSlot(0).isEmpty()) {
                ItemStack ticket = Items.TICKET.getDefaultStack();
                ticket.setCount(toPrint);
                output.setStackInSlot(0, ticket);
            } else if (output.getStackInSlot(0).isOf(Items.TICKET)) {
                output.getStackInSlot(0).increment(1);
            }
        }
    }

    @Override
    public boolean addToGoggleTooltip(List<Text> tooltip, boolean isPlayerSneaking) {
        if (addToTooltip(tooltip,isPlayerSneaking))
            tooltip.add(Text.literal(""));
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
            switch (this.getCachedState().get(HORIZONTAL_FACING)){
                case EAST, WEST -> createParticles(input.getStackInSlot(0),0.3f,-0.5f,0.7f,1.5f);
                default -> createParticles(input.getStackInSlot(0),-0.5f,0.3f,1.5f,0.7f);
            }

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

    private void createParticles(ItemStack item, float x, float z, float dx, float dz) {
        ItemStackParticleEffect particle = new ItemStackParticleEffect(ParticleTypes.ITEM, item);
        if (world == null) return;
        world.addParticle(ParticleTypes.ELECTRIC_SPARK,
                pos.getX() + RANDOM.nextDouble(x, dx),
                pos.getY() + 1,
                pos.getZ() + RANDOM.nextDouble(z, dz),
                RANDOM.nextDouble(-0.7, 0.7),
                MathHelper.clamp((Math.abs(getSpeed())/48f),0.3,5.0),
                RANDOM.nextDouble(-0.7, 0.7));
        for(int i = 0; i < 2; i++)
            world.addParticle(particle,
                    pos.getX() + RANDOM.nextDouble(x, dx),
                    pos.getY() + 1,
                    pos.getZ() + RANDOM.nextDouble(z, dz),
                    RANDOM.nextDouble(-0.1, 0.1),
                    MathHelper.clamp((Math.abs(getSpeed())/300f),0.3,0.5),
                    RANDOM.nextDouble(-0.1, 0.1));
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

    public GrinderOutputBlockEntity getOutputEntity() {
        if (world.getBlockEntity(pos.offset(getCachedState().get(HORIZONTAL_FACING).getOpposite())) instanceof GrinderOutputBlockEntity out) {
            return out;
        }
        return null;
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
            return inserted;
        }

        @Override
        public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
            long extracted = output.extract(resource, maxAmount, transaction);
            return extracted;
        }
    }
}
