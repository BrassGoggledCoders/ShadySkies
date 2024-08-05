package xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.carrieddata;

import net.minecraft.core.Registry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.VehicularContentsAPI;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.carrieddata.serializer.IntegerCarriedDataSerializer;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.carrieddata.serializer.ItemStackHandlerDataSerializer;

public class CarriedDataSerializers {
    private static final DeferredRegister<ICarriedDataSerializer<?>> DEFERRED_REGISTER = DeferredRegister.create(
            ICarriedDataSerializer.REGISTRY_KEY,
            VehicularContentsAPI.MOD_ID
    );

    public static final Registry<ICarriedDataSerializer<?>> REGISTRY = DEFERRED_REGISTER.makeRegistry(
            registryBuilder -> {
            }
    );

    public static final DeferredHolder<ICarriedDataSerializer<?>, IntegerCarriedDataSerializer> INTEGER = DEFERRED_REGISTER.register(
            "integer",
            IntegerCarriedDataSerializer::new
    );

    public static final DeferredHolder<ICarriedDataSerializer<?>, ItemStackHandlerDataSerializer> ITEMSTACK_HANDLER = DEFERRED_REGISTER.register(
            "itemstack_handler",
            ItemStackHandlerDataSerializer::new
    );

    public static void setup(IEventBus bus) {
        DEFERRED_REGISTER.register(bus);
    }
}
