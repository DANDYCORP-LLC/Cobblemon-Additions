package net.dandycorp.dccobblemon.util.vendor;

import com.google.gson.Gson;
import net.minecraft.network.PacketByteBuf;

import java.util.List;

public class VendorData {
    private List<VendorCategory> categories;

    // Default constructor
    public VendorData() {}

    public List<VendorCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<VendorCategory> categories) {
        this.categories = categories;
    }

    public void toPacket(PacketByteBuf buf) {
        String jsonData = new Gson().toJson(this);
        buf.writeString(jsonData);
    }

    public static VendorData fromPacket(PacketByteBuf buf) {
        String jsonData = buf.readString(32767);
        return new Gson().fromJson(jsonData, VendorData.class);
    }
}
