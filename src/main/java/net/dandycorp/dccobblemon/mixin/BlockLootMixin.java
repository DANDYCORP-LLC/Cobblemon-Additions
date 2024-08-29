package net.dandycorp.dccobblemon.mixin;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketsApi;
import net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions;
import net.dandycorp.dccobblemon.DANDYCORPDamageTypes;
import net.dandycorp.dccobblemon.item.Items;
import net.dandycorp.dccobblemon.item.custom.BadgeItem;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBlockTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static net.minecraft.block.Block.dropStack;
import static net.minecraft.block.Block.getDroppedStacks;

@Mixin(Block.class)
public class BlockLootMixin {

    @Inject(method="dropStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)V",
            at=@At("TAIL"))
    private static void dropStacks(BlockState state, World world, BlockPos pos, BlockEntity blockEntity, Entity entity, ItemStack tool, CallbackInfo ci){
        if(entity instanceof LivingEntity livingEntity && world instanceof ServerWorld serverWorld){
            TrinketsApi.getTrinketComponent(livingEntity).ifPresent(trinketComponent -> {
                // Get all non-empty equipped trinkets
                List<Pair<SlotReference, ItemStack>> equippedTrinkets = trinketComponent.getAllEquipped();

                // Process each equipped trinket to check for badges
                equippedTrinkets.stream()
                        .map(Pair::getRight) // Get the ItemStack from the pair
                        .map(ItemStack::getItem) // Get the Item from the ItemStack
                        .filter(item -> item instanceof BadgeItem) // Filter for BadgeItems
                        .forEach(item -> {
                            if (item == Items.DANDY_BADGE) {
                                if(state.isIn(ConventionalBlockTags.ORES)){
                                    getDroppedStacks(state, serverWorld, pos, blockEntity, entity, tool).forEach(stack -> dropStack(serverWorld, pos, stack));
                                    state.onStacksDropped(serverWorld, pos, tool, true);
                                    serverWorld.spawnParticles(ParticleTypes.WITCH, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 6, 0.5, 0.6, 0.5,10);
                                    serverWorld.playSound(null,pos, DANDYCORPCobblemonAdditions.GREED_EVENT, SoundCategory.PLAYERS,1f, (float) (-0.5 + Math.random()));
                                    livingEntity.damage(DANDYCORPDamageTypes.of(serverWorld,DANDYCORPDamageTypes.DANDY_BADGE), 2);
                                    livingEntity.hurtTime = 0;
                                }
                            }
                            else if (item == Items.LINA_BADGE) {
                                if(state.getBlock() instanceof CropBlock cropBlock) {
                                    if (cropBlock.isMature(state)) {
                                        if(Math.random() > 0.33){
                                            getDroppedStacks(state, serverWorld, pos, blockEntity, entity, tool).forEach(stack -> dropStack(serverWorld, pos, stack));
                                            serverWorld.spawnParticles(ParticleTypes.WAX_OFF, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 3, 0.2, 0.6, 0.2,8);
                                            //entity.sendMessage(Text.literal("doubled"));
                                        }
                                    }
                                }
                                else if(state.isIn(BlockTags.LEAVES)){
                                    getDroppedStacks(state, serverWorld, pos, blockEntity, entity, tool).forEach(stack -> dropStack(serverWorld, pos, stack));
                                    //entity.sendMessage(Text.literal("doubled"));
                                }
                            }
                        });
            });
        }
    }
}
