package xyz.brassgoggledcoders.shadyskies.dataregistering.provider;

import xyz.brassgoggledcoders.shadyskies.dataregistering.blockstate.DeferredBlockStateProvider;
import xyz.brassgoggledcoders.shadyskies.dataregistering.item.DeferredItemModelProvider;
import xyz.brassgoggledcoders.shadyskies.dataregistering.loottable.DataRegisteringLootTableProvider;
import xyz.brassgoggledcoders.shadyskies.dataregistering.loottable.ILootTableProvider;
import xyz.brassgoggledcoders.shadyskies.dataregistering.recipe.DeferredRecipeProvider;
import xyz.brassgoggledcoders.shadyskies.dataregistering.tags.DataRegisteringTagProvider;

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

                return lootTableProvider;
            });

    public static final ProviderType<DataRegisteringTagProvider> TAGS = ProviderType.createType(
            DataRegisteringTagProvider.class,
            (id, event) -> {
                DataRegisteringTagProvider tagProvider = new DataRegisteringTagProvider(
                        event.getGenerator()
                                .getPackOutput(),
                        event.getLookupProvider(),
                        id,
                        event.getExistingFileHelper()
                );

                event.getGenerator().addProvider(event.includeServer(), tagProvider);

                return tagProvider;
            }
    );

    public static final ProviderType<DeferredProvider<DeferredItemModelProvider>> ITEM_MODELS = new DeferredProviderType<>(
            (id, event, deferredActions) -> {
                DeferredItemModelProvider provider = new DeferredItemModelProvider(
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
}
