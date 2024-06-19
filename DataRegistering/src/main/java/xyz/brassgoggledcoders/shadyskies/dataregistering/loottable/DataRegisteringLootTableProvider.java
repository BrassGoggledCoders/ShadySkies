package xyz.brassgoggledcoders.shadyskies.dataregistering.loottable;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class DataRegisteringLootTableProvider extends LootTableProvider implements ILootTableProvider {
    private final Map<LootContextParamSet, DataRegisteringSubProviderEntry> subProviderEntryMap;

    public DataRegisteringLootTableProvider(PackOutput packOutput) {
        super(packOutput, Collections.emptySet(), Collections.emptyList());
        this.subProviderEntryMap = new HashMap<>();
    }

    @Override
    @NotNull
    public List<SubProviderEntry> getTables() {
        return subProviderEntryMap.entrySet()
                .stream()
                .map(entry -> new SubProviderEntry(entry.getValue(), entry.getKey()))
                .toList();
    }

    @Override
    public <B extends Block> void blockLootTable(Supplier<B> blockSupplier, Function<B, LootTable.Builder> builder) {
        this.subProviderEntryMap.computeIfAbsent(
                LootContextParamSets.BLOCK,
                set -> new DataRegisteringSubProviderEntry()
        ).registeringLootTable(
                () -> blockSupplier.get()
                        .getLootTable(),
                () -> builder.apply(blockSupplier.get())
        );
    }

    @Override
    public void lootTable(LootContextParamSet paramSet, ResourceLocation lootTableName, Supplier<LootTable.Builder> builder) {
        this.subProviderEntryMap.computeIfAbsent(paramSet, set -> new DataRegisteringSubProviderEntry())
                .registeringLootTable(
                        () -> lootTableName,
                        builder
                );
    }
}
