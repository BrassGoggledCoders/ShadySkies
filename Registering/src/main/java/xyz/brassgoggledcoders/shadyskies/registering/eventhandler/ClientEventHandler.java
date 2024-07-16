package xyz.brassgoggledcoders.shadyskies.registering.eventhandler;

import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import xyz.brassgoggledcoders.shadyskies.registering.Registering;

import java.util.Iterator;

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
