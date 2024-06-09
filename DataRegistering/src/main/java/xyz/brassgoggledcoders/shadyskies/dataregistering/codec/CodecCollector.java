package xyz.brassgoggledcoders.shadyskies.dataregistering.codec;

import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class CodecCollector<T> {
    private final ResourceKey<Registry<T>> registryKey;
    private final Map<ResourceLocation, T> values;

    public CodecCollector(ResourceKey<Registry<T>> registryKey) {
        this.registryKey = registryKey;
        this.values = new HashMap<>();
    }

    public void add(ResourceLocation key, T value) {
        values.put(key, value);
    }

    public void collect(BootstapContext<T> bootstrap) {
        for (Map.Entry<ResourceLocation, T> entry : values.entrySet()) {
            bootstrap.register(
                    ResourceKey.create(registryKey, entry.getKey()),
                    entry.getValue()
            );
        }
    }
}
