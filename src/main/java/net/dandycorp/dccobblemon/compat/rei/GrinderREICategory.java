package net.dandycorp.dccobblemon.compat.rei;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.dandycorp.dccobblemon.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.LinkedList;
import java.util.List;

import static net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions.MOD_ID;

public class GrinderREICategory implements DisplayCategory<BasicDisplay> {

    public static final CategoryIdentifier<GrinderREIDisplay> GRINDING =
            CategoryIdentifier.of(MOD_ID, "grinding");

    @Override
    public CategoryIdentifier<? extends BasicDisplay> getCategoryIdentifier() {
        return GRINDING;
    }

    @Override
    public List<Widget> setupDisplay(BasicDisplay display, Rectangle bounds) {
        final Point startPoint = new Point(bounds.getCenterX() - 87, bounds.getCenterY() - 35);
        List<Widget> widgets = new LinkedList<>();

        widgets.add(Widgets.createRecipeBase(bounds));

        widgets.add(Widgets.createSlot(new Point(bounds.x+21, startPoint.y+19))
                .entries(display.getInputEntries().get(0))
                .markInput());
        widgets.add(Widgets.createSlot(new Point(bounds.getX()+112, startPoint.y+19))
                .entries(display.getOutputEntries().get(0))
                .markOutput());
        widgets.add(Widgets.createArrow(new Point(bounds.getCenterX()-12, startPoint.y+19)));
        if(display instanceof GrinderREIDisplay gd) {
            widgets.add(Widgets.createLabel(new Point(bounds.getCenterX(), startPoint.y + 7),
                            Text.literal(String.format("Points: " + Formatting.BLACK + "%.3f", gd.getPointValue())))
                            .color(Formatting.DARK_GRAY.getColorValue())
                            .noShadow()
                            .centered());
        }

        widgets.add(Widgets.createDrawableWidget((drawContext, mouseX, mouseY, delta) -> {
            float scale = 2.2f;
            int scaledWidth = (int) (16 * scale);
            int scaledHeight = (int) (16 * scale);
            int x = bounds.getCenterX() - (scaledWidth / 2);
            int y = bounds.getCenterY() - (scaledHeight / 2) + 15;

            drawContext.getMatrices().push();
            drawContext.getMatrices().translate(x, y, 100); // Increase Z to render above other elements
            drawContext.getMatrices().scale(scale, scale, 1.0f);

            // Render the item directly
            ItemStack stack = Blocks.GRINDER_BLOCK.asItem().getDefaultStack();
            drawContext.drawItem(stack, 0, 0);
            drawContext.getMatrices().pop();
        }));
        return widgets;
    }


    @Override
    public Text getTitle() {
        return Text.translatable("block.dccobblemon.grinder");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(Blocks.GRINDER_BLOCK);
    }
}
