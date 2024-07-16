package xyz.brassgoggledcoders.shadyskies.registering;

import com.mojang.datafixers.util.Function3;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType.MenuSupplier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.shadyskies.registering.block.BlockRegisteringBuilder;
import xyz.brassgoggledcoders.shadyskies.registering.blockentity.BlockEntityRegisteringBuilder;
import xyz.brassgoggledcoders.shadyskies.registering.eventhandler.CreativeTabsRegisteringObject;
import xyz.brassgoggledcoders.shadyskies.registering.item.ItemRegisteringBuilder;
import xyz.brassgoggledcoders.shadyskies.registering.menu.MenuRegisteringBuilder;
import xyz.brassgoggledcoders.shadyskies.registering.simple.SimpleBuildingRegisteringBuilder;
import xyz.brassgoggledcoders.shadyskies.registering.util.OneUseValue;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class Registering {
    private final String modId;

    private final Map<ResourceKey<? extends Registry<?>>, DeferredRegister<?>> deferredRegisters;
    private final List<IRegisteringEntry<?, ?>> registeringEntries;

    private final List<Object> registeringObjects;

    private IEventBus modBus;

    private final OneUseValue<String> name;

    public Registering(String modId) {
        this.modId = modId;
        this.deferredRegisters = new HashMap<>();
        this.registeringEntries = new ArrayList<>();
        this.registeringObjects = new ArrayList<>();
        this.name = new OneUseValue<>();
    }

    public IRegisteringEntry<?, ?> getRegisteringEntry(ResourceKey<? extends Registry<?>> registry, String name) {
        return this.getRegisteringEntries()
                .stream()
                .filter(registeringEntry -> registeringEntry.registryKey() == registry && registeringEntry.getId().getPath().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No registering entry found for " + name));
    }

    public <B extends IRegisteringBuilder<Registering, B, E>, E extends IRegisteringEntry<T, U>, T extends U, U> E register(
            BiFunction<Registering, String, B> builderCreator,
            String name,
            Function<B, E> building
    ) {
        return building.apply(builderCreator.apply(this, name));
    }

    public <B extends IRegisteringBuilder<P, B, E>, P, E extends IRegisteringEntry<T, U>, T extends U, U> B begin(
            Function3<Registering, P, String, B> builderCreator,
            P parent,
            String name
    ) {
        return builderCreator.apply(this, parent, name);
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
        Iterator<Object> registeringObjects = this.getRegisteringObjects();
        while (registeringObjects.hasNext()) {
            Object registeringObject = registeringObjects.next();
            if (registeringObject instanceof CreativeTabsRegisteringObject creativeTabsRegisteringObject) {
                creativeTabsRegisteringObject.buildCreativeTab(event);
                registeringObjects.remove();
            }
        }
    }

    public <T> Registry<T> createRegistry(ResourceKey<? extends Registry<T>> registryKey) {
        return this.getDeferredRegister(registryKey)
                .makeRegistry(builder -> {
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

    public Registering object(String name) {
        this.name.set(name);
        return this;
    }

    public <R, T extends R> IRegisteringEntry<T, R> simple(ResourceKey<? extends Registry<R>> registryKey, Supplier<T> supplier) {
        return new SimpleBuildingRegisteringBuilder<>(this, this, this.name.get(), registryKey, supplier)
                .register();
    }

    public <B extends Block> BlockRegisteringBuilder<Registering, B> block(Function<BlockBehaviour.Properties, B> blockCreator) {
        return this.block(this, blockCreator);
    }

    public <P, B extends Block> BlockRegisteringBuilder<P, B> block(P parent, Function<BlockBehaviour.Properties, B> blockCreator) {
        if (parent instanceof RegisteringBuilder<?, ?, ?, ?> registeringBuilder) {
            this.name.set(registeringBuilder.getName());
        }
        return new BlockRegisteringBuilder<>(
                this,
                parent,
                this.name.get(),
                blockCreator
        );
    }

    public <I extends Item> ItemRegisteringBuilder<Registering, I> item(Function<Item.Properties, I> itemCreator) {
        return this.item(this, itemCreator);
    }

    public <P, I extends Item> ItemRegisteringBuilder<P, I> item(P parent, Function<Item.Properties, I> itemCreator) {
        if (parent instanceof RegisteringBuilder<?, ?, ?, ?> registeringBuilder) {
            this.name.set(registeringBuilder.getName());
        }
        return new ItemRegisteringBuilder<>(
                this,
                parent,
                this.name.get(),
                itemCreator
        );
    }

    public <B extends BlockEntity> BlockEntityRegisteringBuilder<Registering, B> blockEntity(BlockEntityType.BlockEntitySupplier<B> supplier) {
        return this.blockEntity(this, supplier);
    }

    public <P, B extends BlockEntity> BlockEntityRegisteringBuilder<P, B> blockEntity(P parent, BlockEntityType.BlockEntitySupplier<B> supplier) {
        if (parent instanceof RegisteringBuilder<?, ?, ?, ?> registeringBuilder) {
            this.name.set(registeringBuilder.getName());
        }
        return new BlockEntityRegisteringBuilder<>(
                this,
                parent,
                this.name.get(),
                supplier
        );
    }

    public <M extends AbstractContainerMenu, S extends AbstractContainerScreen<M>> MenuRegisteringBuilder<Registering, M, S> menu(MenuSupplier<M> menuSupplier) {
        return this.menu(this, menuSupplier);
    }

    public <P, M extends AbstractContainerMenu, S extends AbstractContainerScreen<M>> MenuRegisteringBuilder<P, M, S> menu(P parent, MenuSupplier<M> menuSupplier) {
        if (parent instanceof RegisteringBuilder<?, ?, ?, ?> registeringBuilder) {
            this.name.set(registeringBuilder.getName());
        }
        return new MenuRegisteringBuilder<>(
                this,
                parent,
                this.name.get(),
                menuSupplier
        );
    }

    public static Registering of(String modId) {
        return new Registering(modId);
    }
}
