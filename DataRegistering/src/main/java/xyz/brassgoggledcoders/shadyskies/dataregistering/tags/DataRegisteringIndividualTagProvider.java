package xyz.brassgoggledcoders.shadyskies.dataregistering.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DataRegisteringIndividualTagProvider<T> extends TagsProvider<T> {
    private final List<DataRegisteringTagAppender<T>> tagAppenderList = new ArrayList<>();

    public DataRegisteringIndividualTagProvider(PackOutput packOutput, ResourceKey<? extends Registry<T>> registryKey,
                                                CompletableFuture<HolderLookup.Provider> lookupProvider,
                                                String modId, ExistingFileHelper existingFileHelper) {
        super(packOutput, registryKey, lookupProvider, modId, existingFileHelper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        for (DataRegisteringTagAppender<T> tagAppender : tagAppenderList) {
            tagAppender.run(provider, this::tag);
        }
    }

    public DataRegisteringTagAppender<T> createTagAppender(TagKey<T> tTagKey) {
        DataRegisteringTagAppender<T> tagAppender = new DataRegisteringTagAppender<>(tTagKey);
        tagAppenderList.add(tagAppender);
        return tagAppender;
    }
}
