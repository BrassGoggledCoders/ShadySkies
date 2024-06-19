package xyz.brassgoggledcoders.shadyskies.dataregistering.provider;

import com.mojang.datafixers.util.Function3;
import net.minecraft.data.DataProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DeferredProviderType<T> implements ProviderType<DeferredProvider<T>> {
    private final Function3<String, GatherDataEvent, Supplier<List<Consumer<T>>>, ? extends T> createProvider;

    public DeferredProviderType(Function3<String, GatherDataEvent, Supplier<List<Consumer<T>>>, ? extends T> createProvider) {
        this.createProvider = createProvider;
    }

    @Override
    @SuppressWarnings("unchecked")
    public DeferredProvider<T> cast(Object value) {
        if (value instanceof DeferredProvider<?>) {
            return (DeferredProvider<T>) value;
        }
        throw new IllegalArgumentException(value + " is not an instance of deferred provider");
    }

    @Override
    public DeferredProvider<T> create(String modId, GatherDataEvent event) {
        DeferredProvider<T> deferredProvider = new DeferredProvider<>();
        createProvider.apply(modId, event, deferredProvider::getDeferredActions);
        return deferredProvider;
    }
}