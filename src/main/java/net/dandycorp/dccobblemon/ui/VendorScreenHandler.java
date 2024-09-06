package net.dandycorp.dccobblemon.ui;

import io.wispforest.owo.client.screens.OwoScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class VendorScreenHandler extends ScreenHandler implements OwoScreenHandler {

    public PlayerEntity player;
    public BlockPos pos;
    public BlockState state;
    public World world;

    public VendorScreenHandler(int syncId, PlayerEntity player, BlockPos pos, BlockState state) {
        super(ScreenHandlers.VENDOR_SCREEN_HANDLER, syncId);
        this.player = player;
        this.pos = pos;
        this.state = state;
        this.world = player.getWorld();
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    private void purchase(BlockPos pos, PlayerEntity player, @Nullable ItemStack item, int cost, int count) {
        if(player.getWorld().isClient()) return;
        if(canAfford(player,cost)){
            spendTickets(player, cost);
            if(item != null) {

                ServerWorld world = (ServerWorld) player.getWorld();

                for (int i = 0; i < cost; i++) {

                    ItemEntity ie = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), item);

                    double dx = player.getX() - pos.getX();
                    double dy = player.getY() + player.getStandingEyeHeight() - pos.getY(); // Adjust for player's height
                    double dz = player.getZ() - pos.getZ();

                    double speed = 0.2; // Adjust this value for the desired speed
                    double length = Math.sqrt(dx * dx + dy * dy + dz * dz);

                    ie.setVelocity(dx / length * speed, dy / length * speed, dz / length * speed);

                    world.spawnEntity(ie);

                }
            }
        }
    }

    private void spendTickets(PlayerEntity player, int cost) {
    }

    private boolean canAfford(PlayerEntity player, int cost) {
        return false;
    }


}
