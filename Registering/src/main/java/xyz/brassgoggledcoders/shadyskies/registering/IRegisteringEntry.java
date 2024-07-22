package xyz.brassgoggledcoders.shadyskies.registering;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public interface IRegisteringEntry<T, B> extends Supplier<T> {
    @NotNull
    Holder<B> getHolder();

    @NotNull
    ResourceLocation getId();

    @NotNull
    default String getName() {
        return this.getId()
                .getPath();
    }

    ResourceKey<? extends Registry<B>> registryKey();

    ResourceKey<B> getKey();

    <T2, B2> IRegisteringEntry<T2, B2> getSibling(ResourceKey<? extends Registry<B2>> registryKey);
}
