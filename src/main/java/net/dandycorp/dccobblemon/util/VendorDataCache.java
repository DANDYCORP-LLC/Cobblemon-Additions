package net.dandycorp.dccobblemon.util;

public class VendorDataCache {
    private static VendorData vendorData;

    public static void setVendorData(VendorData data) {
        vendorData = data;
    }

    public static VendorData getVendorData() {
        return vendorData;
    }
}

