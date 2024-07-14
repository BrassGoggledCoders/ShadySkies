package xyz.brassgoggledcoders.shadyskies.dataregistering.provider;

import net.minecraft.data.DataProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DeferredProvider<T> {
    private final List<Consumer<T>> deferredActions = new ArrayList<>();

    public void deferred(Consumer<T> deferredAction) {
        this.deferredActions.add(deferredAction);
    }

    public List<Consumer<T>> getDeferredActions() {
        return this.deferredActions;
    }
}
