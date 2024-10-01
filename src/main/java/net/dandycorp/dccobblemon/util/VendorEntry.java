package net.dandycorp.dccobblemon.util;

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
