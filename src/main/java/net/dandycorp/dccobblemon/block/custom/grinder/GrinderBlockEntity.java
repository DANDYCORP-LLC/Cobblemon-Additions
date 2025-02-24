package net.dandycorp.dccobblemon.block.custom.grinder;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.item.SmartInventory;
import io.github.fabricators_of_create.porting_lib.transfer.TransferUtil;
import net.dandycorp.dccobblemon.DANDYCORPDamageTypes;
import net.dandycorp.dccobblemon.DANDYCORPSounds;
import net.dandycorp.dccobblemon.block.DANDYCORPBlocks;
import net.dandycorp.dccobblemon.block.custom.grinder.multiblock.GrinderOutputBlockEntity;
import net.dandycorp.dccobblemon.util.ScreenShakeController;
import net.dandycorp.dccobblemon.util.grinder.GrinderDataCache;
import net.dandycorp.dccobblemon.item.DANDYCORPItems;
import net.dandycorp.dccobblemon.sound.GrinderSoundScapes;
import net.dandycorp.dccobblemon.util.grinder.GrinderPointGenerator;
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
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;


import java.util.List;
import java.util.Map;

import static com.simibubi.create.content.kinetics.base.HorizontalKineticBlock.HORIZONTAL_FACING;
import static net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions.RANDOM;

public class GrinderBlockEntity extends KineticBlockEntity implements SidedStorageBlockEntity{

    protected SmartInventory input;
    protected SmartInventory output;
    protected GrinderInventoryHandler itemStorage;
    protected int processingTime;
    protected final int maxProcessingTime = 300;
    protected float points;
    protected int ticketPointValue = 2000;
    protected Box hurtBox;
    protected Random random;

    public GrinderBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        hurtBox = switch(state.get(HORIZONTAL_FACING)) {
            case EAST,WEST -> new Box(pos.getX()-0.3, pos.getY() + 1, pos.getZ()-0.9,
                pos.getX() + 1.3, pos.getY() + 1.6, pos.getZ() + 1.9);
            default -> new Box(pos.getX()-0.9, pos.getY() + 1, pos.getZ()-0.3,
                    pos.getX() + 1.9, pos.getY() + 1.6, pos.getZ() + 1.3);
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

        random = Random.create();
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

        if (world == null) return;

        ItemStack stack = input.getStackInSlot(0);

        if (!stack.isEmpty() && isSpeedRequirementFulfilled() && world.isClient()) {
            switch (this.getCachedState().get(HORIZONTAL_FACING)) {
                case EAST, WEST -> createParticles(stack, 0.3f, -0.5f, 0.7f, 1.5f);
                default -> createParticles(stack, -0.5f, 0.3f, 1.5f, 0.7f);
            }
        }

        if (getSpeed() == 0 || !isSpeedRequirementFulfilled()) return;
        if (processingTime < 0) processingTime = 0;

        if (!stack.isEmpty()) {
            processingTime += (int) (Math.abs(getSpeed()) / 16);
            if (processingTime > maxProcessingTime) {
                processingTime = 0;
                handlePoints(stack);
                // Extract item from input slot
                try (Transaction t = TransferUtil.getTransaction()) {
                    for (StorageView<ItemVariant> view : input.nonEmptyViews()) {
                        view.extract(view.getResource(), 1, t);
                    }
                    input.markDirty();
                    t.commit();
                }
                // Play sound
                world.playSound(null, pos, DANDYCORPSounds.GRINDER_GRIND_EVENT, SoundCategory.BLOCKS,
                        0.1f, MathHelper.clamp((0.4f + (Math.abs(getSpeed()) / 170)), 0.4f, 1.8f) + RANDOM.nextFloat(0.0f, 0.4f));
            }
        } else processingTime = 0;

        applyEntityDamage();
    }

    private void handlePoints(ItemStack input){
        if(world == null) return;

        Map<Item, Float> calculatedPointValues;
        if(world.isClient())
            calculatedPointValues = GrinderDataCache.getCalculatedPointValues();
        else
            calculatedPointValues = GrinderPointGenerator.getCalculatedValues();

        Item item = input.getItem();

        if (item.equals(DANDYCORPBlocks.VENDOR_BLOCK.asItem())) {
            if (getCachedState().getBlock() instanceof GrinderBlock gb){
                gb.deconstruct(world,pos,getCachedState(),null,false);
                if (world == null) return;
                LightningEntity lightning = EntityType.LIGHTNING_BOLT.create(world);
                if (lightning != null) {
                    Vec3d lightningPos = Vec3d.ofBottomCenter(pos);
                    lightning.refreshPositionAfterTeleport(lightningPos);
                    world.spawnEntity(lightning);
                }
                world.createExplosion(null,pos.getX(),pos.getY(),pos.getZ(),20, World.ExplosionSourceType.BLOCK);
                for (PlayerEntity player : world.getPlayers()){
                    if (player.squaredDistanceTo(pos.getX(),pos.getY(),pos.getZ()) <= 40 * 40) {
                        float distance = (float) (player.squaredDistanceTo(pos.getX(),pos.getY(),pos.getZ()) / (40 * 40));
                        System.out.println("distance: " + distance + "\nintensity: " + 1.2f*(1-distance));
                        ScreenShakeController.startShake(1.2f * (1-distance),40,120, ScreenShakeController.FadeType.REVERSE_EXPONENTIAL);
                    }
                }
            }
        }

        Float increment = calculatedPointValues.get(item);

        if (increment == null || increment == 0f) {
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
        }

        if (input.isDamageable()){
            increment *= 1 - ((float) input.getDamage() / input.getMaxDamage());
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
                ItemStack ticket = DANDYCORPItems.TICKET.getDefaultStack();
                ticket.setCount(toPrint);
                output.setStackInSlot(0, ticket);
            } else if (output.getStackInSlot(0).isOf(DANDYCORPItems.TICKET)) {
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
    public void tickAudio() {
        if(world == null) return;
        super.tickAudio();
        float componentSpeed = Math.abs(getSpeed());
        float pitch = MathHelper.clamp((0.5f + componentSpeed / 170f), 0.5f, 1.85f);

        if (componentSpeed == 0 || !isSpeedRequirementFulfilled()) {
            stopSounds();
            return;
        }

        // Play passive sound
        GrinderSoundScapes.play(GrinderSoundScapes.AmbienceGroup.GRINDER_PASSIVE, pos, pitch);

        // Play active sound if necessary
        if (!input.isEmpty() && !input.getStackInSlot(0).isEmpty()) {
            GrinderSoundScapes.play(GrinderSoundScapes.AmbienceGroup.GRINDER_ACTIVE, pos, pitch);
        } else {
            GrinderSoundScapes.stop(GrinderSoundScapes.AmbienceGroup.GRINDER_ACTIVE);
        }
    }

    @Environment(EnvType.CLIENT)
    private void stopSounds() {
        GrinderSoundScapes.stop(GrinderSoundScapes.AmbienceGroup.GRINDER_PASSIVE);
        GrinderSoundScapes.stop(GrinderSoundScapes.AmbienceGroup.GRINDER_ACTIVE);
    }

    @Override
    public void onSpeedChanged(float previousSpeed) {
        super.onSpeedChanged(previousSpeed);
        if(!isSpeedRequirementFulfilled() && world.isClient()){
            stopSounds();
        }
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

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        if(world.isClient())
            stopSounds();
    }

    @Override
    public void remove() {
        super.remove();
        if(world.isClient())
            stopSounds();
    }

    @Override
    public void markDirty() {
        super.markDirty();
        input.markDirty();
        output.markDirty();
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
