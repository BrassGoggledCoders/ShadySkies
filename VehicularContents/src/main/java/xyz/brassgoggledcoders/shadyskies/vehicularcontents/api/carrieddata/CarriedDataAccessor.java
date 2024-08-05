package xyz.brassgoggledcoders.shadyskies.vehicularcontents.api.carrieddata;

import org.jetbrains.annotations.NotNull;

public record CarriedDataAccessor<V>(
        int id,
        String name,
        @NotNull ICarriedDataSerializer<V> serializer
) {

}
