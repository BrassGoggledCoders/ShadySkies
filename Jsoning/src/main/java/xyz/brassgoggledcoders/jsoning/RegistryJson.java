package xyz.brassgoggledcoders.jsoning;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.mojang.datafixers.util.Either;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.tags.ITagManager;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Objects;

@SuppressWarnings("unused")
public class RegistryJson {
    @Nonnull
    public static <T> T getValue(JsonObject jsonObject, String fieldName, IForgeRegistry<T> forgeRegistry) {
        JsonPrimitive field = jsonObject.getAsJsonPrimitive(fieldName);
        if (field == null) {
            throw new JsonParseException("Failed to find Value for Field '" + fieldName + "'");
        } else {
            T value = forgeRegistry.getValue(new ResourceLocation(field.getAsString()));
            if (value == null) {
                throw new JsonParseException("Failed to find Object for Value'" + field.getAsString() + "'");
            } else {
                return value;
            }
        }
    }

    @Nonnull
    public static Block getBlock(JsonObject jsonObject, String fieldName) {
        return getValue(jsonObject, fieldName, ForgeRegistries.BLOCKS);
    }

    @Nonnull
    public static Block getBlock(JsonObject jsonObject) {
        return getBlock(jsonObject, "block");
    }

    @Nonnull
    public static EntityType<?> getEntity(JsonObject jsonObject, String fieldName) {
        return getValue(jsonObject, fieldName, ForgeRegistries.ENTITY_TYPES);
    }

    @Nonnull
    public static EntityType<?> getEntity(JsonObject jsonObject) {
        return getEntity(jsonObject, "entity");
    }

    @NotNull
    public static <T> Either<T, TagKey<T>> valueOrTag(IForgeRegistry<T> registry, JsonObject jsonObject, String fieldName) {
        JsonElement jsonElement = jsonObject.get(fieldName);
        if (jsonElement == null || !jsonElement.isJsonPrimitive()) {
            throw new JsonParseException("'%s' must be a string".formatted(fieldName));
        } else {
            String name = jsonElement.getAsString();
            if (name.startsWith("#")) {
                ITagManager<T> tagManager = registry.tags();
                if (tagManager == null) {
                    throw new JsonParseException("Registry type does not have tags");
                } else {
                    return Either.right(tagManager.createTagKey(new ResourceLocation(name.substring(1))));
                }
            } else {
                ResourceLocation valueName = new ResourceLocation(name);
                T value = registry.getValue(valueName);
                if (value != null) {
                    return Either.left(value);
                } else {
                    throw new JsonParseException("Failed to Find Registry Value for '%s'".formatted(name));
                }
            }
        }
    }

    public static <T> JsonPrimitive writeValueOrTag(IForgeRegistry<T> registry, Either<T, TagKey<T>> value) {
        return new JsonPrimitive(value.<String>map(
                entry -> Objects.requireNonNull(registry.getKey(entry)).toString(),
                tagKey -> "#" + tagKey.location()
        ));
    }
}
