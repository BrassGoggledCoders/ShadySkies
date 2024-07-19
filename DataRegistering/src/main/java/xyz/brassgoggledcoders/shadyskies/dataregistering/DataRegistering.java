package xyz.brassgoggledcoders.shadyskies.dataregistering;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import xyz.brassgoggledcoders.shadyskies.dataregistering.builder.Builder;
import xyz.brassgoggledcoders.shadyskies.dataregistering.builder.BuilderFactory;
import xyz.brassgoggledcoders.shadyskies.dataregistering.builder.BuilderRecord;
import xyz.brassgoggledcoders.shadyskies.dataregistering.builder.BuilderType;
import xyz.brassgoggledcoders.shadyskies.dataregistering.provider.DeferredProvider;
import xyz.brassgoggledcoders.shadyskies.dataregistering.provider.DeferredProviderType;
import xyz.brassgoggledcoders.shadyskies.dataregistering.provider.ProviderRecord;
import xyz.brassgoggledcoders.shadyskies.dataregistering.provider.ProviderType;
import xyz.brassgoggledcoders.shadyskies.dataregistering.registering.DataRegisteringEntry;
import xyz.brassgoggledcoders.shadyskies.registering.RegisteringEntry;

import java.util.*;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class DataRegistering {
    private final String id;
    private final Map<ProviderType<?>, Object> providers;
    private final List<Consumer<DataRegistering>> handlers;

    private GatherDataEvent gatherDataEvent;

    public DataRegistering(String id) {
        this.id = id;
        this.providers = new HashMap<>();
        this.handlers = new ArrayList<>();
    }

    public void doGeneration(GatherDataEvent gatherDataEvent) {
        this.gatherDataEvent = gatherDataEvent;

        this.handlers.forEach(handler -> handler.accept(this));
    }

    public <T> T getProvider(ProviderType<T> providerType) {
        Object mappedProvider = providers.get(providerType);
        T provider;
        if (mappedProvider == null) {
            provider = providerType.create(id, Objects.requireNonNull(gatherDataEvent, "Can't get provider before event"));
            this.providers.put(providerType, provider);
        } else {
            provider = providerType.cast(mappedProvider);
        }

        return provider;
    }

    public <T> void doWithProvider(ProviderType<T> providerType, Consumer<T> consumer) {
        this.handlers.add(new ProviderRecord<>(providerType, consumer));
    }

    public <T> void doWithProvider(DeferredProviderType<T> providerType, Consumer<T> consumer) {
        this.handlers.add(new ProviderRecord<>(providerType, tDeferredProvider -> tDeferredProvider.deferred(consumer)));
    }

    public <T> void deferredWithProvider(ProviderType<DeferredProvider<T>> providerType, Consumer<T> consumer) {
        this.handlers.add(new ProviderRecord<>(providerType, tDeferredProvider -> tDeferredProvider.deferred(consumer)));
    }

    public <T extends Builder> void builder(BuilderType<T> builderType, String path, Consumer<T> builder) {
        this.builder(builderType, new ResourceLocation(this.id, path), builder);
    }

    public <T extends Builder> void builder(BuilderType<T> builderType, ResourceLocation id, Consumer<T> builder) {
        this.handlers.add(new BuilderRecord<>(builderType, id, builder));
    }

    public <T extends Builder> void multiBuilder(BuilderType<T> builderType, Consumer<BuilderFactory<T>> builder) {
        this.handlers.add(dataRegistering -> builder.accept(new BuilderFactory<>(dataRegistering, builderType)));
    }

    public void duringGeneration(Runnable runnable) {
        this.handlers.add(dataRegistering -> runnable.run());
    }

    public <T extends RegisteringEntry<U, V>, U extends V, V> DataRegisteringEntry<T, U, V> forEntry(T entry) {
        return new DataRegisteringEntry<>(entry, this);
    }

    public String getModId() {
        return this.id;
    }

    public static DataRegistering of(String id) {
        return new DataRegistering(id);
    }
}

