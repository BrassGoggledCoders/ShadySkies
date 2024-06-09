package xyz.brassgoggledcoders.shadyskies.dataregistering.recipe;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import org.jetbrains.annotations.NotNull;

public class DataRegisteringRecipeProvider extends RecipeProvider {
    public DataRegisteringRecipeProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput recipeOutput) {

    }
}
