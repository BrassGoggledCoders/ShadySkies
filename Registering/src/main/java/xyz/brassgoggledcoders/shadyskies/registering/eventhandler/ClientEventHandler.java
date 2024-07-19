package xyz.brassgoggledcoders.shadyskies.registering.eventhandler;

import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import xyz.brassgoggledcoders.shadyskies.registering.Registering;
import xyz.brassgoggledcoders.shadyskies.registering.entity.EntityRenderingObject;

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

    public static void registerEntityRenders(Registering registering, EntityRenderersEvent.RegisterRenderers event) {
        Iterator<Object> registeringObjects = registering.getRegisteringObjects();
        while (registeringObjects.hasNext()) {
            Object registeringObject = registeringObjects.next();
            if (registeringObject instanceof EntityRenderingObject<?> screenRegisteringObject) {
                screenRegisteringObject.handleEvent(event);
                registeringObjects.remove();
            }
        }
    }
}
