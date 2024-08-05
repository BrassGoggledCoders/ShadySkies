package xyz.brassgoggledcoders.shadyskies.vehicularcontents;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.VehicularContentsAPI;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.carrieddata.CarriedDataSerializers;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.content.VCAttachments;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.content.VCVehicularContents;

@Mod(value = VehicularContentsAPI.MOD_ID)
public class VehicularContents {


    public VehicularContents(IEventBus eventBus) {
        CarriedDataSerializers.setup(eventBus);

        VCAttachments.setup(eventBus);
        VCVehicularContents.setup(eventBus);
    }
}
