package xyz.brassgoggledcoders.shadyskies.registering;

public interface IStartedRegisteringBuilder<E, W> extends IRegisteringBuilder<E> {
    void start(W starting);
}
