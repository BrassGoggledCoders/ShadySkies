package xyz.brassgoggledcoders.shadyskies.dataregistering.item;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DeferredItemModelProvider extends ItemModelProvider {
    private final Supplier<List<Consumer<DeferredItemModelProvider>>> itemModelsToBuild;

    public DeferredItemModelProvider(PackOutput output, String modid, ExistingFileHelper existingFileHelper,
                                     Supplier<List<Consumer<DeferredItemModelProvider>>> itemModelsToBuild) {
        super(output, modid, existingFileHelper);
        this.itemModelsToBuild = itemModelsToBuild;
    }

    @Override
    protected void registerModels() {
        this.itemModelsToBuild.get().forEach(itemModelToBuild -> itemModelToBuild.accept(this));
    }

    public void generated(Block block) {
        ResourceLocation id = Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(block.asItem()));
        this.getBuilder(id.toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", new ResourceLocation(id.getNamespace(), "block/" + id.getPath()));

    }

    public void generated(Block block, String path) {
        ResourceLocation id = Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(block.asItem()));
        this.getBuilder(id.toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", new ResourceLocation(id.getNamespace(), "block/" + path));
    }
}
