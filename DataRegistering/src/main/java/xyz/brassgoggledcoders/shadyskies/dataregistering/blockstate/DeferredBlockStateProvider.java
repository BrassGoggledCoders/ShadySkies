package xyz.brassgoggledcoders.shadyskies.dataregistering.blockstate;


import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DeferredBlockStateProvider extends BlockStateProvider {
    private final Supplier<List<Consumer<DeferredBlockStateProvider>>> blockStatesToBuild;

    public DeferredBlockStateProvider(PackOutput output, String modid, ExistingFileHelper exFileHelper, Supplier<List<Consumer<DeferredBlockStateProvider>>> blockStatesToBuild) {
        super(output, modid, exFileHelper);
        this.blockStatesToBuild = blockStatesToBuild;
    }

    @Override
    protected void registerStatesAndModels() {
        this.blockStatesToBuild.get()
                .forEach(blockStateProviderConsumer -> blockStateProviderConsumer.accept(this));
    }
}
