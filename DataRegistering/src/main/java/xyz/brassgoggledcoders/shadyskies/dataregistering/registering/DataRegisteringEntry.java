package xyz.brassgoggledcoders.shadyskies.dataregistering.registering;

import xyz.brassgoggledcoders.shadyskies.dataregistering.DataRegistering;
import xyz.brassgoggledcoders.shadyskies.dataregistering.builder.Builder;
import xyz.brassgoggledcoders.shadyskies.dataregistering.builder.BuilderType;
import xyz.brassgoggledcoders.shadyskies.dataregistering.provider.ProviderType;
import xyz.brassgoggledcoders.shadyskies.registering.RegisteringEntry;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class DataRegisteringEntry<T extends RegisteringEntry<U, V>, U extends V, V> implements Supplier<U> {
    private final T registeringEntry;
    private final DataRegistering dataRegistering;

    public DataRegisteringEntry(T registeringEntry, DataRegistering dataRegistering) {
        this.registeringEntry = registeringEntry;
        this.dataRegistering = dataRegistering;
    }

    public <P> DataRegisteringEntry<T, U, V> withProvider(
            ProviderType<P> providerType,
            BiConsumer<T, P> data
    ) {
        dataRegistering.doWithProvider(providerType, (provider) -> data.accept(this.registeringEntry, provider));
        return this;
    }

    public <P extends Builder> DataRegisteringEntry<T, U, V> withBuilder(
            BuilderType<P> builderType,
            BiConsumer<T, P> builder) {
        dataRegistering.builder(
                builderType,
                this.registeringEntry.getId(),
                theBuilder -> builder.accept(this.registeringEntry, theBuilder)
        );
        return this;
    }

    public DataRegisteringEntry<T, U, V> withDefaults(BiConsumer<DataRegistering, DataRegisteringEntry<T, U, V>> consumer) {
        dataRegistering.duringGeneration(() -> consumer.accept(this.dataRegistering, this));
        return this;
    }

    public T getRegisteringEntry() {
        return registeringEntry;
    }

    @Override
    public U get() {
        return this.getRegisteringEntry()
                .get();
    }
}
