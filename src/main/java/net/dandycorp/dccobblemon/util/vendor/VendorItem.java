package net.dandycorp.dccobblemon.util.vendor;

public class VendorItem {
    private String id;
    private int quantity;

    // Default constructor
    public VendorItem() {}

    // Getters and setters
    public String getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
