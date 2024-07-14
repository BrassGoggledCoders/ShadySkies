package xyz.brassgoggledcoders.shadyskies.registering;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.shadyskies.registering.item.ItemRegisteringEntry;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

@SuppressWarnings("unused")
public class Registering {
    private final String modId;

    private final Map<ResourceKey<? extends Registry<?>>, DeferredRegister<?>> deferredRegisters;
    private final List<IRegisteringEntry<?, ?>> registeringEntries;

    private final List<Object> registeringObjects;

    private IEventBus modBus;

    public Registering(String modId) {
        this.modId = modId;
        this.deferredRegisters = new HashMap<>();
        this.registeringEntries = new ArrayList<>();
        this.registeringObjects = new ArrayList<>();
    }

    public <B extends IRegisteringBuilder<E>, E extends IRegisteringEntry<T, U>, T extends U, U> E register(
            BiFunction<Registering, String, B> builderCreator,
            String name,
            Function<B, E> building
    ) {
        return building.apply(this.begin(builderCreator, name));
    }

    public <B extends IRegisteringBuilder<E>, E extends IRegisteringEntry<T, U>, T extends U, U> B begin(
            BiFunction<Registering, String, B> builderCreator,
            String name
    ) {
        return builderCreator.apply(this, name);
    }

    public <B extends IStartedRegisteringBuilder<E, W>, E extends IRegisteringEntry<T, U>, T extends U, U, W> B begin(
            BiFunction<Registering, String, B> builderCreator,
            String name,
            W beginningValue
    ) {
        B value = builderCreator.apply(this, name);
        value.start(beginningValue);
        return value;
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public <R> DeferredRegister<R> getDeferredRegister(ResourceKey<? extends Registry<R>> registryKey) {
        return (DeferredRegister<R>) deferredRegisters.computeIfAbsent(
                registryKey,
                key -> {
                    DeferredRegister<R> deferred = DeferredRegister.create(registryKey, modId);
                    if (this.modBus != null) {
                        deferred.register(this.modBus);
                    }
                    return deferred;
                }
        );
    }

    public void setModBus(IEventBus modBus) {
        this.modBus = modBus;

        this.modBus.addListener(this::finishLoad);
        for (DeferredRegister<?> deferredRegister : deferredRegisters.values()) {
            deferredRegister.register(this.modBus);
        }

        this.modBus.addListener(this::handleCreativeTabs);

        if (FMLEnvironment.dist.isClient()) {
            ClientSetup.setupEventHandler(this, modBus);
        }
    }

    public void handleCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        this.getRegisteringEntries()
                .forEach(registeringEntry -> {
                    if (registeringEntry instanceof ItemRegisteringEntry<?> itemRegisteringEntry) {
                        itemRegisteringEntry.buildCreativeTab(event);
                    }
                });
    }

    public void addRegisteringEntry(IRegisteringEntry<?, ?> registeringEntry) {
        this.registeringEntries.add(registeringEntry);
    }

    public List<IRegisteringEntry<?, ?>> getRegisteringEntries() {
        return this.registeringEntries;
    }

    public void addRegisteringObject(Object registeringObject) {
        this.registeringObjects.add(registeringObject);
    }

    public Iterator<Object> getRegisteringObjects() {
        return registeringObjects.iterator();
    }

    public void finishLoad(FMLLoadCompleteEvent event) {
        this.registeringObjects.clear();
    }

    public static Registering of(String modId) {
        return new Registering(modId);
    }
}
