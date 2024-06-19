package xyz.brassgoggledcoders.shadyskies.dataregistering.language;

import net.minecraft.resources.ResourceLocation;
import xyz.brassgoggledcoders.shadyskies.dataregistering.DataRegistering;
import xyz.brassgoggledcoders.shadyskies.dataregistering.builder.Builder;
import xyz.brassgoggledcoders.shadyskies.dataregistering.builder.BuilderType;

import java.util.Objects;

@SuppressWarnings("unused")
public class TranslationBuilder implements Builder {
    public static final BuilderType<TranslationBuilder> TYPE = BuilderType.createType(TranslationBuilder::new);

    private final DataRegistering dataRegistering;
    private final ResourceLocation id;

    private String type;
    private String translation;

    public TranslationBuilder(DataRegistering dataRegistering, ResourceLocation id) {
        this.dataRegistering = dataRegistering;
        this.id = id;
    }

    public TranslationBuilder withType(String type) {
        this.type = type;
        return this;
    }

    public TranslationBuilder withTranslation(String translation) {
        this.translation = translation;
        return this;
    }


    @Override
    public void build() {
        this.dataRegistering.getProvider(TranslationProvider.TYPE)
                .addTranslation(
                        Objects.requireNonNull(type),
                        id,
                        Objects.requireNonNull(translation)
                );
    }
}
