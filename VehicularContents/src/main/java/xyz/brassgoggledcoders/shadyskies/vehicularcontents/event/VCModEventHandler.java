package xyz.brassgoggledcoders.shadyskies.vehicularcontents.event;

import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.VehicularContentsAPI;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.carriedcontent.ICarriedContent;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.network.UpdateClientContentPayload;

@EventBusSubscriber(modid = VehicularContentsAPI.MOD_ID)
public class VCModEventHandler {

    @SubscribeEvent
    public static void datapackRegistry(DataPackRegistryEvent.NewRegistry newRegistryEvent) {
        newRegistryEvent.dataPackRegistry(
                ICarriedContent.REGISTRY_KEY,
                ICarriedContent.CODEC,
                ICarriedContent.CODEC
        );
    }

    @SubscribeEvent
    public static void networkRegister(RegisterPayloadHandlerEvent event) {
        IPayloadRegistrar payloadRegistrar = event.registrar(VehicularContentsAPI.MOD_ID);

        payloadRegistrar.play(
                UpdateClientContentPayload.ID,
                UpdateClientContentPayload::read,
                UpdateClientContentPayload::handle
        );
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.
    }
}
