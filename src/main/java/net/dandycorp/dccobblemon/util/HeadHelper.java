package net.dandycorp.dccobblemon.util;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MagmaCubeEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.text.Text;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.function.Supplier;

public class HeadHelper {

    private static final Map<UUID, GameProfile> uuidToGameProfileCache = new HashMap<>();
    public static final List<UUID> DONOR_UUIDS = Arrays.asList(
            UUID.fromString("afd85ee6-d865-42c4-b58a-e7b54117662d"), // zach
            UUID.fromString("adefb11c-fa51-4af9-b794-42b86f83249e"), // nate
            UUID.fromString("8c09d1cf-26f7-4afd-a9a9-121e12d935e9"), // adah
            UUID.fromString("d216313b-02a8-4db9-a712-df6aaa1abb6b"), // alphys
            UUID.fromString("722ad87f-3850-40b2-908b-5252687fb9e7"), // chamaar
            UUID.fromString("2d4300b0-f9e6-4602-a33f-54c9cd72e92d"), // shannon
            UUID.fromString("0de74cc4-7efc-453a-832e-490fe1b17ebf"), // aliyna
            UUID.fromString("14388874-a7d8-4618-bc20-800d74722801"), // ethan
            UUID.fromString("2e77bb68-981d-48bb-bc79-634dcda28e5b"), // revivedkiller
            UUID.fromString("f4e4c86d-be20-4b34-b834-07c0c8776a88"), // turk
            UUID.fromString("e088b40f-d43d-45d7-8e20-d691faf5421a"), // dandybot
            UUID.fromString("e00a2079-e439-4a48-b920-3cb43b0d89f0"), // anker
            UUID.fromString("da820094-fc05-45ad-bf2a-292db8e33538"), // greg
            UUID.fromString("bb00b4f1-d5cd-4814-afe9-91e0c369715e"), // ava
            UUID.fromString("aef167f4-1786-4ff5-ab7f-6baf16dbb56b"), // sky
            UUID.fromString("ae43eb14-91bf-4327-a77b-e7311be56724"), // malone
            UUID.fromString("ac94d509-d208-42dc-b011-9bf6b8684715"), // dizzy
            UUID.fromString("62753b49-9469-455d-8999-74f0b77fa420"), // owen
            UUID.fromString("fe507c9a-59d6-43a1-925b-628d677c7abf"), // kailene
            UUID.fromString("563ba8d1-ffb9-4658-abaa-901855955fac"), // universicle
            UUID.fromString("238ddd06-8dcb-4f4d-aa13-68b0c1a8355c"), // akino
            UUID.fromString("47af3b1c-096f-48e2-a412-e102a0cbb64a"), // sheldon
            UUID.fromString("8c193ce3-c7d2-482e-b57a-ab6894893820"), // dakota
            UUID.fromString("5f162e1a-cebb-4f14-b77f-d1cd20d2425e"), // lily
            UUID.fromString("3c4e1be1-acef-4ff3-a6dd-76c8de2119c2"), // gage
            UUID.fromString("0bc40a3d-2c32-42a1-ac81-0049db16ca74"), // spencer
            UUID.fromString("60a11574-b7ba-425c-81f5-a34dcf5f2c17"), // jeff
            UUID.fromString("f3ec16c9-4fc5-40d9-a5d3-8fd3a0255cfc"), // timmy
            UUID.fromString("afec4e40-d16f-4de8-a3d2-4747dcc1f615"), // jaden
            UUID.fromString("13eebd5e-1f15-49b6-adf1-85d7129a9487"), // five
            UUID.fromString("0e8d79d3-2e60-41a4-93c3-b04ea333354d"), // chris
            UUID.fromString("2dd1a540-a8bd-4702-b9d9-0155434aa232"), // kelby
            UUID.fromString("34cd44b1-39fc-4156-8f86-d923b7bfa4ac"), // bagabong
            UUID.fromString("87175187-495b-48bb-bc60-3831c13af49f"), // skalding
            UUID.fromString("eb198cfc-127a-4d95-9cb5-68a3f1d9b572"), // bob
            UUID.fromString("b5bc1dda-fe46-43e1-84c3-e235ba3a7a55"), // paige
            UUID.fromString("ceb15c13-b7dc-456b-a87d-a97e8f994deb"), // buffalo
            UUID.fromString("509cb36e-fa28-4718-87a2-974789609651"), // pumpkin
            UUID.fromString("e3e3ecb4-077b-4b7f-bcaf-70b6544e9a66"), // lntrn
            UUID.fromString("e6729d6e-d23f-4ca5-8376-cd81a1252c8d"), // sowaka
            UUID.fromString("3b6aa68d-8460-4cd8-bee6-a3671686f48f"), // emil
            UUID.fromString("1a590d91-cbc6-4130-ba38-6f3568d4c4e2"), // blade
            UUID.fromString("a628538b-b6ae-471b-bed3-5e8fab451c9f"), // alexander
            UUID.fromString("d3ef526c-7d01-466e-af7d-6734b9f6b6df"), // natalie
            UUID.fromString("9f83d568-562c-4b8e-92aa-a6f188e2c175"), // lilith
            UUID.fromString("3f675964-9866-46ff-9372-3656c3b71f41") // akaleaf
    );

