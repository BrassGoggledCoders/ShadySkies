package xyz.brassgoggledcoders.shadyskies.registering.menu;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.fml.loading.FMLEnvironment;
import xyz.brassgoggledcoders.shadyskies.registering.ClientSetup;
import xyz.brassgoggledcoders.shadyskies.registering.IRegisteringBuilder;
import xyz.brassgoggledcoders.shadyskies.registering.Registering;

import java.util.Objects;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class MenuRegisteringBuilder<M extends AbstractContainerMenu, S extends AbstractContainerScreen<M>> implements IRegisteringBuilder<MenuRegisteringEntry<M>> {
    private final Registering registering;
    private final String name;

    private MenuType.MenuSupplier<M> menuSupplier;
    private FeatureFlagSet featureFlags;
    private Supplier<Supplier<MenuScreens.ScreenConstructor<M, S>>> screenConstructor;

    public MenuRegisteringBuilder(Registering registering, String name) {
        this.registering = registering;
        this.name = name;
        this.featureFlags = FeatureFlags.VANILLA_SET;
    }

    public MenuRegisteringBuilder<M, S> withMenuSupplier(MenuType.MenuSupplier<M> menuSupplier) {
        this.menuSupplier = menuSupplier;
        return this;
    }

    public MenuRegisteringBuilder<M, S> withFeatureFlags(FeatureFlagSet featureFlags) {
        this.featureFlags = featureFlags;
        return this;
    }

    public MenuRegisteringBuilder<M, S> withScreenConstructor(Supplier<Supplier<MenuScreens.ScreenConstructor<M, S>>> screenConstructor) {
        this.screenConstructor = screenConstructor;
        return this;
    }

    @Override
    public MenuRegisteringEntry<M> build() {
        MenuRegisteringEntry<M> registeringEntry = new MenuRegisteringEntry<>(registering.getDeferredRegister(Registries.MENU)
                .register(this.name, () -> new MenuType<>(
                        Objects.requireNonNull(this.menuSupplier),
                        this.featureFlags
                ))
        );

        registering.addRegisteringEntry(registeringEntry);
        if (FMLEnvironment.dist.isClient()) {
            ClientSetup.addScreenRegisteringObject(registering, registeringEntry, this.screenConstructor);
        }

        return registeringEntry;
    }

    public static <ME extends AbstractContainerMenu, SC extends AbstractContainerScreen<ME>> MenuRegisteringBuilder<ME, SC> begin(
            Registering registering,
            String name
    ) {
        return registering.begin(
                MenuRegisteringBuilder::new,
                name
        );
    }
}
