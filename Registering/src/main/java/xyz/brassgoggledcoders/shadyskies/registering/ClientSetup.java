package xyz.brassgoggledcoders.shadyskies.registering;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import xyz.brassgoggledcoders.shadyskies.registering.entity.EntityRenderingObject;
import xyz.brassgoggledcoders.shadyskies.registering.eventhandler.ClientEventHandler;
import xyz.brassgoggledcoders.shadyskies.registering.eventhandler.ScreenRegisteringObject;

import java.util.function.Function;
import java.util.function.Supplier;

public class ClientSetup {
    public static void setupEventHandler(Registering registering, IEventBus modBus) {
        modBus.addListener(RegisterMenuScreensEvent.class, event -> ClientEventHandler.registerScreens(registering, event));
        modBus.addListener(EntityRenderersEvent.RegisterRenderers.class, event -> ClientEventHandler.registerEntityRenders(registering, event));
    }

    public static <M extends AbstractContainerMenu, S extends AbstractContainerScreen<M>> void addScreenRegisteringObject(
            Registering registering,
            IRegisteringEntry<MenuType<M>, MenuType<?>> menuRegisteringEntry,
            Supplier<MenuScreens.ScreenConstructor<M, S>> constructor
    ) {
        registering.addRegisteringObject(new ScreenRegisteringObject<>(
                menuRegisteringEntry,
                constructor
        ));
    }

    public static <E extends Entity> void addEntityRenderer(
            Registering registering,
            IRegisteringEntry<EntityType<E>, EntityType<?>> entityType,
            Supplier<Function<EntityRendererProvider.Context, EntityRenderer<E>>> entityRendererSupplier
    ) {
        registering.addRegisteringObject(new EntityRenderingObject<>(
                entityType,
                entityRendererSupplier
        ));
    }
}
