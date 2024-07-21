package xyz.brassgoggledcoders.shadyskies.registering.entity;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.shadyskies.registering.ClientSetup;
import xyz.brassgoggledcoders.shadyskies.registering.IRegisteringEntry;
import xyz.brassgoggledcoders.shadyskies.registering.Registering;
import xyz.brassgoggledcoders.shadyskies.registering.RegisteringBuilder;

import java.util.function.Function;
import java.util.function.Supplier;

public class EntityBuilder<P, E extends Entity> extends RegisteringBuilder<P, EntityBuilder<P, E>, EntityType<?>, EntityType<E>> {
    private final EntityType.EntityFactory<E> factory;

    private MobCategory mobCategory;
    private Function<EntityType.Builder<E>, EntityType.Builder<E>> builderFunction;

    private Supplier<Function<EntityRendererProvider.Context, EntityRenderer<E>>> rendererProvider;

    public EntityBuilder(Registering registering, P parent, String name, EntityType.EntityFactory<E> factory) {
        super(registering, parent, name, Registries.ENTITY_TYPE);
        this.factory = factory;
        this.mobCategory = MobCategory.MISC;
        this.builderFunction = entityBuilder -> entityBuilder;
    }

    public EntityBuilder<P, E> withCategory(MobCategory mobCategory) {
        this.mobCategory = mobCategory;
        return this;
    }

    public EntityBuilder<P, E> withBuilder(Function<EntityType.Builder<E>, EntityType.Builder<E>> builderFunction) {
        this.builderFunction = this.builderFunction.andThen(builderFunction);
        return this;
    }

    public EntityBuilder<P, E> withRenderer(Supplier<Function<EntityRendererProvider.Context, EntityRenderer<E>>> renderer) {
        this.rendererProvider = renderer;
        return this;
    }

    @Override
    protected void afterRegister(IRegisteringEntry<EntityType<E>, EntityType<?>> registeringEntry) {
        if (FMLEnvironment.dist.isClient() && this.rendererProvider != null) {
            ClientSetup.addEntityRenderer(this.getRegistering(), registeringEntry, this.rendererProvider);
        }
    }

    @Override
    protected @NotNull EntityType<E> create() {
        return EntityType.Builder.of(this.factory, this.mobCategory)
                .build(this.getRegistering().getModId() + ":" + this.getName());
    }

    @Override
    public @NotNull EntityBuilder<P, E> self() {
        return this;
    }

    @Override
    protected @NotNull IRegisteringEntry<EntityType<E>, EntityType<?>> createEntry() {
        return new EntityEntry<>(this.getRegistering(), this.createDeferredHolder());
    }

    @NotNull
    @Override
    public EntityEntry<E> register() {
        return (EntityEntry<E>) super.register();
    }
}
