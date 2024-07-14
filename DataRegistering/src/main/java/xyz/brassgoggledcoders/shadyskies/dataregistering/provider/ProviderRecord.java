package xyz.brassgoggledcoders.shadyskies.dataregistering.provider;

import xyz.brassgoggledcoders.shadyskies.dataregistering.DataRegistering;

import java.util.function.Consumer;

public record ProviderRecord<T>(
        ProviderType<T> providerType,
        Consumer<T> consumer
) implements Consumer<DataRegistering> {
    public void accept(DataRegistering dataRegistering) {
        this.consumer()
                .accept(dataRegistering.getProvider(this.providerType()));
    }
}
