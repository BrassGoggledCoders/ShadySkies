package xyz.brassgoggledcoders.jsoning;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class StackJson {
    private static final Gson GSON = new Gson();

    public static JsonObject writeItemStack(ItemStack itemStack) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("item", Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(itemStack.getItem())).toString());
        if (itemStack.getCount() > 1) {
            jsonObject.addProperty("count", itemStack.getCount());
        }
        if (itemStack.getTag() != null) {
            jsonObject.add("nbt", GSON.toJsonTree(itemStack.getTag().toString()));
        }
        return jsonObject;
    }

    public static FluidStack readFluidStack(JsonObject jsonParent, String field) {
        JsonElement jsonElement = jsonParent.get(field);
        if (jsonElement == null || jsonElement.isJsonNull()) {
            throw new JsonParseException("'%s' cannot be null".formatted(field));
        } else if (jsonElement.isJsonPrimitive()) {
            String fluidName = jsonElement.getAsString();
            Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(fluidName));
            if (fluid == null) {
                throw new JsonParseException("Failed to find a fluid with name '%s'".formatted(fluidName));
            } else {
                return new FluidStack(fluid, FluidType.BUCKET_VOLUME);
            }
        } else if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String fluidName = GsonHelper.getAsString(jsonObject, "fluid");
            Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(fluidName));
            if (fluid == null) {
                throw new JsonParseException("Failed to find a fluid with name '%s'".formatted(fluidName));
            } else {

                return new FluidStack(
                        fluid,
                        GsonHelper.getAsInt(jsonObject, "amount", FluidType.BUCKET_VOLUME),
                        jsonObject.has("nbt") ? CraftingHelper.getNBT(jsonObject.get("nbt")) : null
                );
            }
        } else {
            throw new JsonParseException("'input' must be a string or an object");
        }
    }

    public static JsonObject writeFluidStack(FluidStack fluidStack) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("fluid", Objects.requireNonNull(ForgeRegistries.FLUIDS.getKey(fluidStack.getFluid())).toString());
        jsonObject.addProperty("amount", fluidStack.getAmount());
        if (fluidStack.getTag() != null) {
            jsonObject.add("nbt", GSON.toJsonTree(fluidStack.getTag().toString()));
        }

        return jsonObject;
    }
}
