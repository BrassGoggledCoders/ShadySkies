package xyz.brassgoggledcoders.shadyskies.registering.simple;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import xyz.brassgoggledcoders.shadyskies.registering.IRegisteringBuilder;
import xyz.brassgoggledcoders.shadyskies.registering.Registering;
import xyz.brassgoggledcoders.shadyskies.registering.RegisteringEntry;

import java.util.Objects;
import java.util.function.Supplier;

public class SimpleBuildingRegisteringBuilder<R, T extends R> implements IRegisteringBuilder<RegisteringEntry<T, R>> {

    private final Registering registering;
    private final String name;

    private ResourceKey<? extends Registry<R>> registryKey;
    private Supplier<T> simpleSupplier;

    public SimpleBuildingRegisteringBuilder(Registering registering, String name) {
        this.registering = registering;
        this.name = name;
    }

    public SimpleBuildingRegisteringBuilder<R, T> withRegistryKey(ResourceKey<? extends Registry<R>> registryKey) {
        this.registryKey = registryKey;
        return this;
    }

    public SimpleBuildingRegisteringBuilder<R, T> withSupplier(Supplier<T> supplier) {
        this.simpleSupplier = supplier;
        return this;
    }


    @Override
    public RegisteringEntry<T, R> build() {
        RegisteringEntry<T, R> registeringEntry = new RegisteringEntry<>(
                registering.getDeferredRegister(Objects.requireNonNull(this.registryKey))
                        .register(this.name, Objects.requireNonNull(this.simpleSupplier))
        );

        this.registering.addRegisteringEntry(registeringEntry);
        return registeringEntry;
    }
}
