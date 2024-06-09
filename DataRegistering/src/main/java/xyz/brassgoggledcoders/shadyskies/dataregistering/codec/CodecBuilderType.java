package xyz.brassgoggledcoders.shadyskies.dataregistering.codec;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import xyz.brassgoggledcoders.shadyskies.dataregistering.DataRegistering;
import xyz.brassgoggledcoders.shadyskies.dataregistering.builder.BuilderType;

import java.util.HashMap;
import java.util.Map;

public class CodecBuilderType<T> implements BuilderType<CodecValueBuilder<T>> {
    private static final Map<ResourceKey<?>, CodecBuilderType<?>> TYPE_CACHE = new HashMap<>();

    private final ResourceKey<Registry<T>> registryKey;

    public CodecBuilderType(ResourceKey<Registry<T>> registryKey) {
        this.registryKey = registryKey;
    }

    @Override
    public CodecValueBuilder<T> create(DataRegistering dataRegistering, ResourceLocation id) {
        return new CodecValueBuilder<>(dataRegistering, id, registryKey);
    }

    @SuppressWarnings("unchecked")
    public static <T> CodecBuilderType<T> get(ResourceKey<Registry<T>> registry) {
        if (TYPE_CACHE.containsKey(registry)) {
            return (CodecBuilderType<T>) TYPE_CACHE.get(registry);
        } else {
            CodecBuilderType<T> newType = new CodecBuilderType<>(registry);
            TYPE_CACHE.put(registry, newType);
            return newType;
        }
    }
}
