package net.dandycorp.dccobblemon.compat.rei;

import com.cobblemon.mod.common.pokemon.Pokemon;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.dandycorp.dccobblemon.block.Blocks;
import net.dandycorp.dccobblemon.ui.PokemonComponent;
import net.dandycorp.dccobblemon.ui.PokemonRenderUtils;
import net.dandycorp.dccobblemon.util.TextUtils;
import net.dandycorp.dccobblemon.util.vendor.VendorItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

import static net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions.MOD_ID;

public class VendorREICategory implements DisplayCategory<VendorREIDisplay> {

    public static final CategoryIdentifier<VendorREIDisplay> VENDOR =
            CategoryIdentifier.of(MOD_ID, "vendor");
    private static final Identifier background_texture = new Identifier(MOD_ID, "textures/gui/vendor/vendor_REI.png");
    private static final Identifier arrow_texture = new Identifier(MOD_ID, "textures/gui/vendor/vendor_REI_arrow.png");

    @Override
    public CategoryIdentifier<? extends VendorREIDisplay> getCategoryIdentifier() {
        return VENDOR;
    }

    @Override
    public Text getTitle() {
        return Text.translatable("block.dccobblemon.vendor");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(Blocks.VENDOR_BLOCK);
    }

    @Override
    public List<Widget> setupDisplay(VendorREIDisplay display, Rectangle bounds) {
        final Point startPoint = new Point(bounds.getCenterX() - 87, bounds.getCenterY() - 35);
        List<Widget> widgets = new ArrayList<>();
        widgets.add(Widgets.createTexturedWidget(background_texture,bounds,150,66,150,66));

        widgets.add(Widgets.createDrawableWidget((drawContext, mouseX, mouseY, delta) -> {
            float scale = 0.8f;
            drawContext.getMatrices().push();
            int x = bounds.getCenterX();
            int y = startPoint.y + 7;
            drawContext.getMatrices().translate(x, y, 0);
            drawContext.getMatrices().scale(scale, scale, 1.0f);
            drawContext.drawCenteredTextWithShadow(
                    MinecraftClient.getInstance().textRenderer,
                    Text.literal(display.getCategory().getName().toUpperCase()).formatted(Formatting.BOLD),
                    0,
                    0,
                    0xFF00FF00
            );
            drawContext.getMatrices().pop();
        }));

        widgets.add(Widgets.withTooltip(Widgets.createLabel(new Point(bounds.getCenterX(), startPoint.y + 17),
                        Text.literal(display.getEntry().getTitle()))
                .color(Formatting.DARK_GREEN.getColorValue())
                .noShadow()
                .centered(),
                Text.of(TextUtils.wrapString(display.getEntry().getDescription(),50))));


        widgets.add(Widgets.createSlot(new Point(bounds.getCenterX() - 47,bounds.getY() + 35))
                .entry(display.getInputEntries().get(0).get(0))
                .disableHighlight()
                .disableBackground()
                .markInput());

        widgets.add(Widgets.createDrawableWidget((drawContext, mouseX, mouseY, delta) -> {
            drawContext.drawTexture(arrow_texture, bounds.getCenterX()-12, bounds.getY()+30, 0, 0, 24, 24, 24, 24);
        }));


        int itemRenderX = bounds.getCenterX() + 30;
        int itemRenderY = bounds.getY() + 35;
        int itemWidth = 16;
        int itemHeight = 16;

        widgets.add(Widgets.createDrawableWidget((drawContext, mouseX, mouseY, delta) -> {
            VendorItem currentItem = getCurrentVendorItem(display);
            if (currentItem != null) {

                if (currentItem.getId().startsWith("pokemon:")) {
                    String pokemonName = currentItem.getId().substring("pokemon:".length());
                    Pokemon pokemon = createPokemonFromName(pokemonName);
                    if (pokemon != null) {
                        MatrixStack matrices = drawContext.getMatrices();
                        drawContext.enableScissor(itemRenderX, itemRenderY, itemRenderX + itemWidth, itemRenderY + itemHeight);
                        PokemonRenderUtils.renderPokemon(pokemon, matrices, itemRenderX, itemRenderY, itemWidth, itemHeight, false, delta);
                        drawContext.disableScissor();
                    }
                } else {
                    ItemStack stack = getItemStackFromID(currentItem.getId());
                    if (stack != null && !stack.isEmpty()) {
                        drawContext.drawItem(stack, itemRenderX, itemRenderY);
                        if (currentItem.getQuantity() > 1) {
                            String countLabel = String.valueOf(currentItem.getQuantity());
                            MatrixStack matrices = drawContext.getMatrices();
                            matrices.push();
                            matrices.translate(0, 0, 200);
                            drawContext.drawText(
                                    MinecraftClient.getInstance().textRenderer,
                                    countLabel,
                                    itemRenderX + 16 - MinecraftClient.getInstance().textRenderer.getWidth(countLabel),
                                    itemRenderY + 9,
                                    0xFFFFFF,
                                    true
                            );
                            matrices.pop();
                        }
                    }
                }
            }
        }));

        widgets.add(Widgets.createTooltip(
                new Rectangle(itemRenderX, itemRenderY, itemWidth, itemHeight),
                display.getEntry().getItemsString()
        ));

        return widgets;
    }

    private VendorItem getCurrentVendorItem(VendorREIDisplay display) {
        List<VendorItem> items = display.getEntry().getItems();
        if (items.isEmpty()) {
            return null;
        }

        int index = (int) ((System.currentTimeMillis() / 2000) % items.size());
        return items.get(index);
    }

    private Pokemon createPokemonFromName(String pokemonName) {
        String[] tokens = pokemonName.split("_");
        return PokemonComponent.pokemonFromSplitString(tokens);
    }

    private ItemStack getItemStackFromID(String itemId) {
        try {
            Identifier id = new Identifier(itemId);
            Item item = Registries.ITEM.get(id);
            return new ItemStack(item);
        } catch (Exception e) {
            e.printStackTrace();
            return ItemStack.EMPTY;
        }
    }
}
