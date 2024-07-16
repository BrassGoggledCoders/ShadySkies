package xyz.brassgoggledcoders.shadyskies.dataregistering.basic;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import xyz.brassgoggledcoders.shadyskies.dataregistering.DataRegistering;
import xyz.brassgoggledcoders.shadyskies.dataregistering.language.TranslationProvider;
import xyz.brassgoggledcoders.shadyskies.dataregistering.loottable.LootTableHelper;
import xyz.brassgoggledcoders.shadyskies.dataregistering.provider.ProviderTypes;
import xyz.brassgoggledcoders.shadyskies.dataregistering.registering.DataRegisteringEntry;
import xyz.brassgoggledcoders.shadyskies.dataregistering.util.StringHelper;
import xyz.brassgoggledcoders.shadyskies.registering.item.ItemLikeEntry;

@SuppressWarnings("unused")
public class BlockBasics {
    public static <T extends ItemLikeEntry<U, Block>, U extends Block> void defaultBlock(
            DataRegistering dataRegistering,
            DataRegisteringEntry<T, U, Block> dataEntry
    ) {
        defaultLang(dataRegistering, dataEntry);
        defaultLoot(dataRegistering, dataEntry);
        defaultBlockState(dataRegistering, dataEntry);
    }

    public static <T extends ItemLikeEntry<U, Block>, U extends Block> void defaultLang(
            DataRegistering dataRegistering,
            DataRegisteringEntry<T, U, Block> dataEntry
    ) {
        dataRegistering.getProvider(TranslationProvider.TYPE)
                .addTranslation(
                        "block",
                        dataEntry.getRegisteringEntry()
                                .getId(),
                        StringHelper.createName(dataEntry.getRegisteringEntry()
                                .getId()
                                .getPath()
                        )
                );
    }

    public static <T extends ItemLikeEntry<U, Block>, U extends Block> void defaultLoot(
            DataRegistering dataRegistering,
            DataRegisteringEntry<T, U, Block> dataEntry
    ) {
        dataRegistering.getProvider(ProviderTypes.LOOT_TABLE)
                .blockLootTable(dataEntry, LootTableHelper::dropsSelf);
    }

    public static <T extends ItemLikeEntry<U, Block>, U extends Block> void defaultBlockState(
            DataRegistering dataRegistering,
            DataRegisteringEntry<T, U, Block> dataEntry
    ) {
        T entry = dataEntry.getRegisteringEntry();
        dataRegistering.getProvider(ProviderTypes.BLOCKSTATE)
                .deferred(deferredBlockStateProvider -> {
                    ModelFile blockFile = deferredBlockStateProvider.cubeAll(entry.get());
                    deferredBlockStateProvider.simpleBlockItem(
                            entry.get(),
                            blockFile
                    );
                });
    }
}
