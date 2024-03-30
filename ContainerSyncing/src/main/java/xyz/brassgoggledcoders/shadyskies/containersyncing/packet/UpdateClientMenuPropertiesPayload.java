package xyz.brassgoggledcoders.shadyskies.containersyncing.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import org.apache.commons.lang3.tuple.Triple;
import xyz.brassgoggledcoders.shadyskies.containersyncing.property.IPropertyManaged;
import xyz.brassgoggledcoders.shadyskies.containersyncing.property.PropertyManager;
import xyz.brassgoggledcoders.shadyskies.containersyncing.property.PropertyType;
import xyz.brassgoggledcoders.shadyskies.containersyncing.property.PropertyTypes;

import java.util.ArrayList;
import java.util.List;

public record UpdateClientMenuPropertiesPayload(
        ResourceLocation id,
        short menuId,
        List<Triple<PropertyType<?>, Short, Object>> updates
) implements CustomPacketPayload {

    @Override
    public void write(FriendlyByteBuf packetBuffer) {
        packetBuffer.writeShort(menuId);
        List<Triple<PropertyType<?>, Short, Object>> validUpdates = new ArrayList<>();
        for (Triple<PropertyType<?>, Short, Object> update : updates) {
            if (update.getLeft().isValid(update.getRight())) {
                validUpdates.add(update);
            }
        }

        packetBuffer.writeShort(validUpdates.size());
        for (Triple<PropertyType<?>, Short, Object> update : validUpdates) {
            packetBuffer.writeShort(PropertyTypes.getIndex(update.getLeft()));
            packetBuffer.writeShort(update.getMiddle());
            update.getLeft().attemptWrite(packetBuffer, update.getRight());
        }
    }

    public static void handleData(UpdateClientMenuPropertiesPayload pack, PlayPayloadContext context) {
        context.workHandler()
                .submitAsync(() -> {
                    LocalPlayer playerEntity = Minecraft.getInstance().player;
                    if (playerEntity != null && playerEntity.containerMenu instanceof IPropertyManaged propertyManaged) {
                        if (playerEntity.containerMenu.containerId == pack.menuId) {
                            PropertyManager propertyManager = propertyManaged.getPropertyManager();
                            for (Triple<PropertyType<?>, Short, Object> update : pack.updates) {
                                propertyManager.update(update.getLeft(), update.getMiddle(), update.getRight());
                            }
                        }
                    }
                });
    }

    public static UpdateClientMenuPropertiesPayload decode(ResourceLocation id, FriendlyByteBuf packetBuffer) {
        short windowId = packetBuffer.readShort();
        short updateAmount = packetBuffer.readShort();
        List<Triple<PropertyType<?>, Short, Object>> updates = new ArrayList<>();
        for (short i = 0; i < updateAmount; i++) {
            PropertyType<?> propertyType = PropertyTypes.getByIndex(packetBuffer.readShort());
            short propertyLocation = packetBuffer.readShort();
            Object object = propertyType.getReader().apply(packetBuffer);
            updates.add(Triple.of(propertyType, propertyLocation, object));
        }
        return new UpdateClientMenuPropertiesPayload(id, windowId, updates);
    }
}
