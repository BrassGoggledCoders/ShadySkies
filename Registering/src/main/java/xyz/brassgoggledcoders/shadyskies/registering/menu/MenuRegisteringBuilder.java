package xyz.brassgoggledcoders.shadyskies.registering.menu;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.MenuType.MenuSupplier;
import net.neoforged.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.shadyskies.registering.*;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class MenuRegisteringBuilder<P, M extends AbstractContainerMenu, S extends AbstractContainerScreen<M>>
        extends RegisteringBuilder<P, MenuRegisteringBuilder<P, M, S>, MenuType<?>, MenuType<M>> {
    private final MenuSupplier<M> menuSupplier;

    private FeatureFlagSet featureFlags;
    private Supplier<MenuScreens.ScreenConstructor<M, S>> screenConstructor;

    public MenuRegisteringBuilder(Registering registering, P parent, String name, MenuSupplier<M> menuSupplier) {
        super(registering, parent, name, Registries.MENU);
        this.menuSupplier = menuSupplier;
        this.featureFlags = FeatureFlags.VANILLA_SET;
    }

    public MenuRegisteringBuilder<P, M, S> withFeatureFlags(FeatureFlagSet featureFlags) {
        this.featureFlags = featureFlags;
        return this;
    }

    public MenuRegisteringBuilder<P, M, S> withScreenConstructor(Supplier<MenuScreens.ScreenConstructor<M, S>> screenConstructor) {
        this.screenConstructor = screenConstructor;
        return this;
    }

    @Override
    protected void afterRegister(IRegisteringEntry<MenuType<M>, MenuType<?>> registeringEntry) {
        if (FMLEnvironment.dist.isClient()) {
            ClientSetup.addScreenRegisteringObject(this.getRegistering(), registeringEntry, this.screenConstructor);
        }
    }

    @Override
    public @NotNull MenuRegisteringBuilder<P, M, S> self() {
        return this;
    }

    @Override
    protected @NotNull MenuRegisteringEntry<M> createEntry() {
        return new MenuRegisteringEntry<>(this.getRegistering(), this.createDeferredHolder());
    }

    @Override
    public @NotNull MenuRegisteringEntry<M> register() {
        return (MenuRegisteringEntry<M>) super.register();
    }

    @Override
    protected @NotNull MenuType<M> create() {
        return new MenuType<>(this.menuSupplier, this.featureFlags);
    }
}
