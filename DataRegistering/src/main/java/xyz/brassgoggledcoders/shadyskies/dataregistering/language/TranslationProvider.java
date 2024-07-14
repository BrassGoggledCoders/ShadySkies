package xyz.brassgoggledcoders.shadyskies.dataregistering.language;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import xyz.brassgoggledcoders.shadyskies.dataregistering.provider.ProviderType;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface TranslationProvider {
    ProviderType<TranslationProvider> TYPE = ProviderType.createType(
            TranslationProvider.class,
            (id, event) -> {
                RegisteringLanguageProvider provider = new RegisteringLanguageProvider(
                        event.getGenerator()
                                .getPackOutput(),
                        id,
                        "en_us"
                );

                event.getGenerator()
                        .addProvider(event.includeClient(), provider);

                return provider;
            }
    );

    Component addTranslation(String key, String translation);

    Component addTranslation(String type, String key, String translation);

    Component addTranslation(String type, String key, String suffix, String translation);

    Component addTranslation(String type, ResourceLocation id, String translation);
}
