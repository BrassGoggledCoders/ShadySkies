package xyz.brassgoggledcoders.shadyskies.registering;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class RegisteringEntry<T extends B, B> implements IRegisteringEntry<T, B>, Supplier<T> {
    private final DeferredHolder<B, T> holder;

    public RegisteringEntry(DeferredHolder<B, T> holder) {
        this.holder = holder;
    }

    @Override
    @NotNull
    public Holder<B> getHolder() {
        return this.holder;
    }

    @Override
    @NotNull
    public ResourceLocation getId() {
        return this.holder.getId();
    }

    @Override
    public T get() {
        return this.holder.value();
    }
}
