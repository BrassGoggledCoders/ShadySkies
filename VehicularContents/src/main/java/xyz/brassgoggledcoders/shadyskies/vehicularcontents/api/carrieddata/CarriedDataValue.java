package xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.carrieddata;

import net.minecraft.network.FriendlyByteBuf;

public record CarriedDataValue<T>(
        int id,
        ICarriedDataSerializer<T> serializer,
        T value
) {
    public void write(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeInt(this.id());
        friendlyByteBuf.writeId(CarriedDataSerializers.REGISTRY, this.serializer());
        this.serializer().write(friendlyByteBuf, this.value());
    }

    public static CarriedDataValue<?> read(FriendlyByteBuf friendlyByteBuf) {
        return read(friendlyByteBuf, friendlyByteBuf.readInt(), friendlyByteBuf.readById(CarriedDataSerializers.REGISTRY));
    }

    private static <V> CarriedDataValue<V> read(FriendlyByteBuf friendlyByteBuf, int id, ICarriedDataSerializer<V> dataSerializer) {
        return new CarriedDataValue<>(
                id,
                dataSerializer,
                dataSerializer.read(friendlyByteBuf)
        );
    }
}
