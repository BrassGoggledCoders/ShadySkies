package xyz.brassgoggledcoders.shadyskies.dataregistering.builder;

import net.minecraft.resources.ResourceLocation;
import xyz.brassgoggledcoders.shadyskies.dataregistering.DataRegistering;

import java.util.function.BiFunction;

public interface BuilderType<T extends Builder> {
    T create(DataRegistering dataRegistering, ResourceLocation id);

    static <V extends Builder> BuilderType<V> createType(BiFunction<DataRegistering, ResourceLocation, ? extends V> builderCreator) {
        return new BuilderTypeImpl<>(builderCreator);
    }
}
