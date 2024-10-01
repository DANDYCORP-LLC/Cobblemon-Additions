package net.dandycorp.dccobblemon.util;

import java.util.List;

public class VendorData {
    private List<VendorCategory> categories;

    // Default constructor
    public VendorData() {}

    // Getters and setters
    public List<VendorCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<VendorCategory> categories) {
        this.categories = categories;
    }
}
