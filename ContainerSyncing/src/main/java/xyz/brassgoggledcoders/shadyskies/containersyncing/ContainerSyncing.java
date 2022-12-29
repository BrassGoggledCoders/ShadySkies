package xyz.brassgoggledcoders.shadyskies.containersyncing;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.NetworkRegistry.ChannelBuilder;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.NotNull;
import org.jline.utils.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.brassgoggledcoders.shadyskies.containersyncing.packet.UpdateClientContainerPropertiesPacket;
import xyz.brassgoggledcoders.shadyskies.containersyncing.property.PropertyType;

import java.util.List;
import java.util.Objects;

public class ContainerSyncing {
    private static final String VERSION = "1";
    private static Logger logger;
    private static SimpleChannel simpleChannel;

    public static void setup(String modId, Logger logger) {
        if (Objects.isNull(ContainerSyncing.logger)) {
            ContainerSyncing.logger = logger;
            ContainerSyncing.simpleChannel =  ChannelBuilder.named(new ResourceLocation(modId, "container_syncing"))
                    .networkProtocolVersion(() -> VERSION)
                    .clientAcceptedVersions(VERSION::matches)
                    .serverAcceptedVersions(VERSION::matches)
                    .simpleChannel();

            ContainerSyncing.simpleChannel.messageBuilder(UpdateClientContainerPropertiesPacket.class, 0)
                    .encoder(UpdateClientContainerPropertiesPacket::encode)
                    .decoder(UpdateClientContainerPropertiesPacket::decode)
                    .consumerMainThread(UpdateClientContainerPropertiesPacket::consume)
                    .add();
        } else {
            throw new IllegalStateException("ContainerSync#set already called");
        }
    }

    @NotNull
    public static Logger getLogger() {
        return Objects.requireNonNull(logger, () -> "ContainerSync#setup was not called");
    }

    public static void sendUpdateClientContainerPropertiesPacket(ServerPlayer serverPlayer,
                                                                 short menuId,
                                                                 List<Triple<PropertyType<?>, Short, Object>> dirtyProperties) {
        ContainerSyncing.simpleChannel.send(
                PacketDistributor.PLAYER.with(() -> serverPlayer),
                new UpdateClientContainerPropertiesPacket(menuId, dirtyProperties)
        );
    }
}
