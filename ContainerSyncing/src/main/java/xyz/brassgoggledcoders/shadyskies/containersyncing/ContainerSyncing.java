package xyz.brassgoggledcoders.shadyskies.containersyncing;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry.ChannelBuilder;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import xyz.brassgoggledcoders.shadyskies.containersyncing.packet.UpdateClientMenuPropertiesPacket;
import xyz.brassgoggledcoders.shadyskies.containersyncing.packet.UpdateServerMenuPropertyPacket;
import xyz.brassgoggledcoders.shadyskies.containersyncing.property.PropertyManager;
import xyz.brassgoggledcoders.shadyskies.containersyncing.property.PropertyType;

import java.util.List;

public class ContainerSyncing {
    private final static String VERSION = "1";
    private final Logger logger;
    private final SimpleChannel simpleChannel;

    public ContainerSyncing(Logger logger, SimpleChannel simpleChannel) {
        this.logger = logger;
        this.simpleChannel = simpleChannel;
    }

    @NotNull
    public Logger getLogger() {
        return this.logger;
    }

    public PropertyManager createManager(int menuId) {
        return new PropertyManager((short) menuId, this);
    }

    public void sendClientUpdate(ServerPlayer serverPlayer, short menuId, List<Triple<PropertyType<?>, Short, Object>> dirtyProperties) {
        this.simpleChannel.send(
                PacketDistributor.PLAYER.with(() -> serverPlayer),
                new UpdateClientMenuPropertiesPacket(menuId, dirtyProperties)
        );
    }

    public void sendServerUpdate(UpdateServerMenuPropertyPacket updateServerMenuPropertyPacket) {
        this.simpleChannel.send(
                PacketDistributor.SERVER.noArg(),
                updateServerMenuPropertyPacket
        );
    }

    public static ContainerSyncing setup(String modId, Logger logger) {
        SimpleChannel simpleChannel = ChannelBuilder.named(new ResourceLocation(modId, "container_syncing"))
                .networkProtocolVersion(() -> VERSION)
                .clientAcceptedVersions(VERSION::matches)
                .serverAcceptedVersions(VERSION::matches)
                .simpleChannel();

        simpleChannel.messageBuilder(UpdateClientMenuPropertiesPacket.class, 0)
                .encoder(UpdateClientMenuPropertiesPacket::encode)
                .decoder(UpdateClientMenuPropertiesPacket::decode)
                .consumerMainThread(UpdateClientMenuPropertiesPacket::consume)
                .add();

        simpleChannel.messageBuilder(UpdateServerMenuPropertyPacket.class, 1)
                .encoder(UpdateServerMenuPropertyPacket::encode)
                .decoder(UpdateServerMenuPropertyPacket::decode)
                .consumerMainThread(UpdateServerMenuPropertyPacket::consume)
                .add();

        return new ContainerSyncing(logger, simpleChannel);
    }
}
