package xyz.brassgoggledcoders.shadyskies.vehicularcontents.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.VehicularContentsAPI;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.capability.ContentCarrierCapability;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.carrieddata.CarriedDataValue;
import xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.contentcarrier.IContentCarrier;

import java.util.List;

public record UpdateClientContentPayload(
        int entityId,
        List<CarriedDataValue<?>> dataValues
) implements CustomPacketPayload {
    public static final ResourceLocation ID = VehicularContentsAPI.rl("update_client_content");

    @Override
    public void write(@NotNull FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeInt(entityId);
        friendlyByteBuf.writeCollection(
                this.dataValues,
                (listByteBuf, carriedDataValue) -> carriedDataValue.write(listByteBuf)
        );
    }

    @Override
    @NotNull
    public ResourceLocation id() {
        return ID;
    }

    public static void handle(UpdateClientContentPayload updateClientContentPayload, PlayPayloadContext playPayloadContext) {
        playPayloadContext.workHandler()
                .execute(() -> playPayloadContext.level()
                        .ifPresent(level -> {
                            Entity entity = level.getEntity(updateClientContentPayload.entityId());
                            if (entity != null) {
                                IContentCarrier contentCarrier = entity.getCapability(ContentCarrierCapability.ENTITY, null);
                                if (contentCarrier != null) {
                                    contentCarrier.getCarriedData()
                                            .assignValues(updateClientContentPayload.dataValues());
                                }
                            }
                        })
                );
    }

    public static UpdateClientContentPayload read(@NotNull FriendlyByteBuf friendlyByteBuf) {
        return new UpdateClientContentPayload(
                friendlyByteBuf.readInt(),
                friendlyByteBuf.readList(CarriedDataValue::read)
        );
    }
}
