package xyz.brassgoggledcoders.shadyskies.dataregistering.provider;

import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.function.BiFunction;

public class ProviderTypeImpl<T> implements ProviderType<T> {
    private final Class<T> tClass;
    private final BiFunction<String, GatherDataEvent, ? extends T> createProvider;

    public ProviderTypeImpl(Class<T> tClass, BiFunction<String, GatherDataEvent, ? extends T> createProvider) {
        this.tClass = tClass;
        this.createProvider = createProvider;
    }

    @Override
    public T cast(Object value) {
        if (this.tClass.isInstance(value)) {
            return this.tClass.cast(value);
        }
        throw new IllegalArgumentException(value + " is not an instance of " + tClass);
    }

    @Override
    public T create(String modId, GatherDataEvent event) {
        return this.createProvider.apply(modId, event);
    }
}
