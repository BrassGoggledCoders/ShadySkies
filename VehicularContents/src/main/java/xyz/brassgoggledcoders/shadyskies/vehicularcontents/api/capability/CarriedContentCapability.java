package xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.capability;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.ItemCapability;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.VehicularContentsAPI;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.carriedcontent.ICarriedContent;

public class CarriedContentCapability {

    public static final EntityCapability<ICarriedContent, Direction> ENTITY = EntityCapability.createSided(
            VehicularContentsAPI.rl("carried_content"),
            ICarriedContent.class
    );

    public static final ItemCapability<ICarriedContent, Void> ITEM = ItemCapability.createVoid(
            VehicularContentsAPI.rl("carried_content"),
            ICarriedContent.class
    );
}
