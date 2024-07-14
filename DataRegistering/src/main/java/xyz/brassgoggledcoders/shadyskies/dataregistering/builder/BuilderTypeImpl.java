package xyz.brassgoggledcoders.shadyskies.dataregistering.builder;

import net.minecraft.resources.ResourceLocation;
import xyz.brassgoggledcoders.shadyskies.dataregistering.DataRegistering;

import java.util.function.BiFunction;

public class BuilderTypeImpl<T extends Builder> implements BuilderType<T> {
    private final BiFunction<DataRegistering, ResourceLocation, ? extends T> createBuilder;

    public BuilderTypeImpl(BiFunction<DataRegistering, ResourceLocation, ? extends T> createBuilder) {
        this.createBuilder = createBuilder;
    }

    @Override
    public T create(DataRegistering dataRegistering, ResourceLocation id) {
        return this.createBuilder.apply(dataRegistering, id);
    }
}
