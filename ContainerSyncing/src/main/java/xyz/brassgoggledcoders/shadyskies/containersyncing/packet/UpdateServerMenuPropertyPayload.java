package xyz.brassgoggledcoders.shadyskies.containersyncing.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.shadyskies.containersyncing.property.IPropertyManaged;
import xyz.brassgoggledcoders.shadyskies.containersyncing.property.PropertyType;
import xyz.brassgoggledcoders.shadyskies.containersyncing.property.PropertyTypes;

public record UpdateServerMenuPropertyPayload(
        ResourceLocation id,
        short containerId,
        PropertyType<?> propertyType,
        short property,
        Object value
) implements CustomPacketPayload {
    @Override
    public void write(@NotNull FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeShort(this.containerId);
        friendlyByteBuf.writeShort(PropertyTypes.getIndex(propertyType));
        friendlyByteBuf.writeShort(property);
        propertyType.attemptWrite(friendlyByteBuf, value);
    }

    @Override
    public ResourceLocation id() {
        return null;
    }

    public static UpdateServerMenuPropertyPayload decode(ResourceLocation id, FriendlyByteBuf packetBuffer) {
        short windowId = packetBuffer.readShort();
        PropertyType<?> propertyType = PropertyTypes.getByIndex(packetBuffer.readShort());
        short property = packetBuffer.readShort();
        Object value = propertyType.getReader().apply(packetBuffer);
        return new UpdateServerMenuPropertyPayload(id, windowId, propertyType, property, value);
    }

    public static void handleData(UpdateServerMenuPropertyPayload payload, PlayPayloadContext context) {
        context.workHandler()
                .submitAsync(() -> {
                    Player playerEntity = context.player()
                            .orElse(null);
                    if (playerEntity != null) {
                        AbstractContainerMenu container = playerEntity.containerMenu;
                        if (container.containerId == payload.containerId) {
                            if (container instanceof IPropertyManaged propertyManaged) {
                                propertyManaged.getPropertyManager()
                                        .update(payload.propertyType, payload.property, payload.value);
                            }
                        }
                    }
                });

    }
}