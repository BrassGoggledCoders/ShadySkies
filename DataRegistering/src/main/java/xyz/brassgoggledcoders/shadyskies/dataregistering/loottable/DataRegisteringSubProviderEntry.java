package xyz.brassgoggledcoders.shadyskies.dataregistering.loottable;

import com.mojang.datafixers.util.Pair;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class DataRegisteringSubProviderEntry implements Supplier<LootTableSubProvider>, LootTableSubProvider {
    private final List<Pair<Supplier<ResourceLocation>, Supplier<LootTable.Builder>>> lootTables = new ArrayList<>();

    public void registeringLootTable(Supplier<ResourceLocation> name, Supplier<LootTable.Builder> supplier) {
        this.lootTables.add(Pair.of(name, supplier));
    }

    @Override
    public LootTableSubProvider get() {
        return this;
    }

    @Override
    public void generate(@NotNull BiConsumer<ResourceLocation, LootTable.Builder> biConsumer) {
        Map<ResourceLocation, LootTable.Builder> map = new HashMap<>();

        for (Pair<Supplier<ResourceLocation>, Supplier<LootTable.Builder>> entry : lootTables) {
            //Will override the older ones
            map.put(entry.getFirst().get(), entry.getSecond().get());
        }

        map.forEach(biConsumer);
    }
}
