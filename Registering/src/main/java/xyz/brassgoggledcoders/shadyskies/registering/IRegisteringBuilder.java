package xyz.brassgoggledcoders.shadyskies.registering;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface IRegisteringBuilder<P, B extends IRegisteringBuilder<P, B, E>, E> {
    @NotNull
    P getParent();

    @NotNull
    E register();

    @NotNull
    P build();

    @NotNull
    B self();

    default <B2 extends IRegisteringBuilder<P, B2, E>> B2 transform(Function<B, B2> transform) {
        return transform.apply(this.self());
    }
}
