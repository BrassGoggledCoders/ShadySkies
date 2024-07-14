package xyz.brassgoggledcoders.shadyskies.dataregistering.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.tags.TagsProvider.TagAppender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

import java.util.*;
import java.util.function.Function;

public class DataRegisteringTagAppender<T> {
    private final TagKey<T> tagKey;

    private final List<T> values;
    private final List<TagKey<T>> otherTags;

    public DataRegisteringTagAppender(TagKey<T> tagKey) {
        this.tagKey = tagKey;
        this.values = new ArrayList<>();
        this.otherTags = new ArrayList<>();
    }

    @SafeVarargs
    public final DataRegisteringTagAppender<T> with(TagKey<T>... tagKeys) {
        this.otherTags.addAll(List.of(tagKeys));
        return this;
    }

    @SafeVarargs
    public final DataRegisteringTagAppender<T> with(T... values) {
        this.values.addAll(List.of(values));
        return this;
    }

    public void run(HolderLookup.Provider provider, Function<TagKey<T>, TagAppender<T>> tagAppenderCreator) {
        TagAppender<T> tagAppender = tagAppenderCreator.apply(this.tagKey);

        if (!values.isEmpty()) {
            Map<T, ResourceKey<T>> foundValues = new HashMap<>();
            provider.lookup(this.tagKey.registry())
                    .ifPresent(registryLookup -> registryLookup.listElements()
                            .forEach(tReference -> {
                                if (values.contains(tReference.value())) {
                                    values.remove(tReference.value());
                                    foundValues.put(
                                            tReference.value(),
                                            tReference.key()
                                    );
                                }
                            }));
            if (!values.isEmpty()) {
                throw new IllegalStateException("Failed to find all keys for %s".formatted(this.tagKey.toString()));
            }
            for (ResourceKey<T> value : foundValues.values()) {
                tagAppender.add(value);
            }
        }

        for (TagKey<T> otherTag : this.otherTags) {
            tagAppender.addTag(otherTag);
        }
    }
}
