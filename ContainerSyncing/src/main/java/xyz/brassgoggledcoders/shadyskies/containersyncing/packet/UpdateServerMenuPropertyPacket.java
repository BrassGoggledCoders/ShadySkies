package xyz.brassgoggledcoders.shadyskies.containersyncing.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;
import xyz.brassgoggledcoders.shadyskies.containersyncing.property.IPropertyManaged;
import xyz.brassgoggledcoders.shadyskies.containersyncing.property.PropertyType;
import xyz.brassgoggledcoders.shadyskies.containersyncing.property.PropertyTypes;

import java.util.function.Supplier;

public class UpdateServerMenuPropertyPacket {
    private final short containerId;
    private final PropertyType<?> propertyType;
    private final short property;
    private final Object value;

    public UpdateServerMenuPropertyPacket(short containerId, PropertyType<?> propertyType, short property, Object value) {
        this.containerId = containerId;
        this.propertyType = propertyType;
        this.property = property;
        this.value = value;
    }

    public static UpdateServerMenuPropertyPacket decode(FriendlyByteBuf packetBuffer) {
        short windowId = packetBuffer.readShort();
        PropertyType<?> propertyType = PropertyTypes.getByIndex(packetBuffer.readShort());
        short property = packetBuffer.readShort();
        Object value = propertyType.getReader().apply(packetBuffer);
        return new UpdateServerMenuPropertyPacket(windowId, propertyType, property, value);
    }

    public void encode(FriendlyByteBuf packetBuffer) {
        packetBuffer.writeShort(containerId);
        packetBuffer.writeShort(PropertyTypes.getIndex(propertyType));
        packetBuffer.writeShort(property);
        propertyType.attemptWrite(packetBuffer, value);
    }

    public void consume(Supplier<NetworkEvent.Context> contextSupplier) {
        Player playerEntity = contextSupplier.get().getSender();
        if (playerEntity != null) {
            AbstractContainerMenu container = playerEntity.containerMenu;
            if (container.containerId == containerId) {
                if (container instanceof IPropertyManaged propertyManaged) {
                    propertyManaged.getPropertyManager()
                            .update(propertyType, property, value);
                }
            }
        }
    }
}