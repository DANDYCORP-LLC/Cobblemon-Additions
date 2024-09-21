package net.dandycorp.dccobblemon.util;

import java.util.List;

public class VendorCategory {
    private String name;
    private List<VendorItem> items;

    // Default constructor
    public VendorCategory() {}

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<VendorItem> getItems() {
        return items;
    }

    public void setItems(List<VendorItem> items) {
        this.items = items;
    }
}