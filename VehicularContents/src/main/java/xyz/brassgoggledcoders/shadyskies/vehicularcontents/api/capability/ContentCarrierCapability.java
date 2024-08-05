package xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.capability;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.EntityCapability;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.VehicularContentsAPI;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.carriedcontent.ICarriedContent;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.contentcarrier.IContentCarrier;

public class ContentCarrierCapability {

    public static final EntityCapability<IContentCarrier, Direction> ENTITY = EntityCapability.createSided(
            VehicularContentsAPI.rl("content_carrier"),
            IContentCarrier.class
    );
}
