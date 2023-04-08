package xyz.brassgoggledcoders.shadyskies.containersyncing.property;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import org.apache.commons.lang3.tuple.Triple;
import xyz.brassgoggledcoders.shadyskies.containersyncing.ContainerSyncing;
import xyz.brassgoggledcoders.shadyskies.containersyncing.packet.UpdateServerMenuPropertyPacket;

import java.util.ArrayList;
import java.util.List;

public class PropertyManager {
    private final List<Property<?>> properties;
    private final short menuId;
    private final ContainerSyncing containerSyncing;

    public PropertyManager(short menuId, ContainerSyncing containerSyncing) {
        this.menuId = menuId;
        this.properties = new ArrayList<>();
        this.containerSyncing = containerSyncing;
    }

    public <T> Property<T> addTrackedProperty(PropertyType<T> property) {
        return this.addTrackedProperty(property.create());
    }


    public <T> Property<T> addTrackedProperty(Property<T> property) {
        this.properties.add(property);
        return property;
    }

    public void sendChanges(Inventory inventory, boolean firstTime) {
        if (inventory.player instanceof ServerPlayer serverPlayer) {
            this.sendChanges(serverPlayer, firstTime);
        }
    }

    public <T> void updateServer(Property<T> property, T value) {
        short propertyId = -1;
        for (short i = 0; i < properties.size(); i++) {
            if (properties.get(i) == property) {
                propertyId = i;
            }
        }
        property.set(value);
        containerSyncing.sendServerUpdate(new UpdateServerMenuPropertyPacket(
                menuId,
                property.getPropertyType(),
                propertyId,
                value
        ));
    }

    public void sendChanges(ServerPlayer serverPlayer, boolean firstTime) {

        List<Triple<PropertyType<?>, Short, Object>> dirtyProperties = new ArrayList<>();
        for (short i = 0; i < properties.size(); i++) {
            Property<?> property = properties.get(i);
            if (property.isDirty() || firstTime) {
                dirtyProperties.add(Triple.of(property.getPropertyType(), i, property.get()));
            }
        }

        if (!dirtyProperties.isEmpty()) {
            this.containerSyncing.sendClientUpdate(serverPlayer, menuId, dirtyProperties);
        }
    }

    public void update(PropertyType<?> propertyType, short propertyId, Object value) {
        if (propertyId < properties.size()) {
            Property<?> property = properties.get(propertyId);
            if (property != null && property.getPropertyType() == propertyType) {
                try {
                    propertyType.attemptSet(value, property);
                } catch (ClassCastException e) {
                    containerSyncing.getLogger().warn("Failed to set Property", e);
                }
            }
        }
    }
}
