package xyz.brassgoggledcoders.shadyskies.registering.eventhandler;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import xyz.brassgoggledcoders.shadyskies.registering.IRegisteringEntry;

import java.util.function.Supplier;

public record ScreenRegisteringObject<M extends AbstractContainerMenu, S extends AbstractContainerScreen<M>>(
        IRegisteringEntry<MenuType<M>, MenuType<?>> entry,
        Supplier<MenuScreens.ScreenConstructor<M, S>> constructor
) {
    public void register(RegisterMenuScreensEvent event) {
        event.register(
                this.entry()
                        .get(),
                this.constructor()
                        .get()
        );
    }
}
