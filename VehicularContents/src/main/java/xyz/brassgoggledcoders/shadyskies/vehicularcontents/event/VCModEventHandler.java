package xyz.brassgoggledcoders.shadyskies.vehicularcontents.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod.EventBusSubscriber;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.VehicularContentsAPI;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.carriedcontent.ICarriedContent;

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
}
