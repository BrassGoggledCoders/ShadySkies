package xyz.brassgoggledcoders.shadyskies.dataregistering.recipe;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class DeferredRecipeProvider extends RecipeProvider implements RecipeOutput {
    private final Supplier<List<Consumer<DeferredRecipeProvider>>> recipesToBuild;

    private RecipeOutput recipeOutput;

    public DeferredRecipeProvider(PackOutput packOutput, Supplier<List<Consumer<DeferredRecipeProvider>>> recipesToBuild) {
        super(packOutput);
        this.recipesToBuild = recipesToBuild;
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput recipeOutput) {
        this.recipeOutput = recipeOutput;
        this.recipesToBuild.get()
                .forEach(consumer -> consumer.accept(this));
        this.recipeOutput = null;
    }

    @Override
    @NotNull
    public Advancement.Builder advancement() {
        return Objects.requireNonNull(this.recipeOutput)
                .advancement();
    }

    public Criterion<?> unlockedByItem(Item item) {
        return unlockedBy(builder -> builder.of(item));
    }

    public Criterion<?> unlockedByTag(TagKey<Item> itemTagKey) {
        return unlockedBy(builder -> builder.of(itemTagKey));
    }

    public Criterion<?> unlockedBy(Function<ItemPredicate.Builder, ItemPredicate.Builder> function) {
        return CriteriaTriggers.INVENTORY_CHANGED
                .createCriterion(new InventoryChangeTrigger.TriggerInstance(
                        Optional.empty(),
                        InventoryChangeTrigger.TriggerInstance.Slots.ANY,
                        List.of(function.apply(ItemPredicate.Builder.item())
                                .build()
                        )
                ));
    }

    @Override
    public void accept(@NotNull ResourceLocation resourceLocation, @NotNull Recipe<?> recipe,
                       @Nullable AdvancementHolder advancementHolder, ICondition @NotNull ... conditions) {
        Objects.requireNonNull(this.recipeOutput)
                .accept(resourceLocation, recipe, advancementHolder, conditions);
    }
}
