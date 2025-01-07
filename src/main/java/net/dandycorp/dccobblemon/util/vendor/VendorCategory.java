package net.dandycorp.dccobblemon.util.vendor;

import java.util.List;

public class VendorCategory {
    private String name;
    private List<VendorEntry> entries;

    // Default constructor
    public VendorCategory() {}

    // Getters and setters
    public String getName() {
        return name;
    }

    public List<VendorEntry> getEntries() {
        return entries;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEntries(List<VendorEntry> entries) {
        this.entries = entries;
    }
}
