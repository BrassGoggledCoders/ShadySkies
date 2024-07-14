package xyz.brassgoggledcoders.shadyskies.dataregistering.loottable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.neoforged.bus.api.SubscribeEvent;

import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public interface ILootTableProvider {

    <B extends Block> void blockLootTable(Supplier<B> blockSupplier, Function<B, LootTable.Builder> builder);

    void lootTable(LootContextParamSet paramSet, ResourceLocation lootTableName, Supplier<LootTable.Builder> builder);
}
