package xyz.brassgoggledcoders.shadyskies.dataregistering.language;

import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.HashMap;
import java.util.Map;

public class RegisteringLanguageProvider extends LanguageProvider implements TranslationProvider {
    private final Map<String, String> translations;
    private final String modid;

    public RegisteringLanguageProvider(PackOutput output, String modid, String locale) {
        super(output, modid, locale);
        this.translations = new HashMap<>();
        this.modid = modid;
    }

    @Override
    protected void addTranslations() {
        this.translations.forEach(this::add);
    }

    @Override
    public Component addTranslation(String key, String translation) {
        translations.put(key, translation);
        return Component.translatable(key);
    }

    @Override
    public Component addTranslation(String type, String key, String translation) {
        return addTranslation(
                type + "." + modid + "." + key,
                translation
        );
    }

    @Override
    public Component addTranslation(String type, String key, String suffix, String translation) {
        return addTranslation(
                type + "." + modid + "." + key + "." + suffix,
                translation
        );
    }

    @Override
    public Component addTranslation(String type, ResourceLocation id, String translation) {
        return addTranslation(
                type + "." + id.getNamespace() + "." + id.getPath().replace("/", "."),
                translation
        );
    }
}
