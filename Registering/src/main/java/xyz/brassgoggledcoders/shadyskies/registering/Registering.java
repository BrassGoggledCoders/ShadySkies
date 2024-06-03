package xyz.brassgoggledcoders.shadyskies.registering;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

@SuppressWarnings("unused")
public class Registering {
    private final String modId;

    private final Map<ResourceKey<? extends Registry<?>>, DeferredRegister<?>> deferredRegisters;

    private IEventBus modBus;

    public Registering(String modId) {
        this.modId = modId;
        this.deferredRegisters = new HashMap<>();
    }

    public <B extends IRegisteringBuilder<E>, E extends IRegisteringEntry<T, U>, T extends U, U> E register(
            BiFunction<Registering, String, B> builderCreator,
            String name,
            Function<B, E> building
    ) {
        return building.apply(this.begin(builderCreator, name));
    }

    public <B extends IRegisteringBuilder<E>, E extends IRegisteringEntry<T, U>, T extends U, U> B begin(
            BiFunction<Registering, String, B> builderCreator,
            String name
    ) {
        return builderCreator.apply(this, name);
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public <R> DeferredRegister<R> getDeferredRegister(ResourceKey<? extends Registry<R>> registryKey) {
        return (DeferredRegister<R>) deferredRegisters.computeIfAbsent(
                registryKey,
                key -> {
                    DeferredRegister<R> deferred = DeferredRegister.create(registryKey, modId);
                    if (this.modBus != null) {
                        deferred.register(this.modBus);
                    }
                    return deferred;
                }
        );
    }

    public void setModBus(IEventBus modBus) {
        this.modBus = modBus;
        for (DeferredRegister<?> deferredRegister : deferredRegisters.values()) {
            deferredRegister.register(this.modBus);
        }
    }

    public static Registering of(String modId) {
        return new Registering(modId);
    }
}
