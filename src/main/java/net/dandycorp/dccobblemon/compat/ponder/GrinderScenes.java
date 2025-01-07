package net.dandycorp.dccobblemon.compat.ponder;

import com.cobblemon.mod.common.CobblemonItems;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.foundation.ponder.ElementLink;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import com.simibubi.create.foundation.ponder.element.EntityElement;
import com.simibubi.create.foundation.ponder.element.InputWindowElement;
import com.simibubi.create.foundation.ponder.instruction.EmitParticlesInstruction;
import com.simibubi.create.foundation.utility.Pointing;
import net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions;
import net.dandycorp.dccobblemon.block.Blocks;
import net.dandycorp.dccobblemon.util.grinder.GrinderPointGenerator;
import net.dandycorp.dccobblemon.util.TextUtils;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.List;
import java.util.Random;

public class GrinderScenes {

    private static final Random RANDOM = DANDYCORPCobblemonAdditions.RANDOM;
    private static final List<Item> VALUELESS_ITEMS = List.of(
            Items.EGG,
            Items.INK_SAC,
            Items.BOWL,
            Items.PUFFERFISH,
            Items.SNOWBALL,
            Items.FEATHER,
            Items.DISC_FRAGMENT_5,
            Items.BONE_BLOCK,
            Items.SNOW,
            Items.GRASS,
            Items.FERN,
            Items.OAK_SAPLING,
            Items.SPRUCE_SAPLING,
            Items.GLOW_LICHEN,
            Items.BEETROOT_SEEDS,
            Items.WHEAT_SEEDS,
            Items.PINK_PETALS,
            Items.DEAD_BRAIN_CORAL,
            Items.DEAD_BUSH,
            Items.DEAD_BUBBLE_CORAL_BLOCK,
            Items.COBWEB
    );

    private static final List<Item> VALUABLE_ITEMS = List.of(
            Items.DIAMOND,
            Items.NETHERITE_INGOT,
            Items.EMERALD,
            Items.TOTEM_OF_UNDYING,
            Items.NETHER_STAR,
            Items.HEART_OF_THE_SEA,
            Items.DRAGON_EGG,
            Items.ELYTRA,
            Items.GHAST_TEAR,
            Items.END_CRYSTAL,
            Items.BEACON,
            Items.IRON_CHESTPLATE,
            Items.GOLDEN_LEGGINGS,
            Items.DIAMOND_HELMET,
            Items.IRON_PICKAXE,
            Items.GOLDEN_SWORD,
            Items.DIAMOND_BOOTS,
            Items.IRON_AXE,
            Items.GOLDEN_SHOVEL,
            Items.DIAMOND_CHESTPLATE,
            Items.IRON_SWORD,
            Items.GOLDEN_BOOTS,
            Items.DIAMOND_AXE,
            AllBlocks.COPPER_CASING.asItem(),
            AllBlocks.FLYWHEEL.asItem(),
            AllBlocks.ROTATION_SPEED_CONTROLLER.asItem(),
            AllItems.BRASS_INGOT.get(),
            AllItems.CHROMATIC_COMPOUND.get(),
            AllItems.WRENCH.get(),
            AllItems.GOGGLES.get(),
            AllItems.SUPER_GLUE.get(),
            AllItems.SCHEMATIC.get(),
            CobblemonItems.POKE_BALL,
            CobblemonItems.ULTRA_BALL,
            CobblemonItems.MASTER_BALL,
            CobblemonItems.RARE_CANDY,
            CobblemonItems.EXP_SHARE
    );



