package xyz.brassgoggledcoders.shadyskies.vehicularcontents.content;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.VehicularContentsAPI;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.carriedcontent.ICarriedContent;

public class VCVehicularContents {

    public static DeferredRegister<Codec<? extends ICarriedContent>> DEFERRED_REGISTER = DeferredRegister.create(
            ICarriedContent.CODEC_REGISTRY_KEY,
            VehicularContentsAPI.MOD_ID
    );

    public static Registry<Codec<? extends ICarriedContent>> REGISTRY = DEFERRED_REGISTER.makeRegistry(
            codecRegistryBuilder -> {
            }
    );

    public static void setup(IEventBus eventBus) {
        DEFERRED_REGISTER.register(eventBus);

        VehicularContentsAPI.setCarriedContentCodecRegistry(REGISTRY);
    }
}
