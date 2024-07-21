package xyz.brassgoggledcoders.shadyskies.registering;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public abstract class RegisteringBuilder<P, B extends RegisteringBuilder<P, B, R, T>, R, T extends R>
        implements IRegisteringBuilder<P, B, IRegisteringEntry<T, R>> {
    private final Registering registering;
    private final P parent;
    private final String name;
    private final ResourceKey<? extends Registry<R>> registryKey;

    public RegisteringBuilder(Registering registering, P parent, String name, ResourceKey<? extends Registry<R>> registryKey) {
        this.registering = registering;
        this.parent = parent;
        this.name = name;
        this.registryKey = registryKey;
    }

    public Registering getRegistering() {
        return registering;
    }

    public @NotNull P getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }

    public ResourceKey<? extends Registry<R>> getRegistryKey() {
        return registryKey;
    }

    @Override
    public @NotNull P build() {
        this.register();
        return this.getParent();
    }

    @Override
    public @NotNull IRegisteringEntry<T, R> register() {
        IRegisteringEntry<T, R> entry = this.createEntry();
        this.getRegistering()
                .addRegisteringEntry(entry);
        this.afterRegister(entry);
        return entry;
    }

    protected void afterRegister(IRegisteringEntry<T, R> entry) {

    }

    @NotNull
    protected IRegisteringEntry<T, R> createEntry() {
        return new RegisteringEntry<>(this.getRegistering(), this.createDeferredHolder(), this.registryKey);
    }

    protected DeferredHolder<R, T> createDeferredHolder() {
        return this.getRegistering()
                .getDeferredRegister(this.getRegistryKey())
                .register(this.getName(), this::create);
    }

    @NotNull
    protected abstract T create();
}
