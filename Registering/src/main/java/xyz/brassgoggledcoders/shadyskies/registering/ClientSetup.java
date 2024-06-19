package xyz.brassgoggledcoders.shadyskies.registering;

import com.google.common.base.Suppliers;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import xyz.brassgoggledcoders.shadyskies.registering.eventhandler.ClientEventHandler;
import xyz.brassgoggledcoders.shadyskies.registering.eventhandler.ScreenRegisteringObject;
import xyz.brassgoggledcoders.shadyskies.registering.menu.MenuRegisteringEntry;

import java.util.function.Supplier;

public class ClientSetup {
    public static void setupEventHandler(Registering registering, IEventBus modBus) {
        modBus.addListener(RegisterMenuScreensEvent.class, event -> ClientEventHandler.registerScreens(registering, event));
    }

    public static <M extends AbstractContainerMenu, S extends AbstractContainerScreen<M>> void addScreenRegisteringObject(
            Registering registering,
            MenuRegisteringEntry<M> menuRegisteringEntry,
            Supplier<Supplier<MenuScreens.ScreenConstructor<M, S>>> constructor
    ) {
        registering.addRegisteringObject(new ScreenRegisteringObject<>(
                menuRegisteringEntry,
                constructor
        ));
    }
}
