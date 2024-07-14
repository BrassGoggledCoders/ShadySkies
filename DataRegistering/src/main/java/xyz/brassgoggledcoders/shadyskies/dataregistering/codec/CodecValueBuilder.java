package xyz.brassgoggledcoders.shadyskies.dataregistering.codec;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import xyz.brassgoggledcoders.shadyskies.dataregistering.DataRegistering;
import xyz.brassgoggledcoders.shadyskies.dataregistering.builder.Builder;

public class CodecValueBuilder<T> implements Builder {
    private final DataRegistering dataRegistering;
    private final ResourceKey<Registry<T>> registry;
    private final ResourceLocation id;
    private T value;

    public CodecValueBuilder(DataRegistering dataRegistering, ResourceLocation id, ResourceKey<Registry<T>> registry) {
        this.dataRegistering = dataRegistering;
        this.id = id;
        this.registry = registry;
    }

    public CodecValueBuilder<T> withValue(T value) {
        this.value = value;
        return this;
    }

    @Override
    public void build() {
        this.dataRegistering.getProvider(CodecProviderType.get(registry))
                .add(this.id, this.value);
    }
}
