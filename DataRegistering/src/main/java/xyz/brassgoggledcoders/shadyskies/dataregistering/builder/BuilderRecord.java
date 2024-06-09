package xyz.brassgoggledcoders.shadyskies.dataregistering.builder;

import net.minecraft.resources.ResourceLocation;
import xyz.brassgoggledcoders.shadyskies.dataregistering.DataRegistering;

import java.util.function.Consumer;

public record BuilderRecord<T extends Builder>(
        BuilderType<T> builderType,
        ResourceLocation id,
        Consumer<T> consumer
) implements Consumer<DataRegistering> {
    public void accept(DataRegistering dataRegistering) {
        this.consumer()
                .accept(this.builderType()
                        .create(dataRegistering, this.id())
                );
    }
}
