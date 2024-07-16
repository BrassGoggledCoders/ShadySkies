package xyz.brassgoggledcoders.shadyskies.registering.simple;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.shadyskies.registering.Registering;
import xyz.brassgoggledcoders.shadyskies.registering.RegisteringBuilder;
import xyz.brassgoggledcoders.shadyskies.registering.RegisteringEntry;

import java.util.Objects;
import java.util.function.Supplier;

public class SimpleBuildingRegisteringBuilder<P, R, T extends R> extends RegisteringBuilder<P, SimpleBuildingRegisteringBuilder<P, R, T>, R, T> {
    private final Supplier<T> simpleSupplier;

    public SimpleBuildingRegisteringBuilder(Registering registering, P parent, String name,
                                            ResourceKey<? extends Registry<R>> registryKey, Supplier<T> simpleSupplier) {
        super(registering, parent, name, registryKey);
        this.simpleSupplier = Objects.requireNonNull(simpleSupplier);
    }

    @Override
    public @NotNull SimpleBuildingRegisteringBuilder<P, R, T> self() {
        return this;
    }

    @Override
    protected @NotNull T create() {
        return this.simpleSupplier.get();
    }

    @Override
    public @NotNull RegisteringEntry<T, R> register() {
        return (RegisteringEntry<T, R>) super.register();
    }
}
