package xyz.brassgoggledcoders.shadyskies.vehicularcontents.api;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.carriedcontent.ICarriedContent;

import java.util.Objects;

public class VehicularContentsAPI {
    public static final String MOD_ID = "vehicular_contents";

    private static Registry<Codec<? extends ICarriedContent>> carriedContentCodecRegistry;

    public static Registry<Codec<? extends ICarriedContent>> getCarriedContentCodecRegistry() {
        return Objects.requireNonNull(carriedContentCodecRegistry);
    }

    public static void setCarriedContentCodecRegistry(Registry<Codec<? extends ICarriedContent>> registry) {
        carriedContentCodecRegistry = registry;
    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
