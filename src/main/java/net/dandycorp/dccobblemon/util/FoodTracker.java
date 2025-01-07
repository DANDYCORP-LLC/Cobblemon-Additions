package net.dandycorp.dccobblemon.util;

import net.dandycorp.dccobblemon.DANDYCORPCobblemonAdditions;
import net.dandycorp.dccobblemon.util.grinder.GrinderPointGenerator;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FoodTracker {
    public static void initialize(){
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"Id","Name","Meat/Snack","Hunger","Saturation Modifier"});
        Map<Item, Float> base = GrinderPointGenerator.basePointValues;
        Registries.ITEM.forEach(item -> {
                    if(item.isFood() && !base.containsKey(item)){
                        FoodComponent component = item.getFoodComponent();
                        String type;
                        if(component.isSnack()){
                            type = "Snack";
                        }
                        else if(component.isMeat()){
                            type = "Meat";
                        }
                        else {
                            type = "Neither";
                        }
                        data.add(new String[]{
                                Registries.ITEM.getId(item).toString(),
                                item.getName().getString(),
                                type,
                                String.valueOf(component.getHunger()),
                                String.valueOf(component.getSaturationModifier()),
                        });
                    }
                }
        );
        saveToCsv(data, "resources/food_values.csv");
    }

    private static void saveToCsv(List<String[]> data, String filePath) {
        File file = new File(filePath);
        file.getParentFile().mkdirs();
        try (FileWriter writer = new FileWriter(file)) {
            for (String[] row : data) {
                writer.write(String.join(",", row));
                writer.write("\n");
            }
            DANDYCORPCobblemonAdditions.LOGGER.info("Food values successfully saved to " + filePath);
        } catch (IOException e) {
            DANDYCORPCobblemonAdditions.LOGGER.error("Failed to save food values to CSV", e);
        }
    }
}
