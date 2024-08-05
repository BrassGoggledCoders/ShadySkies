package xyz.brassgoggledcoders.shadyskies.vehicularcontents.carrieddata;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent.StopTracking;
import net.neoforged.neoforge.network.PacketDistributor;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.VehicularContentsAPI;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.capability.ContentCarrierCapability;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.contentcarrier.EntityContentCarrier;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.contentcarrier.IContentCarrier;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.network.UpdateClientContentPayload;

import java.lang.ref.WeakReference;
import java.util.*;

@Mod.EventBusSubscriber(modid = VehicularContentsAPI.MOD_ID)
public class TrackingManager {
    private static final TrackingManager INSTANCE = new TrackingManager();

    private final Multimap<UUID, UUID> trackingPeople;
    private final Map<UUID, WeakReference<EntityContentCarrier>> trackedEntities;

    public TrackingManager() {
        this.trackingPeople = Multimaps.newMultimap(new HashMap<>(), HashSet::new);
        this.trackedEntities = new IdentityHashMap<>();
    }

    public void addTrackedEntity(EntityContentCarrier carrier, Player tracker) {
        UUID carrierUUID = carrier.getEntity()
                .getUUID();

        WeakReference<EntityContentCarrier> existingReference = this.trackedEntities.get(carrierUUID);
        if (existingReference == null || existingReference.get() == null) {
            this.trackedEntities.put(carrierUUID, new WeakReference<>(carrier));
        }

        this.trackingPeople.put(carrierUUID, tracker.getUUID());
    }

    public void removeTrackedEntity(UUID carrierUUID, Player tracker) {
        this.trackingPeople.remove(carrierUUID, tracker.getUUID());
        if (!this.trackingPeople.containsKey(carrierUUID)) {
            this.trackedEntities.remove(carrierUUID);
        }
    }

    public void serverTick(MinecraftServer server) {
        if (!this.trackedEntities.isEmpty()) {
            Iterator<WeakReference<EntityContentCarrier>> carriers = this.trackedEntities.values()
                    .iterator();

            while (carriers.hasNext()) {
                EntityContentCarrier carrier = carriers.next()
                        .get();
                if (carrier != null) {
                    if (carrier.getCarriedData().needsSync()) {
                        Entity entity = carrier.getEntity();
                        EntityType<?> type = entity.getType();
                        if (server.getTickCount() % type.updateInterval() == 0) {
                            PacketDistributor.TRACKING_ENTITY.with(entity)
                                    .send(new UpdateClientContentPayload(
                                            entity.getId(),
                                            carrier.getCarriedData()
                                                    .packDirty()
                                    ));
                        }
                    }

                } else {
                    carriers.remove();
                }
            }
        }
    }

    @SubscribeEvent
    public static void playerStartTracking(PlayerEvent.StartTracking startTracking) {
        IContentCarrier contentCarrier = startTracking.getTarget()
                .getCapability(ContentCarrierCapability.ENTITY, null);

        if (contentCarrier instanceof EntityContentCarrier entityCarrier) {
            TrackingManager.getInstance()
                    .addTrackedEntity(entityCarrier, startTracking.getEntity());
        }
    }

    @SubscribeEvent
    public static void playerStopTracking(StopTracking stopTracking) {
        TrackingManager.getInstance()
                .removeTrackedEntity(stopTracking.getTarget().getUUID(), stopTracking.getEntity());
    }

    @SubscribeEvent
    public static void serverTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            TrackingManager.getInstance()
                    .serverTick(event.getServer());
        }

    }

    public static TrackingManager getInstance() {
        return INSTANCE;
    }

}