    public static void initializeCache() {
        loadCacheFromFile(); // Load cache from file

        for (UUID uuid : DONOR_UUIDS) {
            if (!uuidToGameProfileCache.containsKey(uuid)) {
                GameProfile profile = fetchGameProfile(uuid);
                if (profile != null) {
                    uuidToGameProfileCache.put(uuid, profile);
                }
            }
        }

        saveCacheToFile(); // Save cache to file
    }

    public static GameProfile fetchGameProfile(UUID uuid) {
        try {
            String uuidStr = uuid.toString().replace("-", "");
            String urlStr = "https://sessionserver.mojang.com/session/minecraft/profile/" + uuidStr + "?unsigned=false";
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000); // Timeout settings as needed
            conn.setReadTimeout(5000);

            int status = conn.getResponseCode();
            if (status != 200) {
                System.err.println("Failed to fetch GameProfile for UUID: " + uuid + ". HTTP Status: " + status);
                return null;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder jsonResponse = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                jsonResponse.append(inputLine);
            }
            in.close();

            // Parse JSON response to get the GameProfile
            JsonObject jsonObject = JsonParser.parseString(jsonResponse.toString()).getAsJsonObject();
            String name = jsonObject.get("name").getAsString();
            UUID id = UUID.fromString(jsonObject.get("id").getAsString().replaceFirst(
                    "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)",
                    "$1-$2-$3-$4-$5"
            ));

            GameProfile profile = new GameProfile(id, name);

            // Get the properties (skin data)
            JsonArray properties = jsonObject.getAsJsonArray("properties");
            for (JsonElement propertyElement : properties) {
                JsonObject property = propertyElement.getAsJsonObject();
                String propertyName = property.get("name").getAsString();
                String propertyValue = property.get("value").getAsString();
                String propertySignature = property.has("signature") ? property.get("signature").getAsString() : null;

                profile.getProperties().put(propertyName, new Property(propertyName, propertyValue, propertySignature));
            }

            return profile;

        } catch (Exception e) {
            System.err.println("Exception while fetching GameProfile for UUID: " + uuid);
            e.printStackTrace();
            return null;
        }
    }

    // Get player head ItemStack with full GameProfile
    public static ItemStack getPlayerHead(UUID uuid) {
        ItemStack stack = new ItemStack(Items.PLAYER_HEAD);
        GameProfile profile = uuidToGameProfileCache.get(uuid);

        if (profile == null) {
            System.err.println("GameProfile not found in cache for UUID: " + uuid);
            return stack; // Return default head if profile is null
        }

        // Verify that the profile has texture properties before setting it
        if (!profile.getProperties().containsKey("textures")) {
            System.err.println("Profile for UUID " + uuid + " does not contain texture properties.");
            return stack; // Return default head if texture is missing
        }

        // Write GameProfile to NBT
        NbtCompound nbt = new NbtCompound();
        NbtHelper.writeGameProfile(nbt, profile);
        stack.getOrCreateNbt().put("SkullOwner", nbt);

        return stack;
    }

    public static String getPlayerNameFromUUID(UUID uuid) {
        GameProfile profile = uuidToGameProfileCache.get(uuid);
        if (profile != null) {
            return profile.getName();
        }
        return null;
    }

    // Create an ItemStack from a player head item ID (formatted as minecraft:player_head#uuid)
    public static ItemStack getStackFromItemID(String itemID) {
        ItemStack stack = new ItemStack(net.minecraft.item.Items.PLAYER_HEAD);
        String[] parts = itemID.split("#");
        if (parts.length == 2) {
            String idPart = parts[1].trim();
            UUID uuid = null;
            try {
                uuid = UUID.fromString(idPart);
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid UUID: " + idPart);
                return stack; // Return default head
            }

            // Use cached GameProfile
            GameProfile profile = uuidToGameProfileCache.get(uuid);
            if (profile == null) {
                System.err.println("GameProfile not found in cache for UUID: " + uuid);
                return stack; // Return default head
            }

            NbtCompound nbt = stack.getOrCreateNbt();
            NbtHelper.writeGameProfile(nbt, profile);
        } else {
            System.err.println("Invalid player head item ID: " + itemID);
        }
        return stack;
    }


    // Save cache to file
    private static void saveCacheToFile() {
        try {
            File cacheFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), "donor_profiles.json");
            Gson gson = new GsonBuilder().registerTypeAdapter(GameProfile.class, new GameProfileSerializer()).create();
            Map<String, GameProfile> stringKeyedCache = new HashMap<>();
            for (Map.Entry<UUID, GameProfile> entry : uuidToGameProfileCache.entrySet()) {
                stringKeyedCache.put(entry.getKey().toString(), entry.getValue());
            }
            String json = gson.toJson(stringKeyedCache);
            FileWriter writer = new FileWriter(cacheFile);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load cache from file
    private static void loadCacheFromFile() {
        try {
            File cacheFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), "donor_profiles.json");
            if (cacheFile.exists()) {
                Gson gson = new GsonBuilder().registerTypeAdapter(GameProfile.class, new GameProfileSerializer()).create();
                FileReader reader = new FileReader(cacheFile);
                Type type = new TypeToken<Map<String, GameProfile>>() {}.getType();
                Map<String, GameProfile> tempMap = gson.fromJson(reader, type);
                reader.close();

                // Convert keys back to UUID
                for (Map.Entry<String, GameProfile> entry : tempMap.entrySet()) {
                    uuidToGameProfileCache.put(UUID.fromString(entry.getKey()), entry.getValue());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Serializer for GameProfile
    private static class GameProfileSerializer implements JsonSerializer<GameProfile>, JsonDeserializer<GameProfile> {
        @Override
        public JsonElement serialize(GameProfile src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject json = new JsonObject();
            json.addProperty("id", src.getId().toString());
            json.addProperty("name", src.getName());

            JsonArray propertiesArray = new JsonArray();
            for (Property property : src.getProperties().values()) {
                JsonObject propertyObj = new JsonObject();
                propertyObj.addProperty("name", property.getName());
                propertyObj.addProperty("value", property.getValue());
                if (property.hasSignature()) {
                    propertyObj.addProperty("signature", property.getSignature());
                }
                propertiesArray.add(propertyObj);
            }
            json.add("properties", propertiesArray);

            return json;
        }

        @Override
        public GameProfile deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObj = json.getAsJsonObject();
            UUID id = UUID.fromString(jsonObj.get("id").getAsString());
            String name = jsonObj.get("name").getAsString();
            GameProfile profile = new GameProfile(id, name);

            JsonArray propertiesArray = jsonObj.getAsJsonArray("properties");
            for (JsonElement propertyElement : propertiesArray) {
                JsonObject propertyObj = propertyElement.getAsJsonObject();
                String propertyName = propertyObj.get("name").getAsString();
                String propertyValue = propertyObj.get("value").getAsString();
                String propertySignature = propertyObj.has("signature") ? propertyObj.get("signature").getAsString() : null;

                profile.getProperties().put(propertyName, new Property(propertyName, propertyValue, propertySignature));
            }

            return profile;
        }
    }

    public static ItemStack createCustomHead(String textureValue, String displayName) {
        ItemStack head = new ItemStack(net.minecraft.item.Items.PLAYER_HEAD);
        GameProfile profile = new GameProfile(UUID.randomUUID(), displayName);
        profile.getProperties().put("textures", new Property("textures", textureValue));

        NbtCompound nbt = new NbtCompound();
        NbtHelper.writeGameProfile(nbt, profile);
        head.getOrCreateNbt().put("SkullOwner", nbt);

        head.setCustomName(Text.literal(displayName));
        if (head.hasNbt() && head.getNbt().contains("SkullOwner")) {
            System.out.println("Custom head created with SkullOwner: " + head.getNbt().getCompound("SkullOwner"));
        } else {
            System.out.println("Failed to set SkullOwner for custom head.");
        }

        return head;
    }

    public static void createAndGiveTestHead(PlayerEntity player) {
        String textureValue = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTE5MmJiZjFjMGU5ZjBhYzAzNmU1NmVjZjA1NDg2ZmJhMTFlZTk3ZWM1MjMwYjM0NjA3ZjE5NjZkM2FlMGM1YyJ9fX0="; // Example texture
        String displayName = "Test Custom Head";

        ItemStack customHead = HeadHelper.createCustomHead(textureValue, displayName);
        player.getInventory().insertStack(customHead);
        player.sendMessage(Text.literal("You have been given a Test Custom Head!"), false);
    }


    // Get a random donor UUID
    public static UUID getRandomDonor() {
        return DONOR_UUIDS.get(new Random().nextInt(DONOR_UUIDS.size()));
    }
}
