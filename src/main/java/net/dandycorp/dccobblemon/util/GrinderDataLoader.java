package net.dandycorp.dccobblemon.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class GrinderDataLoader {

    private static final Gson GSON = new Gson();
    private static Map<String, Float> basePointData = null;
    private static final Type TYPE = new TypeToken<Map<String, Float>>() {}.getType();


    public static Map<String, Float> loadBasePointData() {
        if (basePointData != null) {
            return basePointData;
        }

        Path configDir = DANDYCORPCobblemonAdditions.CONFIG_DIR;
        Path basePointPath = configDir.resolve("base_point_values.json");

        // Ensure the config directory exists
        if (!Files.exists(configDir)) {
            try {
                Files.createDirectories(configDir);
            } catch (IOException e) {
                DANDYCORPCobblemonAdditions.LOGGER.error("Failed to create config directory.", e);
                return null;
            }
        }

        // If the JSON file doesn't exist, copy the default from resources
        if (!Files.exists(basePointPath)) {
            try (InputStream in = GrinderDataLoader.class.getResourceAsStream("/base_point_values.json")) {
                if (in != null) {
                    Files.copy(in, basePointPath);
                    DANDYCORPCobblemonAdditions.LOGGER.info("Copied default base_point_values.json to config directory.");
                } else {
                    DANDYCORPCobblemonAdditions.LOGGER.error("Default base_point_values.json not found in resources.");
                    return null;
                }
            } catch (IOException e) {
                DANDYCORPCobblemonAdditions.LOGGER.error("Failed to copy base_point_values.json.", e);
                return null;
            }
        }

        // Read and parse the JSON file
        try (Reader reader = Files.newBufferedReader(basePointPath)) {
            basePointData = GSON.fromJson(reader, TYPE);
            DANDYCORPCobblemonAdditions.LOGGER.info("Loaded base point data successfully.");
            return basePointData;
        } catch (Exception e) {
            DANDYCORPCobblemonAdditions.LOGGER.error("Error parsing base_point_values.json at " + basePointPath.toAbsolutePath(), e);
            // Optionally, print the problematic JSON content
            try {
                String content = new String(Files.readAllBytes(basePointPath));
                DANDYCORPCobblemonAdditions.LOGGER.error("JSON Content: " + content);
            } catch (IOException ioException) {
                DANDYCORPCobblemonAdditions.LOGGER.error("Failed to read base_point_values.json content.", ioException);
            }
            return null;
        }
    }


    public static void reloadBasePointData() {
        basePointData = null;
        loadBasePointData();
    }
}
