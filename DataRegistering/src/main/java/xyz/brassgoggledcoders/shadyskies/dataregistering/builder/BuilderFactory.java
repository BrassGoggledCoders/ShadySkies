package xyz.brassgoggledcoders.shadyskies.dataregistering.builder;

import net.minecraft.resources.ResourceLocation;
import xyz.brassgoggledcoders.shadyskies.dataregistering.DataRegistering;

public class BuilderFactory<T extends Builder> {
    private final DataRegistering dataRegistering;
    private final BuilderType<T> builderType;

    public BuilderFactory(DataRegistering dataRegistering, BuilderType<T> builderType) {
        this.dataRegistering = dataRegistering;
        this.builderType = builderType;
    }

    public T start(String path) {
        return this.builderType.create(
                dataRegistering,
                new ResourceLocation(dataRegistering.getModId(), path)
        );
    }
}
