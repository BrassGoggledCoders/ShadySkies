package xyz.brassgoggledcoders.shadyskies.registering.entity;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import xyz.brassgoggledcoders.shadyskies.registering.IRegisteringEntry;

import java.util.function.Function;
import java.util.function.Supplier;

public record EntityRenderingObject<E extends Entity>(
        IRegisteringEntry<EntityType<E>, EntityType<?>> entityType,
        Supplier<Function<EntityRendererProvider.Context, EntityRenderer<E>>> entityRendererFactory
) {
    public void handleEvent(EntityRenderersEvent.RegisterRenderers entityRenderersEvent) {
        entityRenderersEvent.registerEntityRenderer(
                this.entityType()
                        .get(),
                this.entityRendererFactory.get()::apply
        );
    }
}
