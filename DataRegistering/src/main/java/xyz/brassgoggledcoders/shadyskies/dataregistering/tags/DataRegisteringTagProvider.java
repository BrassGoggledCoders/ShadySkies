package xyz.brassgoggledcoders.shadyskies.dataregistering.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class DataRegisteringTagProvider implements DataProvider {

    private final PackOutput packOutput;
    private final CompletableFuture<HolderLookup.Provider> lookupProvider;
    private final String modid;
    private final ExistingFileHelper fileHelper;

    private final Map<ResourceKey<?>, DataRegisteringIndividualTagProvider<?>> tagProviders;

    public DataRegisteringTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider,
                                      String modid, ExistingFileHelper fileHelper) {
        this.packOutput = packOutput;
        this.lookupProvider = lookupProvider;
        this.modid = modid;
        this.fileHelper = fileHelper;

        this.tagProviders = new HashMap<>();
    }

    @SuppressWarnings("unused")
    public <T> DataRegisteringTagAppender<T> tag(TagKey<T> tTagKey) {
        return this.getTagProvider(tTagKey.registry())
                .createTagAppender(tTagKey);
    }

    @SuppressWarnings({"unchecked", "RedundantCast"})
    private <T> DataRegisteringIndividualTagProvider<T> getTagProvider(ResourceKey<? extends Registry<T>> tResourceKey) {
        DataRegisteringIndividualTagProvider<T> tagProvider = (DataRegisteringIndividualTagProvider<T>) this.tagProviders.get(tResourceKey);
        if (tagProvider == null) {
            tagProvider = new DataRegisteringIndividualTagProvider<>(
                    this.packOutput,
                    tResourceKey,
                    this.lookupProvider,
                    this.modid,
                    this.fileHelper
            );
            this.tagProviders.put((ResourceKey<?>) tResourceKey, tagProvider);
        }

        return tagProvider;
    }

    @Override
    @NotNull
    public CompletableFuture<?> run(@NotNull CachedOutput cachedOutput) {
        CompletableFuture<?>[] tagProviderFutures = this.tagProviders.values()
                .parallelStream()
                .map(tagProvider -> tagProvider.run(cachedOutput))
                .toArray(CompletableFuture<?>[]::new);

        return CompletableFuture.allOf(tagProviderFutures);
    }

    @Override
    @NotNull
    public String getName() {
        return this.modid + " All Tags";
    }
}
