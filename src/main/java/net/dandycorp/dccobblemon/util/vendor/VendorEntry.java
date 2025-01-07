package net.dandycorp.dccobblemon.util.vendor;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class VendorEntry {
    private int buttonID;
    private List<VendorItem> items;
    private int cost;
    private String title;
    private String description;

    // Default constructor
    public VendorEntry() {}

    // Getters and setters
    public int getButtonID() {
        return buttonID;
    }

    public void setButtonID(int buttonID) {
        this.buttonID = buttonID;
    }

    public List<VendorItem> getItems() {
        return items;
    }

    public List<Text> getItemsString() {
        List<Text> itemsText = new ArrayList<>();
        itemsText.add(Text.of(Formatting.BOLD + getTitle()));

        for (VendorItem item : items) {
            String id = item.getId();
            int quantity = item.getQuantity();

            if (id.startsWith("pokemon:")) {
                String text = id.substring("pokemon:".length());
                String[] tokens = text.split("_");
                StringBuilder displayNameBuilder = new StringBuilder();
                String pokemonSpecies = "";

                for (String token : tokens) {
                    if (token.equalsIgnoreCase("male") || token.equalsIgnoreCase("female")) {
                        displayNameBuilder.append(StringUtils.capitalize(token)).append(" ");
                    } else if (token.equalsIgnoreCase("shiny")) {
                        displayNameBuilder.append("Shiny ");
                    } else if (token.endsWith("nature")) {
                        String nature = token.substring(0, token.length() - "nature".length());
                        displayNameBuilder.append(StringUtils.capitalize(nature)).append(" Nature").append(" ");
                    } else if (token.matches("\\d+iv")) {
                        displayNameBuilder.append(token.replace("iv", "IV")).append(" ");
                    } else {
                        // Assume it's the Pokémon species
                        pokemonSpecies = StringUtils.capitalize(token);
                    }
                }

                if (!pokemonSpecies.isEmpty()) {
                    displayNameBuilder.append(pokemonSpecies);
                }

                String displayName = displayNameBuilder.toString().trim() + " x" + quantity;
                itemsText.add(Text.literal(displayName));
            } else {
                // Handle items
                ItemStack itemStack = getItemStackFromID(id, quantity);
                if (!itemStack.isEmpty()) {
                    String itemName = itemStack.getName().getString();
                    String combined = itemName + " x" + quantity;
                    itemsText.add(Text.literal(combined));
                } else {
                    itemsText.add(Text.literal("Unknown Item x" + quantity));
                }
            }
        }

        return itemsText;
    }

    private ItemStack getItemStackFromID(String itemId, int quantity) {
        try {
            Identifier id = new Identifier(itemId);
            Item item = Registries.ITEM.get(id);
            return new ItemStack(item, quantity);
        } catch (Exception e) {
            e.printStackTrace();
            return ItemStack.EMPTY;
        }
    }

    public void setItems(List<VendorItem> items) {
        this.items = items;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
