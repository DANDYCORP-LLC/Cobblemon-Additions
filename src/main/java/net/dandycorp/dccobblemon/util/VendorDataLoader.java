package net.dandycorp.dccobblemon.util;

import com.google.gson.Gson;
import net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions;
import net.dandycorp.dccobblemon.util.VendorData;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

public class VendorDataLoader {

    private static final Gson GSON = new Gson();

    private static VendorData vendorData = null;

    public static VendorData loadVendorData() {
        if (vendorData != null) {
            return vendorData;
        }

        Path configDir = DANDYCORPCobblemonAdditions.CONFIG_DIR;
        Path vendorItemsPath = configDir.resolve("vendor_items.json");

        // Ensure the config directory exists
        if (!Files.exists(configDir)) {
            try {
                Files.createDirectories(configDir);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        // If the JSON file doesn't exist, copy the default from resources
        if (!Files.exists(vendorItemsPath)) {
            try (InputStream in = VendorDataLoader.class.getResourceAsStream("/vendor_items.json")) {
                if (in != null) {
                    Files.copy(in, vendorItemsPath);
                } else {
                    System.err.println("Default vendor_items.json not found in resources.");
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        // Read and parse the JSON file
        try (Reader reader = Files.newBufferedReader(vendorItemsPath)) {
            vendorData = GSON.fromJson(reader, VendorData.class);
            return vendorData;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to reload vendor data if needed
    public static void reloadVendorData() {
        vendorData = null;
        loadVendorData();
    }

    public static VendorData getVendorData() {
        return vendorData;
    }
}
