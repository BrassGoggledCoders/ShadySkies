package xyz.brassgoggledcoders.shadyskies.dataregistering.item;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.List;
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
}
