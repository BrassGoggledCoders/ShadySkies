package xyz.brassgoggledcoders.shadyskies.registering;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public interface IRegisteringEntry<T, B> extends Supplier<T> {
    @NotNull
    Holder<B> getHolder();

    @NotNull
    ResourceLocation getId();
}
