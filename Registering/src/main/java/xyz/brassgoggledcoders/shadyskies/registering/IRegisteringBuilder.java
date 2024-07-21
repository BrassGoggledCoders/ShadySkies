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

    default <P2, B2 extends IRegisteringBuilder<P2, B2, E2>, E2> B2 transform(Function<B, B2> transform) {
        return transform.apply(this.self());
    }
}
