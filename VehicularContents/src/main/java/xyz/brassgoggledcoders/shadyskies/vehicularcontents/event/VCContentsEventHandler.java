package xyz.brassgoggledcoders.shadyskies.vehicularcontents.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.EntityInteractSpecific;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.VehicularContentsAPI;

@EventBusSubscriber(modid = VehicularContentsAPI.MOD_ID)
public class VCContentsEventHandler {

    @SubscribeEvent
    public static void interactAt(EntityInteractSpecific entityInteractSpecific) {

    }

}
