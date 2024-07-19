package xyz.brassgoggledcoders.shadyskies.registering.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import xyz.brassgoggledcoders.shadyskies.registering.Registering;
import xyz.brassgoggledcoders.shadyskies.registering.RegisteringEntry;

public class EntityEntry<E extends Entity> extends RegisteringEntry<EntityType<E>, EntityType<?>> {
    public EntityEntry(Registering registering, DeferredHolder<EntityType<?>, EntityType<E>> holder) {
        super(registering, holder, Registries.ENTITY_TYPE);
    }
}
