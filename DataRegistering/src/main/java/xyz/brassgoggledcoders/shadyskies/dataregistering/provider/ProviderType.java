package xyz.brassgoggledcoders.shadyskies.dataregistering.provider;

import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.function.BiFunction;

public interface ProviderType<T> {

    T cast(Object value);

    T create(String modId, GatherDataEvent event);

    static <U> ProviderType<U> createType(Class<U> tClass, BiFunction<String, GatherDataEvent, ? extends U> providerCreator) {
        return new ProviderTypeImpl<>(tClass, providerCreator);
    }
}
