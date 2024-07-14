package xyz.brassgoggledcoders.shadyskies.dataregistering.provider;

import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import xyz.brassgoggledcoders.shadyskies.dataregistering.blockstate.DeferredBlockStateProvider;
import xyz.brassgoggledcoders.shadyskies.dataregistering.loottable.DataRegisteringLootTableProvider;
import xyz.brassgoggledcoders.shadyskies.dataregistering.loottable.ILootTableProvider;
import xyz.brassgoggledcoders.shadyskies.dataregistering.recipe.DeferredRecipeProvider;

@SuppressWarnings("unused")
public class ProviderTypes {
    public static final ProviderType<DeferredProvider<DeferredRecipeProvider>> RECIPE = new DeferredProviderType<>(
            (id, event, deferredActions) -> {
                DeferredRecipeProvider provider = new DeferredRecipeProvider(
                        event.getGenerator()
                                .getPackOutput(),
                        deferredActions
                );

                event.getGenerator()
                        .addProvider(event.includeServer(), provider);

                return provider;
            }
    );

    public static final ProviderType<DeferredProvider<DeferredBlockStateProvider>> BLOCKSTATE = new DeferredProviderType<>(
            (id, event, deferredActions) -> {
                DeferredBlockStateProvider provider = new DeferredBlockStateProvider(
                        event.getGenerator()
                                .getPackOutput(),
                        id,
                        event.getExistingFileHelper(),
                        deferredActions
                );

                event.getGenerator()
                        .addProvider(event.includeClient(), provider);

                return provider;
            }
    );

    public static final ProviderType<ILootTableProvider> LOOT_TABLE = ProviderType.createType(
            ILootTableProvider.class,
            (id, event) -> {
                DataRegisteringLootTableProvider lootTableProvider = new DataRegisteringLootTableProvider(
                        event.getGenerator()
                                .getPackOutput()
                );

                event.getGenerator()
                        .addProvider(event.includeServer(), lootTableProvider);

                return  lootTableProvider;
            });
}
