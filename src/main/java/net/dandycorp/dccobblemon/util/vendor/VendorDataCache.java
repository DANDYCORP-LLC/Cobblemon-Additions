package net.dandycorp.dccobblemon.util.vendor;

public class VendorDataCache {
    private static VendorData vendorData;

    public static void setVendorData(VendorData data) {
        vendorData = data;
    }

    public static VendorData getVendorData() {
        return vendorData;
    }
}

