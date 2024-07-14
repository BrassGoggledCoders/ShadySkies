package xyz.brassgoggledcoders.shadyskies.dataregistering.codec;

import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import xyz.brassgoggledcoders.shadyskies.dataregistering.provider.ProviderType;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public record CodecProviderType<T>(
        ResourceKey<Registry<T>> registryKey
) implements ProviderType<CodecCollector<T>> {
    private static final Map<ResourceKey<?>, CodecProviderType<?>> TYPE_CACHE = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public CodecCollector<T> cast(Object value) {
        if (value instanceof CodecCollector) {
            return (CodecCollector<T>) value;
        }
        throw new IllegalArgumentException("Cannot cast " + value + " to CodecCollector");
    }

    @Override
    public CodecCollector<T> create(String modId, GatherDataEvent event) {
        CodecCollector<T> codecCollector = new CodecCollector<>(this.registryKey());
        event.getGenerator()
                .addProvider(
                        event.includeServer(),
                        (DataProvider.Factory<DataProvider>) packOutput -> new DatapackBuiltinEntriesProvider(
                                packOutput,
                                event.getLookupProvider(),
                                new RegistrySetBuilder()
                                        .add(this.registryKey(), codecCollector::collect),
                                Set.of(modId)
                        )
                );
        return codecCollector;
    }

    @SuppressWarnings("unchecked")
    public static <T> CodecProviderType<T> get(ResourceKey<Registry<T>> registry) {
        if (TYPE_CACHE.containsKey(registry)) {
            return (CodecProviderType<T>) TYPE_CACHE.get(registry);
        } else {
            CodecProviderType<T> newType = new CodecProviderType<>(registry);
            TYPE_CACHE.put(registry, newType);
            return newType;
        }
    }
}