    public static void grind(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("grinder_grind", "The DANDYCORP Industrial Grinder™");
        scene.configureBasePlate(0,1,7);
        scene.showBasePlate();
        scene.idle(2);
        scene.world.showSection(util.select.fromTo(7,0,0,7,1,6), Direction.WEST);
        scene.idle(2);
        scene.world.showSection(util.select.fromTo(0,0,0,7,1,0), Direction.SOUTH);
        scene.idle(2);
        scene.world.showSection(util.select.fromTo(0,0,8,7,1,8), Direction.NORTH);
        scene.idle(8);
        scene.world.showSection(util.select.fromTo(2,1,4,4,3,6), Direction.DOWN);
        scene.idle(12);
        scene.overlay.showText(70)
                .text("The DANDYCORP Industrial Grinder™ turns junk into tickets.")
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(util.vector.topOf(3, 1, 5));
        scene.idle(75);
        scene.world.showSection(util.select.fromTo(6,1,5,5,1,5), Direction.DOWN);
        scene.idle(5);
        scene.world.setKineticSpeed(util.select.position(7,0,4),8);
        scene.world.setKineticSpeed(util.select.fromTo(7,1,5,2,1,5),16);
        scene.idle(5);
        scene.overlay.showText(70)
                .text("The grinder requires at least 32RPM to operate.")
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(util.vector.topOf(3, 1, 5));
        scene.idle(40);
        scene.world.setKineticSpeed(util.select.position(7,0,4),-16);
        scene.world.setKineticSpeed(util.select.fromTo(7,1,5,2,1,5),32);
        scene.idle(45);
        scene.world.showSection(util.select.fromTo(5,1,4,5,1,3), Direction.DOWN);
        scene.world.showSection(util.select.position(4,1,3), Direction.DOWN);
        scene.world.setKineticSpeed(util.select.position(5,1,4),-32);
        scene.world.setKineticSpeed(util.select.fromTo(5,1,3,4,1,3),32);
        scene.idle(8);
        scene.world.showSection(util.select.fromTo(6,1,1,4,2,1), Direction.DOWN); // belt 1
        scene.idle(2);
        scene.world.showSection(util.select.fromTo(3,1,1,3,1,3), Direction.DOWN); // belt 2
        scene.idle(5);
        scene.overlay.showText(60)
                .text("Items can be input via the top of the grinder...")
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(util.vector.topOf(3, 2, 5));
        ElementLink<EntityElement> entity1 =
                scene.world.createItemEntity(util.vector.topOf(3, 4, 5), util.vector.of(0, 0.1, 0), Items.DIAMOND_CHESTPLATE.getDefaultStack());
        scene.idle(30);
        scene.world.modifyEntity(entity1,Entity::discard);
        scene.effects.emitParticles(util.vector.topOf(3, 2, 5),
                EmitParticlesInstruction.Emitter.withinBlockSpace(new ItemStackParticleEffect(ParticleTypes.ITEM, Items.DIAMOND_CHESTPLATE.getDefaultStack()), util.vector.of(0, 0.6, 0)),
                3,
                40);
        scene.idle(35);
        scene.overlay.showText(80)
                .text("...or belted directly through the front")
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(util.vector.of(3.5, 1.5, 4.5));
        scene.idle(10);
        scene.world.setKineticSpeed(util.select.position(5,0,0),-16);
        scene.world.setKineticSpeed(util.select.position(4,1,0),32);
        scene.world.setKineticSpeed(util.select.fromTo(5,1,1,4,1,1),32); // belt 1
        scene.world.setKineticSpeed(util.select.fromTo(3,1,1,3,1,3),32); // belt 2
        for(int i = 0; i < 7; i++) {
            scene.world.createItemOnBelt(util.grid.at(5, 1, 1), Direction.EAST, Items.DIAMOND_PICKAXE.getDefaultStack());
            scene.idle(15);
        }
        scene.idle(10);
        scene.rotateCameraY(-180);
        scene.idle(25);
        scene.world.setKineticSpeed(util.select.position(2,0,8),-16);
        scene.world.showSection(util.select.position(1,1,4),Direction.DOWN);
        scene.world.setKineticSpeed(util.select.position(2,1,5),32);
        scene.world.setKineticSpeed(util.select.position(3,1,8),32);
        scene.idle(5);
        scene.world.showSection(util.select.fromTo(3,1,7,1,2,7), Direction.DOWN); // belt 3
        scene.world.setKineticSpeed(util.select.fromTo(3,1,7,1,1,7),32);
        scene.idle(5);
        scene.world.showSection(util.select.fromTo(0,1,0,0,3,7), Direction.DOWN); // belt 4
        scene.world.setKineticSpeed(util.select.fromTo(0,1,0,0,1,7),-32);
        scene.idle(8);
        scene.overlay.showText(120)
                .text("Tickets can be automatically exported through the back")
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(util.vector.of(3.5, 1.5, 7));
        scene.idle(8);
        for(int i = 0; i < 3; i++) {
            scene.world.createItemOnBelt(util.grid.at(3, 1, 7), Direction.NORTH, net.dandycorp.dccobblemon.item.Items.TICKET.getDefaultStack());
            scene.idle(30);
        }
        scene.idle(70);
        scene.rotateCameraY(90);
        scene.idle(8);
        scene.overlay.showText(120)
                .text("* DANDYCORP is not liable for any injuries or casualties caused by improper use of The DANDYCORP Industrial Grinder™!")
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(util.vector.topOf(3, 2, 5));

        scene.idle(10);

        ElementLink<EntityElement> z = scene.world.createEntity(w -> {
            ZombieEntity zombie = EntityType.ZOMBIE.create(w);
            Vec3d v = util.vector.topOf(3, 1, 5);
            zombie.setPos(v.x, v.y, v.z);
            zombie.setYaw(zombie.prevYaw = 90);
            return zombie;
        });
        for (int i = 0; i < 5; i++) {
            scene.idle(15);
            scene.effects.emitParticles(util.vector.of(3, 2.5f, 5), EmitParticlesInstruction.Emitter.simple(ParticleTypes.DAMAGE_INDICATOR,util.vector.of(RANDOM.nextFloat(-0.5f,0.5f), 0.6, RANDOM.nextFloat(-0.5f,0.5f))), 10, 2);
        }
        scene.effects.emitParticles(util.vector.of(3, 2, 5), EmitParticlesInstruction.Emitter.simple(ParticleTypes.POOF,util.vector.of(RANDOM.nextFloat(-0.5f,0.5f), 0.6, RANDOM.nextFloat(-0.5f,0.5f))), 10, 1);
        scene.world.modifyEntity(z,Entity::discard);
    }

