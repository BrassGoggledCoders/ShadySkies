package xyz.brassgoggledcoders.shadyskies.containersyncing;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.brassgoggledcoders.shadyskies.containersyncing.packet.UpdateClientMenuPropertiesPayload;
import xyz.brassgoggledcoders.shadyskies.containersyncing.packet.UpdateServerMenuPropertyPayload;
import xyz.brassgoggledcoders.shadyskies.containersyncing.property.PropertyManager;
import xyz.brassgoggledcoders.shadyskies.containersyncing.property.PropertyType;

import java.util.List;

@SuppressWarnings("unused")
public record ContainerSyncing(
        Logger logger,
        ResourceLocation clientPayloadId,
        ResourceLocation serverPayloadId
) {
    @Override
    @NotNull
    public Logger logger() {
        return this.logger;
    }

    public PropertyManager createManager(int menuId) {
        return new PropertyManager((short) menuId, this);
    }

    public void sendClientUpdate(ServerPlayer serverPlayer, short menuId, List<Triple<PropertyType<?>, Short, Object>> dirtyProperties) {
        PacketDistributor.PLAYER.with(serverPlayer)
                .send(new UpdateClientMenuPropertiesPayload(this.clientPayloadId(), menuId, dirtyProperties));
    }

    public void sendServerUpdate(short containerId, PropertyType<?> propertyType, short property, Object value) {
        PacketDistributor.SERVER.noArg()
                .send(new UpdateServerMenuPropertyPayload(this.serverPayloadId(), containerId, propertyType, property, value));
    }

    public static ContainerSyncing setup(String modId, RegisterPayloadHandlerEvent event) {
        IPayloadRegistrar registrar = event.registrar(modId);

        ResourceLocation clientPayloadId = new ResourceLocation(modId, "update_client_menu");
        registrar.play(
                clientPayloadId,
                byteBuf -> UpdateClientMenuPropertiesPayload.decode(clientPayloadId, byteBuf),
                handler -> handler.client(UpdateClientMenuPropertiesPayload::handleData)
        );

        ResourceLocation serverPayloadId = new ResourceLocation(modId, "update_server_menu");
        registrar.play(
                serverPayloadId,
                byteBuf -> UpdateServerMenuPropertyPayload.decode(serverPayloadId, byteBuf),
                handler -> handler.server(UpdateServerMenuPropertyPayload::handleData)
        );

        return new ContainerSyncing(LoggerFactory.getLogger(modId), clientPayloadId, serverPayloadId);
    }
}
