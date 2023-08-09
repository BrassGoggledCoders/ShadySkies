package xyz.brassgoggledcoders.jsoning;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;

public class TagJson {
    public static TagKey<EntityType<?>> getEntityTypeTag(JsonObject jsonObject, String fieldName) {
        String tagName = GsonHelper.getAsString(jsonObject, fieldName);
        return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(tagName));
    }

    public static TagKey<EntityType<?>> getEntityTypeTag(JsonObject jsonObject) {
        return getEntityTypeTag(jsonObject, "tag");
    }

    public static TagKey<Block> getBlockTag(JsonObject jsonObject, String fieldName) {
        String tagName = GsonHelper.getAsString(jsonObject, fieldName);
        return TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(tagName));
    }

    public static TagKey<Block> getBlockTag(JsonObject jsonObject) {
        return getBlockTag(jsonObject, "tag");
    }
}
