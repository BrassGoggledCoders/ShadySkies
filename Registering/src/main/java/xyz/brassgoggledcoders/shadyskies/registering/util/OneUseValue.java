package xyz.brassgoggledcoders.shadyskies.registering.util;

import java.util.function.Supplier;

public class OneUseValue<T> implements Supplier<T> {
    private T value;

    public OneUseValue() {
        this(null);
    }

    public OneUseValue(T value) {
        this.value = value;
    }

    public void set(T value) {
        this.value = value;
    }

    @Override
    public T get() {
        T currentValue = value;
        this.value = null;
        if (currentValue == null) {
            throw new IllegalStateException("No value was set");
        }
        return currentValue;
    }
}