    public static void points(SceneBuilder scene, SceneBuildingUtil util){
        scene.title("grinder_points", "Grinder Point System");
        scene.configureBasePlate(0,0,7);
        scene.showBasePlate();
        scene.idle(2);
        scene.world.showSection(util.select.fromTo(7,0,4,7,1,5), Direction.WEST);
        scene.idle(2);
        scene.world.showSection(util.select.fromTo(0,0,7,7,1,7), Direction.NORTH);
        scene.idle(10);
        scene.world.showSection(util.select.fromTo(3,1,3,5,2,5), Direction.DOWN);
        scene.idle(20);
        scene.overlay.showText(220)
                .text("The faster the grinder spins, the faster its items are processed.")
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(util.vector.topOf(4, 1, 6));
        scene.idle(10);
        scene.world.showSection(util.select.fromTo(6,1,0,6,1,7), Direction.DOWN);
        scene.idle(8);
        scene.world.showSection(util.select.fromTo(4,1,0,5,2,2), Direction.DOWN);
        scene.idle(12);

        scene.world.setKineticSpeed(util.select.position(7,0,5),-24);
        scene.world.setKineticSpeed(util.select.fromTo(7,1,4,3,1,4),48);
        scene.world.setKineticSpeed(util.select.position(6,1,3),-48);
        scene.world.setKineticSpeed(util.select.fromTo(6,1,1,4,1,2),48);
        scene.idle(8);
        scene.world.createItemOnBelt(util.grid.at(4, 1, 1), Direction.NORTH, Items.GRANITE.getDefaultStack());
        scene.idle(130);

        scene.world.setKineticSpeed(util.select.position(7,0,5),-64);
        scene.world.setKineticSpeed(util.select.fromTo(7,1,4,3,1,4),128);
        scene.world.setKineticSpeed(util.select.position(6,1,3),-128);
        scene.world.setKineticSpeed(util.select.fromTo(6,1,1,4,1,2),128);
        scene.idle(8);
        scene.world.createItemOnBelt(util.grid.at(4, 1, 1), Direction.NORTH, Items.GRANITE.getDefaultStack());
        scene.idle(70);

        scene.world.setKineticSpeed(util.select.position(7,0,5),-128);
        scene.world.setKineticSpeed(util.select.fromTo(7,1,4,3,1,4),256);
        scene.world.setKineticSpeed(util.select.position(6,1,3),-256);
        scene.world.setKineticSpeed(util.select.fromTo(6,1,1,4,1,2),256);
        scene.idle(8);
        scene.world.createItemOnBelt(util.grid.at(4, 1, 1), Direction.NORTH, Items.GRANITE.getDefaultStack());
        scene.idle(40);

        scene.overlay.showText(80)
                .text("Each item added to the grinder has a unique point value.")
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(util.vector.topOf(4, 1, 6));

        for (int i = 0; i < 5; i++) {
            Item randomItem = getRandomValuableItem();
            scene.overlay.showControls(new InputWindowElement(util.vector.of(4.5, 3, 0.5), Pointing.DOWN).withItem(randomItem.getDefaultStack()),20);
            scene.overlay.showText(20)
                    .text(String.format("%.3f",GrinderPointGenerator.getPointValue(randomItem)))
                    .independent()
                    .placeNearTarget()
                    .pointAt(util.vector.of(4.5, 2, 0.5));
            scene.world.createItemOnBelt(util.grid.at(4, 1, 1), Direction.NORTH, randomItem.getDefaultStack());
            scene.idle(30);
        }
        scene.overlay.showText(80)
                .text("This value can be found by inspecting an item in your inventory [U]")
                .attachKeyFrame()
                .pointAt(util.vector.of(3, 2, 2))
                .placeNearTarget();
        for (int i = 0; i < 3; i++) {
            Item randomItem = getRandomValuableItem();
            scene.overlay.showControls(new InputWindowElement(util.vector.of(4.5, 3, 0.5), Pointing.DOWN).withItem(randomItem.getDefaultStack()),20);
            scene.overlay.showText(20)
                    .text(String.format("%.3f",GrinderPointGenerator.getPointValue(randomItem)))
                    .independent()
                    .placeNearTarget()
                    .pointAt(util.vector.of(4.5, 2, 0.5));
            scene.world.createItemOnBelt(util.grid.at(4, 1, 1), Direction.NORTH, randomItem.getDefaultStack());
            scene.idle(30);
        }
        scene.idle(30);

        scene.overlay.showText(80)
                .text("Not all items have a ticket value, though.")
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(util.vector.topOf(4, 1, 6));
        for (int i = 0; i < 3; i++) {
            Item randomItem = getRandomValuelessItem();
            scene.overlay.showControls(new InputWindowElement(util.vector.of(4.5, 3, 0.5), Pointing.DOWN).withItem(randomItem.getDefaultStack()),20);
            scene.overlay.showText(20)
                    .text(String.format("%.3f",0f))
                    .independent()
                    .placeNearTarget()
                    .pointAt(util.vector.of(4.5, 2, 0.5));
            scene.world.createItemOnBelt(util.grid.at(4, 1, 1), Direction.NORTH, randomItem.getDefaultStack());
            scene.idle(30);
        }
        scene.idle(30);

        ItemStack enchantedItem = EnchantmentHelper.enchant(net.minecraft.util.math.random.Random.create(),Items.DIAMOND_SWORD.getDefaultStack(),45,true);
        scene.overlay.showControls(new InputWindowElement(util.vector.of(4.5, 3, 0.5), Pointing.DOWN).withItem(enchantedItem),80);
        scene.overlay.showText(80)
                .text("An item that is enchanted has its value increased by 5% per enchant level.")
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(util.vector.topOf(4, 1, 6));
        for (int i = 0; i < 3; i++) {
            scene.world.createItemOnBelt(util.grid.at(4, 1, 1), Direction.NORTH, enchantedItem);
            scene.idle(30);
        }
        scene.idle(30);

        scene.overlay.showText(80)
                .text("Once the grinder reaches 2000 points, a ticket will be produced.")
                .attachKeyFrame()
                .pointAt(util.vector.of(4.5, 1, 6.5));
        scene.world.showSection(util.select.fromTo(0,1,6,5,2,6), Direction.DOWN);
        scene.world.setKineticSpeed(util.select.fromTo(0,1,6,5,2,6),32);
        scene.world.setKineticSpeed(util.select.position(4,1,7),32);
        scene.world.setKineticSpeed(util.select.position(3,0,7),-16);
        scene.idle(10);
        scene.overlay.showControls(new InputWindowElement(util.vector.of(4.5, 3, 6.5), Pointing.DOWN).withItem(net.dandycorp.dccobblemon.item.Items.TICKET.getDefaultStack()),40);
        scene.world.createItemOnBelt(util.grid.at(4, 1, 6), Direction.EAST, net.dandycorp.dccobblemon.item.Items.TICKET.getDefaultStack());
        scene.idle(80);
        scene.overlay.showText(180)
                .text("Goggles will show you how close your grinder is to producing a ticket.")
                .attachKeyFrame()
                .pointAt(util.vector.of(4.5, 1, 4.5));
        scene.overlay.showControls(new InputWindowElement(util.vector.of(4.5, 3, 4.5), Pointing.DOWN).withItem(AllItems.GOGGLES.asStack()),180);
        scene.world.createItemOnBelt(util.grid.at(4, 1, 1), Direction.NORTH, getRandomValuableItem().getDefaultStack());
        for (int i = 0; i < 6; i++) {
            scene.world.createItemOnBelt(util.grid.at(4, 1, 1), Direction.NORTH, getRandomValuableItem().getDefaultStack());
            scene.overlay.showText(30)
                    .text(TextUtils.progressBar(i/5f).getString())
                    .pointAt(util.vector.of(4.5, 2, 4.5))
                    .placeNearTarget();
            scene.idle(30);
            if(i == 5){
                scene.world.createItemOnBelt(util.grid.at(4, 1, 6), Direction.EAST, net.dandycorp.dccobblemon.item.Items.TICKET.getDefaultStack());
            }
        }
        scene.idle(30);
        scene.overlay.showControls(new InputWindowElement(util.vector.of(2, 4, 3), Pointing.DOWN).withItem(Blocks.VENDOR_BLOCK.asItem().getDefaultStack()),60);
        scene.overlay.showText(60)
                .text("Tickets can be spent at your local DANDYCORP Vendor™!")
                .attachKeyFrame()
                .pointAt(util.vector.of(1.5, 2, 2.5))
                .placeNearTarget();
        scene.world.showSection(util.select.fromTo(1,1,2,1,3,2), Direction.DOWN);
    }

    private static Item getRandomValuableItem() {
        return VALUABLE_ITEMS.get(RANDOM.nextInt(VALUELESS_ITEMS.size()));
    }

    private static Item getRandomValuelessItem() {
        return VALUELESS_ITEMS.get(RANDOM.nextInt(VALUELESS_ITEMS.size()));
    }
}
