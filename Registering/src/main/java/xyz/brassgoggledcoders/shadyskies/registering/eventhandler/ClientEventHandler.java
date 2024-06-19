package xyz.brassgoggledcoders.shadyskies.registering.eventhandler;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import xyz.brassgoggledcoders.shadyskies.registering.Registering;
import xyz.brassgoggledcoders.shadyskies.registering.menu.MenuRegisteringEntry;

import java.util.Iterator;
import java.util.function.Supplier;

public class ClientEventHandler {


    public static void registerScreens(Registering registering, RegisterMenuScreensEvent event) {
        Iterator<Object> registeringObjects = registering.getRegisteringObjects();
        while (registeringObjects.hasNext()) {
            Object registeringObject = registeringObjects.next();
            if (registeringObject instanceof ScreenRegisteringObject<?, ?> screenRegisteringObject) {
                screenRegisteringObject.register(event);
                registeringObjects.remove();
            }
        }
    }
}
